package dev.watchwolf.core.rpc.objects.types.custom.files.plugins;

import dev.watchwolf.core.entities.files.plugins.Plugin;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.types.RPCObjectWrapper;

public abstract class RPCPlugin extends RPCObjectWrapper<Plugin> {
    public RPCPlugin(Plugin object) {
        super(object);
    }

    @Override
    abstract public void send(MessageChannel channel);

    @MainSubconverter
    public static class RPCPluginConverter extends RPCConverter<RPCPlugin> {
        public RPCPluginConverter() {
            super(RPCPlugin.class);

            // RPCPlugin sub-converters
            this.addSubconverter(new RPCFilePlugin.RPCFilePluginConverter());
            this.addSubconverter(new RPCUploadedPlugin.RPCUploadedPluginConverter());
            this.addSubconverter(new RPCUsualPlugin.RPCUsualPluginConverter());
        }
    }
}

