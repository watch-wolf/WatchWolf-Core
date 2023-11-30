package dev.watchwolf.core.rpc.channel.sockets.client;

import dev.watchwolf.core.rpc.channel.sockets.SocketMessageChannel;

public class ClientSocketMessageChannel extends SocketMessageChannel {
    public ClientSocketMessageChannel(String host, int port) {
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
