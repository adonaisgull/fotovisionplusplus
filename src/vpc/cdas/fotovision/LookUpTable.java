package vpc.cdas.fotovision;

public class LookUpTable {
	private static final int PIXELS = 256;
	private int tabla[];
	private int size;

	LookUpTable() {
		
		setSize(PIXELS);
		tabla = new int[getSize()];
		for (int i = 0; i < getSize(); i++) {
			tabla[i] = i;
		}
	}

	LookUpTable(int valorInicial) {
		
		setSize(PIXELS);
		tabla = new int[getSize()];
		for (int i = 0; i < getSize(); i++) {
			tabla[i] = valorInicial;
		}
	}
	
	public int sumatorio(int k) {
		
		int suma = 0;
		
		if (k > (getSize() - 1))
				k = getSize() - 1;
		if (k < 0 )
			k = 0;
		
		for (int i = 0; i <= k; i++)
			suma += getValor(i);
		
		return suma;
	}
	
	public int getValor(int index) {
		return tabla[index];
	}

	public void setValor(int index, int valor) {
		tabla[index] = valor;
	}

	public void incrementar(int index) {
		tabla[index]++;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}	
}