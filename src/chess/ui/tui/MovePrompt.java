package chess.ui.tui;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public final class MovePrompt {
	private MovePrompt() {}

	private static final String LABEL  = "Move:";
	private static final int    BUF_COL = LABEL.length() + 1;

	public static void drawPlaying(Screen screen, String buffer, int row) {
		TextGraphics tg = screen.newTextGraphics();
		Button.draw(tg, 0, row, LABEL);

		tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
		tg.setForegroundColor(Theme.PROMPT_FG);
		tg.putString(BUF_COL, row, buffer);

		tg.setBackgroundColor(Theme.PROMPT_FG);
		tg.putString(BUF_COL + buffer.length(), row, " ");
		tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
	}

	public static void drawEnded(Screen screen, String endMessage, int row) {
		TextGraphics tg = screen.newTextGraphics();
		tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
		tg.setForegroundColor(new TextColor.RGB(255, 255, 255));
		tg.enableModifiers(SGR.BOLD);
		tg.putString(0, row, endMessage);
		tg.disableModifiers(SGR.BOLD);

		tg.setForegroundColor(new TextColor.RGB(160, 160, 160));
		tg.putString(0, row + 1, "Press any key to exit.");
	}
}
