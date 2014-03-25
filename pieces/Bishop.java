package pieces;

import game.*;

public class Bishop extends Piece {

	/**
	 * Constructor No check to verify if pieceType is a Bishop. It is up to the
	 * caller to verify.
	 */
	public Bishop(int pieceType, Board myBoard, int x, int y) {
		super(pieceType, myBoard, x, y);
	}

	/**
	 * Constructor
	 */
	public Bishop(int pieceType) {
		super(pieceType);
	}

	/**
	 * Check if the piece can move from start to end location.
	 * 
	 * @return true if it can. false otherwise.
	 */
	public boolean canMoveBetween(Position start, Position end) {
		if (start.isDiagTo(end)) {
			if (start.isNextTo(end))
				return true;
			if (myBoard.straightOrDiagIsClear(start, end))
				return true;
		}
		return false;
	}
}
