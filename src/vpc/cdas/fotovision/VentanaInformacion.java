package vpc.cdas.fotovision;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JLabel;

public class VentanaInformacion extends VentanaSecundaria {

	private static final long serialVersionUID = 1L;

	public VentanaInformacion(VentanaImagen padre) {
		super(padre, "Imagen " + (padre.getId() + 1) + " - Información");
		
		BufferedImage imagen = padre.getImagen();

		GridLayout tabla = new GridLayout(6, 2, 10, 5);
		tabla.setHgap(10);
		setLayout(tabla);
		
		double brillo = Operaciones.getBrillo(imagen);
		double contraste = Operaciones.getContraste(imagen);
		double entropia = Math.abs(Operaciones.getEntropia(imagen));
		ArrayList<Integer> rangoValores = Operaciones.rangoValores(imagen);
		int inferior = rangoValores.get(0);
		int superior = rangoValores.get(1);
		
		add(new JLabel("Tipo de fichero"));
		add(new JLabel("BMP"));
		add(new JLabel("Tamaño"));
		add(new JLabel(imagen.getWidth() + " x " + imagen.getHeight()));
		add(new JLabel("Rango de Valores"));
		add(new JLabel("[" + inferior + ", " + superior + "]"));
		add(new JLabel("Brillo"));
		add(new JLabel(String.format("%.4g%n", brillo)));
		add(new JLabel("Constraste"));
		add(new JLabel(String.format("%.4g%n", contraste)));
		add(new JLabel("Entropía"));
		add(new JLabel(String.format("%.3g%n", entropia)));
		
		pack();
		setResizable(false);
	}
}
