package chess.model;

public class Board {

	static Piece[][] board;

	private static int enPassantRow = -1;
	private static int enPassantCol = -1;

	public Board() {
		board = new Piece[8][8];
		enPassantRow = -1;
		enPassantCol = -1;
		initialize();
		populateBoard();
	}

	public static boolean isEnPassantTarget(int row, int col) {
		return row == enPassantRow && col == enPassantCol;
	}

	public static Piece pieceAt(int row, int col) { return board[row][col]; }

	public void initialize() {
		for (int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				board[i][j] = null;
			}
		}
	}

	public void populateBoard() {

		board[0][0] = new Rook("bR", "B");
		board[0][1] = new Knight("bN", "B");
		board[0][2] = new Bishop("bB", "B");
		board[0][3] = new Queen("bQ", "B");
		board[0][4] = new King("bK", "B");
		board[0][5] = new Bishop("bB", "B");
		board[0][6] = new Knight("bN", "B");
		board[0][7] = new Rook("bR", "B");
		board[1][0] = new Pawn("bp", "B");
		board[1][1] = new Pawn("bp", "B");
		board[1][2] = new Pawn("bp", "B");
		board[1][3] = new Pawn("bp", "B");
		board[1][4] = new Pawn("bp", "B");
		board[1][5] = new Pawn("bp", "B");
		board[1][6] = new Pawn("bp", "B");
		board[1][7] = new Pawn("bp", "B");
		board[7][0] = new Rook("wR", "W");
		board[7][1] = new Knight("wN", "W");
		board[7][2] = new Bishop("wB", "W");
		board[7][3] = new Queen("wQ", "W");
		board[7][4] = new King("wK", "W");
		board[7][5] = new Bishop("wB", "W");
		board[7][6] = new Knight("wN", "W");
		board[7][7] = new Rook("wR", "W");
		board[6][0] = new Pawn("wp", "W");
		board[6][1] = new Pawn("wp", "W");
		board[6][2] = new Pawn("wp", "W");
		board[6][3] = new Pawn("wp", "W");
		board[6][4] = new Pawn("wp", "W");
		board[6][5] = new Pawn("wp", "W");
		board[6][6] = new Pawn("wp", "W");
		board[6][7] = new Pawn("wp", "W");
	}

	public void printBoard() {
		boolean checkered = false;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null && board[i][j].symbol != null) {
					System.out.print(board[i][j].symbol);
				} else {
					if (checkered == true) {
						System.out.print("##");
					} else {
						System.out.print("  ");
					}
				}
				checkered = !checkered;
				System.out.print(" ");
			}
			System.out.println(8-i);
			checkered = !checkered;
		}

		for (char ch = 'a'; ch <= 'h'; ch++) {
			System.out.print(" " + ch + " ");
		}

		System.out.println();
	}

	public static boolean validateMove(String from, String to, String currentSide) {
		int[] src = getCoordinate(from);
		int[] dst = getCoordinate(to);
		int x1 = src[0], x2 = src[1];
		int y1 = dst[0], y2 = dst[1];

		Piece mover = board[x1][x2];
		if (mover == null) return false;
		if (!mover.getColor().equals(currentSide)) return false;
		if (!mover.isLegalMove(x1, x2, y1, y2, board)) return false;
		if (wouldLeaveKingInCheck(x1, x2, y1, y2, currentSide)) return false;

		return true;
	}

	private static boolean wouldLeaveKingInCheck(int x1, int x2, int y1, int y2, String color) {
		Move m = move(x1, x2, y1, y2);
		boolean inCheck = isInCheck(color);
		unmake(m);
		return inCheck;
	}

	public static Move move(String from, String to) {
		int[] src = getCoordinate(from);
		int[] dst = getCoordinate(to);
		return move(src[0], src[1], dst[0], dst[1]);
	}

	public static Move move(int x1, int x2, int y1, int y2) {
		Piece mover = board[x1][x2];

		int priorEpRow = enPassantRow;
		int priorEpCol = enPassantCol;
		boolean priorMoverHasMoved = getHasMoved(mover);

		enPassantRow = -1;
		enPassantCol = -1;

		Piece captured = board[y1][y2];
		int capturedRow = y1, capturedCol = y2;
		if (mover instanceof Pawn && x2 != y2 && captured == null
				&& y1 == priorEpRow && y2 == priorEpCol) {
			capturedRow = mover.getColor().equals("W") ? y1 + 1 : y1 - 1;
			capturedCol = y2;
			captured = board[capturedRow][capturedCol];
			board[capturedRow][capturedCol] = null;
		}

		boolean wasCastling = false;
		boolean kingSide = false;
		boolean priorRookHasMoved = false;
		if (mover instanceof King && Math.abs(y2 - x2) == 2) {
			wasCastling = true;
			kingSide = y2 > x2;
			int rookFromCol = kingSide ? 7 : 0;
			int rookToCol   = kingSide ? 5 : 3;
			Piece rook = board[x1][rookFromCol];
			if (rook instanceof Rook) priorRookHasMoved = ((Rook) rook).hasMoved;
			board[x1][rookToCol]   = rook;
			board[x1][rookFromCol] = null;
			if (rook instanceof Rook) ((Rook) rook).hasMoved = true;
		}

		if (mover instanceof Pawn && Math.abs(y1 - x1) == 2) {
			enPassantRow = (x1 + y1) / 2;
			enPassantCol = x2;
		}

		setHasMoved(mover, true);

		board[y1][y2] = mover;
		board[x1][x2] = null;

		Pawn promotedFromPawn = null;
		if (mover instanceof Pawn) {
			int lastRank = mover.getColor().equals("W") ? 0 : 7;
			if (y1 == lastRank) {
				promotedFromPawn = (Pawn) mover;
				String color = mover.getColor();
				String prefix = color.equals("W") ? "w" : "b";
				String choice = Pawn.getPromo();
				if (choice == null) choice = "Q";
				Piece replacement;
				switch (choice) {
					case "R": replacement = new Rook  (prefix + "R", color); break;
					case "N": replacement = new Knight(prefix + "N", color); break;
					case "B": replacement = new Bishop(prefix + "B", color); break;
					default:  replacement = new Queen (prefix + "Q", color); break;
				}
				board[y1][y2] = replacement;
			}
		}

		return new Move(x1, x2, y1, y2,
		                mover, captured, capturedRow, capturedCol,
		                promotedFromPawn,
		                wasCastling, kingSide,
		                priorEpRow, priorEpCol,
		                priorMoverHasMoved, priorRookHasMoved);
	}

	public static void unmake(Move m) {

		enPassantRow = m.priorEpRow;
		enPassantCol = m.priorEpCol;

		Piece restoredMover = (m.promotedFromPawn != null) ? m.promotedFromPawn : m.moved;
		board[m.fromRow][m.fromCol] = restoredMover;

		board[m.toRow][m.toCol] = null;

		if (m.captured != null) {
			board[m.capturedRow][m.capturedCol] = m.captured;
		}

		if (m.wasCastling) {
			int rookFromCol = m.kingSide ? 7 : 0;
			int rookToCol   = m.kingSide ? 5 : 3;
			Piece rook = board[m.fromRow][rookToCol];
			board[m.fromRow][rookFromCol] = rook;
			board[m.fromRow][rookToCol]   = null;
			if (rook instanceof Rook) ((Rook) rook).hasMoved = m.priorCastleRookHasMoved;
		}

		setHasMoved(restoredMover, m.priorMoverHasMoved);
	}

	private static boolean getHasMoved(Piece p) {
		if (p instanceof Pawn) return ((Pawn) p).hasMoved;
		if (p instanceof Rook) return ((Rook) p).hasMoved;
		if (p instanceof King) return ((King) p).hasMoved;
		return false;
	}

	private static void setHasMoved(Piece p, boolean v) {
		if      (p instanceof Pawn) ((Pawn) p).hasMoved = v;
		else if (p instanceof Rook) ((Rook) p).hasMoved = v;
		else if (p instanceof King) ((King) p).hasMoved = v;
	}

	public static boolean isSquareAttacked(int row, int col, String byColor) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Piece p = board[x][y];
				if (p == null || !p.getColor().equals(byColor)) continue;
				if (p instanceof King) {
					int dx = Math.abs(x - row), dy = Math.abs(y - col);
					if (dx <= 1 && dy <= 1 && (dx != 0 || dy != 0)) return true;
				} else if (p instanceof Pawn) {
					int forward = byColor.equals("W") ? -1 : 1;
					if (x + forward == row && (y - 1 == col || y + 1 == col)) return true;
				} else {
					if (p.isLegalMove(x, y, row, col, board)) return true;
				}
			}
		}
		return false;
	}

	public static boolean isInCheck(String color){
        int[] kingPos = getKingPos(color);
        int row = kingPos[0];
        int col = kingPos[1];

        for(int x = 0; x<board.length; x++){
            for(int y = 0; y<board[0].length; y++){
                if(board[x][y] != null){
                    Piece p = board[x][y];
                	if (!King.class.isInstance(p)) {
                		if (!board[x][y].getColor().equals(color)) {
                    		if(board[x][y].isLegalMove(x, y, row, col, board)) {
                                return true;
                            }
                		}

                	}

                }
            }
        }

        return false;
    }

	private static int[] getKingPos(String color){
        int row = 0, col = 0;

        for(int x = 0; x<board.length; x++){
            for(int y = 0; y<board[0].length; y++){
                if(board[x][y] != null){
                	Piece p = board[x][y];
                	if(King.class.isInstance(p) && board[x][y].getColor().equals(color)){
                		row = x;
                        col = y;
                        break;
                    }
                }
            }
        }
        int[] returnArray = new int[2];
        returnArray[0] = row;
        returnArray[1] = col;

        return returnArray;

    }

	public static boolean isInsufficientMaterial() {
		int whiteMinors = 0, blackMinors = 0;
		int whiteBishopParity = -1, blackBishopParity = -1;

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Piece p = board[x][y];
				if (p == null || p instanceof King) continue;
				if (p instanceof Pawn || p instanceof Rook || p instanceof Queen) return false;

				boolean white = p.getColor().equals("W");
				if (white) whiteMinors++; else blackMinors++;
				if (p instanceof Bishop) {
					int parity = (x + y) & 1;
					if (white)  whiteBishopParity = (whiteBishopParity == -1) ? parity : (whiteBishopParity == parity ? parity : 2);
					else        blackBishopParity = (blackBishopParity == -1) ? parity : (blackBishopParity == parity ? parity : 2);
				}

				if (whiteMinors > 1 || blackMinors > 1) return false;
			}
		}

		if (whiteMinors + blackMinors <= 1) return true;

		if (whiteMinors == 1 && blackMinors == 1
				&& whiteBishopParity >= 0 && blackBishopParity >= 0
				&& whiteBishopParity == blackBishopParity) return true;

		return false;
	}

	public static boolean checkmate(String color) {
		return isInCheck(color) && !hasAnyLegalMove(color);
	}

	public static boolean stalemate(String color) {
		return !isInCheck(color) && !hasAnyLegalMove(color);
	}

	private static boolean hasAnyLegalMove(String color) {
		for (int x1 = 0; x1 < 8; x1++) {
			for (int x2 = 0; x2 < 8; x2++) {
				Piece p = board[x1][x2];
				if (p == null || !p.getColor().equals(color)) continue;
				for (int y1 = 0; y1 < 8; y1++) {
					for (int y2 = 0; y2 < 8; y2++) {
						if (x1 == y1 && x2 == y2) continue;
						if (!p.isLegalMove(x1, x2, y1, y2, board)) continue;
						if (!wouldLeaveKingInCheck(x1, x2, y1, y2, color)) return true;
					}
				}
			}
		}
		return false;
	}

	public static int[] getCoordinate(String pos) {
		char p1, p2;
		p1 = pos.charAt(0);
		p2 = pos.charAt(1);

		int[] coordinate = new int[2];

		switch(p1) {
		case 'a':
			coordinate[1] = 0;
			break;
		case 'b':
			coordinate[1] = 1;
			break;
		case 'c':
			coordinate[1] = 2;
			break;
		case 'd':
			coordinate[1] = 3;
			break;
		case 'e':
			coordinate[1] = 4;
			break;
		case 'f':
			coordinate[1] = 5;
			break;
		case 'g':
			coordinate[1] = 6;
			break;
		case 'h':
			coordinate[1] = 7;
			break;
		default:
			break;
		}

		coordinate[0] = 8 - (p2 - '0');

		return coordinate;
	}

}
