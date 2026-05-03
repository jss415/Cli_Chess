package chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Knight extends Piece {
	public Knight(String symbol, String color) {
		super(symbol, color);
	}

	public boolean isLegalMove(int x1, int x2, int y1, int y2, Piece[][] board) {
		int[] possibleMoves = {-17, -15, -10, -6, 6, 10, 15, 17};
		List<Integer> legalMoves = new ArrayList<>();

		int from = (x1*8)+x2;
		int to = (y1*8)+y2;

		for (int offset: possibleMoves) {
			if (incorrectOffset(from, offset)) {
				continue;
			}

			int coordinate = from + offset;

			if (coordinate >= 0 && coordinate <= 63) {
				legalMoves.add(coordinate);
			}
		}

		if(!legalMoves.contains(to)) {
			return false;
		} else {
			Piece piece = board[y1][y2];
			if (piece != null) {
				if (board[x1][x2].getColor().equals(piece.getColor())) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean incorrectOffset(int coordinate, int offset) {
		List<Integer> firstColumn = Arrays.asList(0, 8, 16, 24, 32, 40, 48, 56);
		List<Integer> secondColumn = Arrays.asList(1, 9, 17, 25, 33, 41, 49, 57);
		List<Integer> seventhColumn = Arrays.asList(6, 14, 22, 30, 38, 46, 54, 62);
		List<Integer> eighthColumn = Arrays.asList(7, 15, 23, 31, 39, 47, 55, 63);

		if (firstColumn.contains(coordinate) && (offset == -17 || offset == -10 || offset == 6 || offset == 15)) {
			return true;
		}
		if (secondColumn.contains(coordinate) && (offset == -10 || offset == 6)) {
			return true;
		}
		if (seventhColumn.contains(coordinate) && (offset == -6 || offset == 10)) {
			return true;
		}
		if (eighthColumn.contains(coordinate) && (offset == -15 || offset == -6 || offset == 10 || offset == 17)) {
			return true;
		}

		return false;

	}
}
