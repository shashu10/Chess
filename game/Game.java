package game;

import java.io.Serializable;
import java.util.ArrayList;

import pieces.Piece;

/**
 * The game class is responsible for running a game of chess.
 */
public class Game implements Serializable {

	private static final long serialVersionUID = 3475793756911252685L;

	// Chess board types, spaces and pieces
	public static final int CLASSIC = 1;
	public static final int CUSTOM = 2;
	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = -1;

	public Board board;
	public boolean hasChanged;

	// Game State
	public State currentState;
	Move nextMove;

	// Players
	Player white;
	Player black;

	boolean whiteSelfCheck;
	boolean blackSelfCheck;
	private int winner;

	/**
	 * Constructor. Default constructor sets up classic chess.
	 */
	public Game() {
		newGame(CLASSIC);
	}

	/**
	 * Constructor to start a new game.
	 * 
	 * @param gameType
	 */
	public Game(int gameType) {
		newGame(gameType);
	}

	/**
	 * Starts a new game from a constructor or after one has been called.
	 * 
	 * @param gameType
	 */
	public void newGame(int gameType) {
		winner = 0;
		board = new Board(gameType);
		currentState = new State();
		nextMove = new Move(null, null);
		hasChanged = false;
	}

	/**
	 * Allows a network client/socket to see if the game has changed
	 * 
	 * @return
	 */
	public boolean hasChanged() {
		boolean retval = hasChanged;
		hasChanged = false;
		return retval;
	}

	/**
	 * Make moves by selecting one position at a time. First call to this
	 * function must be made by white with coordinates for a white piece.
	 * 
	 * @param x
	 *            coordinate on the board
	 * @param y
	 *            coordinate
	 * @return True if space can be selected. False otherwise.
	 */
	public boolean selectMove(int x, int y) {
		// Reset flags
		whiteSelfCheck = false;
		blackSelfCheck = false;
		// If game has ended
		if (hasEnded())
			return false;

		Position pos;
		try {
			pos = new Position(x, y);
		} catch (IllegalArgumentException e) {
			return false;
		}
		// Start position is set.
		if (nextMove.start == null) {
			Piece p = board.getPieceAt(pos);
			if (p == null)
				return false;
			if (p.getColor() == currentState.getTurn()) {
				nextMove.start = pos;
				return true;
			}
			return false;
		}

		if (board.occupiedAt(pos) && board.areAllies(nextMove.start, pos)) {
			nextMove.start = pos;
			return true;
		}
		Position start = nextMove.start;
		nextMove.start = null;
		return move(start, pos);

	}

	/**
	 * Crates a new move. Allows us to select move from the start.
	 */
	public void newMove() {
		nextMove.start = null;
		nextMove.end = null;
	}

	public void updateBoard(int[][] boardArray) {
		int size = Board.getSize();
		Position start = null;
		Position end = null;
		for (int j = size - 1; j > -1; j--) {
			for (int i = 0; i < size; i++) {
				if (boardArray[i][j] == 0) {
					if (board.board[i][j] != null) {
						start = new Position(i, j);
					}
				}
				if (boardArray[i][j] != 0) {
					if (board.board[i][j] == null)
						end = new Position(i, j);
					else if (board.board[i][j].getPieceType() != boardArray[i][j])
						end = new Position(i, j);
				}
			}
		}
		if (start != null && end != null)
			move(start, end);
	}

	/**
	 * Tries to move a piece from start to end position
	 * 
	 * @precondition start and end must be on the board.
	 * @param start
	 *            and end positions, and the board object
	 * @return true on successful move, false on failure.
	 */
	public boolean move(Position start, Position end) {

		if (board.emptyAt(start) || start.equals(end))
			return false;
		if (board.occupiedAt(end) && board.areAllies(start, end))
			return false;

		Piece movingPiece = board.getPieceAt(start);

		if (movingPiece.canMoveBetween(start, end)) {
			int color = movingPiece.getColor();

			// Save state in case we need to undo illegal move
			State saveState = new State(currentState);

			updateBoardWithMove(start, end);
			nextTurn();

			if (isCheckFor(color)) {
				System.out.println("Cow");
				boolean changedVal = hasChanged;
				// Gives turn back to player
				undoMove();
				hasChanged = changedVal;
				currentState = saveState;
				if (color == WHITE)
					whiteSelfCheck = true;
				else
					blackSelfCheck = true;

				return false;
			}

			if (isCheckFor(movingPiece.getColor() * -1)) {

				currentState.check = movingPiece.getColor() * -1;

				if (isCheckmateFor(movingPiece.getColor() * -1)) {
					winner = movingPiece.getColor();
					blackSelfCheck = false;
					whiteSelfCheck = false;
				}
			}

			hasChanged = true;
			return true;
		}
		return false;
	}

	/**
	 * Undoes move if possible
	 */
	public boolean undoMove() {
		if (!currentState.canUndo || hasEnded())
			return false;

		Piece movingPiece = board.removePieceFrom(currentState.lastMove.end);
		board.addPieceTo(currentState.lastMove.start, movingPiece);
		if (currentState.lastCaptured != null)
			board.addPieceTo(currentState.lastMove.end,
					currentState.lastCaptured);

		currentState.canUndo = false;
		nextTurn();
		hasChanged = true;
		return true;

	}

	/**
	 * Removes piece from start and puts it in end. Start is set to null. Should
	 * only be called from Mover class.
	 */
	public void updateBoardWithMove(Position start, Position end) {

		currentState.lastMove = new Move(start, end);
		currentState.lastCaptured = board.removePieceFrom(end);
		currentState.canUndo = true;

		Piece movingPiece = board.removePieceFrom(start);
		board.addPieceTo(end, movingPiece);
	}

	/**
	 * Checks if the player's king is in check
	 * 
	 * @return True player is in check, false otherwise
	 */
	public boolean isCheckFor(int color) {
		// If player is Black
		Piece king = board.blackKing;
		ArrayList<Piece> opponentPieces = board.whitePieces;
		// If player is White
		if (color == Piece.WHITE) {
			king = board.whiteKing;
			opponentPieces = board.blackPieces;
		}
		for (Piece p : opponentPieces)
			if (p.canMoveBetween(p.getPos(), king.getPos()))
				return true;

		return false;
	}

	/**
	 * Checks if the player's king is in check
	 * 
	 * @return True player is in check, false otherwise
	 */
	@SuppressWarnings("unchecked")
	public boolean isCheckmateFor(int color) {

		if (!isCheckFor(color))
			return false;

		ArrayList<Piece> allies;
		// If player is White
		if (color == Piece.WHITE) {
			allies = (ArrayList<Piece>) board.whitePieces.clone();
		} else
			// Player is Black
			allies = (ArrayList<Piece>) board.blackPieces.clone();

		// Save state for last move
		State saveState = new State(currentState);

		boolean retval = true;
		// Move every piece to try and remove check

		boolean changedVal = hasChanged;
		for (Piece p : allies) {
			for (Position pos : board.spaces) {
				if (move(p.getPos(), pos)) {
					hasChanged = changedVal;
					if (!isCheckFor(color))
						retval = false;
					undoMove();
					// Restore state for last move
					currentState = saveState;
					hasChanged = changedVal;
					if (!retval)
						return retval;
				}
			}
		}
		hasChanged = changedVal;
		return true;
	}

	/**
	 * Changes turns
	 */
	public void nextTurn() {
		currentState.turn *= -1;
	}

	/**
	 * Checks if the game has ended.
	 * 
	 * @return True if it has ended. False otherwise.
	 */
	public boolean hasEnded() {
		return (winner != 0);
	}

	/**
	 * Gets the winning color of the game
	 * 
	 * @return Winning color. 0 is game has not ended.
	 */
	public int getWinner() {
		return winner;
	}

	/**
	 * Checks if a player put himself in check
	 * 
	 * @return
	 */
	public boolean playerPutSelfInCheck() {

		boolean retval;

		// Is called after the turn is tried and denied.
		// Turn passes back to other player. So we must
		// check if current player is the opposite player
		if (currentState.getTurn() == WHITE)
			retval = whiteSelfCheck;
		else
			retval = blackSelfCheck;
		blackSelfCheck = false;
		whiteSelfCheck = false;
		return retval;
	}

	public State getCurrentState() {
		return currentState;
	}

	/**
	 * State stores information at a specific point in the game.
	 * 
	 * @author Shashank Bharadwaj
	 * 
	 */
	public class State {
		// Initial values
		Move lastMove = null;
		Piece lastCaptured = null;
		Piece pieceMoved = null;
		boolean canUndo = false;
		int check = 0;
		public int turn = 1;

		/**
		 * Empty constructor
		 */
		public State() {
		}

		/**
		 * Constructor
		 */
		public State(State s) {
			if (s == null) {
				return;
			}
			this.lastMove = s.lastMove;
			this.pieceMoved = s.pieceMoved;
			this.lastCaptured = s.lastCaptured;
			this.canUndo = s.canUndo;
			this.check = s.check;
			this.turn = s.turn;
		}

		// Setter
		public void setState(Move lastMove, Piece pieceMoved, Piece lastCaptured) {
			this.lastMove = lastMove;
			this.pieceMoved = pieceMoved;
			this.lastCaptured = lastCaptured;
		}

		public boolean isInCheck() {
			return check != 0;
		}

		public int getCheckedColor() {
			return check;
		}

		public int getTurn() {
			return turn;
		}

		public boolean equals(State s) {
			if (this.lastMove != s.lastMove)
				return false;
			if (this.pieceMoved != s.pieceMoved)
				return false;
			if (this.lastCaptured != s.lastCaptured)
				return false;
			if (this.canUndo != s.canUndo)
				return false;
			if (this.check != s.check)
				return false;
			if (this.turn != s.turn)
				return false;
			return true;
		}
	}
}
