package vpc.cdas.fotovision;

import java.awt.Point;

import javax.swing.JDialog;

public class VentanaSecundaria extends JDialog {
	
	static final long serialVersionUID = 1L;
	
	private VentanaImagen padre;
	
	public VentanaSecundaria(VentanaImagen padre, String titulo) {
		super(padre, titulo);
		setPadre(padre);
		
		Point location = padre.getLocation();
		location.setLocation(location.getX() + padre.getWidth(), location.getY());
		setLocation(location);
	}
	
	public VentanaSecundaria(VentanaImagen padre, String titulo, int ancho, int alto) {
		super(padre, titulo);
		setPadre(padre);
		
		Point location = padre.getLocation();
		location.setLocation(location.getX() + padre.getWidth(), location.getY());
		setLocation(location);
		setSize(ancho, alto);
	}

	public VentanaImagen getPadre() {
		return padre;
	}

	public void setPadre(VentanaImagen padre) {
		this.padre = padre;
	}
}
