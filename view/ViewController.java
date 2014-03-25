package view;

import game.Board;
import game.Game;
import networking.Message;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Enables a graphical game of chess to be played. Works by sending single
 * spaces as part of a move from one space to another.
 * 
 * @author Shashank Bharadwaj
 * 
 */
public class ViewController implements ActionListener, MouseListener {

	// Board and Piece Types
	public static final int WHITE = 1;
	public static final int BLACK = -1;
	public static final int CLASSIC = 1;
	public static final int CUSTOM = 2;

	JPanel highlightedSquare;
	JPanel pressedSquare;
	JPanel lastSelected;

	public int player;
	public Game game;
	private View view;
	boolean hasNotifiedBlackCheck;
	boolean hasNotifiedWhiteCheck;
	boolean hasRestarted = false;
	boolean hasForfeited = false;
	boolean newClassicGame = false;
	boolean newCustomGame = false;
	boolean shownNewGameDialog;

	// No. of wins for white and black.
	static int whiteWins = 0;
	static int blackWins = 0;

	public ViewController(int player) {
		// Used for network game
		this.player = player;
		view = new View();
		view.addListeners(this);
		hasNotifiedBlackCheck = false;
		hasNotifiedWhiteCheck = false;
		shownNewGameDialog = false;
		lastSelected = pressedSquare = highlightedSquare = null;
		newGame(CLASSIC);
		newClassicGame = true;
		view.validate();
		view.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		JMenuItem source = (JMenuItem) event.getSource();
		if (source.getText().contains("New Game")) {
			if (game != null)
				if (!restart())
					return;
			newGame(CLASSIC);
			newClassicGame = true;
//		if (source.getText().contains(" Chess")) {
//			if (game != null)
//				if (!restart())
//					return;
//
//			if (source.getText().contains("Custom")) {
//				newGame(CUSTOM);
//				newCustomGame = true;
//			} else {
//				newGame(CLASSIC);
//				newClassicGame = true;
//			}
		} else if (source.getText().equals("Undo")) {
			if (game == null)
				return;
			if (game.undoMove())
				view.updateWith(game.board.toArray());
			else
				JOptionPane.showMessageDialog(null, "Cannot undo", "Invalid",
						JOptionPane.ERROR_MESSAGE);
			resetHighlight(null);
		} else if (source.getText().equals("Forfeit")) {
			if (game == null)
				return;
			forfeit();
		} else if (source.getText().equals("Restart")) {
			if (game == null)
				return;
			restart();
		} else if (source.getText().equals("Quit")) {
			System.exit(0);
		} else if (source.getText().equals("Rules")) {
			showRules();
		} else if (source.getText().equals("About")) {
			showAbout();
		}

		view.validate();
		view.repaint();
	}

	/**
	 * Creates a new game and updates the view
	 * 
	 * @param gameType
	 */
	public void newGame(int gameType) {
		game = new Game(gameType);
		view.updateWith(game.board.toArray());
	}

	/**
	 * Updates the game when when playing over a network
	 * 
	 * @param game
	 */
	public void updateGame(Message m) {
		if (m == null)
			return;
		if (m.boardArray == null)
			return;
		resetHighlight(null);
		game.updateBoard(m.boardArray);
		view.updateWith(m.boardArray);
		view.validate();
		view.repaint();
		System.out.println("Updating");
	}

	/**
	 * Forfeits a game and notifies players
	 */
	private void forfeit() {
		Object[] options = { "Yes", "No" };
		int n = JOptionPane.showOptionDialog(new JFrame(),
				"Are you sure you want to forfeit?", "",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[1]);
		if (n == JOptionPane.OK_OPTION) {
			String s;
			if (game.getCurrentState().getTurn() == WHITE) {
				blackWins++;
				s = "White";
			} else {
				whiteWins++;
				s = "Black";
			}
			JOptionPane.showMessageDialog(null, s + " Forfeits\nScorecard:\n"
					+ "Black: " + blackWins + "\nWhite: " + whiteWins);
			game = null;
			resetHighlight(null);
		}
		hasForfeited = true;
	}

	/**
	 * Restarts a game and notifies players
	 */
	private boolean restart() {
		Object[] options = { "Yes", "No" };
		int n = JOptionPane.showOptionDialog(new JFrame(),
				"Are you sure you want to restart?", "",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[1]);
		if (n == JOptionPane.OK_OPTION) {
			game = null;
			resetHighlight(null);
			view.updateWith(null);
			view.validate();
			view.repaint();
			JOptionPane.showMessageDialog(null, "Scorecard:\n" + "Black: "
					+ blackWins + "\nWhite: " + whiteWins);
			hasRestarted = true;
			return true;
		}
		return false;
	}

	/**
	 * Shows about blurb on the screen.
	 */
	private void showAbout() {
		JOptionPane.showMessageDialog(null, "Created by Shashank "
				+ "Bharadwaj\n\nChess Pieces by AtskaHeart "
				+ "at deviantart.com\nCustom Pieces by Hann Lindahl", "About",
				JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Displays game rules on the screen.
	 */
	private void showRules() {
		JOptionPane.showMessageDialog(null, ""
				+ "CLassic Chess follows standard rules but\n"
				+ "without castling or en passant.\n\n"
				+ "Custom Chess includes two special pieces.\n"
				+ "A 'fly' that can move two space in any\n"
				+ "direction. And a flytrap that can only move\n"
				+ "one space diagonally.\n\n"
				+ "Upcomming feature for flytrap: converting\n"
				+ "enemy flies to it's team.", "Rules",
				JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Scales pixel location to board location (0-7)
	 * 
	 * @param value
	 *            to be scaled. Represents pixels on the screen
	 * @return scaled value
	 */
	public int scaleFromView(int value) {
		return value / (view.boardSize / Board.getSize());
	}

	/**
	 * Removes old highlighted square
	 * 
	 * @param square
	 *            to be highlighted
	 */
	public void resetHighlight(JPanel square) {
		// Remove highlighted square from last turn
		if (highlightedSquare != null)
			view.removeHighlight(highlightedSquare);
		// Highlight newly selected square
		highlightedSquare = square;
	}

	/**
	 * Checks if two clicks indicate a valid move sequence. Moves the piece in
	 * the model and updates the view if possible.
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// Get clicked square from graphic x, y coordinates
		int x = scaleFromView(arg0.getX());
		int y = scaleFromView(arg0.getY());
		JPanel square = (JPanel) view.panel.getComponent(x + y * 8);
		if (square.equals(lastSelected))
			return;
		lastSelected = square;

		// If a game has not yet been started
		if (game == null) {
			if (!shownNewGameDialog) {
				JOptionPane.showMessageDialog(null, "Create a new game");
				shownNewGameDialog = true;
			}
			return;
		} else if (game.hasEnded())
			return;

		if (player == 0) {
		} else if (player != game.currentState.turn)
			return;

		resetHighlight(square);
		// Convert y coordinate to Cartesian coordinate
		y = Board.getSize() - 1 - y;

		// If move sequence is valid, set highlight to green
		boolean isValidMove = game.selectMove(x, y);
		Color color = Color.RED;
		if (isValidMove)
			color = Color.GREEN;
		// Update view
		view.updateWith(game.board.toArray());
		view.highlightSquare(square, color);
		view.validate();
		view.repaint();
//		if (game.playerPutSelfInCheck()) {
//			JOptionPane
//					.showMessageDialog(null, "This move places you in check");
//		}
		if (game.hasEnded()) {
			notifyEndGame();
			game = null;
		} else if (game.playerPutSelfInCheck()) {
			JOptionPane
					.showMessageDialog(null, "Check!");
		} else
			notifyCheck();
	}

	/**
	 * Notifies a player if he is in check. Flag stops dialog from appearing
	 * with each click.
	 */
	public void notifyCheck() {
		// Reset notification flags.
		if (!game.getCurrentState().isInCheck())
			return;

		if (game.getCurrentState().getCheckedColor() == WHITE) {
			if (hasNotifiedWhiteCheck) {
			} else {
				JOptionPane.showMessageDialog(null, "White is in check");
				hasNotifiedWhiteCheck = true;
			}
		} else {// White is not in check.
			hasNotifiedWhiteCheck = false;
		}
		if (game.getCurrentState().getCheckedColor() == BLACK) {
			if (hasNotifiedBlackCheck) {
			} else {
				JOptionPane.showMessageDialog(null, "Black is in check");
				hasNotifiedBlackCheck = true;
			}
		} else { // Black is not in check.
			hasNotifiedBlackCheck = false;
		}

	}

	public void notifyEndGame() {
		String s;
		if (game.getWinner() == WHITE) {
			whiteWins++;
			s = "White";
		} else {
			blackWins++;
			s = "Black";
		}
		JOptionPane.showMessageDialog(null, s + " has won\n" + "Scorecard:\n"
				+ "Black: " + blackWins + "\nWhite: " + whiteWins);
		resetHighlight(null);
	}

	/**
	 * Reset flags sent over network
	 */
	public void resetFlags() {
		hasRestarted = false;
		hasForfeited = false;
		newClassicGame = false;
		newCustomGame = false;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		int x = scaleFromView(e.getX());
		int y = scaleFromView(e.getY());
		pressedSquare = (JPanel) view.panel.getComponent(x + y * 8);
		mouseClicked(e);
	}

	public void mouseReleased(MouseEvent e) {
		int x = scaleFromView(e.getX());
		int y = scaleFromView(e.getY());
		JPanel releasedSquare = (JPanel) view.panel.getComponent(x + y * 8);
		if (pressedSquare.equals(releasedSquare))
			return;
		mouseClicked(e);
	}
}
