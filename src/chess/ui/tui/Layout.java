package chess.ui.tui;

public final class Layout {
	private Layout() {}

	public static final int SQUARE_W = 3;
	public static final int SQUARE_H = 1;

	public static final int BOARD_ORIGIN_COL = 2;
	public static final int BOARD_ORIGIN_ROW = 1;

	public static final int BOARD_W = SQUARE_W * 8;
	public static final int BOARD_H = SQUARE_H * 8;

	public static final int FILE_LABELS_ROW = BOARD_ORIGIN_ROW + BOARD_H;
	public static final int RIGHT_EDGE_COL  = BOARD_ORIGIN_COL + BOARD_W;

	public static final int PANEL_COL    = RIGHT_EDGE_COL + 4;
	public static final int PLAYER1_ROW  = BOARD_ORIGIN_ROW;
	public static final int PLAYER2_ROW  = BOARD_ORIGIN_ROW + BOARD_H - 2;

	public static final int STATUS_ROW = FILE_LABELS_ROW + 1;
	public static final int PROMPT_ROW = STATUS_ROW + 1;
	public static final int CMDBAR_ROW = PROMPT_ROW + 2;
}
