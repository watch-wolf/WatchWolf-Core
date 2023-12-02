package dev.watchwolf.core.rpc.objects.types.natives;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;

public class RPCShort extends NativeTypeRPCObject<Short> {
    public RPCShort(Short object) {
        super(object);
    }

    public RPCShort(Integer object) {
        this(object == null ? null : object.shortValue());
    }

    @Override
    public void send(MessageChannel channel) {
        int lsb = this.object & 0xFF,
            msb = (this.object >> 8)&0xFF;
        channel.send((byte)lsb, (byte)msb);
    }

    @MainSubconverter
    public static class RPCShortConverter extends RPCConverter<RPCShort> {
        public RPCShortConverter() {
            super(RPCShort.class);
        }

        @Override
        protected RPCShort performWrap(Object obj) {
            short value = ((Number)obj).shortValue();
            return new RPCShort(value);
        }

        @Override
        protected <O> O performUnwrap(RPCShort obj, Class<O> type) {
            if (type.equals(Short.class)) return type.cast(obj.object);
            else if (type.equals(Integer.class)) return type.cast(obj.object.intValue());

            throw new UnsupportedOperationException(this.getClass().getName() + " can't unwrap " + type.getName());
        }

        @Override
        protected RPCShort performUnmarshall(MessageChannel channel, Class<? extends RPCShort> type) {
            int lsb = channel.get(),
                msb = channel.get();
            return new RPCShort((short)(msb << 8 | lsb));
        }

        @Override
        protected boolean canLocallyWrap(Class<?> objectType) {
            return (objectType.equals(Integer.class) || objectType.equals(Short.class));
        }
    }
}
