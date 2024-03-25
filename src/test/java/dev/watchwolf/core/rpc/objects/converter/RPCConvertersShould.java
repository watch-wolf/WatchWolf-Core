package dev.watchwolf.core.rpc.objects.converter;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import dev.watchwolf.core.entities.WorldType;
import dev.watchwolf.core.entities.files.ConfigFile;
import dev.watchwolf.core.entities.files.ZipFile;
import dev.watchwolf.core.entities.files.plugins.Plugin;
import dev.watchwolf.core.entities.files.plugins.UsualPlugin;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.types.RPCObject;
import dev.watchwolf.core.rpc.objects.types.custom.files.RPCConfigFile;
import dev.watchwolf.core.rpc.objects.types.custom.files.plugins.RPCUsualPlugin;
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCArray;
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCChar;
import dev.watchwolf.core.utils.MessageChannelMock;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RPCConvertersShould {
    @Test
    public void wrapAndUnwrap() {
        RPCObjectsConverterFactory factory = new RPCObjectsConverterFactory();
        RPCConverter<?> converters = factory.build();

        Object workWith = 3;

        RPCObject wrappedObject = converters.wrap(3);
        Object unwrappedObject = converters.unwrap(wrappedObject, workWith.getClass());

        assertEquals(workWith, unwrappedObject);
    }

    @Test
    public void wrapAndUnwrapAbstractTypes() {
        RPCObjectsConverterFactory factory = new RPCObjectsConverterFactory();
        RPCConverter<?> converters = factory.build();

        Plugin plugin = new UsualPlugin("WorldGuard");

        RPCObject wrappedObject = converters.wrap(plugin);
        assertTrue(wrappedObject instanceof RPCUsualPlugin);
        Object unwrappedObject = converters.unwrap(wrappedObject, plugin.getClass());

        assertEquals(plugin.toString(), unwrappedObject.toString());
    }

    @Test
    public void wrapAndUnwrapArrays() {
        RPCObjectsConverterFactory factory = new RPCObjectsConverterFactory();
        RPCConverter<?> converters = factory.build();

        String msg = "Hello World!";

        RPCObject wrappedObject = converters.wrap(msg);
        assertTrue(wrappedObject instanceof RPCString);
        Object unwrappedObject = converters.unwrap(wrappedObject, msg.getClass());

        assertEquals(msg, unwrappedObject.toString());
    }

    @Test
    public void unmarshallString() throws Exception {
        RPCObjectsConverterFactory factory = new RPCObjectsConverterFactory();
        RPCConverter<?> converters = factory.build();

        MessageChannel data = new MessageChannelMock(new byte[]{
                0x06, 0x00, // 6 character-long string
                'S', 'p', 'i', 'g', 'o', 't'
        });

        String got = converters.unmarshall(data, String.class);

        assertEquals("Spigot", got);
    }

    @Test
    public void unmarshallEnums() throws Exception {
        RPCObjectsConverterFactory factory = new RPCObjectsConverterFactory();
        RPCConverter<?> converters = factory.build();

        MessageChannel data = new MessageChannelMock(new byte[]{
                0x01, 0x00, // WorldType.FLAT is index 1
        });

        WorldType got = converters.unmarshall(data, WorldType.class);

        assertEquals(WorldType.FLAT, got);
    }

    @Test
    public void exportZipFiles() throws Exception {
        try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
            Path outPath = fs.getPath("/test-out");
            Files.createDirectory(outPath);

            HashMap<String, String> zipContents = new HashMap<>();
            zipContents.put("file1.txt", "This is the file 1.");
            zipContents.put("file2.txt", "This is the file 2.");


            RPCObjectsConverterFactory factory = new RPCObjectsConverterFactory();
            RPCConverter<?> converters = factory.build();

            ZipFile file = new ZipFile("src/test/resources/zip_file_with_two_files.zip", "./");

            RPCObject wrappedObject = converters.wrap(file);
            assertTrue(wrappedObject instanceof RPCConfigFile);
            Object unwrappedObject = converters.unwrap(wrappedObject, ConfigFile.class);
            assertEquals(file.getClass(), unwrappedObject.getClass()); // even after unwrapping as a ConfigFile, we're getting a ZipFile

            // export
            ZipFile got = (ZipFile) unwrappedObject;
            got.exportToDirectory(outPath);

            // validate the results
            List<Path> entries = Files.list(outPath).collect(Collectors.toList());
            assertEquals(zipContents.size(), entries.size(), "Expected only " + zipContents.size() + " file in the output folder; got " + entries.size() + " instead: " + entries.toString());
            for (Path e : entries) {
                String fileName = e.getFileName().toString();
                String expecting = zipContents.get(fileName);
                assertNotEquals(null, expecting, "Couldn't find file " + fileName);
                assertEquals(expecting, readFile(e)); // does the files contains the same as expected?
            }
        }
    }

    @Test
    public void exportZipFileWithBinaryData() throws Exception {
        try (FileSystem fs = Jimfs.newFileSystem(Configuration.unix())) {
            Path outPath = fs.getPath("/test-out");
            Files.createDirectory(outPath);

            HashMap<String, byte[]> zipContents = new HashMap<>();
            zipContents.put("file1.bin", new byte[]{0, 1, 2, 3, 4, 5});


            RPCObjectsConverterFactory factory = new RPCObjectsConverterFactory();
            RPCConverter<?> converters = factory.build();

            ZipFile file = new ZipFile("src/test/resources/zip_file_with_a_binary_file.zip", "./");

            RPCObject wrappedObject = converters.wrap(file);
            assertTrue(wrappedObject instanceof RPCConfigFile);
            Object unwrappedObject = converters.unwrap(wrappedObject, ConfigFile.class);
            assertEquals(file.getClass(), unwrappedObject.getClass()); // even after unwrapping as a ConfigFile, we're getting a ZipFile

            // export
            ZipFile got = (ZipFile) unwrappedObject;
            got.exportToDirectory(outPath);

            // validate the results
            List<Path> entries = Files.list(outPath).collect(Collectors.toList());
            assertEquals(zipContents.size(), entries.size(), "Expected only " + zipContents.size() + " file in the output folder; got " + entries.size() + " instead: " + entries.toString());
            for (Path e : entries) {
                String fileName = e.getFileName().toString();
                byte[] expecting = zipContents.get(fileName);
                assertNotEquals(null, expecting, "Couldn't find file " + fileName);
                assertArrayEquals(expecting, readBinaryFile(e)); // does the files contains the same as expected?
            }
        }
    }

    private static String readFile(Path f) throws IOException {
        return String.join("\n", Files.readAllLines(f));
    }

    private static byte []readBinaryFile(Path f) throws IOException {
        return Files.readAllBytes(f);
    }
}
