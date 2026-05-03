package chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Queen extends Piece {
	public char symbol = 'Q';

	public Queen(String symbol, String color) {
		super(symbol, color);
	}

	public boolean isLegalMove(int x1, int x2, int y1, int y2, Piece[][] board) {

		List<Integer> legalMoves = new ArrayList<>();

		int from = (x1*8)+x2;
		int to = (y1*8)+y2;

		int[] POSSIBLE_MOVES_VECTOR = {-9, -7, 7, 9};

		from = (x1*8)+x2;
		to = (y1*8)+y2;

		for (int offset: POSSIBLE_MOVES_VECTOR) {
			int coordinate = from;

			if (isOutOfBounds1(coordinate, offset)) {
				continue;
			}

			while (coordinate >= 0 && coordinate <= 63) {
				if(isOutOfBounds1(coordinate, offset)) {
					break;
				}
				coordinate = coordinate + offset;
				if (coordinate >= 0 && coordinate <= 63) {
					int x = coordinate / 8;
					int y = coordinate - (x * 8);

					Piece s = board[x][y];
					if (board[x][y] == null) {
						legalMoves.add(coordinate);
					} else {
						Piece p1 = board[x1][x2];
						Piece p2 = board[x][y];
						if (!board[x1][x2].getColor().equals(board[x][y].getColor())) {
							legalMoves.add(coordinate);
						}
						break;
					}
				}
			}
		}

		from = (x1*8)+x2;
		to = (y1*8)+y2;

		int[] POSSIBLE_MOVES_VECTOR1 ={-8, -1, 1, 8};

		for (int offset: POSSIBLE_MOVES_VECTOR1) {
			int coordinate = from;

			if (isOutOfBounds2(coordinate, offset)) {
				continue;
			}

			while (coordinate >= 0 && coordinate <= 63) {
				if (isOutOfBounds2(coordinate, offset)) {
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

		return legalMoves.contains(to);
	}

	public boolean isOutOfBounds1(int coordinate, int offset) {
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

	public boolean isOutOfBounds2(int coordinate, int offset) {
		List<Integer> leftSide = Arrays.asList(0, 8, 16, 24, 32, 40, 48, 56);
		List<Integer> rightSide = Arrays.asList(7, 15, 23, 31, 39, 47, 55, 63);

		if (leftSide.contains(coordinate) && (offset == -1)) {
			return true;
		}

		if (rightSide.contains(coordinate) && (offset == 1)) {
			return true;
		}

		return false;

	}
}
