package vpc.cdas.fotovision;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int ANCHO_MIN = 600;
	private static final int ALTO_MIN = 400;
	
	private ArrayList<VentanaImagen> ventanasImagen;
	private int ventanaActual;
	private boolean mostrarGuias;
	private boolean seleccionActiva;

	public VentanaPrincipal() {

		setVentanasImagen(new ArrayList<VentanaImagen>());
		setVentanaActual(-1);

		setTitle("FotoVision++");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(ANCHO_MIN, ALTO_MIN));
		
		setMostrarGuias(false);
		setSeleccionActiva(false);

		initComponents();
	}

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
		
		if (imagen != null) {
			// Convertimos a escala de grises y mostramos
			imagen = Operaciones.escalaDeGrisesPAL(imagen);
			mostrarImagen(imagen);
		}
	}
	
	private void guardarImagen() {
		
		BufferedImage imagen = getVentanasImagen().get(getVentanaActual()).getImagen();
		
		FileDialog fd = new FileDialog(this, "Guardar imagen", FileDialog.LOAD);
		fd.setVisible(true);

		String nombreFichero = fd.getFile();

		if (nombreFichero != null) {
			nombreFichero = fd.getDirectory() + nombreFichero;

			try {
				ImageIO.write(imagen, "bmp", new File(nombreFichero));

			} catch (IOException e) { }
		}
	}
	
	private void mostrarSubimagen() {
		if (getVentanaActual() >= 0) {
			if (getVentanasImagen().get(getVentanaActual()).isSubSeleccion()) {

				BufferedImage imagen = getVentanasImagen().get(getVentanaActual()).getImagen();
				Punto a = getVentanasImagen().get(getVentanaActual()).getOrigenSeleccion();
				Punto b = getVentanasImagen().get(getVentanaActual()).getFinalSeleccion();

				BufferedImage subimagen = Operaciones.obtenerSubimagen(imagen, a, b);
				mostrarImagen(subimagen);
			}
			else {
				// NO HAY SELECCION
				//System.out.println("No ha seleccionado.");
			}
		}
		else {
			// MOSTRAR UN DIALOG CON MENSAJE DE IMAGEN NO SELECCIONADA
			//System.out.println("No ha imagen seleccionada");
		}
		
	}


	public void transformacionLinealPorTramos() {
		
		VentanaTransformacionLineal ventana = new VentanaTransformacionLineal(getVentanasImagen().get(getVentanaActual()));
		ventana.setVisible(true);
	}
	
	
	public void mostrarImagen(BufferedImage imagen) {
		
		setVentanaActual(getVentanasImagen().size());
		VentanaImagen otraVentana = new VentanaImagen(this, getVentanasImagen().size(), imagen);
		
		otraVentana.setMostrarGuias(isMostrarGuias());
		otraVentana.setSeleccionActiva(isSeleccionActiva());
		
		getVentanasImagen().add(otraVentana);
		otraVentana.setVisible(true);
	}
	
	public void mostrarInformacion() {
		
		VentanaSecundaria ventana = new VentanaInformacion(getVentanasImagen().get(getVentanaActual()));
		ventana.setVisible(true);
	}
	
	public void mostrarHistogramaAbsoluto() {
		
		BufferedImage imagen = getVentanasImagen().get(getVentanaActual()).getImagen();
		VentanaSecundaria ventana = new VentanaHistograma(getVentanasImagen().get(getVentanaActual()), Operaciones.histogramaAbs(imagen), VentanaHistograma.ABSOLUTO);
		ventana.setVisible(true);
	}
	
	public void mostrarHistogramaAcumulado() {
		
		BufferedImage imagen = getVentanasImagen().get(getVentanaActual()).getImagen();
		VentanaSecundaria ventana = new VentanaHistograma(getVentanasImagen().get(getVentanaActual()), Operaciones.histogramaAcu(imagen), VentanaHistograma.ACUMULADO);
		ventana.setVisible(true);
	}
	
	public void correccionGamma() {
		
		VentanaSecundaria ventana = new VentanaCorreccionGamma(getVentanasImagen().get(getVentanaActual()));
		ventana.setVisible(true);
	}
	
	public void ecualizacionHistograma() {
		
		BufferedImage imagen = getVentanasImagen().get(getVentanaActual()).getImagen();
		mostrarImagen(Operaciones.ecualizacion(imagen));
	}

	public void brilloContraste() {
		
		double b = Operaciones.getBrillo(getVentanasImagen().get(getVentanaActual()).getImagen());
		double c = Operaciones.getContraste(getVentanasImagen().get(getVentanaActual()).getImagen());
		
		VentanaSecundaria ventana = new VentanaBrilloContraste(getVentanasImagen().get(getVentanaActual()), b, c);
		ventana.setVisible(true);
	}
	
	public void compararImagenes() {
		
		VentanaSecundaria ventana = new VentanaCompararImagenes(getVentanasImagen().get(getVentanaActual()));
		ventana.setVisible(true);
	}
	
	public void imagenDiferencia() {
		
		VentanaSecundaria ventana = new VentanaImagenDiferencia(getVentanasImagen().get(getVentanaActual()));
		ventana.setVisible(true);
	}

	public void especificacionHistograma() {
		
		VentanaSecundaria ventana = new VentanaEspecificacionHistograma(getVentanasImagen().get(getVentanaActual()));
		ventana.setVisible(true);
	}
	

	private void initComponents() {

		// Creamos la barra del menú principal
		JMenuBar barraPrincipal = new JMenuBar();
		JMenuBar barraSecundaria = new JMenuBar();

		// Creamos las secciones del menú
		JMenu archivoMenu = new JMenu("Archivo");
		JMenu imagenMenu = new JMenu("Imagen");
		JMenu transformacionesMenu = new JMenu("Operaciones");
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
				guardarImagen();
			}
		});

		// CERRAR
		JMenuItem accionCerrar = new JMenuItem("Cerrar");
		accionCerrar.setMnemonic('Q');
		accionCerrar.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_Q, InputEvent.CTRL_MASK));

		// Creamos los items (acciones) del menu IMAGEN

		// SUBIMAGEN		
		
		JMenuItem accionSubimagen = new JMenuItem("Copiar selección");
		accionSubimagen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarSubimagen();
			}
		});

		// INFORMACION

		JMenuItem accionInformacion = new JMenuItem("Información");
		accionInformacion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarInformacion();
			}
		});

		// HISTOGRAMA ABSOLUTO

		JMenuItem accionHistoAbs = new JMenuItem("Histograma Absoluto");
		accionHistoAbs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarHistogramaAbsoluto();
			}
		});

		// HISTOGRAMA ACUMULADO

		JMenuItem accionHistoAcu = new JMenuItem("Histograma Acumulado");
		accionHistoAcu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarHistogramaAcumulado();
			}
		});

		// Creamos los items (acciones) del menu TRANSFORMACIONES

		JMenuItem accionLinealTramos = new JMenuItem("Transformación Lineal por Tramos");
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

		JMenuItem accionEcualizacionHistograma = new JMenuItem("Ecualización del histograma");
		accionEcualizacionHistograma.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ecualizacionHistograma();
			}
		});
		
		JMenuItem accionCorrecionGamma = new JMenuItem("Correción Gamma");
		accionCorrecionGamma.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				correccionGamma();
			}
		});
		
		JMenuItem accionCompararImagenes = new JMenuItem("Comparar con otra imagen");
		accionCompararImagenes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				compararImagenes();
			}
		});
		
		JMenuItem accionImagenDiferencia = new JMenuItem("Imagen diferencia");
		accionImagenDiferencia.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				imagenDiferencia();
			}
		});
		
		JMenuItem accionEspecificacionHistograma = new JMenuItem("Especificación histograma");
		accionEspecificacionHistograma.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				especificacionHistograma();
			}
		});
		

		// Añadimos las opciones a cada seccion

		archivoMenu.add(accionAbrir);
		archivoMenu.add(accionGuardar);
		archivoMenu.add(new JSeparator());
		archivoMenu.add(accionCerrar);

		imagenMenu.add(accionInformacion);
		imagenMenu.add(accionHistoAbs);
		imagenMenu.add(accionHistoAcu);
		imagenMenu.add(new JSeparator());
		imagenMenu.add(accionSubimagen);

		// Lineales
		transformacionesMenu.add(accionLinealTramos);
		transformacionesMenu.add(accionBrilloContraste);

		transformacionesMenu.add(new JSeparator());

		// No lineales
		transformacionesMenu.add(accionEcualizacionHistograma);
		transformacionesMenu.add(accionCorrecionGamma);
		transformacionesMenu.add(new JSeparator());
		transformacionesMenu.add(accionCompararImagenes);
		transformacionesMenu.add(accionImagenDiferencia);
		transformacionesMenu.add(new JSeparator());
		transformacionesMenu.add(accionEspecificacionHistograma);

		// Añadimos las secciones a la barra de menú
		barraPrincipal.add(archivoMenu);
		barraPrincipal.add(imagenMenu);
		barraPrincipal.add(transformacionesMenu);
		barraPrincipal.add(verMenu);
		barraPrincipal.add(ayudaMenu);
		
		JToggleButton btnSeleccion = new JToggleButton();
		btnSeleccion.setMargin(new Insets(1, 1, 1, 1));
		btnSeleccion.setIcon(new ImageIcon("media/icono_seleccion.png"));
		btnSeleccion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JToggleButton source = (JToggleButton)e.getSource();
				if (source.isSelected()) {
					setSeleccionActiva(true);
					for (int i = 0; i < getVentanasImagen().size(); i++)
						getVentanasImagen().get(i).setSeleccionActiva(true);
				}
				else {
					setSeleccionActiva(false);
					for (int i = 0; i < getVentanasImagen().size(); i++)
						getVentanasImagen().get(i).setSeleccionActiva(false);
				}
				
				getVentanasImagen().get(getVentanaActual()).toFront();
			}
		});
		barraSecundaria.add(btnSeleccion);
		
		JToggleButton btnGuias = new JToggleButton();
		btnGuias.setMargin(new Insets(1, 1, 1, 1));
		btnGuias.setIcon(new ImageIcon("media/icono_guias.png"));
		btnGuias.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JToggleButton source = (JToggleButton)e.getSource();
				if (source.isSelected()) {
					setMostrarGuias(true);
					for (int i = 0; i < getVentanasImagen().size(); i++)
						getVentanasImagen().get(i).setMostrarGuias(true);
				}
				else {
					setMostrarGuias(false);
					for (int i = 0; i < getVentanasImagen().size(); i++)
						getVentanasImagen().get(i).setMostrarGuias(false);
				}
				
				if (getVentanasImagen().size() > 0)
					getVentanasImagen().get(getVentanaActual()).toFront();
			}
		});
		
		barraSecundaria.add(btnGuias);
		
		
		JPanel contenedor = new JPanel();
		contenedor.setLayout(new GridLayout(2, 1));
		
		contenedor.add(barraPrincipal);
		contenedor.add(barraSecundaria);
		
		add(contenedor, BorderLayout.NORTH);
		//add(menuPrincipal, BorderLayout.NORTH);
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

	public boolean isMostrarGuias() {
		return mostrarGuias;
	}

	public void setMostrarGuias(boolean mostrarGuias) {
		this.mostrarGuias = mostrarGuias;
	}

	public boolean isSeleccionActiva() {
		return seleccionActiva;
	}

	public void setSeleccionActiva(boolean seleccionActiva) {
		this.seleccionActiva = seleccionActiva;
	}
	
}
