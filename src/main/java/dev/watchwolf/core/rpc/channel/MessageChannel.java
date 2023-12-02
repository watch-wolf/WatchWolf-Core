package dev.watchwolf.core.rpc.channel;

import java.util.concurrent.TimeoutException;

public interface MessageChannel {
    void send(byte ...data);

    byte []get(int numBytes, int timeout) throws TimeoutException;

    default byte []get(int numBytes) {
        try {
            return this.get(numBytes, Integer.MAX_VALUE);
        } catch (TimeoutException e) {
            return null; // shouldn't reach
        }
    }

    default byte get() {
        return this.get(1)[0];
    }

    boolean areBytesAvailable();
}
