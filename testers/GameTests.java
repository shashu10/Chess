package testers;

import static org.junit.Assert.*;
import game.Board;
import game.Game;
import game.Position;

import org.junit.Test;

import pieces.Piece;

public class GameTests {

	Game game = new Game();

	Position a2 = new Position(0, 1);
	Position a4 = new Position(0, 3);
	Position b4 = new Position(1, 3);
	Position c2 = new Position(2, 1);
	Position c3 = new Position(2, 2);
	Position d2 = new Position(3, 1);
	Position d3 = new Position(3, 2);
	Position d4 = new Position(3, 3);
	Position e1 = new Position(4, 0);
	Position e4 = new Position(4, 3);
	Position e5 = new Position(4, 4);
	Position e6 = new Position(4, 5);
	Position e7 = new Position(4, 6);
	Position f8 = new Position(5, 7);

	@Test
	public void testIsCheckFor() {
		game.newGame(Board.CLASSIC);
		// Pawns move to open up white king and black bishop
		game.move(d2, d4);
		game.move(e7, e6);
		game.move(f8, b4);
		game.board.printBoard();
		assertTrue(game.isCheckFor(Piece.WHITE));
		game.move(c2, c3);
		assertFalse(game.isCheckFor(Piece.WHITE));
	}

	@Test
	public void testIsCheckmateFor() {
		game.newGame(Board.CLASSIC);
		assertFalse(game.isCheckmateFor(Piece.WHITE));
		// Pawns move to open up white king and black bishop
		game.move(d2, d4);
		game.move(e7, e6);
		game.move(f8, b4);
		assertFalse(game.isCheckmateFor(Piece.WHITE));
		game.board.printBoard();

		game.board.printBoard();
		// King moves to e7
		Piece king = game.board.removePieceFrom(e1);
		game.board.addPieceTo(e7, king);
		assertTrue(game.isCheckmateFor(Piece.WHITE));
	}

	@Test
	public void testSelectMove() {
		game.newGame(Board.CLASSIC);
		// Move pawn to a4
		game.selectMove(0, 1);
		game.selectMove(0, 3);
		// Try to move pawn again in the same turn
		assertFalse(game.selectMove(0, 3));
		// Try to move an empty space
		assertFalse(game.selectMove(0, 4));
		// Move black pawn to b5
		game.selectMove(1, 6);
		game.selectMove(1, 4);
		// Capture pawn at b5
		game.selectMove(0, 3);
		assertTrue(game.selectMove(1, 4));
	}

	public void testUpdateBoard() {
		game.newGame(Board.CLASSIC);
		// Move pawn to a4
		game.selectMove(0, 1);
		game.selectMove(0, 3);

		Game game1 = new Game();
		game1.newGame(Board.CLASSIC);
		game1.updateBoard(game.board.toArray());
		assertFalse(game1.board.emptyAt(a4));
		assertTrue(game1.board.emptyAt(a2));

	}

	@Test
	public void testTryToMove() {
		// Implicitly tested by pieces
	}
}
