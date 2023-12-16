package dev.watchwolf.core.rpc.objects.converter;

import dev.watchwolf.core.rpc.objects.types.RPCObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
