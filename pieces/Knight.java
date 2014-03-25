package pieces;

import game.*;

public class Knight extends Piece {

	/**
	 * Constructor No check to verify if pieceType is a Knight. It is up to the
	 * caller to verify.
	 */
	public Knight(int pieceType, Board myBoard, int x, int y) {
		super(pieceType, myBoard, x, y);
	}

	/**
	 * Constructor
	 */
	public Knight(int pieceType) {
		super(pieceType);
	}

	/**
	 * Check if the piece can move from start to end location.
	 * 
	 * @return true if it can. false otherwise.
	 */
	public boolean canMoveBetween(Position start, Position end) {
		if (start.isLTo(end)) {
			return true;
		}
		return false;
	}

}
