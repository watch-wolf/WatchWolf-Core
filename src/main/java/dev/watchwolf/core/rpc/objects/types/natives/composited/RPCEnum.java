package dev.watchwolf.core.rpc.objects.types.natives.composited;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.TemplateClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObjectWrapper;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCShort;

import java.io.IOException;

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

    public Class<? extends Enum<?>> getType() {
        return (super.getObject() == null ? null : (Class<? extends Enum<?>>)super.getObject().getClass());
    }

    public Enum<?> getObject(Class<? extends Enum<?>> type) {
        Enum<?> r = super.getObject();
        if (r == null) {
            // no saved instance; cast integer
            return RPCEnum.getObject(this.sendObject.getObject(), type);
        }
        else {
            if (!type.equals(this.getType())) throw new IllegalArgumentException("Trying to convert saved instance of type " + this.getType().getName() + " into " + type.getName());
            return r;
        }
    }

    public static Enum<?> getObject(short index, Class<? extends Enum<?>> type) {
        return type.getEnumConstants()[index];
    }

    @Override
    public void send(MessageChannel channel) throws IOException {
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

            return (O)obj.getObject((Class<? extends Enum<?>>)type.getClassType());
        }

        @Override
        protected RPCEnum performUnmarshall(MessageChannel channel, ClassType<? extends RPCEnum> type) throws IOException {
            // we send the data as integer
            short got = this.getMasterConverter().unmarshall(channel, Short.class);
            RPCEnum r;

            if (type instanceof TemplateClassType) {
                // we can set the type
                TemplateClassType<? extends RPCEnum,? extends Enum<?>> templateType = (TemplateClassType<? extends RPCEnum,? extends Enum<?>>)type;
                r = new RPCEnum(RPCEnum.getObject(got, templateType.getSubtype().getClassType()));
            }
            else {
                // unknown type
                r = new RPCEnum(got);
            }

            return r;
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.isAssignableFrom(Enum.class));
        }
    }
}
