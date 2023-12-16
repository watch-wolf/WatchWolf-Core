package dev.watchwolf.core.rpc.objects.types.custom.items;

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
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCEnum;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte;

public class RPCItem extends RPCObjectWrapper<Item> {
    public RPCItem(Item object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        new RPCEnum(this.getObject().getType()).send(channel);
        new RPCByte(this.getObject().getAmount()).send(channel);
    }

    @MainSubconverter
    public static class RPCItemConverter extends RPCConverter<RPCItem> {
        public RPCItemConverter() {
            super(RPCItem.class);
        }

        @Override
        protected RPCItem performWrap(Object obj) {
            return new RPCItem((Item) obj);
        }

        @Override
        protected <O> O performUnwrap(RPCItem obj, ClassType<O> type) {
            return type.cast(obj.object);
        }

        @Override
        protected RPCItem performUnmarshall(MessageChannel channel, ClassType<? extends RPCItem> type) {
            ItemType itemType = (ItemType)this.getMasterConverter().unmarshall(channel, new TemplateClassType<>(RPCEnum.class, ItemType.class)).getObject();
            RPCByte amount = this.getMasterConverter().unmarshall(channel, RPCByte.class);

            Item item = new Item(itemType, amount.getObject());
            return new RPCItem(item);
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.equals(ClassTypeFactory.getType(Item.class)));
        }
    }
}