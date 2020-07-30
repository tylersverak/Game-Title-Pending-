
public class vector {
	
	private int xv;
	private int yv;
	
	public vector(int xv, int yv) {
		this.xv = xv;
		this.yv = yv;
	}
	
	public int getVectorXV() {
		return xv;
	}
	
	public int getVectorYV() {
		return yv;
	}
	
	public void setVectorXV(int xv) {
		this.xv = xv;
	}
	
	public void setVectorYV(int yv) {
		this.yv = yv;
	}
	
	public int nextX(int x) {
		return x + xv;
	}
	
	public int nextY(int y) {
		return y + yv;
	}
	
}
