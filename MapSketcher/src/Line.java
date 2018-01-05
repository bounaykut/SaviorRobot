
public class Line {

	int fromX;
	int fromY;
	
	int toX;
	int toY;
	
	public Line(int fromX,int fromY,int toX,int toY) {
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}
	
	/*public boolean equals(Line line) {
		return line.fromX == this.fromX && line.fromY == this.fromY && 
				line.toX == this.toX && line.toY == this.toY;
	}*/
}
