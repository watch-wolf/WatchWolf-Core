package dev.watchwolf.core.rpc.objects.types;

import dev.watchwolf.core.rpc.channel.MessageChannel;

public interface RPCObject {
    void send(MessageChannel channel);
}
