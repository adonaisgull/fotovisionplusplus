package vpc.cdas.fotovision;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class VentanaImagen extends JDialog {

	private static final long serialVersionUID = 1L;
	static final int MARGEN = 18;
	static final int DESFASE = 22;
	static final int ESPACIO_VENTANA = 30;
	static final int DESFASE_VENTANA = 100;
	
	private int id;
	private BufferedImage imagen;
	private Punto origenSeleccion;
	private Punto finalSeleccion;
	private Punto actual;
	private boolean subSeleccion;
	private boolean primerPunto;
	private int puntos;
	
	private boolean enFoco;
	private boolean mostrarGuias;
	private boolean seleccionActiva;
	
	private JTextField campoX;
	private JTextField campoY;
	private JTextField camboGris;
	
	private VentanaPrincipal padre;
	
	public VentanaImagen(final VentanaPrincipal padre, final int id, BufferedImage imagen) {
		super(padre, "Imagen " + (id + 1));
		
		setPuntos(0);
		setSubSeleccion(false);
		setId(id);
		setImagen(imagen);
		setPadre(padre);
		
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				setEnFoco(false);
				repaint();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				padre.setVentanaActual(getId());
				setEnFoco(true);
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
				
				ArrayList<VentanaImagen> ventanas = getPadre().getVentanasImagen();
				
				setVisible(false);
				boolean cambio = false;
				for (int i = ventanas.size() - 1; i >= 0 ; i--) {
					if (ventanas.get(i).isVisible()) {
						getPadre().setVentanaActual(i);
						ventanas.get(i).toFront();
						cambio = true;
						break;
					}
				}
				
				if (!cambio)
					getPadre().setVentanaActual(-1);
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
			
				caputaraMovimiento(crearPunto(arg0.getX(), arg0.getY()));
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {	}
		});
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) { }
			
			@Override
			public void mousePressed(MouseEvent arg0) { }
			
			@Override
			public void mouseExited(MouseEvent arg0) { }
			
			@Override
			public void mouseEntered(MouseEvent arg0) {	}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (isSeleccionActiva()) {
					capturaClick(crearPunto(arg0.getX(), arg0.getY()));
				}
			}
		});
		
		JPanel contenedor = new JPanel();
		JLabel capaImagen = new JLabel(new ImageIcon(imagen));
		contenedor.add(capaImagen);
		
		JTextField campoX = new JTextField(4);
		campoX.setEnabled(false);
		campoX.setText("0");
		setCampoX(campoX);
		
		JTextField campoY = new JTextField(4);
		campoY.setEnabled(false);
		campoY.setText("0");
		setCampoY(campoY);
		
		JTextField campoGris = new JTextField(3);
		campoGris.setEnabled(false);
		campoGris.setText("0");
		setCamboGris(campoGris);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		getContentPane().add(contenedor);
		getContentPane().add(new JLabel("  X"));
		getContentPane().add(campoX);
		getContentPane().add(new JLabel("Y"));
		getContentPane().add(campoY);
		getContentPane().add(new JLabel("GRIS"));
		getContentPane().add(campoGris);
		pack();
		setVisible(true);
		
		setSize(imagen.getWidth() + MARGEN * 2, imagen.getHeight() + DESFASE + MARGEN * 2 + 30);
	
		ArrayList<VentanaImagen> ventanas = getPadre().getVentanasImagen();
				
		int ventanasActivas = 0;		
		for (int i = 0; i < ventanas.size(); i++) {
			if (ventanas.get(i).isVisible()) {
				ventanasActivas++;
			}
		}
		
		setLocation((ventanasActivas + 1) * ESPACIO_VENTANA, (ventanasActivas + 1) * ESPACIO_VENTANA + DESFASE_VENTANA);
	}
	
	
	private Punto crearPunto(int x, int y) {
		
		int xPixel = x - MARGEN;
		int yPixel = y - MARGEN - DESFASE;
		
		if (xPixel < 0)
			xPixel = 0;
		if (xPixel >= getImagen().getWidth())
			xPixel = getImagen().getWidth() - 1;
		if (yPixel < 0)
			yPixel = 0;
		if (yPixel >= getImagen().getHeight())
			yPixel = getImagen().getHeight() - 1;
		
		return new Punto(x, y, xPixel, yPixel);
	}
	
	private void caputaraMovimiento(Punto punto) {
		
		if (isDentroImagen(punto)) {
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));


			getCampoX().setText(String.valueOf(punto.getxPixel()));
			getCampoY().setText(String.valueOf(punto.getyPixel()));

			Color color = new Color(getImagen().getRGB(punto.getxPixel(), punto.getyPixel()));
			getCamboGris().setText(String.valueOf(color.getRed()));

			if (isMostrarGuias()) {
				setActual(punto);
				if (!isSubSeleccion())
					repaint();
			}

		}
		else {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private void capturaClick(Punto punto) {
		
		if (isDentroImagen(punto)) {
		
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
		}
		
		repaint();
	}
	
	private void pintarSeleccion(Graphics g, Punto a, Punto b) {
		
		int x1 = a.getX();
		int y1 = a.getY();
		int x2 = b.getX();
		int y2 = b.getY();
		int difX = Math.abs(x1 - x2);
		int difY = Math.abs(y1 - y2);
		
		g.setColor(Color.red);

		if (x1 > x2) {
			x1 -= difX;
		}
		
		if (y1 > y2) {
			y1 -= difY;
		}
				
		g.drawRect(x1, y1, difX, difY);
	}
	
	private void pintarLineas(Graphics g, Punto a) {
		
		int x = a.getX();
		int y = a.getY();
		
		if (isDentroImagen(a)){
			g.setColor(Color.red);
			g.drawLine(x, MARGEN + DESFASE, x, getImagen().getHeight() - 1 + MARGEN + DESFASE);
			g.drawLine(MARGEN, y, getImagen().getWidth() - 1 + MARGEN, y);
		}
	}
	
	private boolean isDentroImagen(Punto punto) {
		
		int x = punto.getX() - MARGEN;
		int y = punto.getY() - MARGEN - DESFASE;
		
		if (x >= 0 && x < getImagen().getWidth() && y >= 0 && y < getImagen().getHeight()) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public void paint(Graphics g) {
		
		super.paint(g);
		
		if (isSeleccionActiva()) {
			
			if (isPrimerPunto()) {
				pintarLineas(g, getOrigenSeleccion());
			}
			
			if (isSubSeleccion()) {	
				pintarSeleccion(g, getOrigenSeleccion(), getFinalSeleccion());
				pintarLineas(g, getOrigenSeleccion());
				pintarLineas(g, getFinalSeleccion());
			}
		}
		
		if (isEnFoco()) {
			if (isMostrarGuias()) {
				if (getActual() != null)
					pintarLineas(g, getActual());
			}
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


	public boolean isMostrarGuias() {
		return mostrarGuias;
	}


	public void setMostrarGuias(boolean mostrarGuias) {
		repaint();
		this.mostrarGuias = mostrarGuias;
	}

	public boolean isSeleccionActiva() {
		return seleccionActiva;
	}

	public void setSeleccionActiva(boolean seleccionActiva) {
		repaint();
		this.seleccionActiva = seleccionActiva;
	}


	public boolean isEnFoco() {
		return enFoco;
	}


	public void setEnFoco(boolean enFoco) {
		this.enFoco = enFoco;
	}

	public VentanaPrincipal getPadre() {
		return padre;
	}

	public void setPadre(VentanaPrincipal padre) {
		this.padre = padre;
	}

	public JTextField getCampoX() {
		return campoX;
	}

	public void setCampoX(JTextField campoX) {
		this.campoX = campoX;
	}


	public JTextField getCampoY() {
		return campoY;
	}

	public void setCampoY(JTextField campoY) {
		this.campoY = campoY;
	}

	public JTextField getCamboGris() {
		return camboGris;
	}

	public void setCamboGris(JTextField camboGris) {
		this.camboGris = camboGris;
	}	
	
}
