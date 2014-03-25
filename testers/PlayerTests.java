package testers;

import static org.junit.Assert.*;
import game.*;
import pieces.*;

import org.junit.Test;

public class PlayerTests {

	Board board = new Board(Board.CLASSIC);
	Player white = new Player("white", Piece.WHITE, board);
	Player black = new Player("black", Piece.BLACK, board);

	@Test
	public void test() {
		assertTrue(true);
	}

}
