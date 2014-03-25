package game;

import java.io.Serializable;
import java.util.ArrayList;

import pieces.*;

/**
 * The Board class represents the Chess board and tracks the pieces.
 * 
 * @author Shashank Bharadwaj
 * @date Wed, 4 Sep 2013
 */
public class Board implements Serializable {

	private static final long serialVersionUID = 3447954848089334090L;

	// Piece pointers for easy access
	ArrayList<Piece> whitePieces = new ArrayList<Piece>();
	ArrayList<Piece> blackPieces = new ArrayList<Piece>();
	public Piece whiteKing;
	public Piece blackKing;

	// List of spaces
	ArrayList<Position> spaces = new ArrayList<Position>();

	// Last move state

	// Board is represented by x,y coordinates. a5 will be 0,4
	public Piece[][] board;
	private static final int size = 8;

	// Classic and custom board types
	public static final int CLASSIC = 1;
	public static final int CUSTOM = 2;

	// Piece types
	public static final int WHITE = +1;
	public static final int BLACK = -1;
	public static final int EMPTY = 0;
	public static final int PAWN = 1;
	public static final int ROOK = 2;
	public static final int KNIGHT = 3;
	public static final int BISHOP = 4;
	public static final int QUEEN = 5;
	public static final int KING = 6;
	public static final int FLY = 7;
	public static final int FLYTRAP = 8;

	// -1 if black is in check, +1 if white is in check
	public int check = Piece.EMPTY;

	/**
	 * Constructor creates an 8x8 array with chess pieces in initial
	 * configuration. Piece numbering: 1-6. White pieces are + and Black are -
	 * Eg. Black rook will be -2
	 */
	public Board(int boardType) {

		board = new Piece[size][size];
		fillBoard(boardType);
	}

	/**
	 * Fills the board with pieces
	 * 
	 * @param boardType
	 *            : Classic board or Custom board
	 */
	private void fillBoard(int boardType) {

		// Get list of spaces for references and set them as empty
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				spaces.add(new Position(i, j));
				board[i][j] = null;
			}
		}
		// Add pawns
		for (int i = 1; i < size - 1; i++) {
			addPieceTo(new Position(i, 1), new Pawn(WHITE * PAWN));
			addPieceTo(new Position(i, 6), new Pawn(BLACK * PAWN));
		}
		// If Custom chess, replace edge pawns with custom pieces
		if (boardType == CUSTOM) {
			addPieceTo(new Position(0, 1), new Fly(WHITE * FLY));
			addPieceTo(new Position(7, 1), new FlyTrap(WHITE * FLYTRAP));
			addPieceTo(new Position(0, 6), new Fly(BLACK * FLY));
			addPieceTo(new Position(7, 6), new FlyTrap(BLACK * FLYTRAP));
		} else {
			addPieceTo(new Position(0, 1), new Pawn(WHITE * PAWN));
			addPieceTo(new Position(7, 1), new Pawn(WHITE * PAWN));
			addPieceTo(new Position(0, 6), new Pawn(BLACK * PAWN));
			addPieceTo(new Position(7, 6), new Pawn(BLACK * PAWN));
		}
		// Add rest of the black and white pieces
		for (int i = 0; i < size; i += 7) {
			int color = BLACK;
			if (i == 0)
				color = WHITE;
			addPieceTo(new Position(0, i), new Rook(color * ROOK));
			addPieceTo(new Position(7, i), new Rook(color * ROOK));
			addPieceTo(new Position(1, i), new Knight(color * KNIGHT));
			addPieceTo(new Position(6, i), new Knight(color * KNIGHT));
			addPieceTo(new Position(2, i), new Bishop(color * BISHOP));
			addPieceTo(new Position(5, i), new Bishop(color * BISHOP));
			addPieceTo(new Position(3, i), new Queen(color * QUEEN));
			addPieceTo(new Position(4, i), new King(color * KING));
		}

		// Kings and pieces stored. Makes check(mate) search easy.
		whiteKing = board[4][0];
		blackKing = board[4][7];
	}

	/**
	 * Checks if a position is empty
	 * 
	 * @param pos
	 *            : position on the board
	 * @return true if empty, false otherwise
	 */
	public boolean emptyAt(Position pos) {
		return board[pos.getX()][pos.getY()] == null;
	}

	/**
	 * Checks if a position is occupied
	 * 
	 * @param pos
	 *            : position on the board
	 * @return true if occupied, false otherwise
	 */
	public boolean occupiedAt(Position pos) {
		return !emptyAt(pos);
	}

	/**
	 * Gets the pieceType at a position on the board
	 * 
	 * @return pieceType on success, 0 if empty
	 */
	public int getPieceTypeAt(Position pos) {

		if (getPieceAt(pos) == null)
			return 0;

		return getPieceAt(pos).getPieceType();
	}

	/**
	 * Gets the piece object at a position on the board
	 * 
	 * @return Piece object on success, null if empty
	 */
	public Piece getPieceAt(Position pos) {
		if (emptyAt(pos))
			return null;

		return board[pos.getX()][pos.getY()];
	}

	/**
	 * Checks if Piece p and piece at pos are opponents. Spaces must be occupied
	 * 
	 * @return true if they are opponents, false otherwise
	 */
	public boolean areOpponents(Position a, Position b) {
		if (occupiedAt(a) && occupiedAt(b)) {
			int aType = getPieceTypeAt(a);
			int bType = getPieceTypeAt(b);
			if (Math.signum(aType) == Math.signum(bType))
				return false;
			return true;
		}
		return false;
	}

	/**
	 * Checks if Piece p and piece at pos are allies. Spaces must be occupied
	 * 
	 * @return true if they are allies, false otherwise
	 */
	public boolean areAllies(Position a, Position b) {
		return !areOpponents(a, b);
	}

	/**
	 * Checks if a straight or diagonal path is clear. Must not be a one step
	 * move.
	 * 
	 * @param start
	 *            and end positions, and the board
	 * @return true if path is clear, false otherwise.
	 */
	public boolean straightOrDiagIsClear(Position start, Position end) {
		int x = start.getX(), y = start.getY();
		while (true) {
			if (start.getX() != end.getX())
				x = (start.getX() < end.getX()) ? x + 1 : x - 1;
			if (start.getY() != end.getY())
				y = (start.getY() < end.getY()) ? y + 1 : y - 1;

			if (end.equals(x, y))
				return true;
			if (occupiedAt(new Position(x, y))) {
				return false;
			}
		}
	}

	/**
	 * Removes a piece from its data structures. Must be called from a move
	 * function
	 * 
	 * @param pos
	 *            : position from which to remove piece
	 */
	public Piece removePieceFrom(Position pos) {

		Piece p = board[pos.getX()][pos.getY()];

		if (p == null)
			return null;

		board[pos.getX()][pos.getY()] = null;
		if (p.getColor() == WHITE)
			whitePieces.remove(p);
		else
			blackPieces.remove(p);

		return p;
	}

	/**
	 * adds a piece to its data structures. Must be called from a move function
	 * 
	 * @param pos
	 *            : position to add piece
	 */
	public void addPieceTo(Position pos, Piece p) {
		p.setPos(pos);
		p.setBoard(this);
		board[pos.getX()][pos.getY()] = p;

		if (p.getColor() == WHITE)
			whitePieces.add(p);
		else
			blackPieces.add(p);
	}

	/**
	 * Prints the 8x8 chessboard. Black pieces on the top row, white on the
	 * bottom
	 */
	public void printBoard() {
		for (int j = size - 1; j > -1; j--) {
			for (int i = 0; i < size; i++) {
				if (board[i][j] == null) {
					System.out.format("__ ");
					continue;
				}
				System.out.format("%02d ", board[i][j].getPieceType());
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("No. of black pieces: " + blackPieces.size());
		System.out.println("No. of white pieces: " + whitePieces.size());
	}

	/**
	 * Returns an array representation of the board
	 */
	public int[][] toArray() {

		int[][] boardArray = new int[size][size];

		for (int j = size - 1; j > -1; j--) {
			for (int i = 0; i < size; i++) {
				if (board[i][j] == null)
					boardArray[i][j] = 0;
				else
					boardArray[i][j] = board[i][j].getPieceType();
			}
		}

		return boardArray;
	}

	/**
	 * Returns an array representation of the board
	 */
	public byte[] toByteArray() {

		byte[] boardArray = new byte[size * size];

		for (int j = size - 1; j > -1; j--) {
			for (int i = 0; i < size; i++) {
				if (board[i][j] == null)
					boardArray[i + j] = 0;
				else
					boardArray[i + j] = (byte) board[i][j].getPieceType();
			}
		}

		return boardArray;
	}

	/**
	 * Getter
	 */
	public static int getSize() {
		return size;
	}
}
