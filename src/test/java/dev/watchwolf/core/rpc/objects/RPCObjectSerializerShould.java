package dev.watchwolf.core.rpc.objects;

import dev.watchwolf.core.entities.Position;
import dev.watchwolf.core.entities.blocks.Block;
import dev.watchwolf.core.entities.blocks.Openable;
import dev.watchwolf.core.entities.blocks.Orientable;
import dev.watchwolf.core.entities.blocks.Blocks;
import dev.watchwolf.core.entities.files.ConfigFile;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RPCObjectSerializerShould {
    @Test
    public void roundTripPosition() throws Exception {
        RPCObjectSerializer serializer = new RPCObjectSerializer();
        Position expected = new Position("world", 1.25, 64, -3.5);

        Position actual = serializer.read(new ByteArrayInputStream(serializer.write(expected)), Position.class);

        assertEquals(expected, actual);
    }

    @Test
    public void preserveBlockProperties() throws Exception {
        RPCObjectSerializer serializer = new RPCObjectSerializer();
        Block expected = (Block)((Openable)((Orientable)Blocks.ACACIA_FENCE_GATE)
                .setOrientation(Orientable.Orientation.N, true)).setOpened(true);

        Block actual = serializer.read(new ByteArrayInputStream(serializer.write(expected)), Block.class);

        assertTrue(((Orientable)actual).isOrientationSet(Orientable.Orientation.N));
        assertTrue(((Openable)actual).isOpened());
    }

    @Test
    public void serializeConfigFileSubclasses() throws Exception {
        RPCObjectSerializer serializer = new RPCObjectSerializer();
        ConfigFile expected = new DerivedConfigFile("level.dat", new byte[] {1, 2, 3}, "world");

        ConfigFile actual = serializer.read(new ByteArrayInputStream(serializer.write(expected)), ConfigFile.class);

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getExtension(), actual.getExtension());
        assertEquals(expected.getOffsetPath(), actual.getOffsetPath());
        assertEquals(3, actual.getData().length);
    }

    private static class DerivedConfigFile extends ConfigFile {
        private DerivedConfigFile(String name, byte[] data, String offset) {
            super(name, data, offset);
        }
    }
}
