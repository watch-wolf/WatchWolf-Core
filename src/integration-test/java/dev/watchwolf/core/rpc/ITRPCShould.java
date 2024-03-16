package dev.watchwolf.core.rpc;

import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketMessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketMessageChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Timeout(60)
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

        RPC server = new RPCFactory().build(serverImplementerFactory, new ServerSocketChannelFactory("127.0.0.1", port)),
             client = new RPCFactory().build(clientImplementerFactory, new ClientSocketChannelFactory("127.0.0.1", port));
        Thread serverThread = new Thread(server),
                clientThread = new Thread(client);
        serverThread.start();
        clientThread.start();

        // get the socket objects
        ServerSocketMessageChannel serverSocket = (ServerSocketMessageChannel) getRPCMessageChannel(server);
        ClientSocketMessageChannel clientSocket = (ClientSocketMessageChannel) getRPCMessageChannel(client);

        // wait for them to connect
        int tries = 4;
        while (tries > 0 && !serverSocket.isEndConnected()) {
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException ignore) {
            }
            tries--;
        }

        // assert - each end should be connected
        assertTrue(server.isRunning());
        assertTrue(client.isRunning());
        assertFalse(serverSocket.isClosed());
        assertTrue(serverSocket.isEndConnected());
        assertFalse(clientSocket.isClosed());
        assertTrue(clientSocket.isEndConnected());

        // close the servers
        server.close();
        client.close();

        serverThread.join(8_000);
        clientThread.join(8_000);

        // assert - the sockets should be closed
        assertFalse(server.isRunning());
        assertFalse(client.isRunning());
    }
}
