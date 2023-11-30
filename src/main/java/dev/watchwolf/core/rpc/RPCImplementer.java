package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;

public interface RPCImplementer {
    void forwardCall(MessageChannel channel, RPCConverter converter);
    void setHandler(RPC handler);
}
