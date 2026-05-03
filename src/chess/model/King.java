package chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class King extends Piece{
	public boolean hasMoved;

	public King(String symbol, String color) {
		super(symbol, color);
		hasMoved = false;
	}

	public boolean isLegalMove(int x1, int x2, int y1, int y2, Piece[][] board) {

		int[] POSSIBLE_MOVES_VECTOR = {-9, -8, -7, -1, 1, 7, 8, 9};

		List<Integer> legalMoves = new ArrayList<>();
		List<Integer> possibleMoves = new ArrayList<>();

		int from = (x1*8)+x2;
		int to = (y1*8)+y2;

		boolean isKingSide = false;
		boolean isQueenSide = false;
		boolean castling = false;

		for (int offset: POSSIBLE_MOVES_VECTOR) {
			int coordinate = from + offset;

			if (incorrectOffset(from, offset)) {
				continue;
			}

			if(coordinate >= 0 && coordinate <= 63) {
				int x = coordinate / 8;
				int y = coordinate - (x * 8);

				if (board[x][y] == null) {
					possibleMoves.add(coordinate);
				} else {
					if (!board[x1][x2].getColor().equals(board[x][y].getColor())) {
						possibleMoves.add(coordinate);
					}
				}
			}
		}

		King king = (King) board[x1][x2];

		if (king.getColor().equals("W")) {
			if (to == 58)      { isQueenSide = true;  castling = true; }
			else if (to == 62) { isKingSide  = true;  castling = true; }
		} else {
			if (to == 2)       { isQueenSide = true;  castling = true; }
			else if (to == 6)  { isKingSide  = true;  castling = true; }
		}

		if (!this.hasMoved && castling) {
			int homeRow = king.getColor().equals("W") ? 7 : 0;
			String opp   = king.getColor().equals("W") ? "B" : "W";
			boolean kingSquareSafe = !Board.isSquareAttacked(homeRow, 4, opp);

			if (kingSquareSafe && isKingSide) {
				Piece r = board[homeRow][7];
				if (r instanceof Rook && !((Rook) r).hasMoved
						&& board[homeRow][5] == null && board[homeRow][6] == null
						&& !Board.isSquareAttacked(homeRow, 5, opp)
						&& !Board.isSquareAttacked(homeRow, 6, opp)) {
					possibleMoves.add(homeRow * 8 + 6);
				}
			}
			if (kingSquareSafe && isQueenSide) {
				Piece r = board[homeRow][0];
				if (r instanceof Rook && !((Rook) r).hasMoved
						&& board[homeRow][1] == null && board[homeRow][2] == null && board[homeRow][3] == null
						&& !Board.isSquareAttacked(homeRow, 3, opp)
						&& !Board.isSquareAttacked(homeRow, 2, opp)) {
					possibleMoves.add(homeRow * 8 + 2);
				}
			}
		}

		for (int i = 0; i < possibleMoves.size(); i++) {
			int coordinate = possibleMoves.get(i);
			int x = coordinate / 8;
			int y = coordinate - (x * 8);
			boolean isObstructing = false;
			for (int j = 0; j < 8; j++) {
				for (int k = 0; k < 8; k++) {
					if (board[j][k] != null) {
						if (!board[j][k].getColor().equals(board[x1][x2].getColor())){
							Piece piece = board[j][k];
							if (!King.class.isInstance(piece)) {
								if (board[j][k].isLegalMove(j, k, x, y, board)) {
									isObstructing = true;
									break;
								}
							}
						}
					}
				}
				if(isObstructing == true) {
					break;
				}
			}

			if(isObstructing == false) {
				legalMoves.add(coordinate);
			}
		}

		return legalMoves.contains(to);
	}

	public boolean incorrectOffset (int coordinate, int offset) {
		List<Integer> firstColumn = Arrays.asList(0, 8, 16, 24, 32, 40, 48, 56);
		List<Integer> eighthColumn = Arrays.asList(7, 15, 23, 31, 39, 47, 55, 63);

		if (firstColumn.contains(coordinate) && (offset == -9 || offset == 1 || offset == 6 || offset == 7)) {
			return true;
		}

		if (eighthColumn.contains(coordinate) && (offset == -7 || offset == 1 || offset == 10 || offset == 9)) {
			return true;
		}

		return false;
	}
}
