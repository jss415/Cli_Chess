package chess.ui.tui;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import chess.model.Board;
import chess.model.Move;
import chess.model.Pawn;
import chess.model.Piece;

public class Tui {

	private final Board board;
	private final Deque<Move> history = new ArrayDeque<>();

	private final List<Piece> capturedByWhite = new ArrayList<>();
	private final List<Piece> capturedByBlack = new ArrayList<>();

	private Move lastMove = null;
	private boolean whiteTurn = true;
	private boolean flipped = false;

	private final StringBuilder moveBuffer = new StringBuilder();
	private String status = "";
	private String drawOfferBy = null;

	private boolean gameOver = false;
	private String  endMessage = "";

	public Tui() {
		this.board = new Board();
	}

	public void play() throws Exception {
		Terminal terminal = new DefaultTerminalFactory().createTerminal();
		Screen screen = new TerminalScreen(terminal);
		screen.startScreen();
		screen.setCursorPosition(null);

		try {
			while (true) {
				screen.clear();
				render(screen);

				screen.refresh();

				KeyStroke key = screen.readInput();
				if (key == null) continue;

				if (gameOver) break;
				handleKey(key);
			}
		} finally {
			screen.stopScreen();
		}
	}

	private void render(Screen screen) {
		BoardRenderer.draw(screen, lastMove, flipped);

		PlayerPanel.draw(screen, "Player 1 (W)", capturedByWhite,
			whiteTurn && !gameOver, Layout.PANEL_COL, Layout.PLAYER1_ROW);
		PlayerPanel.draw(screen, "Player 2 (B)", capturedByBlack,
			!whiteTurn && !gameOver, Layout.PANEL_COL, Layout.PLAYER2_ROW);

		StatusLine.draw(screen, status, Layout.STATUS_ROW);

		if (gameOver) MovePrompt.drawEnded(screen, endMessage, Layout.PROMPT_ROW);
		else          MovePrompt.drawPlaying(screen, moveBuffer.toString(), Layout.PROMPT_ROW);

		CommandBar.draw(screen, Layout.CMDBAR_ROW);
	}

	private void handleKey(KeyStroke key) {
		KeyType t = key.getKeyType();

		if (t == KeyType.F1)        { flipped = !flipped; status = ""; return; }
		if (t == KeyType.F2)        { takeback(); return; }
		if (t == KeyType.F3)        { offerOrAcceptDraw(); return; }
		if (t == KeyType.F4)        { resign(); return; }
		if (t == KeyType.F8)        { gameOver = true; endMessage = "Exited."; return; }
		if (t == KeyType.Escape)    { gameOver = true; endMessage = "Exited."; return; }
		if (t == KeyType.Enter)     { submitMove(); return; }
		if (t == KeyType.Backspace) {
			if (moveBuffer.length() > 0) moveBuffer.deleteCharAt(moveBuffer.length() - 1);
			status = "";
			return;
		}
		if (t == KeyType.Character) {
			Character c = key.getCharacter();
			if (c != null && moveBuffer.length() < 16) {
				moveBuffer.append(c);
				status = "";
			}
		}
	}

	private String currentSide()        { return whiteTurn ? "W" : "B"; }
	private String currentPlayerName()  { return whiteTurn ? "Player 1" : "Player 2"; }
	private String otherPlayerName()    { return whiteTurn ? "Player 2" : "Player 1"; }

	private void submitMove() {
		String input = moveBuffer.toString().trim();
		moveBuffer.setLength(0);

		String[] parsed = parseMove(input);
		if (parsed == null) { status = "Illegal move, try again."; return; }

		Pawn.setPromo(parsed[2]);
		String side = currentSide();
		if (!Board.validateMove(parsed[0], parsed[1], side)) {
			status = "Illegal move, try again.";
			return;
		}

		Move m = Board.move(parsed[0], parsed[1]);
		history.push(m);
		lastMove = m;

		if (m.captured != null) {
			if (side.equals("W")) capturedByWhite.add(m.captured);
			else                   capturedByBlack.add(m.captured);
		}

		if (drawOfferBy != null && !drawOfferBy.equals(side)) drawOfferBy = null;

		String other = whiteTurn ? "B" : "W";
		whiteTurn = !whiteTurn;

		if (Board.checkmate(other)) {
			endGame("Checkmate. " + (side.equals("W") ? "Player 1" : "Player 2") + " wins.");
			return;
		}
		if (Board.stalemate(other)) { endGame("Stalemate. Draw."); return; }
		if (Board.isInsufficientMaterial()) { endGame("Draw by insufficient material."); return; }

		status = Board.isInCheck(other) ? "Check!" : "";
	}

	private void takeback() {
		if (history.isEmpty()) { status = "Nothing to take back."; return; }
		Move m = history.pop();

		if (m.captured != null) {
			List<Piece> list = m.moved.getColor().equals("W") ? capturedByWhite : capturedByBlack;
			if (!list.isEmpty()) list.remove(list.size() - 1);
		}

		Board.unmake(m);
		whiteTurn = !whiteTurn;
		lastMove = history.isEmpty() ? null : history.peek();
		drawOfferBy = null;
		status = "Took back " + otherPlayerName() + "'s move.";
	}

	private void offerOrAcceptDraw() {
		if (drawOfferBy != null && !drawOfferBy.equals(currentSide())) {
			endGame("Draw by agreement.");
			return;
		}
		drawOfferBy = currentSide();
		status = currentPlayerName() + " offered a draw — opponent's F3 accepts.";
	}

	private void resign() {
		endGame(currentPlayerName() + " resigned. " + otherPlayerName() + " wins.");
	}

	private void endGame(String msg) {
		gameOver = true;
		endMessage = msg;
		status = "";
	}

	private static String[] parseMove(String s) {
		if (s == null) return null;
		s = s.trim();
		if (s.length() < 5) return null;
		String[] tokens = s.split("\\s+");
		if (tokens.length < 2 || tokens.length > 3) return null;
		if (!isAlgebraic(tokens[0]) || !isAlgebraic(tokens[1])) return null;
		String promo = null;
		if (tokens.length == 3) {
			if (tokens[2].length() != 1) return null;
			char c = tokens[2].charAt(0);
			if (c != 'Q' && c != 'R' && c != 'B' && c != 'N') return null;
			if (c != 'Q') promo = String.valueOf(c);
		}
		return new String[] { tokens[0], tokens[1], promo };
	}

	private static boolean isAlgebraic(String sq) {
		if (sq.length() != 2) return false;
		char f = sq.charAt(0), r = sq.charAt(1);
		return f >= 'a' && f <= 'h' && r >= '1' && r <= '8';
	}
}
