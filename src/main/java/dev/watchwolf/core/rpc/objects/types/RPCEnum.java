package dev.watchwolf.core.rpc.objects.types;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.types.natives.RPCShort;

public class RPCEnum<T extends Enum<T>> extends RpcObjectWrapper<T> {
    public RPCEnum(T object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        int value = this.object.ordinal();
        RPCObject data = new RPCShort(value);
        data.send(channel);
    }
}
