package pieces;

import game.*;

public class Rook extends Piece {

	/**
	 * Constructor No check to verify if pieceType is a Rook. It is up to the
	 * caller to verify.
	 */
	public Rook(int pieceType, Board myBoard, int x, int y) {
		super(pieceType, myBoard, x, y);
	}

	/**
	 * Constructor
	 */
	public Rook(int pieceType) {
		super(pieceType);
	}

	/**
	 * Check if the piece can move from start to end location.
	 * 
	 * @return true if it can. false otherwise.
	 */
	public boolean canMoveBetween(Position start, Position end) {
		if (start.isStraightTo(end)) {
			if (start.isNextTo(end))
				return true;
			if (myBoard.straightOrDiagIsClear(start, end))
				return true;
		}
		return false;
	}
}
