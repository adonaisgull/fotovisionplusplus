package vpc.cdas.fotovision;

import java.awt.BorderLayout;
import java.awt.FileDialog;
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

	private static final long serialVersionUID = 1L;
	private ArrayList<BufferedImage> imagenes;
	private BufferedImage imagenActual;
	private DialogTransformacionLineal dialog;
	private Tramos tramos;
	
	public VentanaPrincipal() {
		
		setImagenes(new ArrayList<BufferedImage>());
		setImagenActual(null);
		
		setTitle("FotoVision++");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initComponents();
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
		
		getImagenes().add(imagen);
		VentanaImagen otraVentana = new VentanaImagen(this, getImagenes().size() - 1, imagen);
		otraVentana.setVisible(true);
	}
	
	private void initComponents() {
		
		// Creamos la barra del men� principal
		JMenuBar menuPrincipal = new JMenuBar();
		
		// Creamos las secciones del men�
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
		
		// Creamos los items (acciones) del menu Transformaciones
		
		// Transformaciones lineales por tramos
		
		JMenuItem accionLinealTramos = new JMenuItem("Transformaciones Lineales por Tramos");
		accionLinealTramos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				transformacionLinealPorTramos();
			}
		});
		
		// A�adimos las opciones a cada seccion
		
		archivoMenu.add(accionAbrir);
		archivoMenu.add(accionGuardar);
		archivoMenu.add(new JSeparator());
		archivoMenu.add(accionCerrar);
		
		transformacionesMenu.add(accionLinealTramos);
		
		// A�adimos las secciones a la barra de men�
		menuPrincipal.add(archivoMenu);
		menuPrincipal.add(imagenMenu);
		menuPrincipal.add(transformacionesMenu);
		menuPrincipal.add(verMenu);
		menuPrincipal.add(ayudaMenu);
		
		add(menuPrincipal, BorderLayout.NORTH);
	}
	
	public void transformacionLinealPorTramos() {
		dialog = new DialogTransformacionLineal();
		JButton boton = new JButton("Calcular nueva imagen");
		boton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int tr = dialog.get_tramos();
				tramos = new Tramos(tr);
				for (int i = 1; i < tramos.get_num_tramos(); i++) {
					tramos.set_tramo(i, dialog.get_x(i-1), dialog.get_y(i-1));
				}
				abrirVentana(Transformaciones.transormacionLineal(imagenActual, tramos));
			}
		});
		dialog.add(boton);
		dialog.setVisible(true);
	}
	
	public void abrirVentana(BufferedImage nueva) {
		VentanaImagen otraVentana = new VentanaImagen(this, getImagenes().size() - 1, 
				Transformaciones.transormacionLineal(imagenActual, tramos));
		otraVentana.setVisible(true);
	}
	
	public BufferedImage getImagenActual() {
		return imagenActual;
	}

	public void setImagenActual(BufferedImage imagenActual) {
		this.imagenActual = imagenActual;
	}

	public ArrayList<BufferedImage> getImagenes() {
		return imagenes;
	}

	public void setImagenes(ArrayList<BufferedImage> imagenes) {
		this.imagenes = imagenes;
	}
	
}
