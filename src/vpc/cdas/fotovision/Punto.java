package vpc.cdas.fotovision;

public class Punto {
	
	private int x;
	private int y;
	
	private int xPixel;
	private int yPixel;

	
	public Punto(int x, int y, int xPixel, int yPixel) {
		
		setX(x);
		setY(y);
		setxPixel(xPixel);
		setyPixel(yPixel);
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int getxPixel() {
		return xPixel;
	}


	public void setxPixel(int xPixel) {
		this.xPixel = xPixel;
	}


	public int getyPixel() {
		return yPixel;
	}


	public void setyPixel(int yPixel) {
		this.yPixel = yPixel;
	}
	
	public String toString() {
		
		String string = new String("Pixel: (" + getxPixel() + ", " + getyPixel() + ");");
		
		
		
		return string;
	}
}
