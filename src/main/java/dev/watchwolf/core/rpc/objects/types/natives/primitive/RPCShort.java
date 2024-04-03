package dev.watchwolf.core.rpc.objects.types.natives.primitive;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.types.natives.NativeTypeRPCObject;

import java.io.IOException;

public class RPCShort extends NativeTypeRPCObject<Short> {
    public RPCShort(Short object) {
        super(object);
    }

    public RPCShort(Integer object) {
        this(object == null ? null : object.shortValue());
    }

    @Override
    public void send(MessageChannel channel) throws IOException {
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
        protected <O> O performUnwrap(RPCShort obj, ClassType<O> type) {
            if (type.equals(ClassTypeFactory.getType(Short.class))) return type.cast(obj.object);
            else if (type.equals(ClassTypeFactory.getType(Integer.class))) return type.cast(obj.object.intValue());

            throw new UnsupportedOperationException(this.getClass().getName() + " can't unwrap " + type.getName());
        }

        @Override
        protected RPCShort performUnmarshall(MessageChannel channel, ClassType<? extends RPCShort> type) throws IOException {
            int lsb = channel.get(),
                msb = channel.get();
            return new RPCShort((short)(msb << 8 | lsb));
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.equals(ClassTypeFactory.getType(Integer.class)) || objectType.equals(ClassTypeFactory.getType(Short.class)));
        }
    }
}
