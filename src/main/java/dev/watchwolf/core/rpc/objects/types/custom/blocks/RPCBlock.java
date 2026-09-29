package dev.watchwolf.core.rpc.objects.types.custom.blocks;

import dev.watchwolf.core.entities.blocks.*;
import dev.watchwolf.core.entities.blocks.transformer.Transformers;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.MainSubconverter;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObjectWrapper;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCShort;

import java.io.IOException;

public class RPCBlock extends RPCObjectWrapper<Block> {
    public RPCBlock(Block object) {
        super(object);
    }

    @Override
    public void send(MessageChannel channel) throws IOException {
        new RPCShort(this.getObject().getID()).send(channel);
        byte[] blockData = getBlockData(this.getObject());
        for (int i = 2; i < blockData.length; i++) new RPCByte(blockData[i]).send(channel);
    }

    private static byte[] getBlockData(Block block) {
        byte[] data = new byte[RPCBlockConverter.BLOCK_SOCKET_DATA_SIZE];

        if (block instanceof Ageable) data[2] = (byte)((Ageable)block).getAge();
        if (block instanceof Orientable) {
            Orientable orientable = (Orientable)block;
            if (isOrientationSet(orientable, Orientable.Orientation.U)) data[3] |= 0b00_000001;
            if (isOrientationSet(orientable, Orientable.Orientation.D)) data[3] |= 0b00_000010;
            if (isOrientationSet(orientable, Orientable.Orientation.N)) data[3] |= 0b00_000100;
            if (isOrientationSet(orientable, Orientable.Orientation.S)) data[3] |= 0b00_001000;
            if (isOrientationSet(orientable, Orientable.Orientation.E)) data[3] |= 0b00_010000;
            if (isOrientationSet(orientable, Orientable.Orientation.W)) data[3] |= 0b00_100000;
        }
        if (block instanceof Directionable) data[3] |= (byte)(((Directionable)block).getFacingDirection().getSendData() << 6);
        if (block instanceof Groupable) data[5] |= (byte)((((Groupable)block).getGroupAmount() - 1) << 5);
        if (block instanceof Delayable) data[5] |= (byte)((((Delayable)block).getDelay() - 1) << 3);
        if (block instanceof Eyeable && ((Eyeable)block).isEyePlaced()) data[5] |= 0b000_00_1_0_0;
        if (block instanceof Hinged && ((Hinged)block).getHinge() == Hinged.Hinge.RIGHT) data[5] |= 0b000_00_0_1_0;
        if (block instanceof Openable && ((Openable)block).isOpened()) data[5] |= 1;
        if (block instanceof Staggered) data[6] = (byte)((Staggered)block).getStage();
        if (block instanceof Sectionable) data[7] |= (byte)(((Sectionable)block).getSection().getSendData() << 4);
        if (block instanceof Rotable) data[7] |= (byte)((Rotable)block).getRotation().ordinal();
        if (block instanceof Playable) data[8] = (byte)((Playable)block).getNote();
        if (block instanceof Stateful) data[9] |= (byte)(((Stateful)block).getMode().getSendData() << 4);
        if (block instanceof Leaved) data[9] |= (byte)(((Leaved)block).getLeaves().ordinal() << 2);
        if (block instanceof Ignitable && ((Ignitable)block).isIgnited()) data[9] |= 0b0000_00_1_0;
        if (block instanceof Lockable && ((Lockable)block).isLocked()) data[9] |= 0b0000_00_0_1;
        if (block instanceof Conditionable && ((Conditionable)block).isConditional()) data[11] |= 0b00000_0_0_1;
        if (block instanceof Invertable && ((Invertable)block).isInverted()) data[11] |= 0b00000_0_1_0;
        if (block instanceof Powerable && ((Powerable)block).isPowered()) data[11] |= 0b00000_1_0_0;

        return data;
    }

    private static boolean isOrientationSet(Orientable block, Orientable.Orientation orientation) {
        return block.getValidOrientations().contains(orientation) && block.isOrientationSet(orientation);
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
        protected RPCBlock performUnmarshall(MessageChannel channel, ClassType<? extends RPCBlock> type) throws IOException {
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
