package dev.watchwolf.core.rpc.objects.types.custom;

import dev.watchwolf.core.entities.Container;
import dev.watchwolf.core.entities.Position;
import dev.watchwolf.core.entities.items.Item;
import dev.watchwolf.core.entities.items.ItemType;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.converter.class_type.TemplateClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObjectWrapper;
import dev.watchwolf.core.rpc.objects.types.custom.items.RPCItem;
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class RPCContainer extends RPCObjectWrapper<Container> {
    public RPCContainer(Container object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        Item []toSend = this.getObject().getItems();
        List<RPCItem> send = new ArrayList<>();

        // can't send `null` & convert to RPCItem
        RPCItem air = new RPCItem(new Item(ItemType.AIR));
        for (int n = 0; n < toSend.length; n++) {
            send.add((toSend[n] == null) ? air : new RPCItem(toSend[n]));
        }

        new RPCArray(send).send(channel);
    }

    @MainSubconverter
    public static class RPCContainerConverter extends RPCConverter<RPCContainer> {
        public RPCContainerConverter() {
            super(RPCContainer.class);
        }

        @Override
        protected RPCContainer performWrap(Object obj) {
            return new RPCContainer((Container) obj);
        }

        @Override
        protected <O> O performUnwrap(RPCContainer obj, ClassType<O> type) {
            return type.cast(obj.object);
        }

        @Override
        protected RPCContainer performUnmarshall(MessageChannel channel, ClassType<? extends RPCContainer> type) {
            RPCArray array = this.getMasterConverter().unmarshall(channel, new TemplateClassType<>(RPCArray.class, RPCItem.class));
            Iterator<RPCItem> got = (Iterator<RPCItem>)array.getIterator();
            List<Item> contents = new ArrayList<>();

            final Item air = new Item(ItemType.AIR);
            got.forEachRemaining(e -> {
                Item item = e.getObject();
                contents.add(item.equals(air) ? null : item);
            });

            return new RPCContainer(new Container(contents));
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.equals(Container.class));
        }
    }
}
