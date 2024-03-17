package dev.watchwolf.core.rpc.channel.sockets.server;

import dev.watchwolf.core.rpc.channel.ChannelQueue;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.SocketChannelFactory;

public class ServerSocketChannelFactory extends SocketChannelFactory {
    public ServerSocketChannelFactory(String host, int port) {
        super(host, port);
    }

    @Override
    public MessageChannel build() {
        // server socket depends on ChannelQueue to get if there's bytes queued
        return new ChannelQueue(new ServerSocketMessageChannel(this.host, this.port));
    }
}
