package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.ChannelQueue;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketMessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketMessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Timeout(40)
public class ITRPCShould {
    public static MessageChannel getRPCMessageChannel(RPC rpc) throws Exception {
        Field messageChannelField = RPC.class.getDeclaredField("remoteConnection");
        messageChannelField.setAccessible(true);

        return (MessageChannel) messageChannelField.get(rpc);
    }

    @Test
    public void connectOneClientSocketWithAServerSocket() throws Exception {
        int port = 8900;

        RPCImplementer serverImplementer = mock(RPCImplementer.class);
        RPCImplementer clientImplementer = mock(RPCImplementer.class);

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

            // get the socket objects
            ChannelQueue serverQueue = (ChannelQueue) getRPCMessageChannel(server);
            ServerSocketMessageChannel serverSocket = (ServerSocketMessageChannel) serverQueue.getChannel();
            ChannelQueue clientQueue = (ChannelQueue) getRPCMessageChannel(client);
            ClientSocketMessageChannel clientSocket = (ClientSocketMessageChannel) clientQueue.getChannel();

            // wait for them to connect
            int tries = 4;
            while (tries > 0 && !serverSocket.isEndConnected()) {
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException ignore) { }
                tries--;
            }

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
            assertFalse(serverSocket.isClosed(), "Server socket is closed");
            assertTrue(serverSocket.isEndConnected(), "Server socket got no client connection");
            assertFalse(clientSocket.isClosed(), "Client socket is closed");
            assertTrue(clientSocket.isEndConnected(), "Client socket got no server connection");

            // assert - we didn't send anything
            assertFalse(serverQueue.areBytesAvailable(), "We expected no bytes on the server socket; got bytes instead");
            assertFalse(clientQueue.areBytesAvailable(), "We expected no bytes on the client socket; got bytes instead");
        } finally {
            // close the servers
            if (server != null) server.close();
            if (client != null) client.close();

            if (serverThread != null) serverThread.join(8_000);
            if (clientThread != null) clientThread.join(8_000);
        }

        // assert - the sockets should be closed
        assertFalse(server.isRunning(), "Server is still active after turning it off");
        assertFalse(client.isRunning(), "Client is still active after turning it off");
    }

    @Test
    public void connectTwoClientSocketsWithAServerSocket() throws Exception {
        int port = 8900;

        RPCImplementer serverImplementer = mock(RPCImplementer.class);
        RPCImplementer clientImplementer = mock(RPCImplementer.class);

        RPCImplementerFactory serverImplementerFactory = mock(RPCImplementerFactory.class);
        when(serverImplementerFactory.build()).thenReturn(serverImplementer);

        RPCImplementerFactory clientImplementerFactory = mock(RPCImplementerFactory.class);
        when(clientImplementerFactory.build()).thenReturn(clientImplementer);

        RPC server = null,
            client1 = null,
            client2 = null;
        Thread serverThread = null,
                client1Thread = null,
                client2Thread = null;
        try {
            server = new RPCFactory().build(serverImplementerFactory, new ServerSocketChannelFactory("127.0.0.1", port));
            client1 = new RPCFactory().build(clientImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port));
            client2 = new RPCFactory().build(clientImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port));
            serverThread = new Thread(server);
            client1Thread = new Thread(client1);
            client2Thread = new Thread(client2);
            serverThread.start();
            client1Thread.start();
            client2Thread.start();

            // get the socket objects
            ServerSocketMessageChannel serverSocket = (ServerSocketMessageChannel) ((ChannelQueue) getRPCMessageChannel(server)).getChannel();
            ClientSocketMessageChannel client1Socket = (ClientSocketMessageChannel) ((ChannelQueue) getRPCMessageChannel(client1)).getChannel();
            ClientSocketMessageChannel client2Socket = (ClientSocketMessageChannel) ((ChannelQueue) getRPCMessageChannel(client2)).getChannel();

            // wait for them to connect
            int tries = 4;
            while (tries > 0 && (!client1Socket.isEndConnected() || !client2Socket.isEndConnected())) {
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException ignore) { }
                tries--;
            }

            // assert - each end should be connected
            assertTrue(server.isRunning(), "Server is not running");
            assertTrue(client1.isRunning(), "Client 1 is not running");
            assertTrue(client2.isRunning(), "Client 2 is not running");
            assertFalse(serverSocket.isClosed(), "Server socket is closed");
            assertTrue(serverSocket.isEndConnected(), "Server socket got no client connection");
            assertFalse(client1Socket.isClosed(), "Client 1 socket is closed");
            assertTrue(client1Socket.isEndConnected(), "Client 1 socket got no server connection");
            assertFalse(client2Socket.isClosed(), "Client 2 socket is closed");
            assertTrue(client2Socket.isEndConnected(), "Client 2 socket got no server connection");
        } finally {
            if (server != null) server.close();
            if (client1 != null) client1.close();
            if (client2 != null) client2.close();

            if (serverThread != null) serverThread.join(8_000);
            if (client1Thread != null) client1Thread.join(8_000);
            if (client2Thread != null) client2Thread.join(8_000);
        }

        // assert - the sockets should be closed
        assertFalse(server.isRunning(), "Server is still active after turning it off");
        assertFalse(client1.isRunning(), "Client 1 is still active after turning it off");
        assertFalse(client2.isRunning(), "Client 2 is still active after turning it off");
    }

    @Test
    public void forwardEventToTheClient() throws Exception {
        int port = 8900;

        RPCImplementer serverImplementer = mock(RPCImplementer.class);

        RPCImplementer clientImplementer = mock(RPCImplementer.class);
        final ArrayList<Short> got = new ArrayList<>();
        doAnswer((invocation) -> {
                MessageChannel channel = invocation.getArgument(0);
                RPCConverter<?> converter = invocation.getArgument(1);
                short info = converter.unmarshall(channel, Short.class); // discard event data
                got.add(info);
                return null;
            })
            .when(clientImplementer)
                .forwardCall(any(MessageChannel.class), any(RPCConverter.class));

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

            // get the socket objects
            ServerSocketMessageChannel serverSocket = (ServerSocketMessageChannel) ((ChannelQueue) getRPCMessageChannel(server)).getChannel();
            ChannelQueue clientQueue = (ChannelQueue) getRPCMessageChannel(client);
            ClientSocketMessageChannel clientSocket = (ClientSocketMessageChannel) clientQueue.getChannel();

            // wait for them to connect
            int tries = 4;
            while (tries > 0 && !serverSocket.isEndConnected()) {
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException ignore) { }
                tries--;
            }

            // send an event with no data
            server.sendEvent(
                    new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b0000_1_000),
                    new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b00000000)
            );

            // wait for client to get the data
            tries = 3;
            while (tries > 0 && !clientQueue.areBytesAvailable()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) { }
                tries--;
            }
            tries = 8;
            while (tries > 0 && clientQueue.areBytesAvailable()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) { }
                tries--;
            }

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

        RPCImplementer client1Implementer = mock(RPCImplementer.class);
        final ArrayList<Short> falseGot = new ArrayList<>();
        doAnswer((invocation) -> {
            MessageChannel channel = invocation.getArgument(0);
            RPCConverter<?> converter = invocation.getArgument(1);
            short info = converter.unmarshall(channel, Short.class); // discard event data
            falseGot.add(info);
            return null;
        })
                .when(client1Implementer)
                .forwardCall(any(MessageChannel.class), any(RPCConverter.class));

        RPCImplementer client2Implementer = mock(RPCImplementer.class);
        final ArrayList<Short> got = new ArrayList<>();
        doAnswer((invocation) -> {
            MessageChannel channel = invocation.getArgument(0);
            RPCConverter<?> converter = invocation.getArgument(1);
            short info = converter.unmarshall(channel, Short.class); // discard event data
            got.add(info);
            return null;
        })
                .when(client2Implementer)
                .forwardCall(any(MessageChannel.class), any(RPCConverter.class));

        RPCImplementerFactory serverImplementerFactory = mock(RPCImplementerFactory.class);
        when(serverImplementerFactory.build()).thenReturn(serverImplementer);

        RPCImplementerFactory client1ImplementerFactory = mock(RPCImplementerFactory.class);
        when(client1ImplementerFactory.build()).thenReturn(client1Implementer);
        RPCImplementerFactory client2ImplementerFactory = mock(RPCImplementerFactory.class);
        when(client2ImplementerFactory.build()).thenReturn(client2Implementer);

        Thread serverThread = null,
                client1Thread = null,
                client2Thread = null;
        try (   RPC server = new RPCFactory().build(serverImplementerFactory, new ServerSocketChannelFactory("127.0.0.1", port));
                 RPC client1 = new RPCFactory().build(client1ImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port));
                 RPC client2 = new RPCFactory().build(client2ImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port))) {
            serverThread = new Thread(server);
            client1Thread = new Thread(client1);
            client2Thread = new Thread(client2);
            serverThread.start();
            client1Thread.start();
            client2Thread.start();

            // get the socket objects
            ServerSocketMessageChannel serverSocket = (ServerSocketMessageChannel) ((ChannelQueue) getRPCMessageChannel(server)).getChannel();
            ChannelQueue client1Queue = (ChannelQueue) getRPCMessageChannel(client1);
            ClientSocketMessageChannel client1Socket = (ClientSocketMessageChannel) client1Queue.getChannel();
            ChannelQueue client2Queue = (ChannelQueue) getRPCMessageChannel(client2);
            ClientSocketMessageChannel client2Socket = (ClientSocketMessageChannel) client2Queue.getChannel();

            // wait for them to connect
            int tries = 4;
            while (tries > 0 && (!client1Socket.isEndConnected() || !client2Socket.isEndConnected())) {
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException ignore) { }
                tries--;
            }

            // send an event with no data
            // TODO how to choose who to send?
            server.sendEvent(
                    new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b0000_1_000),
                    new dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte((byte) 0b00000000)
            );

            // wait for client to get the data
            tries = 3;
            while (tries > 0 && !client2Queue.areBytesAvailable()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) { }
                tries--;
            }
            tries = 8;
            while (tries > 0 && client2Queue.areBytesAvailable()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) { }
                tries--;
            }

            // assert - an event should have been received
            assertFalse(client1Queue.areBytesAvailable(), "We expected no data sent to the client 1; got some bytes left instead");
            assertFalse(client2Queue.areBytesAvailable(), "We expected the client 2 to have read all bytes; got some bytes left instead");
            verify(client1Implementer, never()).forwardCall(any(MessageChannel.class), any(RPCConverter.class));
            verify(client2Implementer, times(1)).forwardCall(any(MessageChannel.class), any(RPCConverter.class));
            assertEquals((short)0b000000000000_1_000, got.get(0));
            assertTrue(falseGot.isEmpty(), "We expected no data sent to the client 1; got some bytes instead");
        } // implicit server close

        serverThread.join(8_000);
        client1Thread.join(8_000);
        client2Thread.join(8_000);
    }
}
