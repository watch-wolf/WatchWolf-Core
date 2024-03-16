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

    public RPC(RPCImplementer localImplementation, MessageChannel remoteConnection, RPCConverter<?> converter) {
        this.localImplementation = localImplementation;
        this.remoteConnection = remoteConnection;
        this.converter = converter;
    }

    // TODO does it needs synchronization?
    public void sendEvent(RPCObject ...data) throws IOException {
        for (RPCObject o : data) {
            o.send(this.remoteConnection);
        }
    }

    @Override
    public void close() throws IOException {
        this.remoteConnection.close();
    }

    public boolean isRunning() {
        return !this.remoteConnection.isClosed();
    }

    @Override
    public void run() {
        try {
            this.remoteConnection.create();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        while (!this.remoteConnection.isClosed()) {
            try {
                while (!this.remoteConnection.areBytesAvailable()) Thread.sleep(200); // TODO use notify
                this.localImplementation.forwardCall(this.remoteConnection, this.converter);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("RPC connection (" + localImplementation.getClass().getSimpleName() + " over " + this.remoteConnection.toString() + ") closed");
    }
}
