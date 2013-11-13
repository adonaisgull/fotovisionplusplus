package vpc.cdas.fotovision;

public class LookUpTable {
	static final int VAR_PIXELS = 256;
	private int lut[];

	LookUpTable() {
		//inicializacion de la tabla de transformaciones
		lut = new int[VAR_PIXELS];
		for (int i = 0; i < VAR_PIXELS; i++) {
			lut[i] = i;
		}
	}
	
	public int get_valor(int posi) {
		return lut[posi];
	}
	
	public void set_valor(int posi, int valor) {
		lut[posi] = valor;
	}
}
