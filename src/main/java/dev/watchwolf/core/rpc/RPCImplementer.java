package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;

import java.io.Closeable;
import java.io.IOException;

public interface RPCImplementer extends Closeable {
    void forwardCall(MessageChannel channel, RPCConverter<?> converter) throws IOException;
    void setHandler(RPC handler);

    @Override
    void close() throws IOException;
}
