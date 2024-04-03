package dev.watchwolf.core.rpc.objects.types;

public abstract class RPCObjectWrapper<T> implements RPCObject {
    protected final T object;

    public RPCObjectWrapper(T object) {
        this.object = object;
    }

    public T getObject() {
        return this.object;
    }
}
