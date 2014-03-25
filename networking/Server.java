package networking;

import java.io.*;
import java.net.*;
import view.ViewController;

/**
 * Server based on CS 242 Assignment 1.3 instructions Sets up a server to play
 * chess with a client. If no client responds before start of a new game, single
 * player is initiated.
 */
public class Server {

	// Networking data
	public ServerSocket providerSocket;
	public Socket connection = null;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	public ViewController controller;
	public Message message;
	private int port;
	int count = 0;

	/**
	 * Default Constructor
	 * 
	 * @param controller
	 */
	public Server(ViewController controller, int port) {
		this.controller = controller;
		this.port = port;
		run();
	}

	/**
	 * Runs the server and looks for clients
	 */
	public void run() {
		try {
			setUpServer();
			Message.send(new Message(null), out);
			// 4. The two parts communicate via the input and output streams
			while (true) {
				try {
					syncGame();
				} catch (Exception e) {
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				providerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	/**
	 * Sets up server socket at localhost. Gets the input and output streams
	 * 
	 * @throws IOException
	 */
	public boolean setUpServer() throws IOException {
		// 1. creating a server socket
		providerSocket = new ServerSocket(port);
		// 2. Wait for connection
		connection = providerSocket.accept();
		// Enable network play
		controller.player = +1;
		// 3. get Input and Output streams
		out = new ObjectOutputStream(connection.getOutputStream());
		out.flush();
		in = new ObjectInputStream(connection.getInputStream());
		return true;
	}

	/**
	 * Sends game moves over a network socket
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void syncGame() throws IOException, ClassNotFoundException {
		// Read message
		message = (Message) in.readObject();

		if (message != null) {
			// Update game
			controller.updateGame(message);

		} else if (controller.game != null) {
			if (controller.game.hasChanged()) {
				// Send data to sync if the game has changed
				Message.send(new Message(controller.game.board.toArray()), out);
				controller.resetFlags();
			}
		}
		Message.send(null, out);
	}
}
