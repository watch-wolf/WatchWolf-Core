package dev.watchwolf.core.rpc.channel.sockets;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketMessageChannel;
import dev.watchwolf.core.utils.StateChangeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(30)
public class ITSocketMessageChannelShould {
    @Test
    public void sendAndReceiveData() throws Exception {
        int port = 8900;
        MessageChannel server = null,
                        client = null;
        Thread serverThread = null;

        byte[] toSend = {(byte) 0, (byte) 1, (byte) 2};

        try {
            server = new ServerSocketChannelFactory("0.0.0.0", port).build();

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
            StateChangeUtils.pollForCondition(() -> !_server.isClosed(), 4_000,
                    "Expected opened server; got closed one instead");

            client = new ClientSocketChannelFactory("127.0.0.1", port).build().create();

            // wait for user to connect
            StateChangeUtils.pollForCondition(() -> ((ServerSocketMessageChannel)_server).isEndConnected(), 8_000);

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

        int port = 8900;
        MessageChannel server = null, client = null;
        Thread serverThread = null;

        byte[] toSend = new byte[numBytes];
        for (int i = 0; i < numBytes; i++) toSend[i] = (byte) (i % 128);

        try {
            server = new ServerSocketChannelFactory("0.0.0.0", port).build();

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
            StateChangeUtils.pollForCondition(() -> !_server.isClosed(), 1_600,
                                            "Expected opened server; got closed one instead");

            client = new ClientSocketChannelFactory("127.0.0.1", port).build().create();

            // wait for user to connect
            StateChangeUtils.pollForCondition(() -> ((ServerSocketMessageChannel)_server).isEndConnected(), 8_000,
                    "Expected client connected; got empty instead");

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
        int port = 8900;
        MessageChannel server = null, client = null;
        Thread serverThread = null;

        try {
            server = new ServerSocketChannelFactory("0.0.0.0", port).build();

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
            StateChangeUtils.pollForCondition(() -> !_server.isClosed(), 1_600,
                                                "Expected opened server; got closed one instead");

            client = new ClientSocketChannelFactory("127.0.0.1", port).build().create();

            // wait for user to connect
            StateChangeUtils.pollForCondition(() -> ((ServerSocketMessageChannel)_server).isEndConnected(), 8_000,
                    "Expected client connected; got empty instead");

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

    @Test
    public void restoreAClosedConnection() throws Exception {
        int port = 8900;
        MessageChannel server = null,
                client1 = null,
                client2 = null;
        Thread serverThread = null;

        byte[] toSend = {(byte) 0, (byte) 1, (byte) 2};

        try {
            server = new ServerSocketChannelFactory("0.0.0.0", port).build();

            final MessageChannel _server = server;
            final AtomicBoolean clientWasClosed = new AtomicBoolean(false);
            final AtomicReference<MessageChannel> serverInstance = new AtomicReference<>();
            serverThread = new Thread(() -> {
                try {
                    MessageChannel connection = _server.create();
                    System.out.println("A connection was established");
                    connection.close();
                    synchronized (clientWasClosed) {
                        clientWasClosed.set(true);
                    }
                    System.out.println("Waiting for second connection...");
                    serverInstance.set(_server.create());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            serverThread.start();

            // wait for it to start
            StateChangeUtils.pollForCondition(() -> !_server.isClosed(), 4_000,
                    "Expected opened server; got closed one instead");

            client1 = new ClientSocketChannelFactory("127.0.0.1", port).build().create();

            // wait for user to connect&disconnect
            StateChangeUtils.pollForCondition(() -> {
                synchronized (clientWasClosed) {
                    return clientWasClosed.get();
                }
            }, 8_000, "Expected client 1 to be closed, but never reached that section");
            client1 = null;

            client2 = new ClientSocketChannelFactory("127.0.0.1", port).build().create();

            // wait for user to connect
            StateChangeUtils.pollForCondition(() -> ((ServerSocketMessageChannel)_server).isEndConnected(), 8_000,
                    "Expected client 2 to connect; got otherwise instead");

            client2.send(toSend);

            byte[] got = serverInstance.get().get(toSend.length, 5000);
            assertEquals(toSend.length, got.length, "Different length got");

            assertTrue(Arrays.equals(toSend, got), "Got different between sent and got. Sent: " + Arrays.toString(toSend) + "; got: " + Arrays.toString(got));
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (server != null) server.close();
            if (client1 != null) client1.close();
            if (client2 != null) client2.close();
            if (serverThread != null) serverThread.join(8_000);
        }
    }
}
