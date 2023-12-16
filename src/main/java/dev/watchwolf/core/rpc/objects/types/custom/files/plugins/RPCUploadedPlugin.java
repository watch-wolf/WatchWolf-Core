package dev.watchwolf.core.rpc.objects.types.custom.files.plugins;

import dev.watchwolf.core.entities.files.plugins.UploadedPlugin;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.Subconverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte;

public class RPCUploadedPlugin extends RPCPlugin {
    public static final byte UPLOADED_PLUGIN_ID = (byte) 0x01;

    public RPCUploadedPlugin(UploadedPlugin object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        UploadedPlugin plugin = (UploadedPlugin) this.getObject();
        new RPCByte(UPLOADED_PLUGIN_ID).send(channel); // file plugin type
        new RPCString(plugin.getUrl()).send(channel);
    }

    @Subconverter(RPCPluginConverter.class)
    public static class RPCUploadedPluginConverter extends RPCConverter<RPCPlugin> {
        public RPCUploadedPluginConverter() {
            super(RPCPlugin.class);
        }

        @Override
        protected RPCPlugin performWrap(Object obj) throws UnsupportedOperationException {
            if (obj instanceof UploadedPlugin) return new RPCUploadedPlugin((UploadedPlugin) obj);

            throw new UnsupportedOperationException(this.getClass().getName() + " can't wrap " + obj.getClass().getName());
        }

        @Override
        protected <O> O performUnwrap(RPCPlugin obj, ClassType<O> type) throws UnsupportedOperationException {
            if (type.isAssignableFrom(UploadedPlugin.class)) return type.cast(((RPCUploadedPlugin) obj).object);

            throw new UnsupportedOperationException(this.getClass().getName() + " can't unwrap " + type.getName());
        }

        @Override
        protected RPCPlugin performUnmarshall(MessageChannel channel, ClassType<? extends RPCPlugin> type) throws UnsupportedOperationException {
            byte id = this.getMasterConverter().unmarshall(channel, RPCByte.class).getObject();
            if (id != UPLOADED_PLUGIN_ID) throw new UnsupportedOperationException("Got plugin of different type");

            String uploadedPluginUrl = this.getMasterConverter().unmarshall(channel, RPCString.class).getObject();
            return new RPCUploadedPlugin(new UploadedPlugin(uploadedPluginUrl));
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.isAssignableFrom(UploadedPlugin.class));
        }
    }
}