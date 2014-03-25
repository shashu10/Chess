package pieces;

import game.*;

public class Pawn extends Piece {

	/**
	 * Constructor No check to verify if pieceType is a pawn. It is up to the
	 * caller to verify.
	 */
	public Pawn(int pieceType, Board myBoard, int x, int y) {
		super(pieceType, myBoard, x, y);
	}

	/**
	 * Constructor
	 */
	public Pawn(int pieceType) {
		super(pieceType);
	}

	/**
	 * Check if the piece can move from start to end location.
	 * 
	 * @return true if it can. false otherwise.
	 */
	public boolean canMoveBetween(Position start, Position end) {
		int yChange = end.getY() - start.getY();
		int xChange = end.getX() - start.getX();
		// illegal pawn move
		if (Math.abs(xChange) > 1 || Math.abs(yChange) > 2)
			return false;
		// moving in the right direction
		if (Math.signum(yChange) != Math.signum(color))
			return false;
		// Diagonal Move
		if (Math.abs(xChange) == 1) {
			// Either capture with exact diagonal move or is illegal.
			if (Math.abs(yChange) == 1)
				return myBoard.areOpponents(start, end);
			return false;
		}
		// Straight move has to be empty at the end
		if (myBoard.occupiedAt(end))
			return false;
		// Two step move possible at the beginning
		if (Math.abs(yChange) == 2) {
			int midY = (start.getY() + end.getY()) / 2;
			if (myBoard.occupiedAt(new Position(start.getX(), midY)))
				return false;
			if (pieceType > 0 && start.getY() == 1)
				return true;
			if (pieceType < 0 && start.getY() == 6)
				return true;
			return false;
		}
		// Else pawn moves one step ahead into empty space.
		return true;
	}

	/**
	 * Checks if pawn is moving in the right direction and returns the number of
	 * ranks changed
	 * 
	 * @return -1 if not moving ahead, number of ranks moving otherwise
	 *         (including 0)
	 */
	public boolean isPawnMove(Position start, Position end, Board board) {
		int yChange = end.getY() - start.getY();
		int xChange = end.getX() - start.getX();
		if (Math.abs(xChange) == 1 && Math.abs(yChange) == 1)
			return board.areOpponents(start, end);
		if (Math.abs(xChange) > 1 || Math.abs(yChange) > 2)
			return true;
		if (Math.signum(yChange) == Math.signum(color))
			return false;
		return true;
	}
}
