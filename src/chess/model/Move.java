package chess.model;

public final class Move {
	public final int fromRow, fromCol, toRow, toCol;
	public final Piece moved;

	public final Piece captured;
	public final int capturedRow, capturedCol;

	public final Pawn promotedFromPawn;

	public final boolean wasCastling;
	public final boolean kingSide;

	public final int priorEpRow, priorEpCol;
	public final boolean priorMoverHasMoved;
	public final boolean priorCastleRookHasMoved;

	public Move(int fromRow, int fromCol, int toRow, int toCol,
	            Piece moved, Piece captured, int capturedRow, int capturedCol,
	            Pawn promotedFromPawn,
	            boolean wasCastling, boolean kingSide,
	            int priorEpRow, int priorEpCol,
	            boolean priorMoverHasMoved, boolean priorCastleRookHasMoved) {
		this.fromRow = fromRow;
		this.fromCol = fromCol;
		this.toRow = toRow;
		this.toCol = toCol;
		this.moved = moved;
		this.captured = captured;
		this.capturedRow = capturedRow;
		this.capturedCol = capturedCol;
		this.promotedFromPawn = promotedFromPawn;
		this.wasCastling = wasCastling;
		this.kingSide = kingSide;
		this.priorEpRow = priorEpRow;
		this.priorEpCol = priorEpCol;
		this.priorMoverHasMoved = priorMoverHasMoved;
		this.priorCastleRookHasMoved = priorCastleRookHasMoved;
	}
}
