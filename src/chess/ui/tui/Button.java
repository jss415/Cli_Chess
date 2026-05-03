package chess.ui.tui;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public final class Button {
	private Button() {}

	public static void draw(TextGraphics tg, int col, int row, String label) {
		tg.setBackgroundColor(Theme.BUTTON_BG);
		tg.setForegroundColor(Theme.BUTTON_FG);
		tg.enableModifiers(SGR.BOLD);
		tg.putString(col, row, label);
		tg.disableModifiers(SGR.BOLD);
		tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
	}

	public static int width(String label) { return label.length(); }
}
