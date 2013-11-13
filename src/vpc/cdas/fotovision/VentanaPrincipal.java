package vpc.cdas.fotovision;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class VentanaPrincipal extends JFrame {

	// CAMBIAR ESTO PARA QUE ESTA CLASE CONTENGA UN ARRAY DE VENTANAS IMAGEN
	
	private static final long serialVersionUID = 1L;
	private ArrayList<VentanaImagen> ventanasImagen;
	private int ventanaActual;
	private DialogTransformacionLineal dialog_lineal;
	private DialogBrilloContraste dialog_contrasteBrillo;
    private Tramos tramos;
	
	public VentanaPrincipal() {
		
		setVentanasImagen(new ArrayList<VentanaImagen>());
		setVentanaActual(-1);
		
		setTitle("FotoVision++");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initComponents();
	}
	
	private void crearSubImagen(BufferedImage imagen, Punto a, Punto b) {
		
		BufferedImage subseleccion = new BufferedImage(b.getxPixel() - a.getxPixel(), b.getyPixel() - a.getyPixel(), imagen.getType());
		int gris;
		int x2 = 0;
		int y2 = 0;
		
		for (int x = a.getxPixel(); x < b.getxPixel(); x++) {
			y2 = 0;
			for (int y = a.getyPixel(); y < b.getyPixel(); y++) {
				gris = new Color(imagen.getRGB(x, y)).getRed();
				
				subseleccion.setRGB(x2, y2, new Color(gris, gris, gris).getRGB());
				y2++;
			}
			x2++;
		}
		
		abrirVentana(subseleccion);
	}
	
	/*
	 * Abre un dialogo para buscar un fichero en el disco duro.
	 * Devuelve un BufferedImage con la imagen que elige el usuario.
	 */
	private void cargarImagen() {
		
		BufferedImage imagen = null;
		FileDialog fd = new FileDialog(this, "Elige un fichero", FileDialog.LOAD);
		fd.setFile("*.bmp");
		fd.setVisible(true);
		
		String nombreFichero = fd.getFile();
		
		if (nombreFichero != null) {
			nombreFichero = fd.getDirectory() + nombreFichero;
			
			try {
				imagen = ImageIO.read(new File(nombreFichero));
			
			} catch (IOException e) { }
		}
		
		// Convertimos a escala de grises
		imagen = Transformaciones.escalaDeGrisesPAL(imagen);
		abrirVentana(imagen);
	}
	
	public void transformacionLinealPorTramos() {
        dialog_lineal = new DialogTransformacionLineal();
        JButton boton = new JButton("Aplicar");
        boton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                        int tr = dialog_lineal.get_tramos();
                        tramos = new Tramos(tr);
                        for (int i = 1; i < tramos.get_num_tramos(); i++) {
                                tramos.set_tramo(i, dialog_lineal.get_x(i-1), dialog_lineal.get_y(i-1));
                        }
                        
                        abrirVentana(Transformaciones.transormacionLineal(getVentanasImagen().get(getVentanaActual()).getImagen(), tramos));
                }
        });
        dialog_lineal.add(boton);
        dialog_lineal.setVisible(true);
	}
	
	public void abrirVentana(BufferedImage imagen) {
		setVentanaActual(getVentanasImagen().size());
        VentanaImagen otraVentana = new VentanaImagen(this, getVentanasImagen().size(), imagen);
        getVentanasImagen().add(otraVentana);
        otraVentana.setVisible(true);
	}
	
	public void brilloContraste() {
		double b = Transformaciones.brillo(getVentanasImagen().get(getVentanaActual()).getImagen());
		double c = Transformaciones.contraste(getVentanasImagen().get(getVentanaActual()).getImagen());
		dialog_contrasteBrillo = new DialogBrilloContraste(b, c);
		JButton boton = new JButton("Aplicar brillo y contraste");
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                double brillo = dialog_contrasteBrillo.get_brillo();
                double contraste = dialog_contrasteBrillo.get_contraste();
                abrirVentana(Transformaciones.cambiarBrilloContraste
                		(getVentanasImagen().get(getVentanaActual()).getImagen(), 
                				contraste, brillo));
            }
        });
        dialog_contrasteBrillo.add(boton);
		dialog_contrasteBrillo.setVisible(true);
	}
	
	private void initComponents() {
		
		// Creamos la barra del menú principal
		JMenuBar menuPrincipal = new JMenuBar();
		
		// Creamos las secciones del menú
		JMenu archivoMenu = new JMenu("Archivo");
		JMenu imagenMenu = new JMenu("Imagen");
		JMenu transformacionesMenu = new JMenu("Transformaciones");
		JMenu verMenu = new JMenu("Ver");
		JMenu ayudaMenu = new JMenu("Ayuda");
		
		// Creamos los items (acciones) del menu ARCHIVO
		
		// ABRIR
		JMenuItem accionAbrir = new JMenuItem("Abrir");
		accionAbrir.setMnemonic('O');
		accionAbrir.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_O, InputEvent.CTRL_MASK));
		accionAbrir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cargarImagen();
			}
		});
		
		// GUARDAR
		JMenuItem accionGuardar = new JMenuItem("Guardar");
		accionGuardar.setMnemonic('S');
		accionGuardar.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_S, InputEvent.CTRL_MASK));
		accionGuardar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Abrir fichero");
				
			}
		});
		
		// CERRAR
		JMenuItem accionCerrar = new JMenuItem("Cerrar");
		accionCerrar.setMnemonic('Q');
		accionCerrar.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		
		// Creamos los items (acciones) del menu IMAGEN
		
		// SUBIMAGEN
		
		JMenuItem accionSubimagen = new JMenuItem("Crear subimagen");
		accionSubimagen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (getVentanaActual() >= 0) {
					if (getVentanasImagen().get(getVentanaActual()).isSubSeleccion()) {
					
						BufferedImage imagen = getVentanasImagen().get(getVentanaActual()).getImagen();
						Punto a = getVentanasImagen().get(getVentanaActual()).getOrigenSeleccion();
						Punto b = getVentanasImagen().get(getVentanaActual()).getFinalSeleccion();
						
						crearSubImagen(imagen, a, b);
					}
					else {
						// NO HAY SELECCION
						System.out.println("No ha seleccionado.");
					}
				}
				else {
					// MOSTRAR UN DIALOG CON MENSAJE DE IMAGEN NO SELECCIONADA
					System.out.println("No ha imagen seleccionada");
				}
			}
		});
		
		// Creamos los items (acciones) del menu TRANSFORMACIONES
		
		JMenuItem accionLinealTramos = new JMenuItem("Transformaciones Lineales por Tramos");
        accionLinealTramos.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        transformacionLinealPorTramos();
                }
        });
        
        JMenuItem accionBrilloContraste = new JMenuItem("Brillo y Contraste");
        accionBrilloContraste.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        brilloContraste();
                }
        });
		
		
		// Añadimos las opciones a cada seccion
		
		archivoMenu.add(accionAbrir);
		archivoMenu.add(accionGuardar);
		archivoMenu.add(new JSeparator());
		archivoMenu.add(accionCerrar);
		
		imagenMenu.add(accionSubimagen);
		
		transformacionesMenu.add(accionLinealTramos);
		transformacionesMenu.add(accionBrilloContraste);
		
		// Añadimos las secciones a la barra de menú
		menuPrincipal.add(archivoMenu);
		menuPrincipal.add(imagenMenu);
		menuPrincipal.add(transformacionesMenu);
		menuPrincipal.add(verMenu);
		menuPrincipal.add(ayudaMenu);
		
		add(menuPrincipal, BorderLayout.NORTH);
	}
	
	public int getVentanaActual() {
		return ventanaActual;
	}

	public void setVentanaActual(int ventanaActual) {
		this.ventanaActual = ventanaActual;
	}

	public ArrayList<VentanaImagen> getVentanasImagen() {
		return ventanasImagen;
	}

	public void setVentanasImagen(ArrayList<VentanaImagen> ventanasImagen) {
		this.ventanasImagen = ventanasImagen;
	}
	
}
