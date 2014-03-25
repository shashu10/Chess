package game;

/**
 * Represents location on the board
 * 
 * @author Shashank Bharadwaj
 * @date Wed, 4 Sep 2013
 * 
 * @param x
 *            : Columns a-h
 * @param y
 *            : Rows 1-8
 */
public class Position {
	private int y;
	private int x;

	/**
	 * Constructs a valid (x,y) position tuple on the board.
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @throws IllegalArgumentException
	 *             if coordinates are illegal
	 */
	public Position(int x, int y) throws IllegalArgumentException {
		if (x < 0 || y < 0 || x >= Board.getSize() || y >= Board.getSize()) {
			throw new IllegalArgumentException("Coordinates not on board");
		}
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter
	 * 
	 * @return y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Getter
	 * 
	 * @return x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Setter
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Setter
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Checks if calling object is equal to parameter p
	 * 
	 * @param Positoin
	 *            p to check with calling position object
	 * @return True if positions are equal, false otherwise.
	 */
	public boolean equals(Position p) {
		if (this.y == p.y && this.x == p.x) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if calling object is equal to coordinate x,y
	 * 
	 * @param Coordinate
	 *            x,y to check with calling position object
	 * @return True if positions are equal, false otherwise.
	 */
	public boolean equals(int x, int y) {
		if (this.x == x && this.y == y)
			return true;
		return false;
	}

	/**
	 * Checks if positions are not equal
	 */
	public boolean notEquals(Position p) {
		return !this.equals(p);
	}

	/**
	 * Checks if positions are not equal
	 */
	public boolean notEquals(int x, int y) {
		return !equals(x, y);
	}

	/**
	 * Checks if two positions are horizontally or vertically aligned
	 * 
	 * @param Positoin
	 *            p to check with calling position object
	 * @return True if they are, false otherwise
	 */
	public boolean isStraightTo(Position p) {
		if (Math.abs(this.x - p.x) == 0 || Math.abs(this.y - p.y) == 0)
			return true;
		return false;
	}

	/**
	 * Checks if two positions are diagonally aligned
	 * 
	 * @param Positoin
	 *            p to check with calling position object
	 * @return True if they are, false otherwise
	 */
	public boolean isDiagTo(Position p) {
		if (Math.abs(this.x - p.x) == Math.abs(this.y - p.y))
			return true;
		return false;
	}

	/**
	 * Checks if two positions are diagonally, horizontally or vertically
	 * aligned
	 * 
	 * @param Positoin
	 *            p to check with calling position object
	 * @return True if they are, false otherwise
	 */
	public boolean isStraightOrDiagTo(Position p) {
		if (this.isStraightTo(p) || this.isDiagTo(p))
			return true;
		return false;
	}

	/**
	 * Checks if two positions are straight or diagonally next to each other
	 * 
	 * @param Positoin
	 *            p to check with calling position object
	 * @return True if they are, false otherwise
	 */
	public boolean isNextTo(Position p) {
		int absXChange = Math.abs(this.x - p.x);
		int absYChange = Math.abs(this.y - p.y);
		if (absYChange == 0 && absXChange == 0)
			return false;
		if (absYChange < 2 && absXChange < 2)
			return true;
		return false;
	}

	/**
	 * Checks if two positions are 2 spaces apart and 1 space to the side.
	 * 
	 * @param Positoin
	 *            p to check with calling position object
	 * @return True if they are, false otherwise
	 */
	public boolean isLTo(Position p) {
		if (Math.abs(this.x - p.x) == 2 && Math.abs(this.y - p.y) == 1)
			return true;
		else if (Math.abs(this.x - p.x) == 1 && Math.abs(this.y - p.y) == 2)
			return true;
		else
			return false;
	}

	/**
	 * Checks if two positions are 2 spaces apart. Can be straight or diagonal
	 * 
	 * @param Positoin
	 *            p to check with calling position object
	 * @return True if they are, false otherwise
	 */
	public boolean isTwoSpacesFrom(Position p) {
		if (Math.abs(this.x - p.x) == 2 || Math.abs(this.x - p.x) == 0)
			if (Math.abs(this.y - p.y) == 2 || Math.abs(this.y - p.y) == 0)
				if (Math.abs(this.x - p.x) == 0 && Math.abs(this.y - p.y) == 0)
					return false;
				else
					return true;
		return false;
	}
}

class Move {
	public Position start;
	public Position end;

	public Move(Position start, Position end) {
		this.start = start;
		this.end = end;
	}
}