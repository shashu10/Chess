package pieces;

import game.*;

public class Fly extends Piece {

	/**
	 * Constructor No check to verify if pieceType is a Archer. It is up to the
	 * caller to verify.
	 */
	public Fly(int pieceType, Board myBoard, int x, int y) {
		super(pieceType, myBoard, x, y);
	}

	/**
	 * Constructor
	 */
	public Fly(int pieceType) {
		super(pieceType);
	}

	/**
	 * Check if the piece can move from start to end location.
	 * 
	 * @return true if it can. false otherwise.
	 */
	public boolean canMoveBetween(Position start, Position end) {
		if (start.isTwoSpacesFrom(end) || start.isLTo(end))
			return true;
		return false;
	}

}