package ticketing;

public class Seat {
	int row;
	int column;
	boolean isAccessible;

	public Seat(int row, int col, boolean isAccessible){
		this.row=row;
		this.column=col;
		this.isAccessible=isAccessible;
	}
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean isAccessible() {
		return isAccessible;
	}

	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
}
