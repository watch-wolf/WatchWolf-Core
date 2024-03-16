package dev.watchwolf.core.rpc.channel.sockets;

import dev.watchwolf.core.rpc.channel.MessageChannel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract class SocketMessageChannel implements MessageChannel {
    protected final String host;
    protected final int port;

    public SocketMessageChannel(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    @Override
    public abstract void send(byte[] data) throws IOException;

    @Override
    public abstract byte[] get(int numBytes, int timeout) throws TimeoutException, IOException;

    @Override
    public boolean areBytesAvailable() {
        return !this.isClosed() && this.isEndConnected(); // TODO to tell if there's bytes left in a socket use `get` with the `timeout`
    }

    @Override
    public abstract boolean isClosed();

    public abstract boolean isEndConnected();
}
