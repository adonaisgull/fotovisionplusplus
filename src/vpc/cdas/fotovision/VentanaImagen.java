package vpc.cdas.fotovision;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VentanaImagen extends JDialog {

	private static final long serialVersionUID = 1L;
	static final int MARGEN = 13;
	static final int DESFASE = 22;
	static final int ESPACIO_VENTANA = 40;
	
	private int id;
	private BufferedImage imagen;
	private Punto origenSeleccion;
	private Punto finalSeleccion;
	private Punto actual;
	private boolean subSeleccion;
	private boolean primerPunto;
	private int puntos;
	
	public VentanaImagen(final VentanaPrincipal padre, final int id, BufferedImage imagen) {
		super(padre, "Imagen " + (id + 1));
		
		setPuntos(0);
		setSubSeleccion(false);
		setId(id);
		setImagen(imagen);
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				//padre.setVentanaActual(-1);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				padre.setVentanaActual(getId());
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
				caputaraMovimiento(crearPunto(arg0.getX(), arg0.getY()));			
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
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
				
				capturaClick(crearPunto(arg0.getX(), arg0.getY()));
			}
		});
		
		JPanel contenedor = new JPanel();
		JLabel capaImagen = new JLabel(new ImageIcon(imagen));
		contenedor.add(capaImagen);
		
		getContentPane().add(contenedor);
		pack();
		setVisible(true);
		
		setLayout(null);
		setSize(imagen.getWidth() + MARGEN * 2, imagen.getHeight() + DESFASE + MARGEN * 2);
		setLocation((id + 1) * ESPACIO_VENTANA, (id + 1) * ESPACIO_VENTANA);
		
	}
	
	
	private Punto crearPunto(int x, int y) {
		
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
		
		return new Punto(x, y, xPixel, yPixel);
	}
	
	private void caputaraMovimiento(Punto punto) {
		
		setActual(punto);
		if (!isSubSeleccion())
			repaint();
	}
	
	private void capturaClick(Punto punto) {
		
		switch (getPuntos()) {
		
		case 0:
			setOrigenSeleccion(punto);
			setPuntos(getPuntos() + 1);
			setSubSeleccion(false);
			setPrimerPunto(true);
			break;
		case 1:
			setFinalSeleccion(punto);
			setSubSeleccion(true);
			setPrimerPunto(false);
			setPuntos(0);
			break;
		}
		
		repaint();
	}
	
	private void pintarSeleccion(Graphics g, Punto a, Punto b) {
		
		int x1 = a.getX();
		int y1 = a.getY();
		int x2 = b.getX();
		int y2 = b.getY();
		int aux;
		
		g.setColor(Color.red);
		
		int difX = Math.abs(x1 - x2);
		int difY = Math.abs(y1 - y2);
		
		
		if (x1 > x2) {
			x1 -= difX;
		}
		
		if (y1 > y2) {
			y1 -= difY;
		}
		
		g.drawRect(x1, y1, difX, difY);
	}
	
	private void pintarMarca(Graphics g, Punto a) {
		
		int x = a.getX();
		int y = a.getY();
		
		pintarLineas(g, a);
		g.setColor(Color.green);
		g.drawLine(x, y + 10, x, y - 10);
		g.drawLine(x - 10, y, x + 10, y);
	}
	
	private void pintarLineas(Graphics g, Punto a) {
		
		int x = a.getX();
		int y = a.getY();
		
		g.setColor(Color.red);
		
		g.drawLine(x, MARGEN + DESFASE, x, getImagen().getHeight() + MARGEN + DESFASE);
		g.drawLine(MARGEN, y, getImagen().getWidth() + MARGEN, y);
	}
	
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		//g.drawImage(getImagen(), MARGEN, MARGEN + DESFASE, this);
		
		if (isPrimerPunto()) {
			pintarMarca(g, getOrigenSeleccion());
		}
		
		if (isSubSeleccion()) {	
			pintarSeleccion(g, getOrigenSeleccion(), getFinalSeleccion());
		}
		
		if (!isSubSeleccion()) {
			if (getActual() != null)
				pintarLineas(g, getActual());
		}
		
	}
	
	public Punto getActual() {
		return actual;
	}

	public void setActual(Punto actual) {
		this.actual = actual;
	}

	public boolean isPrimerPunto() {
		return primerPunto;
	}

	public void setPrimerPunto(boolean primerPunto) {
		this.primerPunto = primerPunto;
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
