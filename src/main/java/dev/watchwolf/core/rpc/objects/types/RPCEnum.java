package dev.watchwolf.core.rpc.objects.types;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.types.natives.RPCShort;

public class RPCEnum extends RpcObjectWrapper<Enum<?>> {
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

    public <O extends Enum<O>> O getObject(Class<O> type) {
        Enum<?> r = super.getObject();
        if (r == null) {
            // no saved instance; cast integer
            return type.getEnumConstants()[this.sendObject.getObject()];
        }
        else {
            if (!r.getClass().equals(type)) throw new IllegalArgumentException("Trying to convert saved instance of type " + r.getClass().getName() + " into " + type.getName());
            return (O)r;
        }
    }

    @Override
    public void send(MessageChannel channel) {
        this.sendObject.send(channel);
    }

    @MainSubconverter
    public static class RPCEnumConverter extends RPCConverter<RPCEnum> {
        private final RPCConverter<?> converterObject;

        public RPCEnumConverter() {
            super(RPCEnum.class);

            // we send the data as integer
            this.converterObject = new RPCShort.RPCShortConverter();
        }

        @Override
        protected RPCEnum performWrap(Object obj) {
            return new RPCEnum((Enum<?>) obj);
        }

        @Override
        protected <O> O performUnwrap(RPCEnum obj, Class<O> type) {
            if (!type.isAssignableFrom(Enum.class)) throw new UnsupportedOperationException(this.getClass().getName() + " can't unwrap " + type.getName());

            return (O)obj.getObject((Class<? extends Enum>)type);
        }

        @Override
        protected RPCEnum performUnmarshall(MessageChannel channel, Class<? extends RPCEnum> type) {
            short got = this.converterObject.unmarshall(channel, Short.class);
            return new RPCEnum(got);
        }

        @Override
        protected boolean canLocallyWrap(Class<?> objectType) {
            return (objectType.isAssignableFrom(Enum.class));
        }
    }
}
