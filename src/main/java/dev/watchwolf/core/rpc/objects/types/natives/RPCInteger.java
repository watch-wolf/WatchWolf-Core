package dev.watchwolf.core.rpc.objects.types.natives;

import dev.watchwolf.core.rpc.channel.MessageChannel;

public class RPCInteger extends NativeTypeRPCObject<Integer> {
    public RPCInteger(Integer object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        // TODO
    }
}
