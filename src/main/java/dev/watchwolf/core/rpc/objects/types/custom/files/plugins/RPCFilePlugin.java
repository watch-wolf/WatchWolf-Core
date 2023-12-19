package dev.watchwolf.core.rpc.objects.types.custom.files.plugins;

import dev.watchwolf.core.entities.files.ConfigFile;
import dev.watchwolf.core.entities.files.plugins.FilePlugin;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.Subconverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.types.custom.files.RPCConfigFile;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte;

import java.io.IOException;

public class RPCFilePlugin extends RPCPlugin {
    public static final byte FILE_PLUGIN_ID = (byte) 0x02;

    public RPCFilePlugin(FilePlugin object) {
            super(object);
    }

    @Override
    public void send(MessageChannel channel) throws IOException {
        FilePlugin plugin = (FilePlugin)this.getObject();
        new RPCByte(FILE_PLUGIN_ID).send(channel); // file plugin type
        new RPCConfigFile(plugin.getFile()).send(channel);
    }

    @Subconverter(RPCPluginConverter.class)
    public static class RPCFilePluginConverter extends RPCConverter<RPCPlugin> {
        public RPCFilePluginConverter() {
            super(RPCPlugin.class);
        }

        @Override
        protected RPCPlugin performWrap(Object obj) throws UnsupportedOperationException {
            if (obj instanceof FilePlugin) return new RPCFilePlugin((FilePlugin) obj);

            throw new UnsupportedOperationException(this.getClass().getName() + " can't wrap " + obj.getClass().getName());
        }

        @Override
        protected <O> O performUnwrap(RPCPlugin obj, ClassType<O> type) throws UnsupportedOperationException {
            if (type.equals(ClassTypeFactory.getType(FilePlugin.class))) return type.cast(((RPCFilePlugin) obj).object);

            throw new UnsupportedOperationException(this.getClass().getName() + " can't unwrap " + type.getName());
        }

        @Override
        protected RPCPlugin performUnmarshall(MessageChannel channel, ClassType<? extends RPCPlugin> type) throws UnsupportedOperationException, IOException {
            byte id = this.getMasterConverter().unmarshall(channel, RPCByte.class).getObject();
            if (id != FILE_PLUGIN_ID) throw new UnsupportedOperationException("Got plugin of different type");

            ConfigFile configFile = this.getMasterConverter().unmarshall(channel, RPCConfigFile.class).getObject();
            return new RPCFilePlugin(new FilePlugin(configFile));
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.equals(ClassTypeFactory.getType(FilePlugin.class)));
        }
    }
}
