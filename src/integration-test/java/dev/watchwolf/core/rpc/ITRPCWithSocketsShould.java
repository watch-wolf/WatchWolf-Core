package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketMessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketMessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.utils.StateChangeUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Timeout(60)
public class ITRPCWithSocketsShould {
    public static MessageChannel getRPCMessageChannel(RPC rpc) throws Exception {
        Field messageChannelField = RPC.class.getDeclaredField("remoteConnection");
        messageChannelField.setAccessible(true);

        return (MessageChannel) messageChannelField.get(rpc);
    }

    public static ClientSocketMessageChannel getRPCMessageChannelInstance(RPC rpc) throws Exception {
        Field messageChannelField = RPC.class.getDeclaredField("linkedConnection");
        messageChannelField.setAccessible(true);

        return (ClientSocketMessageChannel) messageChannelField.get(rpc);
    }

    public static RPCImplementer getClientMock(final ArrayList<Short> got) throws IOException {
        RPCImplementer clientImplementer = mock(RPCImplementer.class);
        doAnswer((invocation) -> {
            MessageChannel channel = invocation.getArgument(0);
            RPCConverter<?> converter = invocation.getArgument(1);
            short info = converter.unmarshall(channel, Short.class); // discard event data
            if (got != null) got.add(info);
            return null;
        })
                .when(clientImplementer)
                .forwardCall(any(MessageChannel.class), any(RPCConverter.class));
        return clientImplementer;
    }

    public static ClientSocketMessageChannel getClientSocketChannelListeningForClose(RPC server, final AtomicBoolean closed) throws Exception {
        closed.set(false);

        ClientSocketMessageChannel connection = getRPCMessageChannelInstance(server);
        connection.addClientClosedListener(() -> {
            synchronized (closed) {
                if (closed.get()) throw new RuntimeException("Got second 'client closed' event");
                closed.set(true);
            }
        });

        return connection;
    }

    @Test
    public void connectOneClientSocketWithAServerSocket() throws Exception {
        int port = 8900;

        RPCImplementer serverImplementer = mock(RPCImplementer.class);
        RPCImplementer clientImplementer = getClientMock(null);

        RPCImplementerFactory serverImplementerFactory = mock(RPCImplementerFactory.class);
        when(serverImplementerFactory.build()).thenReturn(serverImplementer);

        RPCImplementerFactory clientImplementerFactory = mock(RPCImplementerFactory.class);
        when(clientImplementerFactory.build()).thenReturn(clientImplementer);

        RPC server = null,
            client = null;
        Thread serverThread = null,
                clientThread = null;
        try {
            server = new RPCFactory().build(serverImplementerFactory, new ServerSocketChannelFactory("127.0.0.1", port));
            client = new RPCFactory().build(clientImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port));
            serverThread = new Thread(server);
            clientThread = new Thread(client);
            serverThread.start();
            clientThread.start();

            // wait for them to connect
            final RPC _server = server;
            StateChangeUtils.pollForCondition(() -> _server.isRunning(), 8_000,
                                            "Expected server to be running; found stopped server otherwise");

            // get the socket objects
            ClientSocketMessageChannel serverClientConnectionSocket = getRPCMessageChannelInstance(server);
            ClientSocketMessageChannel clientSocket = getRPCMessageChannelInstance(client);

            // capture handler
            ArgumentCaptor<RPC> serverCaptor = ArgumentCaptor.forClass(RPC.class),
                    clientCaptor = ArgumentCaptor.forClass(RPC.class);
            verify(serverImplementer, times(1)).setHandler(serverCaptor.capture());
            verify(clientImplementer, times(1)).setHandler(clientCaptor.capture());

            // assert - the implementers should have been connected with its RPC
            assertEquals(server, serverCaptor.getValue(), "Expected server RPC given to the implementer; got " + serverCaptor.getValue() + " instead");
            assertEquals(client, clientCaptor.getValue(), "Expected client RPC given to the implementer; got " + clientCaptor.getValue() + " instead");

            // assert - each end should be connected
            assertTrue(server.isRunning(), "Server is not running");
            assertTrue(client.isRunning(), "Client is not running");
            assertFalse(serverClientConnectionSocket.isClosed(), "Server socket is closed");
            assertTrue(serverClientConnectionSocket.isEndConnected(), "Server socket got no client connection");
            assertFalse(clientSocket.isClosed(), "Client socket is closed");
            assertTrue(clientSocket.isEndConnected(), "Client socket got no server connection");

            // assert - we didn't send anything
            assertFalse(serverClientConnectionSocket.areBytesAvailable(), "We expected no bytes on the server socket; got bytes instead");
            assertFalse(clientSocket.areBytesAvailable(), "We expected no bytes on the client socket; got bytes instead");
        } finally {
            // close the servers
            if (server != null) server.close();
            if (client != null) client.close();

            if (serverThread != null) serverThread.join(8_000);
            if (clientThread != null) clientThread.join(8_000);
        }

        // assert - the sockets should be closed
        assertFalse(server.isRunning(), "Server is still active after turning it off");
        assertTrue(getRPCMessageChannel(server).isClosed(), "Server main socket is still active after turning it off");
        assertFalse(client.isRunning(), "Client is still active after turning it off");
        assertTrue(getRPCMessageChannel(client).isClosed(), "Client main socket is still active after turning it off");
    }

    @Test
    public void connectTwoClientSocketsWithAServerSocket() throws Exception {
        int port = 8900;

        RPCImplementer serverImplementer = getClientMock(null);
        RPCImplementer clientImplementer = mock(RPCImplementer.class);

        RPCImplementerFactory serverImplementerFactory = mock(RPCImplementerFactory.class);
        when(serverImplementerFactory.build()).thenReturn(serverImplementer);

        RPCImplementerFactory clientImplementerFactory = mock(RPCImplementerFactory.class);
        when(clientImplementerFactory.build()).thenReturn(clientImplementer);

        RPC server = null,
            serverEndpointForClient2 = null,
            client1 = null,
            client2 = null;
        Thread serverThread = null,
                serverSecondThread = null,
                client1Thread = null,
                client2Thread = null;
        try {
            server = new RPCFactory().build(serverImplementerFactory, new ServerSocketChannelFactory("127.0.0.1", port));
            serverEndpointForClient2 = new RPCFactory().build(serverImplementerFactory, server);
            client1 = new RPCFactory().build(clientImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port));
            client2 = new RPCFactory().build(clientImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port));
            serverThread = new Thread(server);
            serverSecondThread = new Thread(serverEndpointForClient2);
            client1Thread = new Thread(client1);
            client2Thread = new Thread(client2);
            serverThread.start();
            serverSecondThread.start();
            client1Thread.start();
            client2Thread.start();

            // wait for them to connect
            final RPC _server = server,
                    _serverEndpointForClient2 = serverEndpointForClient2;
            StateChangeUtils.pollForCondition(() -> _server.isRunning() && _serverEndpointForClient2.isRunning(), 8_000);

            // get the socket objects
            ClientSocketMessageChannel serverClientConnectionSocket = getRPCMessageChannelInstance(server);
            ClientSocketMessageChannel client1Socket = getRPCMessageChannelInstance(client1);
            ClientSocketMessageChannel client2Socket = getRPCMessageChannelInstance(client2);

            // assert - each end should be connected
            assertTrue(getRPCMessageChannel(server) == getRPCMessageChannel(serverEndpointForClient2), "Main socket differs within RPC servers");
            assertTrue(server.isRunning(), "Server is not running");
            assertTrue(client1.isRunning(), "Client 1 is not running");
            assertTrue(client2.isRunning(), "Client 2 is not running");
            assertFalse(serverClientConnectionSocket.isClosed(), "Server socket is closed");
            assertTrue(serverClientConnectionSocket.isEndConnected(), "Server socket got no client connection");
            assertFalse(client1Socket.isClosed(), "Client 1 socket is closed");
            assertTrue(client1Socket.isEndConnected(), "Client 1 socket got no server connection");
            assertFalse(client2Socket.isClosed(), "Client 2 socket is closed");
            assertTrue(client2Socket.isEndConnected(), "Client 2 socket got no server connection");
        } finally {
            if (server != null) server.close();
            if (serverEndpointForClient2 != null) serverEndpointForClient2.close();
            if (client1 != null) client1.close();
            if (client2 != null) client2.close();

            if (serverThread != null) serverThread.join(8_000);
            if (serverSecondThread != null) serverSecondThread.join(8_000);
            if (client1Thread != null) client1Thread.join(8_000);
            if (client2Thread != null) client2Thread.join(8_000);
        }

        // assert - the sockets should be closed
        assertFalse(server.isRunning(), "Server is still active after turning it off");
        assertTrue(getRPCMessageChannel(server).isClosed(), "Server main socket is still active after turning it off");
        assertFalse(serverEndpointForClient2.isRunning(), "Server (second connection) is still active after turning it off");
        assertFalse(client1.isRunning(), "Client 1 is still active after turning it off");
        assertTrue(getRPCMessageChannel(client1).isClosed(), "Client 1 main socket is still active after turning it off");
        assertFalse(client2.isRunning(), "Client 2 is still active after turning it off");
        assertTrue(getRPCMessageChannel(client2).isClosed(), "Client 2 main socket is still active after turning it off");
    }

    @Test
    public void forwardEventToTheClient() throws Exception {
        int port = 8900;

        RPCImplementer serverImplementer = mock(RPCImplementer.class);

        final ArrayList<Short> got = new ArrayList<>();
        RPCImplementer clientImplementer = getClientMock(got);

        RPCImplementerFactory serverImplementerFactory = mock(RPCImplementerFactory.class);
        when(serverImplementerFactory.build()).thenReturn(serverImplementer);

        RPCImplementerFactory clientImplementerFactory = mock(RPCImplementerFactory.class);
        when(clientImplementerFactory.build()).thenReturn(clientImplementer);

        Thread serverThread = null,
                clientThread = null;
        try (   RPC server = new RPCFactory().build(serverImplementerFactory, new ServerSocketChannelFactory("127.0.0.1", port));
                RPC client = new RPCFactory().build(clientImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port))) {
            serverThread = new Thread(server);
            clientThread = new Thread(client);
            serverThread.start();
            clientThread.start();

            // wait for them to connect
            StateChangeUtils.pollForCondition(() -> server.isRunning(), 8_000);

            // get the socket objects
            ClientSocketMessageChannel clientQueue = getRPCMessageChannelInstance(client);

            // send an event with no data
            server.sendEvent(
                    new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b0000_1_000),
                    new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b00000000)
            );

            // wait for client to get (and then consume) the data
            StateChangeUtils.tryPollForCondition(() -> clientQueue.areBytesAvailable(), 1_500);
            StateChangeUtils.pollForCondition(() -> !clientQueue.areBytesAvailable(), 4_000);

            // assert - an event should have been received
            assertFalse(clientQueue.areBytesAvailable(), "We expected the client to have read all bytes; got some bytes left instead");
            verify(clientImplementer, times(1)).forwardCall(any(MessageChannel.class), any(RPCConverter.class));
            assertEquals((short)0b000000000000_1_000, got.get(0));
        } // implicit server close

        serverThread.join(8_000);
        clientThread.join(8_000);
    }

    @Test
    public void forwardEventToOneClient() throws Exception {
        int port = 8900;

        RPCImplementer serverImplementer = mock(RPCImplementer.class);

        final ArrayList<Short> falseGot = new ArrayList<>();
        RPCImplementer client1Implementer = getClientMock(falseGot);

        final ArrayList<Short> got = new ArrayList<>();
        RPCImplementer client2Implementer = getClientMock(got);

        RPCImplementerFactory serverImplementerFactory = mock(RPCImplementerFactory.class);
        when(serverImplementerFactory.build()).thenReturn(serverImplementer);

        RPCImplementerFactory client1ImplementerFactory = mock(RPCImplementerFactory.class);
        when(client1ImplementerFactory.build()).thenReturn(client1Implementer);
        RPCImplementerFactory client2ImplementerFactory = mock(RPCImplementerFactory.class);
        when(client2ImplementerFactory.build()).thenReturn(client2Implementer);

        Thread serverThread = null,
                serverSecondThread = null,
                client1Thread = null,
                client2Thread = null;
        try (   RPC server = new RPCFactory().build(serverImplementerFactory, new ServerSocketChannelFactory("127.0.0.1", port));
                RPC serverEndpointForClient2 = new RPCFactory().build(serverImplementerFactory, server);
                 RPC client1 = new RPCFactory().build(client1ImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port));
                 RPC client2 = new RPCFactory().build(client2ImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port))) {
            serverThread = new Thread(server);
            serverSecondThread = new Thread(serverEndpointForClient2);
            client1Thread = new Thread(client1);
            client2Thread = new Thread(client2);

            // link server with client 1
            serverThread.start();
            client1Thread.start();

            // wait first 2 to connect
            StateChangeUtils.pollForCondition(() -> server.isRunning(), 8_000);

            // link second server RPC instance with client 2
            serverSecondThread.start();
            client2Thread.start();

            // wait for the rest to connect
            StateChangeUtils.pollForCondition(() -> serverEndpointForClient2.isRunning(), 8_000);

            // get the socket objects
            ClientSocketMessageChannel client1Queue = getRPCMessageChannelInstance(client1);
            ClientSocketMessageChannel client2Queue = getRPCMessageChannelInstance(client2);

            // send an event with no data
            serverEndpointForClient2.sendEvent(
                    new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b0000_1_000),
                    new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b00000000)
            );

            // wait for client to get (and then consume) the data
            StateChangeUtils.tryPollForCondition(() -> client2Queue.areBytesAvailable(), 1_500);
            StateChangeUtils.pollForCondition(() -> !client2Queue.areBytesAvailable(), 4_000);

            // assert - an event should have been received
            assertFalse(client1Queue.areBytesAvailable(), "We expected no data sent to the client 1; got some bytes left instead");
            assertFalse(client2Queue.areBytesAvailable(), "We expected the client 2 to have read all bytes; got some bytes left instead");
            verify(client1Implementer, never()).forwardCall(any(MessageChannel.class), any(RPCConverter.class));
            verify(client2Implementer, times(1)).forwardCall(any(MessageChannel.class), any(RPCConverter.class));
            assertEquals((short)0b000000000000_1_000, got.get(0));
            assertTrue(falseGot.isEmpty(), "We expected no data sent to the client 1; got some bytes instead");
        } // implicit server close

        serverThread.join(8_000);
        serverSecondThread.join(8_000);
        client1Thread.join(8_000);
        client2Thread.join(8_000);
    }

    @Test
    public void getAsManyConnectionClosedAsClientsDisconnect() throws Exception {
        int port = 8900;

        RPCImplementer serverImplementer = getClientMock(null);
        RPCImplementer clientImplementer = mock(RPCImplementer.class);

        RPCImplementerFactory serverImplementerFactory = mock(RPCImplementerFactory.class);
        when(serverImplementerFactory.build()).thenReturn(serverImplementer);

        RPCImplementerFactory clientImplementerFactory = mock(RPCImplementerFactory.class);
        when(clientImplementerFactory.build()).thenReturn(clientImplementer);

        RPC server = null,
                serverEndpointForClient2 = null,
                client1 = null,
                client2 = null;
        Thread serverThread = null,
                serverSecondThread = null,
                client1Thread = null,
                client2Thread = null;
        try {
            server = new RPCFactory().build(serverImplementerFactory, new ServerSocketChannelFactory("127.0.0.1", port));
            serverEndpointForClient2 = new RPCFactory().build(serverImplementerFactory, server);
            client1 = new RPCFactory().build(clientImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port));
            client2 = new RPCFactory().build(clientImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port));
            serverThread = new Thread(server);
            serverSecondThread = new Thread(serverEndpointForClient2);
            client1Thread = new Thread(client1);
            client2Thread = new Thread(client2);
            serverThread.start();
            serverSecondThread.start();
            client1Thread.start();
            client2Thread.start();

            // wait for them to connect
            final RPC _server = server,
                    _serverEndpointForClient2 = serverEndpointForClient2,
                    _client1 = client1,
                    _client2 = client2;
            StateChangeUtils.pollForCondition(() -> _server.isRunning() && _serverEndpointForClient2.isRunning(), 8_000,
                                            "Couldn't start both server connections");

            final ServerSocketMessageChannel serverSocketMessageChannel = (ServerSocketMessageChannel) getRPCMessageChannel(server);
            final AtomicBoolean serverClient1ConnectionSocketClosed = new AtomicBoolean(),
                                serverClient2ConnectionSocketClosed = new AtomicBoolean();
            final ClientSocketMessageChannel serverClient1ConnectionSocket = getClientSocketChannelListeningForClose(server, serverClient1ConnectionSocketClosed),
                                            serverClient2ConnectionSocket = getClientSocketChannelListeningForClose(serverEndpointForClient2, serverClient2ConnectionSocketClosed);

            StateChangeUtils.pollForCondition(() -> serverClient1ConnectionSocket.isEndConnected() && serverClient2ConnectionSocket.isEndConnected(), 8_000,
                                            "Couldn't get both clients connected");

            // disconnect one user
            System.out.println("[i] Closing client 2...");
            client2.close();
            client2Thread.join(8_000);
            client2Thread = null;
            StateChangeUtils.pollForCondition(() -> serverClient1ConnectionSocket.isClosed() || serverClient2ConnectionSocket.isClosed(), 8_000,
                                            "Tried to close one client, but they are both still running");
            StateChangeUtils.pollForCondition(() -> !_client2.isRunning(), 8_000, "Client 2 is still running");
            StateChangeUtils.pollForCondition(() -> {
                synchronized (serverClient2ConnectionSocketClosed) {
                    return serverClient2ConnectionSocketClosed.get();
                }
            }, 6_000, "Expected to get client 2 disconnected event; got nothing instead");

            // assert - server should be still running
            assertFalse(serverSocketMessageChannel.isClosed(), "Expected server to keep running; got server stopped instead");
            assertTrue(server.isRunning() ^ serverEndpointForClient2.isRunning(), (server.isRunning() ? "Both server instances are running" : "Server is not running"));
            assertTrue(client1.isRunning(), "Client 1 is not running");

            // disconnect second user
            System.out.println("[i] Closing client 1...");
            client1.close();
            client1Thread.join(8_000);
            client1Thread = null;
            StateChangeUtils.pollForCondition(() -> serverClient1ConnectionSocket.isClosed() && serverClient2ConnectionSocket.isClosed(), 8_000,
                                            "Couldn't stop both clients");
            StateChangeUtils.pollForCondition(() -> !_client1.isRunning(), 8_000, "Client 1 is still running");
            StateChangeUtils.pollForCondition(() -> {
                        synchronized (serverClient1ConnectionSocketClosed) {
                            return serverClient1ConnectionSocketClosed.get();
                        }
                    }, 6_000, "Expected to get client 1 disconnected event; got nothing instead");
        } finally {
            System.out.println("- Clearing resources...");
            if (server != null) server.close();
            if (serverEndpointForClient2 != null) serverEndpointForClient2.close();
            if (client1 != null) client1.close();
            if (client2 != null) client2.close();

            if (serverThread != null) serverThread.join(8_000);
            if (serverSecondThread != null) serverSecondThread.join(8_000);
            if (client1Thread != null) client1Thread.join(8_000);
            if (client2Thread != null) client2Thread.join(8_000);
        }
    }

    @Test
    public void allowNewUsersToJoinWhenLastOneDisconnected() throws Exception {
        // TODO launch server
        // TODO connect one user
        // TODO disconnect user
        // TODO listen for other user
        // TODO connect user
        // TODO assert connection
    }
}
