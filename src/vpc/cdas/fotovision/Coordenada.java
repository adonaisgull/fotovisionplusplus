package vpc.cdas.fotovision;

public class Coordenada {

	private int x;
	private int y;
	
	Coordenada() {
		setX(0);
		setY(0);
	}
	
	Coordenada(int x, int y) {
		setX(x);
		setY(y);
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
	
	
}
