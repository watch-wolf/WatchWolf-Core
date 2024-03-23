package dev.watchwolf.core.rpc.channel.sockets;

import dev.watchwolf.core.rpc.channel.ChannelQueue;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketChannelFactory;
import dev.watchwolf.core.rpc.channel.sockets.client.ClientSocketMessageChannel;
import dev.watchwolf.core.rpc.channel.sockets.server.ServerSocketChannelFactory;
import dev.watchwolf.core.utils.StateChangeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ITClientSocketMessageChannelShould {
    private static final String host = "127.0.0.1";
    private static final int port = 8900;

    private MessageChannel server;
    private AtomicReference<MessageChannel> clientServerConnection;
    private Thread serverThread;

    /**
     * ... given an started server
     */
    @BeforeEach
    public void initServer() throws RuntimeException, TimeoutException {
        this.server = new ServerSocketChannelFactory(host, port).build();

        this.clientServerConnection = new AtomicReference<>();
        final AtomicReference<MessageChannel> _clientServerConnection = this.clientServerConnection;
        final MessageChannel _server = this.server;
        serverThread = new Thread(() -> {
            try {
                MessageChannel connection = _server.create();
                synchronized (ITClientSocketMessageChannelShould.class) {
                    _clientServerConnection.set(connection);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        serverThread.start();

        StateChangeUtils.pollForCondition(() -> !_server.isClosed(), 16_000,
                                            "Couldn't start the server in 16s");
    }

    @AfterEach
    public void disposeServer() throws IOException,InterruptedException {
        synchronized (ITClientSocketMessageChannelShould.class) {
            if (this.clientServerConnection.get() != null) this.clientServerConnection.get().close();
        }
        this.server.close();
        this.serverThread.join(8_000);
    }

    public static ClientSocketMessageChannel getClientSocketChannelListeningForClose(String host, int port, final AtomicBoolean closed) throws Exception {
        closed.set(false);

        ClientSocketMessageChannel clientChannel = (ClientSocketMessageChannel)new ClientSocketChannelFactory(host, port).build().create();
        clientChannel.addClientClosedListener(() -> {
            synchronized (closed) {
                if (closed.get()) throw new RuntimeException("Got second 'client closed' event");
                closed.set(true);
            }
        });

        return clientChannel;
    }

    @Test
    public void connectToARunningServer() throws Exception {
        ClientSocketMessageChannel client = null;
        try {
            client = (ClientSocketMessageChannel)new ClientSocketChannelFactory(host, port).build().create();
            final ClientSocketMessageChannel _client = client;

            // assert - user should be connected
            StateChangeUtils.pollForCondition(() -> _client.isEndConnected(), 8_000,
                                            "Couldn't connect to the server");
        } finally {
            if (client != null) client.close();
        }
    }

    @Test
    public void detectWhenServerCloses() throws Exception {
        ClientSocketMessageChannel client = null;
        try {
            client = (ClientSocketMessageChannel)new ClientSocketChannelFactory(host, port).build().create();
            final ClientSocketMessageChannel _client = client;

            // wait for user to connect
            StateChangeUtils.pollForCondition(() -> _client.isEndConnected(), 8_000,
                                            "Couldn't connect to the server");

            // act - close the server
            this.server.close();

            // assert - the user should detect the server close
            StateChangeUtils.pollForCondition(() -> !_client.isEndConnected(), 12_000,
                                            "Expected client to disconnect when the server closes; got end connected instead");
            StateChangeUtils.pollForCondition(() -> _client.isClosed(), 6_000,
                                            "Expected client to close when the server closes; got running instead");
        } finally {
            if (client != null) client.close();
        }
    }

    @Test
    public void notifyClientClosedWhenClientCloses() throws Exception {
        ClientSocketMessageChannel client = null;
        try {
            final AtomicBoolean gotClientClosedEvent = new AtomicBoolean();
            client = getClientSocketChannelListeningForClose(host, port, gotClientClosedEvent);
            ClientSocketMessageChannel _client = client;

            // wait for user to connect
            StateChangeUtils.pollForCondition(() -> _client.isEndConnected(), 8_000,
                                            "Couldn't connect to the server");

            // act - close the server
            client.close();
            StateChangeUtils.pollForCondition(() -> _client.isClosed(), 8_000,
                                            "Couldn't close the client");
            client = null; // closed

            // assert - the user should detect the server close
            StateChangeUtils.pollForCondition(() -> {
                synchronized (gotClientClosedEvent) {
                    return gotClientClosedEvent.get();
                }
            }, 2_000, "Expected to get client disconnected event; got nothing instead");
        } finally {
            if (client != null) client.close();
        }
    }

    @Test
    public void notifyClientClosedWhenServerCloses() throws Exception {
        ClientSocketMessageChannel client = null;
        try {
            final AtomicBoolean gotClientClosedEvent = new AtomicBoolean();
            client = (ClientSocketMessageChannel)getClientSocketChannelListeningForClose(host, port, gotClientClosedEvent);
            MessageChannel _server = server;
            ClientSocketMessageChannel _client = client;

            // wait for user to connect
            StateChangeUtils.pollForCondition(() -> _client.isEndConnected(), 8_000,
                                                "Couldn't connect to the server");

            // act - close the server
            this.server.close();
            StateChangeUtils.pollForCondition(() -> _server.isClosed(), 8_000,
                                                "Couldn't close the server");
            StateChangeUtils.pollForCondition(() -> _client.isClosed(), 8_000,
                                                "Expected client to be closed; got client open instead");

            // assert - the user should detect the server close
            StateChangeUtils.pollForCondition(() -> {
                synchronized (gotClientClosedEvent) {
                    return gotClientClosedEvent.get();
                }
            }, 2_000, "Expected to get client disconnected event; got nothing instead");
        } finally {
            if (client != null) client.close();
        }
    }
}
