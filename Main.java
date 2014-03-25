/*! \mainpage Documentation for Chess

 * This chess game library contains a board, piece, mover and a View class that
 * can be used to play a game of chess.
 */

import networking.*;
import view.ViewController;

@SuppressWarnings("unused")
public class Main {

	/**
	 * Main Function calls necessary methods to display board.
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

		new ViewController(0);
		
		// Run chess server and socket.
//		int port = 5623;
		// Try setting up a client if a server is active
//		try {
//			Client client = new Client(null, port);
//			// Else, set up server
//		} catch (Exception e) {
//			Server server = new Server(new ViewController(0), port);
//		}
	}
}
