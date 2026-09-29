package dev.watchwolf.core.rpc.channel;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

/**
 * Adapts ordinary Java streams to the channel interface used by the RPC object converters.
 * The streams remain owned by the caller and are therefore not closed by this adapter.
 */
public class StreamMessageChannel implements MessageChannel {
    private final InputStream input;
    private final OutputStream output;

    public StreamMessageChannel(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public StreamMessageChannel(InputStream input) {
        this(input, null);
    }

    public StreamMessageChannel(OutputStream output) {
        this(null, output);
    }

    @Override
    public void send(byte... data) throws IOException {
        if (this.output == null) throw new IOException("This channel has no output stream");
        this.output.write(data);
    }

    @Override
    public byte[] get(int numBytes, int timeout) throws IOException, TimeoutException {
        if (this.input == null) throw new IOException("This channel has no input stream");

        byte[] result = new byte[numBytes];
        int offset = 0;
        while (offset < result.length) {
            int read = this.input.read(result, offset, result.length - offset);
            if (read < 0) throw new EOFException("Expected " + numBytes + " bytes, but the stream ended after " + offset);
            offset += read;
        }
        return result;
    }

    @Override
    public boolean areBytesAvailable() {
        if (this.input == null) return false;
        try {
            return this.input.available() > 0;
        } catch (IOException ignored) {
            return false;
        }
    }

    @Override
    public MessageChannel create() {
        throw new UnsupportedOperationException("A stream-backed channel cannot create another stream");
    }

    @Override
    public void close() {
        // The caller owns the streams.
    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
