package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;

import java.io.IOException;

public interface RPCImplementer {
    void forwardCall(MessageChannel channel, RPCConverter<?> converter) throws IOException;
    void setHandler(RPC handler);
}
