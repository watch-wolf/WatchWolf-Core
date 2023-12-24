package dev.watchwolf.core.rpc.objects.types.natives.composited;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.TemplateClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObject;
import dev.watchwolf.core.rpc.objects.types.natives.NativeTypeRPCObject;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCShort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RPCArray extends NativeTypeRPCObject<Collection<? extends RPCObject>> {
    private final Class<? extends RPCObject> objectEntriesClass;

    public RPCArray(Collection<? extends RPCObject> object) {
        super(object);

        // try to get the entry class
        this.objectEntriesClass = (object.isEmpty() ? null : object.stream().findFirst().get().getClass());
    }

    @Override
    public void send(MessageChannel channel) throws IOException {
        new RPCShort(this.object.size()).send(channel);
        for (RPCObject entry : this.object) entry.send(channel);
    }

    public Class<? extends RPCObject> getObjectEntriesClass() {
        return this.objectEntriesClass;
    }

    public Iterator<? extends RPCObject> getIterator() {
        return super.object.iterator();
    }

    public <T> Collection<T> getObject(RPCConverter<?> masterConverter, ClassType<T> targetType) {
        Collection<T> r = new ArrayList<>();
        for (RPCObject e : this.getObject()) r.add(masterConverter.unwrap(e, targetType));
        return r;
    }

    public <T> Collection<T> getObject(RPCConverter<?> masterConverter, Class<T> targetType) {
        Collection<T> r = new ArrayList<>();
        for (RPCObject e : this.getObject()) r.add(masterConverter.unwrap(e, targetType));
        return r;
    }

    public int size() {
        return super.object.size();
    }

    @MainSubconverter
    public static class RPCStringConverter extends RPCConverter<RPCArray> {
        public RPCStringConverter() {
            super(RPCArray.class);
        }

        @Override
        protected RPCArray performWrap(Object obj) {
            Collection<?> that = (Collection<?>)obj;
            Collection<RPCObject> object = new ArrayList<>(that.size());
            for (Object o : that) object.add(this.getMasterConverter().wrap(o));
            return new RPCArray(object);
        }

        @Override
        protected <O> O performUnwrap(RPCArray obj, ClassType<O> type) {
            if (type.isAssignableFrom(Collection.class)) {
                if (!(type instanceof TemplateClassType)) throw new IllegalArgumentException("A list must be a template");
                TemplateClassType<O,?> subtype = (TemplateClassType<O,?>)type;

                Collection<Object> castedList = new ArrayList<>();
                for (RPCObject entry : obj.object) {
                    castedList.add(this.getMasterConverter().unwrap(entry, subtype.getSubtype()));
                }
                return type.cast(castedList);
            }

            throw new UnsupportedOperationException(this.getClass().getName() + " can't unwrap " + type.getName());
        }

        @Override
        protected RPCArray performUnmarshall(MessageChannel channel, ClassType<? extends RPCArray> type) throws IOException {
            // size
            int size = this.getMasterConverter().unmarshall(channel, Short.class);

            if (!(type instanceof TemplateClassType)) throw new IllegalArgumentException("A list must be a template");
            TemplateClassType<? extends RPCArray,?> subtype = (TemplateClassType<? extends RPCArray,?>)type;
            if (!subtype.getSubtype().isAssignableFrom(RPCObject.class)) throw new IllegalArgumentException("`type` sub-type must be an RPC type!");

            // elements
            Collection<RPCObject> elements = new ArrayList<>();
            ClassType<? extends RPCObject> elementsType = (TemplateClassType<? extends RPCArray,? extends RPCObject>)subtype.getSubtype();
            for (int n = 0; n < size; n++) {
                elements.add(this.getMasterConverter().unmarshall(channel, elementsType));
            }

            return new RPCArray(elements);
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.isAssignableFrom(Collection.class));
        }
    }
}
