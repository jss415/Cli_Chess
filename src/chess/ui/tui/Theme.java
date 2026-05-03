package chess.ui.tui;

import com.googlecode.lanterna.TextColor;

public final class Theme {
	private Theme() {}

	public static final TextColor LIGHT_SQUARE   = new TextColor.RGB(106, 145, 130);
	public static final TextColor DARK_SQUARE    = new TextColor.RGB( 60,  60,  95);
	public static final TextColor HIGHLIGHT      = new TextColor.RGB(165, 200,  80);
	public static final TextColor WHITE_PIECE_FG = new TextColor.RGB(252, 252, 252);
	public static final TextColor BLACK_PIECE_FG = new TextColor.RGB( 25,  25,  25);
	public static final TextColor LABEL_FG       = new TextColor.RGB(180, 180, 180);
	public static final TextColor STATUS_FG      = new TextColor.RGB(255, 200,  80);
	public static final TextColor PROMPT_FG      = new TextColor.RGB(220, 220, 220);
	public static final TextColor BUTTON_BG      = new TextColor.RGB(106, 200, 180);
	public static final TextColor BUTTON_FG      = new TextColor.RGB(  0,   0,   0);
	public static final TextColor ACTIVE_FG      = new TextColor.RGB(255, 140,  66);
}
