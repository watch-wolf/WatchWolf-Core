package dev.watchwolf.core.rpc.channel.sockets.client;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.SocketMessageChannel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

public class ClientSocketMessageChannel extends SocketMessageChannel {
    private Socket socket;

    public ClientSocketMessageChannel(String host, int port) {
        super(host, port);
    }

    public MessageChannel create() throws IOException {
        this.socket = new Socket(this.host, this.port);
        return this;
    }

    public MessageChannel create(Socket preparedSocket) {
        this.socket = preparedSocket;
        return this;
    }

    @Override
    public void send(byte[] data) throws IOException {
        DataOutputStream dos = new DataOutputStream(this.socket.getOutputStream());
        dos.write(data);
    }

    @Override
    public byte[] get(int numBytes, int timeout) throws TimeoutException, IOException {
        if (timeout == 0) timeout = 1; // 0 timeout is "no timeout"; set a small value

        DataInputStream dis = new DataInputStream(this.socket.getInputStream());
        this.socket.setSoTimeout(timeout);

        try {
            byte []r = new byte[numBytes];
            int outBytes = dis.read(r);
            if (outBytes != numBytes) throw new IOException("Couldn't read " + numBytes + " bytes (read " + outBytes + " instead)");
            return r;
        } catch (SocketTimeoutException ex) {
            throw new TimeoutException(ex.getMessage());
        }
    }

    @Override
    public boolean isClosed() {
        return this.socket == null || this.socket.isClosed();
    }

    @Override
    public boolean isEndConnected() {
        return this.socket != null && this.socket.isConnected();
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
    }
}
