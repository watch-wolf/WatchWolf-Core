package dev.watchwolf.core.rpc.channel.sockets;

import dev.watchwolf.core.rpc.channel.MessageChannel;

public abstract class SocketMessageChannel implements MessageChannel {
    protected final String host;
    protected final int port;

    public SocketMessageChannel(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public abstract void send(byte[] data);

    @Override
    public abstract byte[] get(int numBytes, int timeout);

    @Override
    public abstract boolean areBytesAvailable();
}
