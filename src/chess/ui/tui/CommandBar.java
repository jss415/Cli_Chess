package chess.ui.tui;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public final class CommandBar {
	private CommandBar() {}

	private static final String[] BUTTONS = {
		"F1 Flip board",
		"F2 Takeback",
		"F3 Offer draw",
		"F4 Resign",
		"F8 Exit",
	};
	private static final int GAP = 2;

	public static void draw(Screen screen, int row) {
		TextGraphics tg = screen.newTextGraphics();
		int col = 0;
		for (String label : BUTTONS) {
			Button.draw(tg, col, row, label);
			col += Button.width(label) + GAP;
		}
	}
}
