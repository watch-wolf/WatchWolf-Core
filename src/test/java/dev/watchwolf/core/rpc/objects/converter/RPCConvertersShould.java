package dev.watchwolf.core.rpc.objects.converter;

import dev.watchwolf.core.entities.files.plugins.Plugin;
import dev.watchwolf.core.entities.files.plugins.UsualPlugin;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.types.RPCObject;
import dev.watchwolf.core.rpc.objects.types.custom.files.plugins.RPCUsualPlugin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
