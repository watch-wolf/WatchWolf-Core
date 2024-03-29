package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.types.RPCObject;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;

public class RPC implements Runnable, Closeable {
    private final Logger logger = LogManager.getLogger(RPC.class.getName());

    private final RPCImplementer localImplementation;
    private final MessageChannel remoteConnection;
    private final RPCConverter<?> converter;

    private MessageChannel linkedConnection;

    public RPC(RPCImplementer localImplementation, MessageChannel remoteConnection, RPCConverter<?> converter) {
        this.localImplementation = localImplementation;
        this.remoteConnection = remoteConnection;
        this.converter = converter;
        this.linkedConnection = null;
    }

    /**
     * Note: this may lead to undesired behaviour; only tested with server socket
     * @param localImplementation   New implementer
     * @param that                  RPC data to clone
     */
    public RPC(RPCImplementer localImplementation, RPC that) {
        this.localImplementation = localImplementation;
        this.remoteConnection = that.remoteConnection;
        this.converter = that.converter;
        this.linkedConnection = null;
    }

    // TODO does it needs synchronization?
    public void sendEvent(RPCObject ...data) throws IOException {
        logger.traceEntry(null, (RPCObject[])data);
        if (this.linkedConnection == null) {
            logger.warn("sendEvent was called before upper connection was made");
            throw new IllegalArgumentException("The connection must be stablished before launching an event");
        }

        for (RPCObject o : data) {
            o.send(this.linkedConnection);
        }
        logger.traceExit();
    }

    @Override
    public void close() throws IOException {
        logger.info("Closing RPC connection...");
        if (this.linkedConnection != null) this.linkedConnection.close();
        this.localImplementation.close();
    }

    public boolean isRunning() {
        if (this.linkedConnection == null) return false;
        return !this.linkedConnection.isClosed();
    }

    public MessageChannel _getRemoteConnection() {
        return this.remoteConnection;
    }

    /**
     * Invokes the `create` method of the connection; waiting for a client and linking it to this instance
     * @throws IOException Channel exception
     * @throws InterruptedException The socket server was closed before a connection was made
     */
    public void createConnection() throws IOException,InterruptedException {
        logger.traceEntry();
        this.linkedConnection = this.remoteConnection.create();
        if (this.linkedConnection == null) {
            try {
                this.close(); // as couldn't establish connection, destroy the localImplementation
            } catch (IOException ignore) {}
            throw this.logger.throwing(new InterruptedException("Closed server before establishing client connection"));
        }

        logger.info("RPC connection established (connection: " + this.linkedConnection.toString() + ")");
        logger.traceExit();
    }

    /**
     * If the connection is still active, and bytes were received, it will forward the call to the implementation.
     * @throws IOException Channel exception
     */
    public void processOneCall() throws IOException {
        if (this.linkedConnection == null) throw new IllegalArgumentException("You have to call first `createConnection`");
        if (this.linkedConnection.isClosed()) return;
        if (!this.linkedConnection.areBytesAvailable()) return;

        this.localImplementation.forwardCall(this.linkedConnection, this.converter);
    }

    @Override
    public void run() {
        logger.traceEntry();
        if (this.linkedConnection == null) {
            try {
                this.createConnection();
            } catch (IOException | InterruptedException ex) {
                throw this.logger.throwing(new RuntimeException(ex));
            }
        }

        try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.push(this.localImplementation.getClass().getSimpleName()).push(this.linkedConnection.toString())) {
            while (!this.linkedConnection.isClosed()) {
                try {
                    this.logger.debug("Waiting for bytes...");
                    while (!this.linkedConnection.areBytesAvailable()) Thread.sleep(200); // TODO use notify
                    this.logger.debug("Got bytes; forwarding...");
                    this.processOneCall();
                } catch (IOException | InterruptedException ex) {}

                try {
                    Thread.sleep(200); // give it some break
                } catch (InterruptedException ignore) {}
            }

            logger.info("RPC connection closed");
            try {
                this.close(); // just in case it closed because of the client and not because `close` was called
            } catch (IOException ignore) {}

            logger.traceExit();
        }
    }
}
