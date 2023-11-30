package dev.watchwolf.core.rpc.channel.sockets.client;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.SocketChannelFactory;

public class ClientSocketChannelFactory extends SocketChannelFactory {
    public ClientSocketChannelFactory(String host, int port) {
        super(host, port);
    }

    @Override
    public MessageChannel build() {
        return new ClientSocketMessageChannel(this.host, this.port);
    }
}
