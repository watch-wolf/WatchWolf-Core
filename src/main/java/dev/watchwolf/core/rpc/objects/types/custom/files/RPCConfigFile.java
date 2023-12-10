package dev.watchwolf.core.rpc.objects.types.custom.files;

import dev.watchwolf.core.entities.files.ConfigFile;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObjectWrapper;
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte;

public class RPCConfigFile extends RPCObjectWrapper<ConfigFile> {
    public RPCConfigFile(ConfigFile object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        ConfigFile configFile = this.getObject();

        new RPCString(configFile.getName() + ((configFile.getExtension() != null) ? ("." + configFile.getExtension()): "")).send(channel);
        new RPCString(configFile.getOffsetPath()).send(channel);

        // add a 4-byte integer
        int fileLength = configFile.getData().length;
        for (int n = 0; n < 4; n++) {
            new RPCByte((byte)(fileLength & 0xFF)).send(channel);
            fileLength >>= 8;
        }

        // add the data
        for (byte b : configFile.getData()) new RPCByte(b).send(channel);
    }

    @MainSubconverter
    public static class RPCConfigFileConverter extends RPCConverter<RPCConfigFile> {
        public RPCConfigFileConverter() {
            super(RPCConfigFile.class);
        }

        @Override
        protected RPCConfigFile performWrap(Object obj) {
            return new RPCConfigFile((ConfigFile) obj);
        }

        @Override
        protected <O> O performUnwrap(RPCConfigFile obj, ClassType<O> type) {
            if (type.equals(ConfigFile.class)) return type.cast(obj.object);

            throw new UnsupportedOperationException(this.getClass().getName() + " can't unwrap " + type.getName());
        }

        @Override
        protected RPCConfigFile performUnmarshall(MessageChannel channel, ClassType<? extends RPCConfigFile> type) {
            RPCString nameAndExtension = this.getMasterConverter().unmarshall(channel, RPCString.class);
            RPCString offsetPath = this.getMasterConverter().unmarshall(channel, RPCString.class);

            // read a 4-byte integer
            int length = 0;
            int multiplier = 0;
            for (int n = 0; n < 4; n++) {
                length |= (this.getMasterConverter().unmarshall(channel, RPCByte.class).getUnsignedObject() << multiplier);
                multiplier += 8;
            }

            byte []file = new byte[length];
            for (int n = 0; n < length; n++) file[n] = (byte)this.getMasterConverter().unmarshall(channel, RPCByte.class).getUnsignedObject();

            ConfigFile object = new ConfigFile(nameAndExtension.getObject(), file, offsetPath.getObject());
            return new RPCConfigFile(object);
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.equals(ConfigFile.class));
        }
    }
}
