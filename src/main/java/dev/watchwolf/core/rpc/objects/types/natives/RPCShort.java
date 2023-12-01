package dev.watchwolf.core.rpc.objects.types.natives;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.types.RPCObject;

public class RPCShort extends NativeTypeRPCObject<Integer> {
    public RPCShort(Integer object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        int lsb = this.object & 0xFF,
            msb = (this.object >> 8)&0xFF;
        channel.send((byte)lsb, (byte)msb);
    }

    @MainSubconverter
    public static class RPCShortConverter extends RPCConverter {
        public RPCShortConverter() {
            super(RPCShort.class);
        }

        @Override
        protected RPCObject performWrap(Object obj) {
            int value = ((Number)obj).intValue();
            return new RPCShort(value);
        }

        @Override
        protected Object performUnwrap(RPCObject obj) {
            return ((RPCShort)obj).object;
        }

        @Override
        protected RPCObject performUnmarshall(MessageChannel channel, Class<? extends RPCObject> type) {
            int lsb = channel.get(1)[0],
                msb = channel.get(1)[0];
            return new RPCShort(msb << 8 | lsb);
        }

        @Override
        protected boolean canLocallyWrap(Class<?> objectType) {
            return (objectType.equals(Integer.class) || objectType.equals(Short.class));
        }
    }
}
