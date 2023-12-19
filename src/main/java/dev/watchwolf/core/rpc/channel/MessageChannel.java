package dev.watchwolf.core.rpc.channel;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface MessageChannel extends Closeable {
    void send(byte ...data) throws IOException;

    byte []get(int numBytes, int timeout) throws TimeoutException, IOException;

    default byte []get(int numBytes) throws IOException {
        try {
            return this.get(numBytes, Integer.MAX_VALUE);
        } catch (TimeoutException e) {
            return null; // shouldn't reach
        }
    }

    default byte get() throws IOException {
        return this.get(1)[0];
    }

    boolean areBytesAvailable();

    MessageChannel create() throws IOException;

    default void close() throws IOException {}
}
