package dev.watchwolf.core.rpc.objects.types;

public abstract class RpcObjectWrapper<T> implements RPCObject {
    protected final T object;

    public RpcObjectWrapper(T object) {
        this.object = object;
    }

    public T getObject() {
        return this.object;
    }
}
