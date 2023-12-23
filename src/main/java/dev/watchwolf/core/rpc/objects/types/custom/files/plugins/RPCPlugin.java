package dev.watchwolf.core.rpc.objects.types.custom.files.plugins;

import dev.watchwolf.core.entities.files.plugins.Plugin;
import dev.watchwolf.core.entities.files.plugins.UploadedPlugin;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.types.RPCObject;
import dev.watchwolf.core.rpc.objects.types.RPCObjectWrapper;

import java.io.IOException;

public abstract class RPCPlugin extends RPCObjectWrapper<Plugin> {
    public RPCPlugin(Plugin object) {
        super(object);
    }

    @Override
    abstract public void send(MessageChannel channel) throws IOException;

    @MainSubconverter
    public static class RPCPluginConverter extends RPCConverter<RPCPlugin> {
        public RPCPluginConverter() {
            super(RPCPlugin.class);
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.isAssignableFrom(Plugin.class));
        }

        @Override
        protected boolean canLocallyUnwrap(ClassType<? extends RPCObject> rpcType) {
            return false;
        }
    }
}

