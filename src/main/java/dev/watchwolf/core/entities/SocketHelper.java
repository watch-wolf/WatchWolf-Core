package dev.watchwolf.core.entities;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SocketHelper {
    public static byte []toByteArray(List<Byte> bytes) {
        byte []r = new byte[bytes.size()];
        for (int x = 0; x < r.length; x++) r[x] = bytes.get(x);
        return r;
    }





    public static void addBool(ArrayList<Byte> out, boolean b) {
        if (b) out.add((byte) 0xFF);
        else out.add((byte) 0);
    }

    public static boolean readBool(DataInputStream dis) throws IOException {
        return dis.readUnsignedByte() != 0x00;
    }

    public static void fill(ArrayList<Byte> out, int bytes) {
        for (int n = 0; n < bytes; n++) out.add((byte)0);
    }

    public static void discard(DataInputStream dis, int bytes) throws IOException {
        for (int n = 0; n < bytes; n++) dis.readUnsignedByte();
    }
}
