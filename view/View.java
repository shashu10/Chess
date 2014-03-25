package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * View class displays the chessboard and pieces.
 * 
 * @author Shashank Bharadwaj. using material from "Chess game (Swing)" on
 *         forgetcode.com
 * @date Wed, 12 Sep 2013
 */
public class View extends JFrame {
	private static final long serialVersionUID = 1L;

	public static final int CLASSIC = 1;
	public static final int CUSTOM = 2;
	public static final int WHITE = 1;
	public static final int BLACK = -1;

	JLayeredPane pane;
	JPanel panel;
	ArrayList<JLabel> icons;

	JMenu help;
	JMenu file;
	JMenuItem newGame;
//	JMenu newGame;
//	JMenuItem classicChess;
//	JMenuItem customChess;
	JMenuItem undo;
	JMenuItem restart;
	JMenuItem forfeit;
	JMenuItem quit;
	JMenuItem rules;
	JMenuItem about;

	int boardSize = 560;

	/**
	 * Constructor: Sets up a square view with a grid. Allows pieces and tiles
	 * to be added easily rather than placing them at specific coordinates.
	 */
	public View() {

		BufferedImage image = null;

		try {
			image = ImageIO.read(getClass().getResource("board.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		setIconImage(image);

		icons = new ArrayList<JLabel>();
		setTitle("Shashank's Chess");
		// Chessboard is surrounded by a border that displays the rows and
		// columns
		// Start indicates the start of chess tiles.
		int start = 19;

		// Layered pane allows tiles and pieces to be placed as JPanels
		pane = new JLayeredPane();
		getContentPane().add(pane);
		getContentPane().setPreferredSize(new Dimension(596, 596));
		pane.setBounds(start, start, boardSize, boardSize);
		setUpMenu();
		addBackground();
		createGrid();
		// addTilesAndPieces();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}

	/**
	 * Creates an 8x8 grid to place pieces on a square board. Allows pieces and
	 * tiles to be added sequentially rather than placing them at specific
	 * coordinates.
	 */
	public void createGrid() {
		// Add a chess board to the Layered Pane
		panel = new JPanel();
		pane.add(panel, JLayeredPane.DEFAULT_LAYER);
		panel.setLayout(new GridLayout(8, 8));
		panel.setBounds(0, 0, boardSize, boardSize);
		panel.setOpaque(false);
		for (int i = 0; i < 64; i++) {
			JPanel square = new JPanel(new BorderLayout());
			panel.add(square);
			square.setOpaque(false);
		}
	}

	/**
	 * Adds a background image "board.png"
	 */
	public void addBackground() {
		// Adds background chessboard image
		try {
			this.getContentPane().add(
					new JLabel(new ImageIcon(ImageIO.read(getClass()
							.getResource("board.png")))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setUpMenu() {

		rules = new JMenuItem("Rules");
		about = new JMenuItem("About");

//		classicChess = new JMenuItem("Classic Chess");
//		customChess = new JMenuItem("Custom Chess");

//		newGame = new JMenu("New Game");
//		newGame.add(classicChess);
//		newGame.add(customChess);
		newGame = new JMenuItem("New Game");

		undo = new JMenuItem("Undo");
		restart = new JMenuItem("Restart");
		forfeit = new JMenuItem("Forfeit");
		quit = new JMenuItem("Quit");

		help = new JMenu("Help");
		help.add(rules);
		help.add(about);

		file = new JMenu("File");
		file.add(newGame);
		file.add(undo);
		file.add(restart);
		file.add(forfeit);
		file.add(quit);

		JMenuBar menubar = new JMenuBar();
		menubar.add(file);
		menubar.add(help);
		this.setJMenuBar(menubar);
	}

	public void addListeners(ViewController c) {
		rules.addActionListener(c);
		about.addActionListener(c);

		newGame.addActionListener(c);
//		classicChess.addActionListener(c);
//		customChess.addActionListener(c);
		
		undo.addActionListener(c);
		restart.addActionListener(c);
		forfeit.addActionListener(c);
		quit.addActionListener(c);
		pane.addMouseListener(c);
	}

	/**
	 * Sets up tiles and chess pieces in initial configuration
	 */
	public void updateWith(int[][] board) {
		// Clear board
		if (board == null) {
			for (int i = 0; i < 64; i++) {
				JPanel square = (JPanel) panel.getComponent(i);
				if (square.getComponents().length == 1)
					square.remove(square.getComponent(0));
			}
			return;
		}
		// Update board
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				int y = board[i].length - 1 - j;
				JPanel square = (JPanel) panel.getComponent(i + y * 8);
				if (square.getComponents().length == 1) {
					square.remove(square.getComponent(0));
				}
				if (Math.abs(board[i][j]) == 0)
					continue;
				String color = "Black";
				if (Math.signum(board[i][j]) > 0)
					color = "White";
				JLabel piece = new JLabel(new ImageIcon(getClass().getResource(
						color + Math.abs(board[i][j]) + ".png")));
				icons.add(piece);
				square.add(piece);
			}
		}
	}

	public void highlightSquare(JPanel square, Color color) {

		square.setBorder(BorderFactory.createLineBorder(color, 5));
	}

	public void removeHighlight(JPanel square) {
		square.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	}

	/**
	 * Sets up tiles and chess pieces in initial configuration
	 */
	public void addTilesAndPieces(int chessType) {
		// Grid layout allows going iterating from 1-64
		for (int i = 0; i < 64; i++) {
			JPanel square = new JPanel(new BorderLayout());
			panel.add(square);
			square.setOpaque(false);
			JLabel piece;
			if (i == 0 || i == 7) {
				piece = new JLabel(new ImageIcon("images/Black/2.png"));
			} else if (i == 1 || i == 6) {
				piece = new JLabel(new ImageIcon("images/Black/3.png"));
			} else if (i == 2 || i == 5) {
				piece = new JLabel(new ImageIcon("images/Black/4.png"));
			} else if (i == 3) {
				piece = new JLabel(new ImageIcon("images/Black/5.png"));
			} else if (i == 4) {
				piece = new JLabel(new ImageIcon("images/Black/6.png"));
			} else if (i == 8) {
				if (chessType == CLASSIC)
					piece = new JLabel(new ImageIcon("images/Black/1.png"));
				else
					piece = new JLabel(new ImageIcon("images/Black/7.png"));
			} else if (i == 15) {
				if (chessType == CLASSIC)
					piece = new JLabel(new ImageIcon("images/Black/1.png"));
				else
					piece = new JLabel(new ImageIcon("images/Black/8.png"));
			} else if (i < 16) {
				piece = new JLabel(new ImageIcon("images/Black/1.png"));
			} else if (i == 56 || i == 63) {
				piece = new JLabel(new ImageIcon("images/White/2.png"));
			} else if (i == 57 || i == 62) {
				piece = new JLabel(new ImageIcon("images/White/3.png"));
			} else if (i == 58 || i == 61) {
				piece = new JLabel(new ImageIcon("images/White/4.png"));
			} else if (i == 59) {
				piece = new JLabel(new ImageIcon("images/White/5.png"));
			} else if (i == 60) {
				piece = new JLabel(new ImageIcon("images/White/6.png"));
			} else if (i == 48) {
				if (chessType == CLASSIC)
					piece = new JLabel(new ImageIcon("images/White/1.png"));
				else
					piece = new JLabel(new ImageIcon("images/White/7.png"));
			} else if (i == 55) {
				if (chessType == CLASSIC)
					piece = new JLabel(new ImageIcon("images/White/1.png"));
				else
					piece = new JLabel(new ImageIcon("images/White/8.png"));
			} else if (i > 47) {
				piece = new JLabel(new ImageIcon("images/White/1.png"));
			} else {
				continue;
			}
			square.add(piece);
		}
	}
}