package dev.watchwolf.core.rpc.objects.converter;

import dev.watchwolf.core.rpc.channel.ChannelEmu;
import dev.watchwolf.core.rpc.channel.ChannelQueue;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.converter.class_type.TemplateClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObject;

import javax.naming.InsufficientResourcesException;
import java.io.IOException;
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
            try {
                return this.performWrap(obj);
            } catch (UnsupportedOperationException ignore) {} // false positive
        }

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (!subconverter.canWrap(objType)) continue; // this sub-class doesn't implement the type

            try {
                return subconverter.wrap(obj);
            } catch (UnsupportedOperationException ignore) {} // false positive
        }

        // raw class; it doesn't implement anything
        throw new UnsupportedOperationException("No converter was found with the argument obj_type=" + obj.getClass().getName() + " in the class, nor the sub-classes.");
    }

    @SuppressWarnings("NullAway")
    public <O> O unwrap(RPCObject obj, ClassType<O> type) {
        ClassType<? extends RPCObject> objType = ClassTypeFactory.getType(obj);
        if (this.canLocallyUnwrap(objType)) {
            try {
                return this.performUnwrap(this.locallyConverting.cast(obj), type);
            } catch (UnsupportedOperationException ignore) {} // false positive
        }

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (!subconverter.canUnwrap(objType)) continue; // this sub-class doesn't implement the type

            try {
                return subconverter.unwrap(obj, type);
            } catch (UnsupportedOperationException ignore) {} // false positive
        }

        // raw class; it doesn't implement anything
        throw new UnsupportedOperationException("No converter was found with the argument obj_type=" + obj.getClass().getName() + " in the class, nor the sub-classes.");
    }

    public <O> O unwrap(RPCObject obj, Class<O> type) {
        return this.unwrap(obj, ClassTypeFactory.getType(type));
    }

    protected RPCObject _unmarshall(ChannelQueue channel, ClassType<? extends RPCObject> rpcType) throws IOException {
        if (this.canLocallyUnwrap(rpcType)) {
            ChannelEmu emulatedChannel = new ChannelEmu(channel);
            try {
                return this.performUnmarshall(emulatedChannel, (ClassType<? extends T>) rpcType);
            } catch (UnsupportedOperationException ignore) {
                // false positive; discard changes
                emulatedChannel.discard();
            }
        }

        for (RPCConverter<?> subconverter : this.subconverters) {
            if (!subconverter.canUnwrap(rpcType)) continue; // this sub-class doesn't implement the type

            try {
                return subconverter._unmarshall(channel, rpcType);
            } catch (UnsupportedOperationException ignore) {} // false positive
        }

        // raw class; it doesn't implement anything
        throw new UnsupportedOperationException("No converter was found with the argument obj_type=" + rpcType.getName() + " in the class, nor the sub-classes.");
    }

    public <O> O unmarshall(MessageChannel channel, ClassType<O> type) throws IOException {
        ClassType<? extends RPCObject> rpcType = this.getRPCWrapClass(type);
        ChannelQueue queuedChannel = new ChannelQueue(channel);
        RPCObject unmarshalledRpcObject = this._unmarshall(queuedChannel, rpcType);
        return this.unwrap(unmarshalledRpcObject, type);
    }

    public <O> O unmarshall(MessageChannel channel, Class<O> type) throws IOException {
        return this.unmarshall(channel, ClassTypeFactory.getType(type));
    }

    public ClassType<? extends RPCObject> getRPCWrapClass(ClassType<?> type) {
        if (type.isAssignableFrom(RPCObject.class)) return (ClassType<? extends RPCObject>)type; // already RPC; nothing to do

        if (this.canLocallyWrap(type)) {
            if (this.locallyConverting == null) throw new IllegalArgumentException("Specified by arguments converting nothing, but found a match for type=" + type.getName());
            if (type instanceof TemplateClassType && !(this.locallyConverting instanceof TemplateClassType)) {
                // generic convert; set second element
                ClassType<?> subtype = ((TemplateClassType<?,?>)type).getSubtype();
                return ClassTypeFactory.getTemplateType(this.locallyConverting.getClassType(), this.getMasterConverter().getRPCWrapClass(subtype));
            }
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
        return this.locallyConverting.isAssignableFrom(rpcType);
    }

    protected T performWrap(Object obj) {
        throw new UnsupportedOperationException("Undefined operation.");
    }

    protected <O> O performUnwrap(T obj, ClassType<O> type) {
        throw new UnsupportedOperationException("Undefined operation.");
    }

    protected T performUnmarshall(MessageChannel channel, ClassType<? extends T> type) throws IOException {
        throw new UnsupportedOperationException("Undefined operation.");
    }

    void setMasterConverter(RPCConverter<?> masterConverter) {
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
        if (subconverter == null) return this;

        this.subconverters.add(subconverter);
        subconverter.setMasterConverter(this.masterConverter); // we need to notify who's the boss
        return this;
    }

    protected List<RPCConverter<?>> getSubconverters() {
        return this.subconverters;
    }

    public static byte []toByteArray(List<Byte> bytes) {
        byte []r = new byte[bytes.size()];
        for (int x = 0; x < r.length; x++) r[x] = bytes.get(x);
        return r;
    }
}
