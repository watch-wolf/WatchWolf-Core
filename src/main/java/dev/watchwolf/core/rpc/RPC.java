package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.types.RPCObject;

import java.io.IOException;

public class RPC implements Runnable {
    private final RPCImplementer localImplementation;
    private final MessageChannel remoteConnection;
    private final RPCConverter<?> converter;

    public RPC(RPCImplementer localImplementation, MessageChannel remoteConnection, RPCConverter<?> converter) {
        this.localImplementation = localImplementation;
        this.remoteConnection = remoteConnection;
        this.converter = converter;
    }

    public void sendEvent(RPCObject ...data) throws IOException {
        for (RPCObject o : data) {
            o.send(this.remoteConnection);
        }
    }

    @Override
    public void run() {
        this.localImplementation.setHandler(this);

        // TODO read from `remoteConnection` and forward to RPCImplementer
    }
}
