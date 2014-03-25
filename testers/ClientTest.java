package testers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

import networking.*;

public class ClientTest {

	@Test
	public void testClient() throws IOException {
		int port = 7777;
		Server server = mock(Server.class);
		ServerSocket providerSocket = new ServerSocket(port);
		Socket connection = providerSocket.accept();
		// new ObjectOutputStream(new Message (null));
		Client client = Mockito.mock(Client.class);
		server.setUpServer();
		client.setupClient();
		when(Message.send(new Message(null), null)).thenReturn(true);

	}

}
