package chess.ui.tui;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public final class StatusLine {
	private StatusLine() {}

	public static void draw(Screen screen, String message, int row) {
		if (message == null || message.isEmpty()) return;
		TextGraphics tg = screen.newTextGraphics();
		tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
		tg.setForegroundColor(Theme.STATUS_FG);
		tg.putString(0, row, message);
	}
}
