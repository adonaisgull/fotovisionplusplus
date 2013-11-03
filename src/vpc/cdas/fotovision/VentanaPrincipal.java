package vpc.cdas.fotovision;

import java.awt.BorderLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class VentanaPrincipal extends JFrame {
	
	private JMenuBar menuPrincipal;
	private JMenu archivo, operaciones, ver, ayuda;
	private JMenuItem actAbrir, actGuardar, actCerrar;
	
	
	public VentanaPrincipal() {
		
		setTitle("FotoVision++");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initComponents();
	}
	
	private void initComponents() {
		
		//JMenu aux = new JMenu("Archivo");
		
		setMenuPrincipal(new JMenuBar());
		
		setArchivo(new JMenu("Archivo"));
		setOperaciones(new JMenu("Operaciones"));
		setVer(new JMenu("Ver"));
		setAyuda(new JMenu("Ayuda"));
		
		setActAbrir(new JMenuItem("Abrir"));		
		setActGuardar(new JMenuItem("Guardar"));
		setActCerrar(new JMenuItem("Cerrar"));
		
		getActAbrir().setMnemonic('O');
		getActAbrir().setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_O, InputEvent.CTRL_MASK));
		
		getActGuardar().setMnemonic('S');
		getActGuardar().setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_S, InputEvent.CTRL_MASK));
		
		
		getArchivo().add(getActAbrir());
		getArchivo().add(getActGuardar());
		getArchivo().add(new JSeparator());
		getArchivo().add(getActCerrar());
		
		getMenuPrincipal().add(getArchivo());
		getMenuPrincipal().add(getOperaciones());
		getMenuPrincipal().add(getVer());
		getMenuPrincipal().add(getAyuda());
		
		
		add(getMenuPrincipal(), BorderLayout.NORTH);
	}
	
	public JMenuItem getActGuardar() {
		return actGuardar;
	}

	public void setActGuardar(JMenuItem actGuardar) {
		this.actGuardar = actGuardar;
	}

	public JMenuItem getActAbrir() {
		return actAbrir;
	}

	public void setActAbrir(JMenuItem actAbrir) {
		this.actAbrir = actAbrir;
	}

	public JMenuItem getActCerrar() {
		return actCerrar;
	}

	public void setActCerrar(JMenuItem actCerrar) {
		this.actCerrar = actCerrar;
	}

	public JMenuBar getMenuPrincipal() {
		return menuPrincipal;
	}

	public void setMenuPrincipal(JMenuBar menuPrincipal) {
		this.menuPrincipal = menuPrincipal;
	}

	public JMenu getArchivo() {
		return archivo;
	}

	public void setArchivo(JMenu archivo) {
		this.archivo = archivo;
	}

	public JMenu getOperaciones() {
		return operaciones;
	}

	public void setOperaciones(JMenu operaciones) {
		this.operaciones = operaciones;
	}

	public JMenu getVer() {
		return ver;
	}

	public void setVer(JMenu ver) {
		this.ver = ver;
	}

	public JMenu getAyuda() {
		return ayuda;
	}

	public void setAyuda(JMenu ayuda) {
		this.ayuda = ayuda;
	}
	
	
	
}