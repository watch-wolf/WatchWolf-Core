package dev.watchwolf.core.protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private static final int MAX_BYTES_SENT = 1024;

    private final DataOutputStream output;
    private final ArrayList<Byte> contents = new ArrayList<>();

    public Message(DataOutputStream output) {
        this.output = output;
    }

    public Message(OutputStream output) {
        this(new DataOutputStream(output));
    }

    public Message(Socket socket) throws IOException {
        this(socket.getOutputStream());
    }

    public void add(byte value) {
        this.contents.add(value);
    }

    public void add(boolean value) {
        this.add(value ? (byte)0xFF : (byte)0);
    }

    public void add(double value) {
        SocketHelper.addDouble(this.contents, value);
    }

    public void add(float value) {
        SocketHelper.addFloat(this.contents, value);
    }

    public void add(short value) {
        SocketHelper.addShort(this.contents, value);
    }

    public void add(Object value) {
        if (value instanceof Byte) this.add((byte)value);
        else if (value instanceof Boolean) this.add((boolean)value);
        else if (value instanceof Double) this.add((double)value);
        else if (value instanceof Float) this.add((float)value);
        else if (value instanceof String) SocketHelper.addString(this.contents, (String)value);
        else if (value instanceof Short) this.add((short)value);
        else SocketHelper.addObject(this.contents, value);
    }

    public void send() throws IOException {
        for (List<Byte> chunk : chopped(this.contents, MAX_BYTES_SENT)) {
            this.output.write(SocketHelper.toByteArray(chunk), 0, chunk.size());
        }
    }

    static <T> List<List<T>> chopped(List<T> list, int maximumSize) {
        List<List<T>> parts = new ArrayList<>();
        for (int index = 0; index < list.size(); index += maximumSize) {
            parts.add(new ArrayList<>(list.subList(index, Math.min(list.size(), index + maximumSize))));
        }
        return parts;
    }
}
