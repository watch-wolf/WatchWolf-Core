package dev.watchwolf.core.rpc.channel.sockets.server;

import dev.watchwolf.core.rpc.channel.sockets.SocketMessageChannel;

public class ServerSocketMessageChannel extends SocketMessageChannel {
    public ServerSocketMessageChannel(String host, int port) {
        super(host, port);
    }

    @Override
    public void send(byte[] data) {

    }

    @Override
    public byte[] get(int numBytes, int timeout) {
        return new byte[0];
    }

    @Override
    public boolean areBytesAvailable() {
        return false;
    }
}
