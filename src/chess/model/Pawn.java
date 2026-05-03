package chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pawn extends Piece {

	public boolean hasMoved;
	private static String promo = null;

	public static void setPromo(String newpromo) {
		promo = newpromo;
	}

	public static String getPromo() {
		return promo;
	}

	List<Integer> firstColumn = Arrays.asList(0, 8, 16, 24, 32, 40, 48, 56);
	List<Integer> eighthColumn = Arrays.asList(7, 15, 23, 31, 39, 47, 55, 63);

	List<Integer> firstRow = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
	List<Integer> eighthRow = Arrays.asList(56, 57, 58, 59, 60, 61, 62, 63);

	public Pawn(String symbol, String color) {
		super(symbol, color);
		this.hasMoved = false;
	}

	public boolean isLegalMove(int x1, int x2, int y1, int y2, Piece[][] board) {

		int multiplier = board[x1][x2].getColor().equals("W") ? -1 : 1;

		List<Integer> legalMoves = new ArrayList<>();
		List<Integer> attackMoves = new ArrayList<>();
		List<Integer> oneSquareMoves = new ArrayList<>();
		List<Integer> twoSquareMoves = new ArrayList<>();

		int from = (x1*8)+x2;
		int to = (y1*8)+y2;

		int[] POSSIBLE_MOVES_VECTOR = {16, 8,  7, 9};

		for (int offset: POSSIBLE_MOVES_VECTOR) {
			int coordinate1 = from + (offset * multiplier);

			int x = coordinate1 / 8 ;
			int y = coordinate1 - (x * 8);

			if (offset == 8 && board[x][y] == null) {
				oneSquareMoves.add(coordinate1);
			} else if (offset == 16 && board[x][y] == null && this.hasMoved == false) {
				int coordinate2 = from + (8 * multiplier);

				int i = coordinate2 / 8;
				int j = coordinate2 - (i * 8);

				if (board[i][j] == null) {
					twoSquareMoves.add(coordinate1);
				}
			} else if (offset == 7 && !( (eighthColumn.contains(from) && board[x1][x2].getColor().equals("W")) || (firstColumn.contains(from) && board[x1][x2].getColor().equals("B")) )) {
				if (isPawnCaptureSquare(board, x, y, board[x1][x2].getColor())) {
					attackMoves.add(coordinate1);
				}
			} else if (offset == 9 && !( (firstColumn.contains(from) && board[x1][x2].getColor().equals("W") ) || (eighthColumn.contains(from) && board[x1][x2].getColor().equals("B") ) ) ) {
				if (isPawnCaptureSquare(board, x, y, board[x1][x2].getColor())) {
					attackMoves.add(coordinate1);
				}
			}
		}

		legalMoves.addAll(attackMoves);
		legalMoves.addAll(oneSquareMoves);
		legalMoves.addAll(twoSquareMoves);

		return legalMoves.contains(to);
	}

	private static boolean isPawnCaptureSquare(Piece[][] board, int row, int col, String moverColor) {
		Piece target = board[row][col];
		if (target != null) {
			return !moverColor.equals(target.getColor());
		}
		return Board.isEnPassantTarget(row, col);
	}
}
