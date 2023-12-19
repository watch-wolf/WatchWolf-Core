package dev.watchwolf.core.rpc.objects.types.natives.primitive;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.types.natives.NativeTypeRPCObject;

import java.io.IOException;

public class RPCByte extends NativeTypeRPCObject<Byte> {
    public RPCByte(Byte object) {
        super(object);
    }

    public RPCByte(byte object) {
        this((Byte)object);
    }

    public RPCByte() {
        this((byte) 0); // empty
    }

    @Override
    public void send(MessageChannel channel) throws IOException {
        channel.send(this.object);
    }

    public int getUnsignedObject() {
        return Byte.toUnsignedInt(this.getObject());
    }

    @MainSubconverter
    public static class RPCByteConverter extends RPCConverter<RPCByte> {
        public RPCByteConverter() {
            super(RPCByte.class);
        }

        @Override
        protected RPCByte performWrap(Object obj) {
            return new RPCByte((Byte) obj);
        }

        @Override
        protected <O> O performUnwrap(RPCByte obj, ClassType<O> type) {
            return type.cast(obj.object);
        }

        @Override
        protected RPCByte performUnmarshall(MessageChannel channel, ClassType<? extends RPCByte> type) throws IOException {
            return new RPCByte(channel.get());
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.equals(ClassTypeFactory.getType(Byte.class)));
        }
    }
}
