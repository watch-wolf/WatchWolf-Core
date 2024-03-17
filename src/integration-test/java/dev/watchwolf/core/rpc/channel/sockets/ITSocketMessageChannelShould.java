package dev.watchwolf.core.rpc.channel.sockets;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketMessageChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(30)
public class ITSocketMessageChannelShould {
    @Test
    public void sendAndReceiveData() throws Exception {
        String host = "127.0.0.1";
        int port = 8900;
        MessageChannel server = null,
                        client = null;
        Thread serverThread = null;

        byte[] toSend = {(byte) 0, (byte) 1, (byte) 2};

        try {
            server = new ServerSocketChannelFactory(host, port).build();

            final AtomicReference<MessageChannel> serverInstance = new AtomicReference<>();
            final MessageChannel _server = server;
            serverThread = new Thread(() -> {
                try {
                    serverInstance.set(_server.create());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            serverThread.start();

            // wait for it to start
            int tries = 8;
            while (tries > 0 && server.isClosed()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignore) { }
                tries--;
            }
            assertFalse(server.isClosed(), "Expected opened server; got closed one instead");

            client = new ClientSocketChannelFactory(host, port).build().create();

            // wait for user to connect
            tries = 5;
            while (tries > 0 && !((ServerSocketMessageChannel)server).isEndConnected()) {
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException ignore) {}
                tries--;
            }
            assertTrue(((ServerSocketMessageChannel)server).isEndConnected());

            client.send(toSend);

            byte[] got = serverInstance.get().get(toSend.length, 5000);
            assertEquals(toSend.length, got.length, "Different length got");

            assertTrue(Arrays.equals(toSend, got), "Got different between sent and got. Sent: " + Arrays.toString(toSend) + "; got: " + Arrays.toString(got));
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (server != null) server.close();
            if (client != null) client.close();
            if (serverThread != null) serverThread.join(8_000);
        }
    }

    @Test
    public void sendHugeData() throws Exception {
        int numBytes = 65536*4; // 4 times more than the limit

        String host = "127.0.0.1";
        int port = 8900;
        MessageChannel server = null, client = null;
        Thread serverThread = null;

        byte[] toSend = new byte[numBytes];
        for (int i = 0; i < numBytes; i++) toSend[i] = (byte) (i % 128);

        try {
            server = new ServerSocketChannelFactory(host, port).build();

            final AtomicReference<MessageChannel> serverInstance = new AtomicReference<>();
            final MessageChannel _server = server;
            serverThread = new Thread(() -> {
                try {
                    serverInstance.set(_server.create());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            serverThread.start();

            // wait for it to start
            int tries = 8;
            while (tries > 0 && server.isClosed()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignore) { }
                tries--;
            }
            assertFalse(server.isClosed(), "Expected opened server; got closed one instead");

            client = new ClientSocketChannelFactory(host, port).build().create();

            // wait for user to connect
            tries = 5;
            while (tries > 0 && !((ServerSocketMessageChannel)server).isEndConnected()) {
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException ignore) {}
                tries--;
            }
            assertTrue(((ServerSocketMessageChannel)server).isEndConnected());

            client.send(toSend);

            byte[] got = serverInstance.get().get(toSend.length, 5000);
            assertEquals(toSend.length, got.length, "Different length got. Sent: " + Arrays.toString(toSend) + " (" + toSend.length + " bits); got: " + Arrays.toString(got) + " (" + got.length + " bits)");

            assertTrue(Arrays.equals(toSend, got), "Got different between sent and got. Sent: " + Arrays.toString(toSend) + "; got: " + Arrays.toString(got));
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (server != null) server.close();
            if (client != null) client.close();
            if (serverThread != null) serverThread.join(8_000);
        }
    }

    @Test
    public void interruptIfNoData() throws Exception {
        String host = "127.0.0.1";
        int port = 8900;
        MessageChannel server = null, client = null;
        Thread serverThread = null;

        try {
            server = new ServerSocketChannelFactory(host, port).build();

            final AtomicReference<MessageChannel> serverInstance = new AtomicReference<>();
            final MessageChannel _server = server;
            serverThread = new Thread(() -> {
                try {
                    serverInstance.set(_server.create());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            serverThread.start();

            // wait for it to start
            int tries = 8;
            while (tries > 0 && server.isClosed()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignore) { }
                tries--;
            }
            assertFalse(server.isClosed(), "Expected opened server; got closed one instead");

            client = new ClientSocketChannelFactory(host, port).build().create();

            // wait for user to connect
            tries = 5;
            while (tries > 0 && !((ServerSocketMessageChannel)server).isEndConnected()) {
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException ignore) {}
                tries--;
            }
            assertTrue(((ServerSocketMessageChannel)server).isEndConnected());

            // don't send anything

            // try to get something
            try {
                serverInstance.get().get(1, 3000);
                fail("We expected a TimeoutException; got data instead");
            } catch (TimeoutException ex) {
                // ok!
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (server != null) server.close();
            if (client != null) client.close();
            if (serverThread != null) serverThread.join(8_000);
        }
    }
}
