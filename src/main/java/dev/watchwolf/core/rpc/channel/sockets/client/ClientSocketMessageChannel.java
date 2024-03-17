package dev.watchwolf.core.rpc.channel.sockets.client;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.SocketMessageChannel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class ClientSocketMessageChannel extends SocketMessageChannel {
    /**
     * Max bytes that can be sent to a socket.
     * It is 65536, but I can only get 65482 (maybe because of the header?), so we won't send more than that amount.
     * For more information check <a href="https://www.ibm.com/docs/en/ztpf/2020?topic=apis-send-send-data-connected-socket">IBM - Send data on a connected socket</a>.
     */
    public static final int MAX_SOCKET_MESSAGE_LENGTH = 65482;

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
        for (byte []maxLenghtData : splitBytes(data, MAX_SOCKET_MESSAGE_LENGTH)) dos.write(maxLenghtData);
    }

    @Override
    public byte[] get(int numBytes, int timeout) throws TimeoutException, IOException {
        if (timeout == 0) timeout = 1; // 0 timeout is "no timeout"; set a small value

        DataInputStream dis = new DataInputStream(this.socket.getInputStream());
        this.socket.setSoTimeout(timeout);

        try {
            byte []r = new byte[numBytes];
            segmentedRead(dis, r);
            return r;
        } catch (SocketTimeoutException ex) {
            throw new TimeoutException(ex.getMessage());
        }
    }

    private int segmentedRead(DataInputStream dis, byte []r) throws IOException,SocketTimeoutException {
        int outBytes = 0, tmp;
        int numBytes = r.length;

        if (numBytes <= MAX_SOCKET_MESSAGE_LENGTH) {
            // we can read it in one try
            outBytes = dis.read(r);
            if (outBytes != numBytes) throw new IOException("Couldn't read " + numBytes + " bytes (read " + outBytes + " instead)");
        }
        else {
            // too big; we need to split it
            while (numBytes > 0) {
                byte []chunk = new byte[Math.min(numBytes, MAX_SOCKET_MESSAGE_LENGTH)];
                tmp = dis.read(chunk);
                if (tmp != chunk.length) throw new IOException("Couldn't read chunk of " + chunk.length + " bytes (read " + tmp + " instead)");

                System.arraycopy(chunk, 0, r, outBytes, chunk.length);

                outBytes += tmp;
                numBytes -= MAX_SOCKET_MESSAGE_LENGTH; // it doesn't matter if `numBytes` is negative; we only check if there's something left to read
            }
        }

        return outBytes;
    }

    @Override
    public boolean isClosed() {
        return this.socket == null || this.socket.isClosed();
    }

    @Override
    public boolean isEndConnected() {
        if (this.isClosed()) return false;

        return this.socket != null && this.socket.isConnected();
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
    }

    /**
     * Splits an array into smaller arrays of a fixed size
     * @author https://stackoverflow.com/a/32179121/9178470
     *
     * @param data Array to split
     * @param chunkSize Max size of a sub-array
     * @return Array of chunk-sized arrays
     */
    public static byte[][] splitBytes(final byte[] data, final int chunkSize)
    {
        final int length = data.length;
        final byte[][] dest = new byte[(length + chunkSize - 1)/chunkSize][];
        int destIndex = 0;
        int stopIndex = 0;

        for (int startIndex = 0; startIndex + chunkSize <= length; startIndex += chunkSize) {
            stopIndex += chunkSize;
            dest[destIndex++] = Arrays.copyOfRange(data, startIndex, stopIndex);
        }

        if (stopIndex < length) dest[destIndex] = Arrays.copyOfRange(data, stopIndex, length);

        return dest;
    }
}
