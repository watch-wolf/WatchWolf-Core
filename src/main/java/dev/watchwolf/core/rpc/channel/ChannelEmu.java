package dev.watchwolf.core.rpc.channel;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

public class ChannelEmu implements MessageChannel {
    private ChannelQueue queue;
    private LinkedList<Byte> gotBytes;

    public ChannelEmu(ChannelQueue queue) {
        this.queue = queue;
        this.gotBytes = new LinkedList<>();
    }

    @Override
    public void send(byte... data) throws IOException {
        this.queue.send(data);
    }

    @Override
    public byte[] get(int numBytes, int timeout) throws TimeoutException,IOException {
        byte []r = this.queue.get(numBytes, timeout);
        for (byte b : r) this.gotBytes.add(b);
        return r;
    }

    /**
     * Push back all the getted bytes
     */
    public void discard() {
        this.queue.pushBack(this.gotBytes);
        this.gotBytes = new LinkedList<>();
    }

    @Override
    public boolean areBytesAvailable() {
        return this.queue.areBytesAvailable();
    }

    @Override
    public MessageChannel create() throws IOException {
        return this.queue.create();
    }

    @Override
    public void close() throws IOException {
        this.queue.close();
    }

    @Override
    public boolean isClosed() {
        return this.queue.isClosed();
    }
}
