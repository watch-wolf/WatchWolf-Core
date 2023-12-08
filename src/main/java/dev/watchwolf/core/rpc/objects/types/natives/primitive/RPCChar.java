package dev.watchwolf.core.rpc.objects.types.natives.primitive;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.types.natives.NativeTypeRPCObject;

public class RPCChar extends NativeTypeRPCObject<Character> {
    public RPCChar(Character object) {
        super(object);
    }

    public RPCChar(char object) {
        this((Character)object);
    }

    @Override
    public void send(MessageChannel channel) {
        channel.send((byte)this.object.charValue());
    }

    @MainSubconverter
    public static class RPCCharConverter extends RPCConverter<RPCChar> {
        public RPCCharConverter() {
            super(RPCChar.class);
        }

        @Override
        protected RPCChar performWrap(Object obj) {
            return new RPCChar((Character) obj);
        }

        @Override
        protected <O> O performUnwrap(RPCChar obj, ClassType<O> type) {
            if (type.equals(Character.class)) return type.cast(obj.object);

            throw new UnsupportedOperationException(this.getClass().getName() + " can't unwrap " + type.getName());
        }

        @Override
        protected RPCChar performUnmarshall(MessageChannel channel, ClassType<? extends RPCChar> type) {
            return new RPCChar((char) channel.get());
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.equals(Character.class));
        }
    }
}