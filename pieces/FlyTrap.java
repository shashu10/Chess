package pieces;

import game.*;

public class FlyTrap extends Piece {

	/**
	 * Constructor No check to verify if pieceType is a Peasant. It is up to the
	 * caller to verify.
	 */
	public FlyTrap(int pieceType, Board myBoard, int x, int y) {
		super(pieceType, myBoard, x, y);
	}

	/**
	 * Constructor
	 */
	public FlyTrap(int pieceType) {
		super(pieceType);
	}

	/**
	 * Check if the piece can move from start to end location
	 * 
	 * @return true if it can. false otherwise.
	 */
	public boolean canMoveBetween(Position start, Position end) {
		if (start.isNextTo(end) && start.isDiagTo(end)) {
			return true;
		}
		return false;
	}

}
