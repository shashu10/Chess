package networking;

import java.io.*;
import java.net.*;

import view.ViewController;

/**
 * Client based on CS 242 Assignment 1.3 instructions Sets up a client server
 * only if a sever is already up and running.
 */
public class Client {

	// Networking data
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	ViewController controller;
	Message message;
	private int port;
	int count = 0;

	/**
	 * Default Constructor
	 * 
	 * @param controller
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public Client(ViewController controller, int port)
			throws InterruptedException, IOException {
		this.controller = controller;
		this.port = port;
		run();
	}

	/**
	 * Runs the client if a server is running
	 * 
	 * @throws IOException
	 */
	public void run() throws IOException {
		try {
			setupClient();
			// 3: Communicating with the server
			while (true) {
				try {
					syncGame();
				} catch (Exception e) {
					return;
				}
			}
		} catch (IOException ioException) {
			throw new IOException();
		} finally {
			// 4: Closing connection
			in.close();
			out.close();
			requestSocket.close();
		}
	}

	/**
	 * Sets up a client by connecting to a host server. If not client is
	 * present, it exits.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public void setupClient() throws UnknownHostException, IOException {
		// 1. creating a socket to connect to the server
		requestSocket = new Socket("localhost", port);
		// If server is responsive, create a new game.
		if (controller == null)
			this.controller = new ViewController(-1);
		// 2. get Input and Output streams
		out = new ObjectOutputStream(requestSocket.getOutputStream());
		out.flush();
		in = new ObjectInputStream(requestSocket.getInputStream());
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
			// Send data to sync if the game has changed
			if (controller.game.hasChanged()) {
				Message.send(new Message(controller.game.board.toArray()), out);
			}
		}
		Message.send(null, out);
	}
}
