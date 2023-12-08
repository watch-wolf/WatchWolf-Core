package dev.watchwolf.core.rpc.objects.types.natives.composited;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObjectWrapper;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCShort;

public class RPCEnum extends RPCObjectWrapper<Enum<?>> {
    private final RPCShort sendObject;

    public RPCEnum(Enum<?> object) {
        super(object);

        // we send the data as integer
        this.sendObject = new RPCShort((short)object.ordinal());
    }

    public RPCEnum(int index) {
        super(null);

        // we send the data as integer
        this.sendObject = new RPCShort((short)object.ordinal());
    }

    @Override
    public Enum<?> getObject() {
        Enum<?> r = super.getObject();
        if (r == null) throw new UnsupportedOperationException("Trying to access RPC instance without real enum.");
        return r;
    }

    public Enum<?> getObject(ClassType<? extends Enum<?>> type) {
        Enum<?> r = super.getObject();
        if (r == null) {
            // no saved instance; cast integer
            return type.getClassType().getEnumConstants()[this.sendObject.getObject()];
        }
        else {
            if (!type.equals(r.getClass())) throw new IllegalArgumentException("Trying to convert saved instance of type " + r.getClass().getName() + " into " + type.getName());
            return r;
        }
    }

    @Override
    public void send(MessageChannel channel) {
        this.sendObject.send(channel);
    }

    @MainSubconverter
    public static class RPCEnumConverter extends RPCConverter<RPCEnum> {
        public RPCEnumConverter() {
            super(RPCEnum.class);
        }

        @Override
        protected RPCEnum performWrap(Object obj) {
            return new RPCEnum((Enum<?>) obj);
        }

        @Override
        protected <O> O performUnwrap(RPCEnum obj, ClassType<O> type) {
            if (!type.isAssignableFrom(Enum.class)) throw new UnsupportedOperationException(this.getClass().getName() + " can't unwrap " + type.getName());

            return (O)obj.getObject((ClassType<? extends Enum<?>>)type);
        }

        @Override
        protected RPCEnum performUnmarshall(MessageChannel channel, ClassType<? extends RPCEnum> type) {
            // we send the data as integer
            short got = this.getMasterConverter().unmarshall(channel, Short.class);

            return new RPCEnum(got);
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.isAssignableFrom(Enum.class));
        }
    }
}
