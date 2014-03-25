package pieces;

import game.*;

/**
 * An abstract class to represent a piece on the board. Each subclass must
 * implement a unique canMoveBetween function
 * 
 * @author Shashank Bharadwaj
 * @date Wed, 4 Sep 2013
 */
abstract public class Piece {

	// Constants
	public static int EMPTY = 0;
	public static int PAWN = 1;
	public static int ROOK = 2;
	public static int KNIGHT = 3;
	public static int BISHOP = 4;
	public static int QUEEN = 5;
	public static int KING = 6;
	public static int ARCHER = 7;
	public static int PEASANT = 8;
	public static final int WHITE = 1;
	public static final int BLACK = -1;

	static int size = 8;
	static int numTypes = 8;

	protected int color;
	protected int pieceType;
	protected Position pos;

	Board myBoard;

	/**
	 * Constructs a Piece with given type, color and position
	 * 
	 * @param pieceType
	 *            : Pieces numbered 1-6. Black - and White + values
	 * @param myBoard
	 * @param x
	 *            : coordinate
	 * @param y
	 *            : coordinate
	 */
	public Piece(int pieceType, Board myBoard, int x, int y)
			throws IllegalArgumentException {
		if (Math.abs(pieceType) > numTypes || pieceType == 0) {
			throw new IllegalArgumentException("Type not valid");
		}
		pos = new Position(x, y);
		color = (int) Math.signum(pieceType);
		this.myBoard = myBoard;
		this.pieceType = pieceType;
	}

	/**
	 * Constructs a Piece with given type and color
	 * 
	 * @param pieceType
	 *            : Pieces numbered 1-6. Black - and White + values
	 * @param myBoard
	 */
	public Piece(int pieceType) {
		color = (int) Math.signum(pieceType);
		this.pieceType = pieceType;
	}

	/**
	 * Check if the piece can move from start to end location. Must be
	 * overridden in subclass.
	 * 
	 * @return true if it can. false otherwise.
	 */
	public boolean canMoveBetween(Position start, Position end) {
		return false;
	}

	/**
	 * pieceType getter
	 */
	public int getPieceType() {
		return pieceType;
	}

	/**
	 * color getter
	 */
	public int getColor() {
		return color;
	}

	/**
	 * position getter
	 */
	public Position getPos() {
		return pos;
	}

	/**
	 * position setter
	 */
	public void setPos(Position pos) {
		this.pos = pos;
	}

	/**
	 * position setter
	 */
	public void setBoard(Board myBoard) {
		this.myBoard = myBoard;
	}
}
