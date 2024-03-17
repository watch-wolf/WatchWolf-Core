package dev.watchwolf.core.rpc.channel.sockets.server;

import dev.watchwolf.core.rpc.channel.ChannelQueue;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.SocketMessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketMessageChannel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
        if (this.isClosed()) {
            // first call; initialize server
            synchronized (this) {
                this.serverSocket = new ServerSocket(this.port, ServerSocketMessageChannel.BACKLOG, InetAddress.getByName(this.host));
                this.clients = new ArrayList<>();
            }
        }

        return this.acceptConnection();
    }

    private MessageChannel acceptConnection() throws IOException {
        if (this.isClosed()) throw new IllegalArgumentException("You must have an open connection first!");

        Socket clientSocket = null;
        while (clientSocket == null) {
            synchronized (this) {
                int timeout = this.serverSocket.getSoTimeout();
                this.serverSocket.setSoTimeout(200); // don't wait eternally
                try {
                    clientSocket = this.serverSocket.accept();
                } catch (SocketTimeoutException ignore) {}
                this.serverSocket.setSoTimeout(timeout); // restore timeout
            }

            if (clientSocket == null) {
                try {
                    Thread.sleep(800); // don't take all the resources!
                } catch (InterruptedException ignore) {}
            }
        }

        ChannelQueue clientChannelQueue = (ChannelQueue) new ClientSocketChannelFactory(clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort()).build();
        ClientSocketMessageChannel clientChannel = (ClientSocketMessageChannel) clientChannelQueue.getChannel();
        clientChannel.create(clientSocket); // don't connect; re-use the connection
        final ServerSocketMessageChannel _this = this;
        final Socket _clientSocket = clientSocket;
        clientChannel.addClientClosedListener(() -> {
            // if it's the last client, close the server
            System.out.println("Client closed event (" + _clientSocket.getInetAddress().getHostAddress() + ":" + _clientSocket.getPort() + ")");
            boolean needsClosing;
            synchronized (_this) {
                this.clients.removeIf(ClientSocketMessageChannel::isClosed);
                needsClosing = this.clients.isEmpty();
            }

            if (needsClosing) {
                System.out.println("Last client disconnected from " + _clientSocket.getInetAddress().getHostAddress() + ":" + _clientSocket.getPort() + "; closing server...");
                try {
                    _this.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        System.out.println("Got client conencted to socket server: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

        synchronized (this) {
            this.clients.add(clientChannel);
        }
        return clientChannelQueue;
    }


    @Override
    public void send(byte[] data) throws IOException {
        this.broadcast(data);
    }

    /**
     * Will send the data into all the clients
     * @param data Bytes to send
     * @throws IOException Socket exception
     */
    public synchronized void broadcast(byte[] data) throws IOException {
        this.clients.removeIf(ClientSocketMessageChannel::isClosed);
        if (this.clients.isEmpty()) throw new IOException("No target client to send");

        for (ClientSocketMessageChannel client : this.clients) client.send(data);
    }

    @Override
    public byte[] get(int numBytes, int timeout) throws TimeoutException, IOException {
        throw new UnsupportedOperationException("Use the get method from the specific client");
    }

    @Override
    public synchronized boolean isClosed() {
        return (this.serverSocket == null || this.serverSocket.isClosed());
    }

    @Override
    public synchronized boolean isEndConnected() {
        if (this.isClosed()) return false;

        return this.clients.stream().anyMatch(ClientSocketMessageChannel::isEndConnected);
    }

    @Override
    public synchronized void close() throws IOException {
        if (this.clients != null) {
            for (ClientSocketMessageChannel client : new ArrayList<>(this.clients)) client.close();
            this.clients.clear(); // no connection
        }

        if (this.serverSocket != null) this.serverSocket.close();
    }
}
