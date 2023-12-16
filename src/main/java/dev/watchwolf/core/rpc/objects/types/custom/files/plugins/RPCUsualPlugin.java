package dev.watchwolf.core.rpc.objects.types.custom.files.plugins;

import dev.watchwolf.core.entities.files.plugins.UsualPlugin;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.Subconverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte;

public class RPCUsualPlugin extends RPCPlugin {
    public static final byte USUAL_PLUGIN_ID = (byte) 0x00;

    public RPCUsualPlugin(UsualPlugin object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        UsualPlugin plugin = (UsualPlugin) this.getObject();
        new RPCByte(USUAL_PLUGIN_ID).send(channel); // file plugin type
        new RPCString(plugin.getName()).send(channel);
        String pluginVersion = plugin.getVersion();
        new RPCString((pluginVersion == null) ? "" : pluginVersion).send(channel);
    }

    @Subconverter(RPCPluginConverter.class)
    public static class RPCUsualPluginConverter extends RPCConverter<RPCPlugin> {
        public RPCUsualPluginConverter() {
            super(RPCPlugin.class);
        }

        @Override
        protected RPCPlugin performWrap(Object obj) throws UnsupportedOperationException {
            if (obj instanceof UsualPlugin) return new RPCUsualPlugin((UsualPlugin) obj);

            throw new UnsupportedOperationException(this.getClass().getName() + " can't wrap " + obj.getClass().getName());
        }

        @Override
        protected <O> O performUnwrap(RPCPlugin obj, ClassType<O> type) throws UnsupportedOperationException {
            if (type.equals(UsualPlugin.class)) return type.cast(((RPCUsualPlugin) obj).object);

            throw new UnsupportedOperationException(this.getClass().getName() + " can't unwrap " + type.getName());
        }

        @Override
        protected RPCPlugin performUnmarshall(MessageChannel channel, ClassType<? extends RPCPlugin> type) throws UnsupportedOperationException {
            byte id = this.getMasterConverter().unmarshall(channel, RPCByte.class).getObject();
            if (id != USUAL_PLUGIN_ID) throw new UnsupportedOperationException("Got plugin of different type");

            String pluginName = this.getMasterConverter().unmarshall(channel, RPCString.class).getObject();
            String pluginVersion = this.getMasterConverter().unmarshall(channel, RPCString.class).getObject();
            return new RPCUsualPlugin(new UsualPlugin(pluginName, pluginVersion.isEmpty() ? null : pluginVersion));
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.isAssignableFrom(UsualPlugin.class));
        }
    }
}