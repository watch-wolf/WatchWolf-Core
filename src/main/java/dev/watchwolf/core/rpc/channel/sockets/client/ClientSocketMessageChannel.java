package dev.watchwolf.core.rpc.channel.sockets.client;

import dev.watchwolf.core.rpc.channel.ChannelQueue;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.SocketMessageChannel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ClientSocketMessageChannel extends SocketMessageChannel {
    /**
     * Max bytes that can be sent to a socket.
     * It is 65536, but I can only get 65482 (maybe because of the header?), so we won't send more than that amount.
     * For more information check <a href="https://www.ibm.com/docs/en/ztpf/2020?topic=apis-send-send-data-connected-socket">IBM - Send data on a connected socket</a>.
     */
    public static final int MAX_SOCKET_MESSAGE_LENGTH = 65482;

    private Socket socket;

    private boolean isClosed;

    private List<Runnable> onClientClosedListener;

    private boolean connectedEnd;

    private final ChannelQueue byteQueue;

    public ClientSocketMessageChannel(String host, int port) {
        super(host, port);
        this.connectedEnd = false;
        this.onClientClosedListener = new ArrayList<>();

        final ClientSocketMessageChannel _this = this;
        this.byteQueue = new ChannelQueue(new MessageChannel() {
            @Override
            public void send(byte... data) throws IOException {
                DataOutputStream dos = new DataOutputStream(_this.socket.getOutputStream());
                for (byte []maxLenghtData : splitBytes(data, MAX_SOCKET_MESSAGE_LENGTH)) dos.write(maxLenghtData);
                _this.connectedEnd = true; // end is connected
            }

            @Override
            public byte[] get(int numBytes, int timeout) throws TimeoutException, IOException {
                int originalTimeout = _this.socket.getSoTimeout();
                try {
                    _this.socket.setSoTimeout(timeout);

                    DataInputStream dis = new DataInputStream(_this.socket.getInputStream());

                    try {
                        byte[] r = new byte[numBytes];
                        segmentedRead(dis, r);
                        _this.connectedEnd = true; // end is connected
                        return r;
                    } catch (SocketTimeoutException ex) {
                        _this.connectedEnd = true; // end is connected
                        throw new TimeoutException(ex.getMessage());
                    }
                } finally {
                    _this.socket.setSoTimeout(originalTimeout);
                }
            }

            @Override
            public boolean areBytesAvailable() {
                throw new UnsupportedOperationException();
            }

            @Override
            public MessageChannel create() throws IOException {
                _this.socket = new Socket(_this.host, _this.port);
                _this.isClosed = false;
                return _this;
            }

            @Override
            public void close() throws IOException {
                if (_this.isClosed) return; // already closed

                _this.isClosed = true;
                if (!_this.socket.isClosed()) _this.socket.close();

                // invoke closed events
                for (Runnable listener : _this.onClientClosedListener) listener.run();
            }

            @Override
            public boolean isClosed() {
                if (_this.isClosed) return true;

                if (!_this.socket.isClosed() && !_this.disconnectedEnd()) return false; // still running
                else {
                    // it is actually closed, but not officially
                    // close it on the next tick
                    new Thread(() -> {
                        try {
                            _this.close();
                        } catch (IOException ignore) {}
                    }).start();

                    return true;
                }
            }
        });
    }

    public MessageChannel create() throws IOException {
        return this.byteQueue.create();
    }

    public synchronized void addClientClosedListener(Runnable onClientClosedListener) {
        this.onClientClosedListener.add(onClientClosedListener);
    }

    public MessageChannel create(Socket preparedSocket) throws IOException {
        this.socket = preparedSocket;
        this.isClosed = false;
        return this;
    }

    @Override
    public synchronized void send(byte[] data) throws IOException {
        this.byteQueue.send(data);
    }

    @Override
    public synchronized byte[] get(int numBytes, int timeout) throws TimeoutException, IOException {
        if (timeout == 0) timeout = 1; // 0 timeout is "no timeout"; set a small value

        return this.byteQueue.get(numBytes, timeout);
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
    public synchronized boolean isClosed() {
        return this.byteQueue.isClosed();
    }

    @Override
    public boolean areBytesAvailable() {
        return this.byteQueue.areBytesAvailable();
    }

    @Override
    public synchronized boolean isEndConnected() {
        try {
            byte []got = this.byteQueue.get(1, 5);
            this.byteQueue.pushBack(got);
            return true; // succeed
        } catch (TimeoutException ex) {
            return true; // not failed
        } catch (IOException ex) {
            return false; // failed
        }
    }

    private synchronized boolean disconnectedEnd() {
        return (this.connectedEnd && !this.isEndConnected()); // was connected and now it isn't
    }

    @Override
    public synchronized void close() throws IOException {
        this.byteQueue.close();
    }

    /**
     * Splits an array into smaller arrays of a fixed size
     * @author https://stackoverflow.com/a/32179121/9178470
     *
     * @param data Array to split
     * @param chunkSize Max size of a sub-array
     * @return Array of chunk-sized arrays
     */
    public static byte[][] splitBytes(final byte[] data, final int chunkSize) {
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
