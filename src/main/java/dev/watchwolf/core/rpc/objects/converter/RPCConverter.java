package dev.watchwolf.core.rpc.objects.converter;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.types.RPCObject;

import java.util.ArrayList;
import java.util.List;

public class RPCConverter<T extends RPCObject> {
    private final Class<T> locallyConverting;
    private final List<RPCConverter<?>> subconverters;

    protected RPCConverter(Class<T> converting) {
        this.subconverters = new ArrayList<>();
        this.locallyConverting = converting;
    }

    public RPCConverter() {
        this(null);
    }

    public RPCObject wrap(Object obj) {
        if (obj instanceof RPCObject) return (RPCObject) obj;

        if (this.canLocallyWrap(obj.getClass())) {
            return this.performWrap(obj);
        }

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (!subconverter.canWrap(obj.getClass())) continue; // this sub-class doesn't implement the type

            return subconverter.wrap(obj);
        }

        // raw class; it doesn't implement anything
        throw new UnsupportedOperationException("No converter was found with the argument obj_type=" + obj.getClass().getName() + " in the class, nor the sub-classes.");
    }

    @SuppressWarnings("NullAway")
    public Object unwrap(RPCObject obj) {
        if (this.canLocallyUnwrap(obj.getClass())) {
            return this.performUnwrap(this.locallyConverting.cast(obj));
        }

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (!subconverter.canUnwrap(obj.getClass())) continue; // this sub-class doesn't implement the type

            return subconverter.unwrap(obj);
        }

        // raw class; it doesn't implement anything
        throw new UnsupportedOperationException("No converter was found with the argument obj_type=" + obj.getClass().getName() + " in the class, nor the sub-classes.");
    }

    protected RPCObject _unmarshall(MessageChannel channel, Class<? extends RPCObject> type) {
        if (this.canLocallyUnwrap(type)) {
            return this.performUnmarshall(channel, (Class<? extends T>)type);
        }

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (!subconverter.canUnwrap(type)) continue; // this sub-class doesn't implement the type

            return subconverter._unmarshall(channel, type);
        }

        // raw class; it doesn't implement anything
        throw new UnsupportedOperationException("No converter was found with the argument obj_type=" + type.getName() + " in the class, nor the sub-classes.");
    }

    public <O> O unmarshall(MessageChannel channel, Class<O> type) {
        Class<? extends RPCObject> rpcType = this.getRPCWrapClass(type);
        RPCObject unmarshalledRpcObject = this._unmarshall(channel, rpcType);
        Object unmarshalledObject = this.unwrap(unmarshalledRpcObject);
        return type.cast(unmarshalledObject);
    }

    public Class<? extends RPCObject> getRPCWrapClass(Class<?> type) {
        if (type.isAssignableFrom(RPCConverter.class)) return (Class<? extends RPCObject>)type;

        if (this.canLocallyWrap(type)) {
            if (this.locallyConverting == null) throw new IllegalArgumentException("Specified by arguments converting nothing, but found a match for type=" + type.getName());
            return this.locallyConverting;
        }

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (!subconverter.canWrap(type)) continue; // this sub-class doesn't implement the type

            return subconverter.getRPCWrapClass(type);
        }

        // raw class; it doesn't implement anything
        throw new UnsupportedOperationException("No converter was found with the argument type=" + type.getName() + " in the class, nor the sub-classes.");
    }

    protected boolean canLocallyWrap(Class<?> objectType) {
        return false; // raw class; it doesn't implement anything
    }

    protected boolean canLocallyUnwrap(Class<? extends RPCObject> rpcType) {
        if (this.locallyConverting == null) return false; // empty implementation
        return rpcType.isAssignableFrom(this.locallyConverting);
    }

    protected T performWrap(Object obj) {
        throw new UnsupportedOperationException("Undefined operation.");
    }

    protected Object performUnwrap(T obj) {
        throw new UnsupportedOperationException("Undefined operation.");
    }

    protected T performUnmarshall(MessageChannel channel, Class<? extends T> type) {
        throw new UnsupportedOperationException("Undefined operation.");
    }

    private boolean canWrap(Class<?> objectType) {
        if (objectType.isAssignableFrom(RPCObject.class)) return true; // nothing to wrap

        if (this.canLocallyWrap(objectType)) return true;

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (subconverter.canWrap(objectType)) return true;
        }

        // nothing found
        return false;
    }

    private boolean canUnwrap(Class<? extends RPCObject> rpcType) {
        if (this.canLocallyUnwrap(rpcType)) return true;

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (subconverter.canUnwrap(rpcType)) return true;
        }

        // nothing found
        return false;
    }

    public RPCConverter<T> addSubconverter(RPCConverter<?> subconverter) {
        this.subconverters.add(subconverter);
        return this;
    }

    public static byte []toByteArray(List<Byte> bytes) {
        byte []r = new byte[bytes.size()];
        for (int x = 0; x < r.length; x++) r[x] = bytes.get(x);
        return r;
    }
}
