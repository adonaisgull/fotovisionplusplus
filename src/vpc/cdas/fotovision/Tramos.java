package vpc.cdas.fotovision;

public class Tramos {
	private int x[];
	private int y[];
	private int num_tramos;
	
	Tramos(int num_tramos) {
		this.num_tramos = num_tramos + 1;
		x = new int[num_tramos];
		y = new int[num_tramos];
		x[0] = 0;
		y[0] = 0;
	}
	
	public int get_num_tramos() {
		return num_tramos;
	}
	
	public void set_tramo(int tramo, int x, int y) {
		this.x[tramo] = x;
		this.y[tramo] = y;
	}
	
	public int[] get_tramo(int tramo) {
		int array_tramo[] = new int[2];
		array_tramo[0] = x[tramo];
		array_tramo[1] = y[tramo];
		return array_tramo;
	}
}
