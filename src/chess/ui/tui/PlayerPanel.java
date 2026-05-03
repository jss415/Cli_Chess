package chess.ui.tui;

import java.util.List;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import chess.model.Piece;

public final class PlayerPanel {
	private PlayerPanel() {}

	public static void draw(Screen screen, String name, List<Piece> captures, boolean active, int col, int row) {
		TextGraphics tg = screen.newTextGraphics();
		tg.setBackgroundColor(TextColor.ANSI.DEFAULT);

		String prefix = active ? "\u25C0 " : "  ";
		tg.setForegroundColor(active ? Theme.ACTIVE_FG : Theme.LABEL_FG);
		if (active) tg.enableModifiers(SGR.BOLD);
		tg.putString(col, row, prefix + name);
		if (active) tg.disableModifiers(SGR.BOLD);

		tg.setForegroundColor(Theme.LABEL_FG);
		StringBuilder sb = new StringBuilder("  ");
		if (captures.isEmpty()) {
			sb.append("(no captures)");
		} else {
			for (Piece p : captures) sb.append(BoardRenderer.glyphFor(p)).append(' ');
		}
		tg.putString(col, row + 1, sb.toString());
	}
}
