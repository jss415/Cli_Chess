package chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bishop extends Piece{

	public Bishop(String symbol, String color) {
		super(symbol, color);
	}

	public boolean isLegalMove(int x1, int x2, int y1, int y2, Piece[][] board) {
		int[] POSSIBLE_MOVES_VECTOR = {-9, -7, 7, 9};

		List<Integer> legalMoves = new ArrayList<>();

		int from = (x1*8)+x2;
		int to = (y1*8)+y2;

		for (int offset: POSSIBLE_MOVES_VECTOR) {
			int coordinate = from;

			if (isOutOfBounds(coordinate, offset)) {
				continue;
			}

			while (coordinate >= 0 && coordinate <= 63) {
				if(isOutOfBounds(coordinate, offset)) {
					break;
				}
				coordinate = coordinate + offset;
				if (coordinate >= 0 && coordinate <= 63) {
					int x = coordinate / 8;
					int y = coordinate - (x * 8);

					if (board[x][y] == null) {
						legalMoves.add(coordinate);
					} else {
						if (!board[x1][x2].getColor().equals(board[x][y].getColor())) {
							legalMoves.add(coordinate);
						}
						break;
					}
				}
			}

		}

		if (!legalMoves.contains(to)) {
			return false;
		}

		return true;
	}

	public boolean isOutOfBounds(int coordinate, int offset) {
		List<Integer> leftSide = Arrays.asList(0, 8, 16, 24, 32, 40, 48, 56);
		List<Integer> rightSide = Arrays.asList(7, 15, 23, 31, 39, 47, 55, 63);

		if (leftSide.contains(coordinate) && (offset == -9 || offset == 7)) {
			return true;
		}

		if (rightSide.contains(coordinate) && (offset == -7 || offset == 9)) {
			return true;
		}

		return false;

	}
}
