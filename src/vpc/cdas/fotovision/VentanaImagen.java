package vpc.cdas.fotovision;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class VentanaImagen extends JDialog {

	private int id;
	
	public VentanaImagen(final VentanaPrincipal padre, final int id, BufferedImage imagen) {
		super(padre, "Imagen " + (id + 1));
		
		setId(id);
		
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				padre.setImagenActual(null);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				padre.setImagenActual(padre.getImagenes().get(id));
			}
		});
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				padre.getImagenes().remove(id);
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		JLabel contenedorImagen = new JLabel();
		contenedorImagen.setIcon(new ImageIcon(imagen));
		setSize(imagen.getWidth(), imagen.getHeight());
		add(contenedorImagen, BorderLayout.CENTER);
		pack();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
