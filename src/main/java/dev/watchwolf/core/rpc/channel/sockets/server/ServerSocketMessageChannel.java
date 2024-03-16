package dev.watchwolf.core.rpc.channel.sockets.server;

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

public class ServerSocketMessageChannel extends SocketMessageChannel implements Runnable {
    /**
     * Requested maximum length of the queue of incoming connections.
     */
    public static final int BACKLOG = 50;

    private ServerSocket serverSocket;
    private Thread socketThread;
    private List<ClientSocketMessageChannel> clients;

    public ServerSocketMessageChannel(String host, int port) {
        super(host, port);
    }

    public synchronized MessageChannel create() throws IOException {
        if (!this.isClosed()) throw new RuntimeException("Can't create an already initialized server!");

        this.serverSocket = new ServerSocket(this.port, ServerSocketMessageChannel.BACKLOG, InetAddress.getByName(this.host));
        this.clients = new ArrayList<>();
        this.socketThread = new Thread(this);
        this.socketThread.start();
        return this;
    }

    public synchronized ClientSocketMessageChannel acceptConnection() throws IOException {
        if (this.isClosed()) throw new IllegalArgumentException("You must have an open connection first!");

        int timeout = this.serverSocket.getSoTimeout();
        this.serverSocket.setSoTimeout(20); // wait 20ms for connections
        Socket clientSocket = this.serverSocket.accept();
        this.serverSocket.setSoTimeout(timeout); // restore timeout

        ClientSocketMessageChannel clientChannel = (ClientSocketMessageChannel) new ClientSocketChannelFactory(clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort()).build();
        clientChannel.create(clientSocket); // don't connect; re-use the connection
        System.out.println("Got client conencted to socket server: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

        this.clients.add(clientChannel);
        return clientChannel;
    }

    /**
     * Will send the data into the first client of the connected clients
     * @param data Bytes to send
     * @throws IOException Socket exception
     */
    @Override
    public synchronized void send(byte[] data) throws IOException {
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
    public synchronized byte[] get(int numBytes, int timeout) throws TimeoutException, IOException {
        this.clients.removeIf(ClientSocketMessageChannel::isClosed);
        if (this.clients.isEmpty()) throw new IOException("No target client to get");

        return this.clients.get(0).get(numBytes, timeout);
    }

    @Override
    public synchronized boolean isClosed() {
        return (this.serverSocket == null || this.serverSocket.isClosed());
    }

    @Override
    public synchronized boolean isEndConnected() {
        if (this.isClosed()) return false;

        return /*this.clients.stream().anyMatch(ClientSocketMessageChannel::isEndConnected)*/this.clients.size() > 0;
    }

    @Override
    public void close() throws IOException {
        synchronized (this) {
            if (this.clients != null) {
                for (ClientSocketMessageChannel client : this.clients) client.close();
                this.clients.clear(); // no connection
            }

            if (this.serverSocket != null) this.serverSocket.close();
        }
        if (this.socketThread != null) {
            try {
                this.socketThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            this.socketThread = null;
        }
    }

    @Override
    public void run() {
        while (!this.isClosed()) {
            // try to accept a connection
            try {
                this.acceptConnection();
            } catch (SocketTimeoutException ignore) {
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                Thread.sleep(1_000);
            } catch (InterruptedException ignore) {}
        }
    }
}
