package dev.watchwolf.core.protocol;

import dev.watchwolf.core.entities.ArrayAdder;
import dev.watchwolf.core.rpc.objects.RPCObjectSerializer;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SocketHelper {
    private static final RPCObjectSerializer SERIALIZER = new RPCObjectSerializer();

    private SocketHelper() {}

    public static byte[] toByteArray(List<Byte> bytes) {
        byte[] result = new byte[bytes.size()];
        for (int index = 0; index < result.length; index++) result[index] = bytes.get(index);
        return result;
    }

    public static void addRaw(ArrayList<Byte> out, Object[] file) {
        for (Byte value : (Byte[])file) out.add(value);
    }

    public static <T> void addArray(ArrayList<Byte> out, T[] array, ArrayAdder<T> arrayAdder) {
        addShort(out, array.length);
        if (array.length > 0) arrayAdder.addToArray(out, array);
    }

    public static <T> void addArray(ArrayList<Byte> out, T[] array) {
        addShort(out, array.length);
        for (T element : array) addObject(out, element);
    }

    public static void addObject(ArrayList<Byte> out, Object object) {
        try {
            for (byte value : SERIALIZER.write(object)) out.add(value);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not serialize " + object.getClass().getName(), e);
        }
    }

    public static <T> T readObject(DataInputStream input, Class<T> type) throws IOException {
        return SERIALIZER.read(input, type);
    }

    public static void addString(ArrayList<Byte> out, String value) {
        Byte[] bytes = new Byte[value.length()];
        for (int index = 0; index < bytes.length; index++) bytes[index] = (byte)value.charAt(index);
        addArray(out, bytes, SocketHelper::addRaw);
    }

    public static void addDouble(ArrayList<Byte> out, double value) {
        long bits = Double.doubleToLongBits(value);
        for (int index = 0; index < 8; index++) out.add((byte)((bits >> ((7 - index) * 8)) & 0xff));
    }

    public static void addFloat(ArrayList<Byte> out, float value) {
        addDouble(out, value);
    }

    public static void addBool(ArrayList<Byte> out, boolean value) {
        out.add(value ? (byte)0xFF : (byte)0);
    }

    public static boolean readBool(DataInputStream input) throws IOException {
        return input.readUnsignedByte() != 0;
    }

    public static void addShort(ArrayList<Byte> out, int value) {
        out.add((byte)(value & 0xFF));
        out.add((byte)((value >> 8) & 0xFF));
    }

    public static int readShort(DataInputStream input) throws IOException {
        int leastSignificant = input.readUnsignedByte();
        int mostSignificant = input.readUnsignedByte();
        return mostSignificant << 8 | leastSignificant;
    }

    public static double readDouble(DataInputStream input) throws IOException {
        long bits = 0;
        for (int index = 0; index < 8; index++) bits = bits << 8 | input.readUnsignedByte();
        return Double.longBitsToDouble(bits);
    }

    public static float readFloat(DataInputStream input) throws IOException {
        return (float)readDouble(input);
    }

    public static String readString(DataInputStream input) throws IOException {
        int size = readShort(input);
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < size; index++) result.append((char)input.read());
        return result.toString();
    }
}
