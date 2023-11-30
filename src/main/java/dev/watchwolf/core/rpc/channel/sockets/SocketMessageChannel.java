package dev.watchwolf.core.rpc.channel.sockets;

import dev.watchwolf.core.rpc.channel.MessageChannel;

public class SocketMessageChannel implements MessageChannel {
    private final String host;
    private final int port;

    public SocketMessageChannel(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void send(byte[] data) {

    }

    @Override
    public byte[] get(int numBytes, int timeout) {
        return null;
    }

    @Override
    public boolean areBytesAvailable() {
        return false;
    }
}
