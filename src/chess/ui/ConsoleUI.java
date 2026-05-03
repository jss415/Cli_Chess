package chess.ui;

import java.util.NoSuchElementException;
import java.util.Scanner;

import chess.model.Board;
import chess.model.Pawn;

public class ConsoleUI {

	private final Board board = new Board();
	private final Scanner playerIn = new Scanner(System.in);
	private boolean drawReq = false;

	public void play() {
		boolean whiteTurn = true;
		while (true) {
			board.printBoard();
			String side = whiteTurn ? "W" : "B";
			TurnOutcome result = takeTurn(side);
			switch (result) {
				case CONTINUE:
					whiteTurn = !whiteTurn;
					break;
				case RESIGNED:
					System.out.println(side.equals("W") ? "Black wins" : "White wins");
					return;
				case DRAW_AGREED:
					System.out.println("draw");
					return;
				case CHECKMATE:
					System.out.println("Checkmate");
					System.out.println();
					System.out.println(side.equals("W") ? "White wins" : "Black wins");
					return;
				case STALEMATE:
					System.out.println("Stalemate");
					System.out.println();
					System.out.println("Draw");
					return;
				case INSUFFICIENT_MATERIAL:
					System.out.println("Draw by insufficient material");
					return;
				case EOF:

					return;
			}
		}
	}

	private enum TurnOutcome { CONTINUE, RESIGNED, DRAW_AGREED, CHECKMATE, STALEMATE, INSUFFICIENT_MATERIAL, EOF }

	private TurnOutcome takeTurn(String side) {
		String prompt = side.equals("W") ? "White's Move: " : "Black's Move: ";

		while (true) {
			System.out.println();
			System.out.print(prompt);

			String input = readLine();
			if (input == null) return TurnOutcome.EOF;
			System.out.println();

			if (input.equals("resign")) return TurnOutcome.RESIGNED;

			if (drawReq && input.equals("draw")) return TurnOutcome.DRAW_AGREED;
			boolean proposingDraw = input.contains("draw?");

			if (drawReq && !proposingDraw) drawReq = false;

			String[] parsed = parseMoveInput(input);
			if (parsed == null) {
				System.out.println("Illegal move, try again.");
				continue;
			}
			String from = parsed[0];
			String to   = parsed[1];
			Pawn.setPromo(parsed[2]);

			if (!Board.validateMove(from, to, side)) {
				System.out.println("Illegal move, try again.");
				continue;
			}

			if (proposingDraw) drawReq = true;

			Board.move(from, to);
			String other = side.equals("W") ? "B" : "W";

			if (Board.checkmate(other)) return TurnOutcome.CHECKMATE;
			if (Board.stalemate(other)) return TurnOutcome.STALEMATE;
			if (Board.isInsufficientMaterial()) return TurnOutcome.INSUFFICIENT_MATERIAL;

			if (Board.isInCheck(other)) {
				System.out.println("Check!");
			}
			return TurnOutcome.CONTINUE;
		}
	}

	private String readLine() {
		try {
			if (!playerIn.hasNextLine()) return null;
			return playerIn.nextLine();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	static String[] parseMoveInput(String raw) {
		if (raw == null) return null;
		String s = raw.trim();
		if (s.length() < 5) return null;

		String[] tokens = s.split("\\s+");
		if (tokens.length < 2 || tokens.length > 3) return null;

		String from = tokens[0];
		String to   = tokens[1];
		if (!isAlgebraicSquare(from) || !isAlgebraicSquare(to)) return null;

		String promo = null;
		if (tokens.length == 3) {
			String t = tokens[2];
			if (t.length() != 1) return null;
			char c = t.charAt(0);
			if (c != 'Q' && c != 'R' && c != 'B' && c != 'N') return null;
			promo = (c == 'Q') ? null : String.valueOf(c);
		}
		return new String[] { from, to, promo };
	}

	private static boolean isAlgebraicSquare(String sq) {
		if (sq.length() != 2) return false;
		char file = sq.charAt(0);
		char rank = sq.charAt(1);
		return file >= 'a' && file <= 'h' && rank >= '1' && rank <= '8';
	}
}
