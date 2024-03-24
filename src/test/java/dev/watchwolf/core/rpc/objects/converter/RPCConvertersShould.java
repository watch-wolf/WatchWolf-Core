package dev.watchwolf.core.rpc.objects.converter;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
    public void exportZipFiles() throws Exception {
        // TODO refactor using FS mock com.google.jimfs:jimfs
        File outPath = new File("test-out/");
        HashMap<String,String> zipContents = new HashMap<>();
        zipContents.put("file1.txt", "This is the file 1.");
        zipContents.put("file2.txt", "This is the file 2.");


        // delete the contents (if any)
        String[]entries = outPath.list();
        if (entries != null) {
            for (String s : entries) {
                File currentFile = new File(outPath.getPath(), s);
                currentFile.delete();
            }
        }


        RPCObjectsConverterFactory factory = new RPCObjectsConverterFactory();
        RPCConverter<?> converters = factory.build();

        ZipFile file = new ZipFile("src/test/resources/zip_file_with_two_files.zip", "./");

        RPCObject wrappedObject = converters.wrap(file);
        assertTrue(wrappedObject instanceof RPCConfigFile);
        Object unwrappedObject = converters.unwrap(wrappedObject, ConfigFile.class);
        assertEquals(file.getClass(), unwrappedObject.getClass()); // even after unwrapping as a ConfigFile, we're getting a ZipFile

        // export
        ZipFile got = (ZipFile)unwrappedObject;
        got.exportToDirectory(outPath);

        // validate the results
        entries = outPath.list();
        assertNotEquals(null, entries);
        assertEquals(zipContents.size(), entries.length); // we expect only those files
        for (String e : entries) {
            String expecting = zipContents.get(e);
            assertNotEquals(null, expecting, "Couldn't find file " + e);
            assertEquals(expecting, readFile(new File(outPath.getPath(), e))); // does the files contains the same as expected?
        }
    }

    private static String readFile(File f) throws IOException {
        StringBuilder sb = new StringBuilder();
        Scanner myReader = new Scanner(f);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            sb.append(data);
        }
        myReader.close();
        return sb.toString();
    }
}
