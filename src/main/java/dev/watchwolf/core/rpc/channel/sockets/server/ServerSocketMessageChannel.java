package dev.watchwolf.core.rpc.channel.sockets.server;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.SocketMessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketMessageChannel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ServerSocketMessageChannel extends SocketMessageChannel {
    /**
     * Requested maximum length of the queue of incoming connections.
     */
    public static final int BACKLOG = 50;

    private ServerSocket serverSocket;
    private List<ClientSocketMessageChannel> clients;

    public ServerSocketMessageChannel(String host, int port) {
        super(host, port);
    }

    public MessageChannel create() throws IOException {
        this.serverSocket = new ServerSocket(this.port, ServerSocketMessageChannel.BACKLOG, InetAddress.getByName(this.host));
        this.clients = new ArrayList<>();
        return this;
    }

    public ClientSocketMessageChannel acceptConnection() throws IOException {
        if (this.isClosed()) throw new IllegalArgumentException("You must have an open connection first!");
        Socket clientSocket = this.serverSocket.accept();

        ClientSocketMessageChannel clientChannel = (ClientSocketMessageChannel) new ClientSocketChannelFactory(clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort()).build();
        clientChannel.create(clientSocket); // don't connect; re-use the connection

        this.clients.add(clientChannel);
        return clientChannel;
    }

    /**
     * Will send the data into the first client of the connected clients
     * @param data Bytes to send
     * @throws IOException Socket exception
     */
    @Override
    public void send(byte[] data) throws IOException {
        this.clients.removeIf(ClientSocketMessageChannel::isClosed);
        if (this.clients.isEmpty()) throw new IOException("No target client to send");

        this.clients.get(0).send(data);
    }

    /**
     * Will try to get data from the first client of the connected clients
     * @param numBytes Number of bytes to get
     * @param timeout Max time until wait
     * @return Read bytes
     * @throws TimeoutException Max waiting time reached
     * @throws IOException Socket exception
     */
    @Override
    public byte[] get(int numBytes, int timeout) throws TimeoutException, IOException {
        this.clients.removeIf(ClientSocketMessageChannel::isClosed);
        if (this.clients.isEmpty()) throw new IOException("No target client to get");

        return this.clients.get(0).get(numBytes, timeout);
    }

    @Override
    public boolean isClosed() {
        return (this.serverSocket == null || this.serverSocket.isClosed());
    }

    @Override
    public boolean isEndConnected() {
        if (this.isClosed()) return false;

        return this.clients.stream().anyMatch(ClientSocketMessageChannel::isEndConnected);
    }

    @Override
    public void close() throws IOException {
        if (this.clients != null) {
            for (ClientSocketMessageChannel client : this.clients) client.close();
            this.clients.clear(); // no connection
        }

        if (this.serverSocket != null) this.serverSocket.close();
    }
}
