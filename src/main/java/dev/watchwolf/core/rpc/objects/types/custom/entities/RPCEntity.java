package dev.watchwolf.core.rpc.objects.types.custom.entities;

import dev.watchwolf.core.entities.Position;
import dev.watchwolf.core.entities.entities.DroppedItem;
import dev.watchwolf.core.entities.entities.Entity;
import dev.watchwolf.core.entities.entities.EntityType;
import dev.watchwolf.core.entities.items.Item;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.TemplateClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObjectWrapper;
import dev.watchwolf.core.rpc.objects.types.custom.RPCPosition;
import dev.watchwolf.core.rpc.objects.types.custom.items.RPCItem;
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCEnum;
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString;

import java.lang.reflect.Constructor;

public class RPCEntity extends RPCObjectWrapper<Entity> {
    public RPCEntity(Entity object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        Entity e = this.getObject();

        new RPCEnum(e.getType()).send(channel);
        new RPCPosition(e.getPosition()).send(channel);
        new RPCString(e.getUUID()).send(channel);

        if (e instanceof DroppedItem) new RPCItem(((DroppedItem)e).getItem()).send(channel); // TODO individual class
    }

    @MainSubconverter
    public static class RPCEntityConverter extends RPCConverter<RPCEntity> {
        public RPCEntityConverter() {
            super(RPCEntity.class);
        }

        @Override
        protected RPCEntity performWrap(Object obj) {
            return new RPCEntity((Entity) obj);
        }

        @Override
        protected <O> O performUnwrap(RPCEntity obj, ClassType<O> type) {
            return type.cast(obj.object);
        }

        @Override
        protected RPCEntity performUnmarshall(MessageChannel channel, ClassType<? extends RPCEntity> type) {
            EntityType entityType = (EntityType) this.getMasterConverter().unmarshall(channel, new TemplateClassType<>(RPCEnum.class, EntityType.class)).getObject();

            Position entityPos = this.getMasterConverter().unmarshall(channel, RPCPosition.class).getObject();
            String uuid = this.getMasterConverter().unmarshall(channel, RPCString.class).getObject();

            // TODO individual class
            if (entityType.equals(EntityType.DROPPED_ITEM)) {
                Item droppedItem = this.getMasterConverter().unmarshall(channel, RPCItem.class).getObject();

                return new RPCEntity(new DroppedItem(uuid, entityPos, droppedItem));
            }
            else {
                try {
                    // get the entity from the `dev.watchwolf.core.entities.entities` package
                    Class<? extends Entity> entityClass = Class.forName("dev.watchwolf.core.entities.entities." + entityType.toClassName()).asSubclass(Entity.class);
                    Constructor<? extends Entity> entityConstructor = entityClass.getConstructor(String.class, Position.class);
                    Entity instance = entityConstructor.newInstance(uuid, entityPos);
                    return new RPCEntity(instance);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null; // failed to get the class
                }
            }
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.isAssignableFrom(Entity.class));
        }
    }
}