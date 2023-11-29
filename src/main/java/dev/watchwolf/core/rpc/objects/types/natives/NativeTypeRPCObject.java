package dev.watchwolf.core.rpc.objects.types.natives;

import dev.watchwolf.core.rpc.objects.types.RpcObjectWrapper;

public abstract class NativeTypeRPCObject<T> extends RpcObjectWrapper<T> {
    public NativeTypeRPCObject(T object) {
        super(object);
    }
}
