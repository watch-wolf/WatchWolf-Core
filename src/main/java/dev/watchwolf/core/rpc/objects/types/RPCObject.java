package dev.watchwolf.core.rpc.objects.types;

import dev.watchwolf.core.rpc.channel.MessageChannel;

import java.io.IOException;

public interface RPCObject {
    void send(MessageChannel channel) throws IOException;
}
