package vpc.cdas.fotovision;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class VentanaPrincipal extends JFrame {
	
	public VentanaPrincipal() {
		
		setTitle("FotoVision++");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initComponents();
	}
	
	private void cargarFichero() {
		
		// Dialogo para la carga de un fichero
		FileDialog fd = new FileDialog(this, "Elige un fichero", FileDialog.LOAD);
		fd.setFile("*.tiff");
		fd.setVisible(true);
		String filename = fd.getFile();
		
		System.out.println(filename);
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
				System.out.println("Abrir fichero");
				cargarFichero();
			}
		});
		
		// GUARDAR
		JMenuItem accionGuardar = new JMenuItem("Guardar");
		accionGuardar.setMnemonic('S');
		accionGuardar.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_S, InputEvent.CTRL_MASK));
		
		// CERRAR
		JMenuItem accionCerrar = new JMenuItem("Cerrar");
		accionCerrar.setMnemonic('Q');
		accionCerrar.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		
		// Añadimos las opciones a cada seccion
		
		archivoMenu.add(accionAbrir);
		archivoMenu.add(accionGuardar);
		archivoMenu.add(new JSeparator());
		archivoMenu.add(accionCerrar);
		
		// Añadimos las secciones a la barra de menú
		menuPrincipal.add(archivoMenu);
		menuPrincipal.add(imagenMenu);
		menuPrincipal.add(transformacionesMenu);
		menuPrincipal.add(verMenu);
		menuPrincipal.add(ayudaMenu);
		
		/*
		JDialog otraVentana = new JDialog(this, "Imagen");
		otraVentana.getContentPane().add(new JLabel("Imagen"));
		otraVentana.pack();
		//otraVentana.setVisible(true);
		*/
		
		add(menuPrincipal, BorderLayout.NORTH);
	}	
}