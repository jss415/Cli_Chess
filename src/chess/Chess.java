package chess;

import chess.ui.ConsoleUI;
import chess.ui.tui.Tui;

public class Chess {
	public static void main(String[] args) throws Exception {
		if (args.length > 0 && args[0].equals("--console")) {
			new ConsoleUI().play();
		} else {
			try {
				new Tui().play();
			} catch (Exception e) {

				System.err.println("[TUI unavailable: " + e.getMessage() + "] Falling back to console UI.");
				new ConsoleUI().play();
			}
		}
	}
}
