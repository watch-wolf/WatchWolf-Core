package dev.watchwolf.core.utils;

import dev.watchwolf.core.rpc.channel.MessageChannel;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class MessageChannelMock implements MessageChannel {
    private int sendingIndex;
    private final byte []toSend;
    public MessageChannelMock(byte... toSend) {
        this.toSend = toSend;
        this.sendingIndex = 0;
    }

    @Override
    public void send(byte... data) throws IOException { }

    @Override
    public byte[] get(int numBytes, int timeout) throws TimeoutException, IOException {
        if (this.sendingIndex+numBytes > this.toSend.length) throw new TimeoutException();

        int firstIndex = this.sendingIndex,
            lastIndex = this.sendingIndex+numBytes;
        this.sendingIndex += numBytes;

        return Arrays.copyOfRange(this.toSend, firstIndex, lastIndex);
    }

    @Override
    public boolean areBytesAvailable() {
        return this.sendingIndex < this.toSend.length;
    }

    @Override
    public MessageChannel create() throws IOException {
        return this;
    }

    @Override
    public void close() throws IOException { }

    @Override
    public boolean isClosed() {
        return false;
    }
}
