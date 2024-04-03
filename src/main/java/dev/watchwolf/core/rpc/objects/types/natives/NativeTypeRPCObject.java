package dev.watchwolf.core.rpc.objects.types.natives;

import dev.watchwolf.core.rpc.objects.types.RPCObjectWrapper;

public abstract class NativeTypeRPCObject<T> extends RPCObjectWrapper<T> {
    public NativeTypeRPCObject(T object) {
        super(object);
    }
}
