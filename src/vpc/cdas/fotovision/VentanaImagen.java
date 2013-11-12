package vpc.cdas.fotovision;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;

public class VentanaImagen extends JDialog {

	private static final long serialVersionUID = 1L;
	static final int MARGEN = 13;
	static final int DESFASE = 22;
	static final int ESPACIO_VENTANA = 40;
	
	private int id;
	private BufferedImage imagen;
	private Punto origenSeleccion;
	private Punto finalSeleccion;
	
	private boolean subSeleccion;
	private int puntos;
	
	public VentanaImagen(final VentanaPrincipal padre, final int id, BufferedImage imagen) {
		super(padre, "Imagen " + (id + 1));
		
		setPuntos(0);
		setSubSeleccion(false);
		setId(id);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	
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
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				capturaClick(arg0.getX(), arg0.getY());
			}
		});
		
		//JLabel contenedorImagen = new JLabel();
		//contenedorImagen.setIcon(new ImageIcon(imagen));
		setLayout(null);
		setSize(imagen.getWidth() + MARGEN * 2, imagen.getHeight() + DESFASE + MARGEN * 2);
		//setResizable(false);
		setLocation((id + 1) * ESPACIO_VENTANA, (id + 1) * ESPACIO_VENTANA);
		
		setImagen(imagen);
		
		//add(contenedorImagen, BorderLayout.CENTER);
		//pack();
	}
	
	private void capturaClick(int x, int y) {
		
		int xPixel = x - MARGEN;
		int yPixel = y - MARGEN - DESFASE;
		
		if (xPixel < 0)
			xPixel = 0;
		if (xPixel > getImagen().getWidth())
			xPixel = getImagen().getWidth();
		if (yPixel < 0)
			yPixel = 0;
		if (yPixel > getImagen().getHeight())
			yPixel = getImagen().getHeight();
		
		Punto punto = new Punto(x, y, xPixel, yPixel);
		
		switch (getPuntos()) {
		
		case 0:
			setOrigenSeleccion(punto);
			setPuntos(getPuntos() + 1);
			setSubSeleccion(false);
			break;
		case 1:
			setFinalSeleccion(punto);
			setSubSeleccion(true);
			setPuntos(0);
			break;
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(getImagen(), MARGEN, MARGEN + DESFASE, this);
		
		if (isSubSeleccion()) {	
			int x1 = getOrigenSeleccion().getX();
			int y1 = getOrigenSeleccion().getY();
			int x2 = getFinalSeleccion().getX();
			int y2 = getFinalSeleccion().getY();
			
			g.setColor(Color.red);
			g.drawRect(x1, y1, x2 - x1, y2 - y1);
		}
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BufferedImage getImagen() {
		return imagen;
	}

	public void setImagen(BufferedImage imagen) {
		this.imagen = imagen;
	}

	public Punto getOrigenSeleccion() {
		return origenSeleccion;
	}

	public void setOrigenSeleccion(Punto origenSeleccion) {
		this.origenSeleccion = origenSeleccion;
	}

	public Punto getFinalSeleccion() {
		return finalSeleccion;
	}

	public void setFinalSeleccion(Punto finalSeleccion) {
		this.finalSeleccion = finalSeleccion;
	}

	public boolean isSubSeleccion() {
		return subSeleccion;
	}

	public void setSubSeleccion(boolean subSeleccion) {
		this.subSeleccion = subSeleccion;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
	
}
