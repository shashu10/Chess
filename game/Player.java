package game;

public class Player {
	@SuppressWarnings("unused")
	private String name;
	private int color;
	@SuppressWarnings("unused")
	private Board board;
	private boolean isInCheck;

	/**
	 * Constructor
	 */
	public Player(String name, int color, Board board) {
		this.name = name;
		this.color = color;
		this.board = board;
		isInCheck = false;
	}

	public int getColor() {
		return color;
	}

	public boolean isInCheck() {
		return isInCheck;
	}

	public void putInCheck(boolean isInCheck) {
		this.isInCheck = isInCheck;
	}
}
