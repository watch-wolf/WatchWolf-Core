package dev.watchwolf.core.rpc.objects.types.natives.primitive;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.types.natives.NativeTypeRPCObject;

import java.io.IOException;

public class RPCDouble extends NativeTypeRPCObject<Double> {
    public RPCDouble(Double object) {
        super(object);
    }

    public RPCDouble(Float object) {
        this(object == null ? null : object.doubleValue());
    }

    @Override
    public void send(MessageChannel channel) throws IOException {
        long lng = Double.doubleToLongBits(this.object);
        for(int i = 0; i < 8; i++) channel.send((byte)((lng >> ((7 - i) * 8)) & 0xff));
    }

    @MainSubconverter
    public static class RPCDoubleConverter extends RPCConverter<RPCDouble> {
        public RPCDoubleConverter() {
            super(RPCDouble.class);
        }

        @Override
        protected RPCDouble performWrap(Object obj) {
            double value = ((Number)obj).doubleValue();
            return new RPCDouble(value);
        }

        @Override
        protected <O> O performUnwrap(RPCDouble obj, ClassType<O> type) {
            if (type.equals(ClassTypeFactory.getType(Double.class))) return type.cast(obj.object);
            else if (type.equals(ClassTypeFactory.getType(Float.class))) return type.cast(obj.object.floatValue());

            throw new UnsupportedOperationException(this.getClass().getName() + " can't unwrap " + type.getName());
        }

        @Override
        protected RPCDouble performUnmarshall(MessageChannel channel, ClassType<? extends RPCDouble> type) throws IOException {
            long lng = 0;
            for (int i = 0; i < 8; i++) lng = (lng << 8) | channel.get();
            return new RPCDouble(Double.longBitsToDouble(lng));
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.equals(ClassTypeFactory.getType(Double.class)) || objectType.equals(ClassTypeFactory.getType(Float.class)));
        }
    }
}
