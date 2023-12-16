package dev.watchwolf.core.rpc.objects.types.custom.blocks;

import dev.watchwolf.core.entities.blocks.Block;
import dev.watchwolf.core.entities.blocks.Blocks;
import dev.watchwolf.core.entities.blocks.transformer.Transformers;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObjectWrapper;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCShort;

public class RPCBlock extends RPCObjectWrapper<Block> {
    public RPCBlock(Block object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) {
        new RPCShort(this.getObject().getID()).send(channel);
        // TODO send the additional arguments
        for (int i = 2; i < RPCBlockConverter.BLOCK_SOCKET_DATA_SIZE; i++) new RPCByte().send(channel);
    }

    @MainSubconverter
    public static class RPCBlockConverter extends RPCConverter<RPCBlock> {
        /**
         * Bytes that the Block packet has. The first 2 specifies the block itself,
         * and the rest adds information to the block. Refer to the
         * <a href="https://github.com/watch-wolf/WatchWolf/blob/9d3a6016b5823aba1ee61187349e13c0edfe9c5f/Standard/Protocols.pdf">API documentation</a>,
         * under subsection 2.4.9. Block.
         */
        public static final int BLOCK_SOCKET_DATA_SIZE = 56;

        public RPCBlockConverter() {
            super(RPCBlock.class);
        }

        @Override
        protected RPCBlock performWrap(Object obj) {
            return new RPCBlock((Block) obj);
        }

        @Override
        protected <O> O performUnwrap(RPCBlock obj, ClassType<O> type) {
            return type.cast(obj.object);
        }

        @Override
        protected RPCBlock performUnmarshall(MessageChannel channel, ClassType<? extends RPCBlock> type) {
            short blockId = this.getMasterConverter().unmarshall(channel, RPCShort.class).getObject();

            Block block = Blocks.getBlockById(blockId);

            int []blockData = new int[BLOCK_SOCKET_DATA_SIZE];
            // read 54 bytes (the first 2 were already readed)
            for (int i = 2; i < blockData.length; i++) blockData[i] = this.getMasterConverter().unmarshall(channel, RPCByte.class).getUnsignedObject();

            // apply block-specific properties
            if (block == null) return null;
            block = Transformers.getInstance().loadAllSocketData(block, blockData);

            return new RPCBlock(block);
        }

        @Override
        protected boolean canLocallyWrap(ClassType<?> objectType) {
            return (objectType.isAssignableFrom(Block.class));
        }
    }
}
