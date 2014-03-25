package testers;

import static org.junit.Assert.*;
import game.Board;
import game.Position;

import org.junit.Test;

public class PositionTests {

	// Initialize a classic chess board
	Board board = new Board(Board.CLASSIC);
	Position b2 = new Position(1, 1);
	Position b3 = new Position(1, 2);
	Position d1 = new Position(3, 0);
	Position d3 = new Position(3, 2);
	Position e1 = new Position(4, 0);
	Position e2 = new Position(4, 1);
	Position e3 = new Position(4, 2);
	Position f2 = new Position(5, 1);
	Position g4 = new Position(6, 3);
	Position h8 = new Position(7, 7);

	@Test
	public void testEquals() {
		assertTrue(d1.equals(d1));
		assertTrue(d1.equals(3, 0));
	}

	@Test
	public void testNotEquals() {
		assertTrue(d1.notEquals(4, 1));
		assertFalse(d1.notEquals(d1));
	}

	@Test
	public void testLTo() {
		assertFalse(d1.isLTo(d1));
		assertFalse(d1.isLTo(d3));
		assertTrue(d1.isLTo(e3));
		assertTrue(d1.isLTo(b2));
	}

	@Test
	public void testNextTo() {
		assertFalse(d1.isNextTo(d1));
		assertFalse(d1.isNextTo(h8));
		assertTrue(d1.isNextTo(e2));
		assertTrue(d1.isNextTo(e1));
	}

	@Test
	public void testIsDiagTo() {
		assertTrue(d1.isDiagTo(d1));
		assertTrue(d1.isDiagTo(e2));
		assertTrue(d1.isDiagTo(g4));
		assertFalse(d1.isDiagTo(h8));
	}

	@Test
	public void testIsStraightTo() {
		assertTrue(e2.isStraightTo(f2));
		assertTrue(e2.isStraightTo(e2));
		assertFalse(e2.isStraightTo(g4));
	}

	@Test
	public void testIsStraightOrDiagTo() {
		assertTrue(d1.isStraightOrDiagTo(g4));
		assertTrue(e2.isStraightOrDiagTo(f2));
	}

	@Test
	public void testIsTwoSpacesFrom() {
		assertTrue(d1.isTwoSpacesFrom(b3));
		assertTrue(d1.isTwoSpacesFrom(d3));
		assertFalse(d1.isTwoSpacesFrom(h8));
		assertFalse(d1.isTwoSpacesFrom(d1));
	}

	@Test
	public void testSettersAndGetters() {
		Position e6 = new Position(4, 5);
		e6.setX(7);
		e6.setY(7);
		assertTrue(h8.equals(e6.getX(), e6.getY()));
	}
}
