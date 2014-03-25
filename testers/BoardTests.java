package testers;

import static org.junit.Assert.*;

import org.junit.*;

import game.*;
import pieces.*;

public class BoardTests {

	Board board;

	@Before
	public void initialize() {
		board = new Board(Board.CLASSIC);
		board.printBoard();
	}

	Position a7 = new Position(0, 6);
	Position b4 = new Position(1, 3);
	Position b6 = new Position(1, 5);
	Position c2 = new Position(2, 1);
	Position c3 = new Position(2, 2);
	Position d1 = new Position(3, 0);
	Position d2 = new Position(3, 1);
	Position d3 = new Position(3, 2);
	Position d4 = new Position(3, 3);
	Position e1 = new Position(4, 0);
	Position e4 = new Position(4, 3);
	Position e5 = new Position(4, 4);
	Position e6 = new Position(4, 5);
	Position e7 = new Position(4, 6);
	Position f2 = new Position(5, 1);
	Position f7 = new Position(5, 6);
	Position f8 = new Position(5, 7);
	Position h8 = new Position(7, 7);
	Piece p, p1;

	// Placing position outside the board.
	@Test(expected = IllegalArgumentException.class)
	public void tetstPosition1() throws IllegalArgumentException {
		@SuppressWarnings("unused")
		Position p1 = new Position(10, 10);
	}

	@Test
	public void testEmptyAt() {
		assertTrue(board.emptyAt(e5));
		assertFalse(board.emptyAt(f2));
	}

	@Test
	public void testPointsAreOpponents() {
		assertFalse(board.areOpponents(d1, e6));
		assertFalse(board.areOpponents(d1, f2));
		assertTrue(board.areOpponents(d1, h8));
	}

	@Test
	public void testGetPieceAt() {
		assertTrue(board.getPieceAt(d1) instanceof Queen);
		assertTrue(board.getPieceAt(e7) instanceof Pawn);
		assertNull(board.getPieceAt(e6));
	}

	@Test
	public void testGetPieceTypeAt() {
		assertEquals(board.getPieceTypeAt(d1), 5);
		assertEquals(board.getPieceTypeAt(e6), 0);
		assertEquals(board.getPieceTypeAt(h8), -2);
	}

	@Test
	public void testStraightOrDiagIsClear() {
		// Diagonal clear move to an empty spot
		assertTrue(board.straightOrDiagIsClear(f2, b6));
		// Diagonal clear move to enemy piece
		assertTrue(board.straightOrDiagIsClear(f2, a7));
		// Straight clear move
		assertTrue(board.straightOrDiagIsClear(f2, f7));
		// Straight blocked move
		assertFalse(board.straightOrDiagIsClear(d1, d3));
		// One step move
		assertTrue(board.straightOrDiagIsClear(d1, d2));
		// Non straight or diagonal move
		assertFalse(board.straightOrDiagIsClear(d1, e6));
	}

	@Test
	public void testRemovePieceFrom() {
		p = board.removePieceFrom(d1);
		assertTrue(board.emptyAt(d1));
	}

	@Test
	public void testAddPieceTo() {
		p1 = new Fly(7, board, e6.getX(), e6.getY());
		board.addPieceTo(e6, p1);
		assertTrue(board.getPieceTypeAt(e6) == p1.getPieceType());
	}
}
