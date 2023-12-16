package dev.watchwolf.core.rpc.objects.types.custom;

import dev.watchwolf.core.entities.Position;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.types.RPCObjectWrapper;
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCDouble;

public class RPCPosition extends RPCObjectWrapper<Position> {
    public RPCPosition(Position object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        new RPCString(this.getObject().getWorld()).send(channel);
        new RPCDouble(this.getObject().getX()).send(channel);
        new RPCDouble(this.getObject().getY()).send(channel);
        new RPCDouble(this.getObject().getZ()).send(channel);
    }

    @MainSubconverter
    public static class RPCPositionConverter extends RPCConverter<RPCPosition> {
        public RPCPositionConverter() {
            super(RPCPosition.class);
        }

        @Override
        protected RPCPosition performWrap(Object obj) {
            return new RPCPosition((Position) obj);
        }

        @Override
        protected <O> O performUnwrap(RPCPosition obj, ClassType<O> type) {
            return type.cast(obj.object);
        }

        @Override
        protected RPCPosition performUnmarshall(MessageChannel channel, ClassType<? extends RPCPosition> type) {
            String world = this.getMasterConverter().unmarshall(channel, String.class);
            double x = this.getMasterConverter().unmarshall(channel, Double.class);
            double y = this.getMasterConverter().unmarshall(channel, Double.class);
            double z = this.getMasterConverter().unmarshall(channel, Double.class);

            Position pos = new Position(world, x, y, z);
            return new RPCPosition(pos);
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.equals(ClassTypeFactory.getType(Position.class)));
        }
    }
}
