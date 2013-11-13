package vpc.cdas.fotovision;

import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DialogBrilloContraste extends JDialog {

	private static final long serialVersionUID = 1L;
	private double brillo;
	private double contraste;
	private double brillo_nuevo;
	private double contraste_nuevo;
	private JLabel label_brillo;
	private JLabel label_contraste;
	private JLabel info_brillo;
	private JLabel info_contraste;
	private JTextField texto_brillo;
	private JTextField texto_contraste;
	
	DialogBrilloContraste(double b, double c) {
		super();
		setBrillo(b);
		setContraste(c);
		label_brillo = new JLabel("Brillo: " + b);
		label_contraste = new JLabel("Contraste: " + c);
		texto_brillo = new JTextField(6);
		texto_contraste = new JTextField(6);
		info_brillo = new JLabel("Nuevo brillo:");
		info_contraste = new JLabel("Nuevo contraste:");
		setLayout(new FlowLayout());
		add(label_brillo);
		add(label_contraste);
		add(info_brillo);
		add(texto_brillo);
		add(info_contraste);
		add(texto_contraste);
		setTitle("Brillo y Contraste");
		setSize(240, 300);
		setResizable(false);
	}
	
	public double get_brillo() {
		return (Double.parseDouble(texto_brillo.getText()));
	}
	
	public double get_contraste() {
		return (Double.parseDouble(texto_contraste.getText()));
	}

	public double getBrillo() {
		return brillo;
	}

	public void setBrillo(double brillo) {
		this.brillo = brillo;
	}

	public double getContraste() {
		return contraste;
	}

	public void setContraste(double contraste) {
		this.contraste = contraste;
	}

	public double getBrillo_nuevo() {
		return brillo_nuevo;
	}

	public void setBrillo_nuevo(double brillo_nuevo) {
		this.brillo_nuevo = brillo_nuevo;
	}

	public double getContraste_nuevo() {
		return contraste_nuevo;
	}

	public void setContraste_nuevo(double contraste_nuevo) {
		this.contraste_nuevo = contraste_nuevo;
	}
}
