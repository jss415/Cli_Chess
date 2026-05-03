package chess.model;

public abstract class Piece {

	public String symbol;
	public String color;

	public Piece(String symbol, String color) {
		this.symbol = symbol;
		this.color = color;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public abstract boolean isLegalMove(int x1, int x2, int y1, int y2, Piece[][] board);

}
