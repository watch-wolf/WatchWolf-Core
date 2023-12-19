package dev.watchwolf.core.rpc.channel;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

public class ChannelQueue implements MessageChannel {
    private MessageChannel channel;
    private final LinkedList<Byte> pushedBackBytes;

    public ChannelQueue(MessageChannel channel) {
        this.channel = channel;
        this.pushedBackBytes = new LinkedList<>();
    }

    @Override
    public void send(byte... data) throws IOException {
        this.channel.send(data);
    }

    @Override
    public byte[] get(int numBytes, int timeout) throws TimeoutException,IOException {
        byte []r = new byte[numBytes];

        // use first the queue
        int index = 0;
        for (; index < numBytes && !this.pushedBackBytes.isEmpty(); index++) {
            r[index] = this.pushedBackBytes.removeFirst();
        }

        int remainingBytes = numBytes - index;
        if (remainingBytes > 0) {
            // out of bytes in the queue
            for (byte additional : this.channel.get(remainingBytes, timeout)) {
                r[index++] = additional;
            }
        }

        return r;
    }

    public void pushBack(byte... data) {
        for (byte d : data) this.pushedBackBytes.add(d);
    }

    public void pushBack(Iterable<Byte> data) {
        for (byte d : data) this.pushedBackBytes.add(d);
    }

    @Override
    public boolean areBytesAvailable() {
        if (!this.pushedBackBytes.isEmpty()) return true;
        return this.channel.areBytesAvailable();
    }

    @Override
    public MessageChannel create() throws IOException {
        return this.channel.create();
    }

    @Override
    public void close() throws IOException {
        this.channel.close();
    }
}
