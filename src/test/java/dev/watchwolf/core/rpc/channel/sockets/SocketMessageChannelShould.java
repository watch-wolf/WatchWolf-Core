package dev.watchwolf.core.rpc.channel.sockets;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketMessageChannel;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SocketMessageChannelShould {
    @Test
    public void sendAndReceiveData() throws Exception {
        String host = "127.0.0.1";
        int port = 8900;
        MessageChannel server = null, client = null;

        byte[] toSend = {(byte) 0, (byte) 1, (byte) 2};

        try {
            server = new ServerSocketChannelFactory(host, port).build().create();
            client = new ClientSocketChannelFactory(host, port).build().create();

            ((ServerSocketMessageChannel)server).acceptConnection(); // allow client to connect

            client.send(toSend);

            byte[] got = server.get(toSend.length, 5000);
            assertEquals(toSend.length, got.length, "Different length got");

            assertTrue(Arrays.equals(toSend, got), "Got different between sent and got. Sent: " + Arrays.toString(toSend) + "; got: " + Arrays.toString(got));
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (server != null) server.close();
            if (client != null) client.close();
        }
    }

    @Test
    public void sendHugeData() throws Exception {
        int numBytes = 65536*4; // 4 times more than the limit

        String host = "127.0.0.1";
        int port = 8900;
        MessageChannel server = null, client = null;

        byte[] toSend = new byte[numBytes];
        for (int i = 0; i < numBytes; i++) toSend[i] = (byte) (i % 128);

        try {
            server = new ServerSocketChannelFactory(host, port).build().create();
            client = new ClientSocketChannelFactory(host, port).build().create();

            ((ServerSocketMessageChannel)server).acceptConnection(); // allow client to connect

            client.send(toSend);

            byte[] got = server.get(toSend.length, 5000);
            assertEquals(toSend.length, got.length, "Different length got");

            assertTrue(Arrays.equals(toSend, got), "Got different between sent and got. Sent: " + Arrays.toString(toSend) + "; got: " + Arrays.toString(got));
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (server != null) server.close();
            if (client != null) client.close();
        }
    }
}
