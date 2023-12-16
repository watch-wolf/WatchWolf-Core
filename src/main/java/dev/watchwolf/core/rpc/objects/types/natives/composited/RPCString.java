package dev.watchwolf.core.rpc.objects.types.natives.composited;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.converter.class_type.TemplateClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObject;
import dev.watchwolf.core.rpc.objects.types.natives.NativeTypeRPCObject;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCChar;

import java.util.ArrayList;
import java.util.Collection;

public class RPCString extends NativeTypeRPCObject<String> {
    public RPCString(String object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        Collection<RPCChar> data = new ArrayList<>();
        for (char c : this.object.toCharArray()) data.add(new RPCChar(c));
        new RPCArray(data).send(channel);
    }

    @MainSubconverter
    public static class RPCStringConverter extends RPCConverter<RPCString> {
        public RPCStringConverter() {
            super(RPCString.class);
        }

        @Override
        protected RPCString performWrap(Object obj) {
            return new RPCString((String) obj);
        }

        @Override
        protected <O> O performUnwrap(RPCString obj, ClassType<O> type) {
            return type.cast(obj.object);
        }

        @Override
        protected RPCString performUnmarshall(MessageChannel channel, ClassType<? extends RPCString> type) {
            // strings are arrays of characters
            RPCArray array = this.getMasterConverter().unmarshall(channel, new TemplateClassType<>(RPCArray.class, RPCChar.class));

            // convert to string
            final StringBuilder sb = new StringBuilder();
            array.getIterator().forEachRemaining(e -> {
                if (!(e instanceof RPCChar)) throw new RuntimeException("Expected elements to be char; found " + e.getClass() + " instead");
                sb.append(((RPCChar)e).getObject());
            });

            return new RPCString(sb.toString());
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.equals(ClassTypeFactory.getType(String.class)));
        }
    }
}
