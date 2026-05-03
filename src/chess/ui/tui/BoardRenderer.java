package chess.ui.tui;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

import chess.model.Board;
import chess.model.Move;
import chess.model.Piece;

public final class BoardRenderer {
	private BoardRenderer() {}

	public static void draw(Screen screen, Move lastMove, boolean flipped) {
		TextGraphics tg = screen.newTextGraphics();
		int glyphOffsetCol = Layout.SQUARE_W / 2;
		int glyphOffsetRow = Layout.SQUARE_H / 2;
		String blank = " ".repeat(Layout.SQUARE_W);

		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {

				int boardRow = flipped ? 7 - rank : rank;
				int boardCol = flipped ? 7 - file : file;

				int sqCol = Layout.BOARD_ORIGIN_COL + file * Layout.SQUARE_W;
				int sqRow = Layout.BOARD_ORIGIN_ROW + rank * Layout.SQUARE_H;

				boolean isLight = ((boardRow + boardCol) & 1) == 0;
				boolean isHl    = isHighlighted(lastMove, boardRow, boardCol);
				TextColor bg    = isHl ? Theme.HIGHLIGHT : (isLight ? Theme.LIGHT_SQUARE : Theme.DARK_SQUARE);

				tg.setBackgroundColor(bg);
				for (int dy = 0; dy < Layout.SQUARE_H; dy++) {
					tg.putString(sqCol, sqRow + dy, blank);
				}

				Piece p = Board.pieceAt(boardRow, boardCol);
				if (p != null) {
					tg.setForegroundColor(p.getColor().equals("W") ? Theme.WHITE_PIECE_FG : Theme.BLACK_PIECE_FG);
					tg.enableModifiers(SGR.BOLD);
					tg.putString(sqCol + glyphOffsetCol, sqRow + glyphOffsetRow, String.valueOf(glyphFor(p)));
					tg.disableModifiers(SGR.BOLD);
				}
			}
		}

		tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
		tg.setForegroundColor(Theme.LABEL_FG);
		for (int rank = 0; rank < 8; rank++) {
			int boardRow = flipped ? 7 - rank : rank;
			int label = 8 - boardRow;
			tg.putString(Layout.BOARD_ORIGIN_COL - 2,
				Layout.BOARD_ORIGIN_ROW + rank * Layout.SQUARE_H + glyphOffsetRow,
				String.valueOf(label));
		}

		for (int file = 0; file < 8; file++) {
			int boardCol = flipped ? 7 - file : file;
			char label = (char) ('a' + boardCol);
			tg.putString(Layout.BOARD_ORIGIN_COL + file * Layout.SQUARE_W + glyphOffsetCol,
				Layout.FILE_LABELS_ROW, String.valueOf(label));
		}
	}

	private static boolean isHighlighted(Move m, int boardRow, int boardCol) {
		if (m == null) return false;
		if (boardRow == m.fromRow && boardCol == m.fromCol) return true;
		if (boardRow == m.toRow   && boardCol == m.toCol)   return true;
		return false;
	}

	public static char glyphFor(Piece p) {
		String s = p.getSymbol();
		if (s == null) return ' ';

		switch (s.charAt(1)) {
			case 'K': return '\u265A';
			case 'Q': return '\u265B';
			case 'R': return '\u265C';
			case 'B': return '\u265D';
			case 'N': return '\u265E';
			case 'p': return '\u265F';
			default:  return '?';
		}
	}
}
