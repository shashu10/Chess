package networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 3018121326309853506L;
	public int[][] boardArray;
	boolean hasRestarted = false;
	boolean hasForfeited = false;
	boolean newClassicGame = false;
	boolean newCustomGame = false;

	public Message(int[][] boardArray) {
		this.boardArray = boardArray;
	}

	public static boolean send(Message msg, ObjectOutputStream out) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
			return false;
		}
		return true;
	}
}
