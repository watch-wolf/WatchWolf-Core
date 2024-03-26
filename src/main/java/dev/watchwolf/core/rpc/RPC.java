package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.types.RPCObject;

import java.io.Closeable;
import java.io.IOException;

public class RPC implements Runnable, Closeable {
    private final RPCImplementer localImplementation;
    private final MessageChannel remoteConnection;
    private final RPCConverter<?> converter;

    private MessageChannel linkedConnection;

    public RPC(RPCImplementer localImplementation, MessageChannel remoteConnection, RPCConverter<?> converter) {
        this.localImplementation = localImplementation;
        this.remoteConnection = remoteConnection;
        this.converter = converter;
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
    }

    // TODO does it needs synchronization?
    public void sendEvent(RPCObject ...data) throws IOException {
        if (this.linkedConnection == null) throw new IllegalArgumentException("The connection must be stablished before launching an event");

        for (RPCObject o : data) {
            o.send(this.linkedConnection);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.linkedConnection != null) this.linkedConnection.close();
    }

    public boolean isRunning() {
        if (this.linkedConnection == null) return false;
        return !this.linkedConnection.isClosed();
    }

    public MessageChannel _getRemoteConnection() {
        return this.remoteConnection;
    }

    @Override
    public void run() {
        try {
            this.linkedConnection = this.remoteConnection.create();
            if (this.linkedConnection == null) throw new RuntimeException(new InterruptedException("Closed server before establishing client connection"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        while (!this.linkedConnection.isClosed()) {
            try {
                while (!this.linkedConnection.areBytesAvailable()) Thread.sleep(200); // TODO use notify
                this.localImplementation.forwardCall(this.linkedConnection, this.converter);
            } catch (IOException | InterruptedException ex) { }

            try {
                Thread.sleep(200); // give it some break
            } catch (InterruptedException ignore) {}
        }

        System.out.println("RPC connection (" + localImplementation.getClass().getSimpleName() + " over " + this.linkedConnection.toString() + ") closed");
    }
}
