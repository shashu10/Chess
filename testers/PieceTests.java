package testers;

import static org.junit.Assert.*;
import game.*;

import org.junit.Test;

import pieces.*;

public class PieceTests {

	Game game = new Game();

	Position a3 = new Position(0, 2);
	Position a4 = new Position(0, 3);
	Position a5 = new Position(0, 4);
	Position a6 = new Position(0, 5);
	Position b1 = new Position(1, 0);
	Position b2 = new Position(1, 1);
	Position b4 = new Position(1, 3);
	Position c1 = new Position(2, 0);
	Position c3 = new Position(2, 2);
	Position c5 = new Position(2, 4);
	Position c7 = new Position(2, 6);
	Position d1 = new Position(3, 0);
	Position d5 = new Position(3, 4);
	Position d6 = new Position(3, 5);
	Position e1 = new Position(4, 0);
	Position e2 = new Position(4, 1);
	Position g2 = new Position(6, 1);
	Position g3 = new Position(6, 2);
	Position g4 = new Position(6, 3);
	Position h2 = new Position(7, 1);
	Position h3 = new Position(7, 2);
	Position h4 = new Position(7, 3);
	Position h5 = new Position(7, 4);
	Position h7 = new Position(7, 6);
	Position h8 = new Position(7, 7);

	@Test
	public void testPawnMove() {
		game.newGame(Board.CLASSIC);
		// One step
		assertTrue(game.move(h2, h3));
		// Capturing ally
		assertFalse(game.move(g2, h3));
		// Sideways move
		assertFalse(game.move(h3, g3));
		// Backward move
		assertFalse(game.move(h3, h2));
		// Double step after initial move
		assertFalse(game.move(h3, h5));
		// Double steps for black and white pawn
		game.move(h7, h5);
		game.move(g2, g4);
		// Black captures white
		assertTrue(game.move(h5, g4));
		// White moves forward and tries to capture black horizontally
		game.move(h3, h4);
		assertFalse(game.move(h4, g4));
	}

	@Test
	public void testKnightMove() {
		game.newGame(Board.CLASSIC);
		// Forward L Move
		game.move(b1, c3);
		// Attack Ally (illegal)
		game.move(c3, d1);
		// Forward L Move
		game.move(c3, d5);
		// Non L move (illegal)
		game.move(d5, a4);
		// Capture
		assertTrue(game.move(d5, c7));
		// Final knight position: c7
	}

	@Test
	public void testRookMove() {
		game.newGame(Board.CLASSIC);
		// White Rook on a3
		game.board.addPieceTo(a3, new Rook(2, game.board, 0, 2));
		// Sideways move
		game.move(a3, h3);
		// Forward move
		game.move(h3, h5);
		// Trying to jump over an enemy and capture (illegal)
		game.move(h5, h8);
		// Attacking ally (illegal)
		game.move(h5, h2);
		// Forward capture
		assertTrue(game.move(h5, h7));
		// Final rook position: h7
	}

	@Test
	public void testBishopMove() {
		game.newGame(Board.CLASSIC);
		// Black bishop on a3
		game.board.addPieceTo(a3, new Bishop(-4, game.board, 0, 2));
		// Up-Right diagonal move
		game.move(a3, d6);
		// Down-Right diagonal capture
		game.move(d6, h2);
		// Attacking ally (illegal)
		game.move(h2, c7);
		// Illegal straight move
		game.move(h2, h8);
		// Up-Left diagonal move
		assertTrue(game.move(h2, g3));
		// Final bishop position: g3
	}

	@Test
	public void testQueenMove() {
		game.newGame(Board.CLASSIC);
		// White Queen on a3
		game.board.addPieceTo(a3, new Queen(5, game.board, 0, 2));
		// White: Sideways move: Queen to h3
		game.move(a3, h3);
		// Black Queen on a3
		game.board.addPieceTo(a3, new Queen(-5, game.board, 0, 2));
		// Black: Up-Right diagonal move: Queen to d6
		game.move(a3, d6);
		// White: Forward move: Queen to h5
		game.move(h3, h5);
		// Black: down-Right diagonal capture: Queen to h2
		game.move(d6, h2);
		// White: Jump over enemy and capture: Queen to h8 (illegal)
		game.move(h5, h8);
		// White: Attacking ally: Queen to h2 (illegal)
		game.move(h5, e2);
		// White: Forward capture: Queen to h7
		assertTrue(game.move(h5, h7));
		// Attacking ally: Black Queen to c7 (illegal)
		game.move(h2, c7);
		// Straight move: Black Queen to h8 (illegal)
		game.move(h2, h8);
		// Up-Left diagonal move: Black Queen to d6
		assertTrue(game.move(h2, d6));
		// Final Black Queen position: g6
		// Final White Queen position: h7
	}

	@Test
	public void testKingMove() {
		game.newGame(Board.CLASSIC);
		// Forward L Move
		game.move(b1, c3);
		// Attack Ally (illegal)
		game.move(c3, d1);
		// Forward L Move
		game.move(c3, d5);
		// Non L move (illegal)
		game.move(d5, a4);
		// Capture
		assertTrue(game.move(d5, c7));
		// Final knight position: c7
	}

	@Test
	public void testArcherMove() {
		game.newGame(Board.CLASSIC);
		// Archer on a3
		game.board.addPieceTo(a3, new Fly(7, game.board, 0, 2));
		// Down-Right attacking ally
		game.move(a3, c1);
		// Up-Right move
		game.move(a3, c5);
		// Same space move
		game.move(c5, c5);
		// Straight up capture
		game.move(c5, c7);
		// Down-right L move
		assertTrue(game.move(c7, d5));
		// Final Archer position: d5
	}

	@Test
	public void testPeasantMove() {
		game.newGame(Board.CLASSIC);
		// Peasant on a3
		game.board.addPieceTo(a3, new FlyTrap(8, game.board, 0, 2));
		// Down-Right attacking ally
		game.move(a3, b2);
		// Up-Right move
		game.move(a3, b4);
		// Up-Right move
		game.move(b4, c5);
		// Up-Right move
		game.move(c5, d6);
		// Up-Right move
		assertTrue(game.move(d6, c7));
		// Final Peasant position: c7
	}
}
