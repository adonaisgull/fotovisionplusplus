package vpc.cdas.fotovision;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class VentanaCompararImagenes extends VentanaSecundaria {

	private static final long serialVersionUID = 1L;
	private static final int ANCHO = 450;
	private static final int ALTO = 80;
	
	private JComboBox<String> combo;
	private JTextField umbral;

	public VentanaCompararImagenes(VentanaImagen padre) {
		
		super(padre, "Comparar imágenes", ANCHO, ALTO);
		
		ArrayList<VentanaImagen> ventanas = getPadre().getPadre().getVentanasImagen();
		
		JComboBox<String> combo = new JComboBox<String>();		
		for (int i = 0; i < ventanas.size(); i++) {
			if (ventanas.get(i).isVisible() && ventanas.get(i).getId() != getPadre().getId()) {
				combo.addItem(ventanas.get(i).getTitle());
			}
		}
		
		JButton boton = new JButton("Comparar");
		boton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int umbral = Integer.parseInt(getUmbral().getText());
				if (umbral < 0) umbral = 0;
				if (umbral > 255) umbral = 255;
				
				BufferedImage otraImagen = null;
				
				ArrayList<VentanaImagen> ventanas = getPadre().getPadre().getVentanasImagen();	
				for (int i = 0; i < ventanas.size(); i++) {
					if (ventanas.get(i).getTitle() == getCombo().getSelectedItem()) {
						otraImagen = ventanas.get(i).getImagen();
					}
				}
				
				BufferedImage imagen = Operaciones.compararImagenes(getPadre().getImagen(), otraImagen, umbral);
				getPadre().getPadre().mostrarImagen(imagen);
				
			}
		});
		
		JTextField campoUmbral = new JTextField(3);
		
		setLayout(new FlowLayout());
		getContentPane().add(new JLabel("Comparar '" + getPadre().getTitle() + "' con: "));
		getContentPane().add(combo);
		getContentPane().add(new JLabel("Valor: "));
		getContentPane().add(campoUmbral);
		getContentPane().add(boton);
		
		setUmbral(campoUmbral);
		setCombo(combo);
	}

	public JComboBox<String> getCombo() {
		return combo;
	}

	public void setCombo(JComboBox<String> combo) {
		this.combo = combo;
	}

	public JTextField getUmbral() {
		return umbral;
	}

	public void setUmbral(JTextField umbral) {
		this.umbral = umbral;
	}
	
}
