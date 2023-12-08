package dev.watchwolf.core.rpc.objects.converter;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.types.RPCObject;

import javax.naming.InsufficientResourcesException;
import java.util.ArrayList;
import java.util.List;

public class RPCConverter<T extends RPCObject> {
    private final ClassType<T> locallyConverting;
    private final List<RPCConverter<?>> subconverters;
    private RPCConverter<?> masterConverter;

    protected RPCConverter(ClassType<T> converting) {
        this.subconverters = new ArrayList<>();
        this.locallyConverting = converting;
    }

    protected RPCConverter(Class<T> converting) {
        this(ClassTypeFactory.getType(converting));
    }

    public RPCConverter() {
        this((ClassType)null);
    }

    public RPCObject wrap(Object obj) {
        if (obj instanceof RPCObject) return (RPCObject) obj;

        ClassType<?> objType = ClassTypeFactory.getType(obj);
        if (this.canLocallyWrap(objType)) {
            return this.performWrap(obj);
        }

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (!subconverter.canWrap(objType)) continue; // this sub-class doesn't implement the type

            return subconverter.wrap(obj);
        }

        // raw class; it doesn't implement anything
        throw new UnsupportedOperationException("No converter was found with the argument obj_type=" + obj.getClass().getName() + " in the class, nor the sub-classes.");
    }

    @SuppressWarnings("NullAway")
    public <O> O unwrap(RPCObject obj, ClassType<O> type) {
        ClassType<? extends RPCObject> objType = ClassTypeFactory.getType(obj);
        if (this.canLocallyUnwrap(objType)) {
            return this.performUnwrap(this.locallyConverting.cast(obj), type);
        }

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (!subconverter.canUnwrap(objType)) continue; // this sub-class doesn't implement the type

            return subconverter.unwrap(obj, type);
        }

        // raw class; it doesn't implement anything
        throw new UnsupportedOperationException("No converter was found with the argument obj_type=" + obj.getClass().getName() + " in the class, nor the sub-classes.");
    }

    protected RPCObject _unmarshall(MessageChannel channel, ClassType<? extends RPCObject> rpcType) {
        if (this.canLocallyUnwrap(rpcType)) {
            return this.performUnmarshall(channel, (ClassType<? extends T>)rpcType);
        }

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (!subconverter.canUnwrap(rpcType)) continue; // this sub-class doesn't implement the type

            return subconverter._unmarshall(channel, rpcType);
        }

        // raw class; it doesn't implement anything
        throw new UnsupportedOperationException("No converter was found with the argument obj_type=" + rpcType.getName() + " in the class, nor the sub-classes.");
    }

    public <O> O unmarshall(MessageChannel channel, ClassType<O> type) {
        ClassType<? extends RPCObject> rpcType = this.getRPCWrapClass(type);
        RPCObject unmarshalledRpcObject = this._unmarshall(channel, rpcType);
        return this.unwrap(unmarshalledRpcObject, type);
    }

    public <O> O unmarshall(MessageChannel channel, Class<O> type) {
        return this.unmarshall(channel, ClassTypeFactory.getType(type));
    }

    public ClassType<? extends RPCObject> getRPCWrapClass(ClassType<?> type) {
        if (type.isAssignableFrom(RPCConverter.class)) return (ClassType<? extends RPCObject>)type; // already RPC; nothing to do

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

    protected boolean canLocallyWrap(ClassType<?> objectType) {
        return false; // raw class; it doesn't implement anything
    }

    protected boolean canLocallyUnwrap(ClassType<? extends RPCObject> rpcType) {
        if (this.locallyConverting == null) return false; // empty implementation
        return rpcType.isAssignableFrom(this.locallyConverting);
    }

    protected T performWrap(Object obj) {
        throw new UnsupportedOperationException("Undefined operation.");
    }

    protected <O> O performUnwrap(T obj, ClassType<O> type) {
        throw new UnsupportedOperationException("Undefined operation.");
    }

    protected T performUnmarshall(MessageChannel channel, ClassType<? extends T> type) {
        throw new UnsupportedOperationException("Undefined operation.");
    }

    protected void setMasterConverter(RPCConverter<?> masterConverter) {
        this.masterConverter = masterConverter;

        for (RPCConverter<?> subconverter : this.subconverters) {
            subconverter.setMasterConverter(masterConverter);
        }
    }

    public RPCConverter<?> getMasterConverter() {
        if (this.masterConverter == null) throw new RuntimeException("InsufficientResourcesException: `masterConverter` is null");
        return this.masterConverter;
    }

    private boolean canWrap(ClassType<?> objectType) {
        if (objectType.isAssignableFrom(RPCObject.class)) return true; // nothing to wrap

        if (this.canLocallyWrap(objectType)) return true;

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (subconverter.canWrap(objectType)) return true;
        }

        // nothing found
        return false;
    }

    private boolean canUnwrap(ClassType<? extends RPCObject> rpcType) {
        if (this.canLocallyUnwrap(rpcType)) return true;

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (subconverter.canUnwrap(rpcType)) return true;
        }

        // nothing found
        return false;
    }

    public RPCConverter<T> addSubconverter(RPCConverter<?> subconverter) {
        this.subconverters.add(subconverter);
        subconverter.setMasterConverter(this.masterConverter); // we need to notify who's the boss
        return this;
    }

    public static byte []toByteArray(List<Byte> bytes) {
        byte []r = new byte[bytes.size()];
        for (int x = 0; x < r.length; x++) r[x] = bytes.get(x);
        return r;
    }
}
