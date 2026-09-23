package dev.watchwolf.core.rpc.objects;

import dev.watchwolf.core.rpc.channel.StreamMessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.RPCObjectsConverterFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Reads and writes Core entities with the wire representation used by WatchWolf components.
 */
public class RPCObjectSerializer {
    private final RPCConverter<?> converter;

    public RPCObjectSerializer() {
        this.converter = new RPCObjectsConverterFactory().build();
    }

    public void write(Object object, OutputStream output) throws IOException {
        this.converter.wrap(object).send(new StreamMessageChannel(output));
    }

    public byte[] write(Object object) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        this.write(object, output);
        return output.toByteArray();
    }

    public <T> T read(InputStream input, Class<T> type) throws IOException {
        return this.converter.unmarshall(new StreamMessageChannel(input), type);
    }
}
