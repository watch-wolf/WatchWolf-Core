package dev.watchwolf.core.rpc.channel;

public interface MessageChannel {
    void send(byte []data);

    byte []get(int numBytes, int timeout);

    default byte []get(int numBytes) {
        return this.get(numBytes, Integer.MAX_VALUE);
    }

    boolean areBytesAvailable();
}
