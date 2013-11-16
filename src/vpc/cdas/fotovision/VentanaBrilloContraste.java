package vpc.cdas.fotovision;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VentanaBrilloContraste extends VentanaSecundaria {

	private static final long serialVersionUID = 1L;
	private static final int ANCHO = 250;
	private static final int ALTO = 200;
	private JSlider campoBrillo;
	private JSlider campoContraste;
	private JLabel etiquetaBrillo;
	private JLabel etiquetaContraste;
	
	private VentanaImagen padre;

	VentanaBrilloContraste(VentanaImagen padre, double brillo, double contraste) {
		super(padre, "Brillo y Contraste", ANCHO, ALTO);
		
		// Slider para el brillo
		JSlider campoBrillo = new JSlider(JSlider.HORIZONTAL, -150, 150, (int) Math.round(brillo));
		campoBrillo.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				getEtiquetaBrillo().setText(getCampoBrillo().getValue() + "");
			}
		});
		setCampoBrillo(campoBrillo);
		
		// Slider para el contraste
		JSlider campoContraste = new JSlider(JSlider.HORIZONTAL, -50, 100, (int) Math.round(contraste));
		campoContraste.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				getEtiquetaContraste().setText(getCampoContraste().getValue() + "");
			}
		});
		setCampoContraste(campoContraste);
		
		JPanel sliders = new JPanel();
		sliders.setLayout(new GridLayout(5, 2));
		
		setEtiquetaBrillo(new JLabel((int) Math.round(brillo) + ""));
		setEtiquetaContraste(new JLabel((int) Math.round(contraste) + ""));
		
		sliders.add(new JLabel("Brillo"));
		sliders.add(new JLabel(""));
		
		sliders.add(getCampoBrillo());
		sliders.add(getEtiquetaBrillo());
		
		sliders.add(new JLabel("Contraste"));
		sliders.add(new JLabel(""));
		
		sliders.add(getCampoContraste());
		sliders.add(getEtiquetaContraste());
		
		JButton boton = new JButton("Aplicar");
		
		boton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BufferedImage imagen = Operaciones.cambiarBrilloContraste(getPadre().getImagen(), getBrilloNuevo(), getContrasteNuevo());
				getPadre().getPadre().mostrarImagen(imagen);
			}
		});
		
		
		setLayout(new BorderLayout());
		getContentPane().add(sliders, BorderLayout.CENTER);
		getContentPane().add(boton, BorderLayout.SOUTH);
		
		setResizable(false);
	}

	public double getBrilloNuevo() {
		return getCampoBrillo().getValue();
	}
	
	public double getContrasteNuevo() {	
		return getCampoContraste().getValue();
	}

	public JSlider getCampoBrillo() {
		return campoBrillo;
	}

	public void setCampoBrillo(JSlider campoBrillo) {
		this.campoBrillo = campoBrillo;
	}

	public JSlider getCampoContraste() {
		return campoContraste;
	}

	public void setCampoContraste(JSlider campoContraste) {
		this.campoContraste = campoContraste;
	}

	public JLabel getEtiquetaBrillo() {
		return etiquetaBrillo;
	}

	public void setEtiquetaBrillo(JLabel etiquetaBrillo) {
		this.etiquetaBrillo = etiquetaBrillo;
	}

	public JLabel getEtiquetaContraste() {
		return etiquetaContraste;
	}

	public void setEtiquetaContraste(JLabel etiquetaContraste) {
		this.etiquetaContraste = etiquetaContraste;
	}

	public VentanaImagen getPadre() {
		return padre;
	}

	public void setPadre(VentanaImagen padre) {
		this.padre = padre;
	}
	
	
	
}