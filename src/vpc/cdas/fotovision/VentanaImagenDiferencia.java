package vpc.cdas.fotovision;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class VentanaImagenDiferencia extends VentanaSecundaria {
	private static final long serialVersionUID = 1L;
	private static final int ANCHO = 450;
	private static final int ALTO = 80;
	
	private JComboBox<String> combo;

	public VentanaImagenDiferencia(VentanaImagen padre) {
		
		super(padre, "Imagen diferencia", ANCHO, ALTO);
		
		ArrayList<VentanaImagen> ventanas = getPadre().getPadre().getVentanasImagen();
		
		JComboBox<String> combo = new JComboBox<String>();		
		for (int i = 0; i < ventanas.size(); i++) {
			if (ventanas.get(i).isVisible() && ventanas.get(i).getId() != getPadre().getId()) {
				combo.addItem(ventanas.get(i).getTitle());
			}
		}
		
		JButton boton = new JButton("Calcular");
		boton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				BufferedImage otraImagen = null;
				
				ArrayList<VentanaImagen> ventanas = getPadre().getPadre().getVentanasImagen();	
				for (int i = 0; i < ventanas.size(); i++) {
					if (ventanas.get(i).getTitle() == getCombo().getSelectedItem()) {
						otraImagen = ventanas.get(i).getImagen();
					}
				}
				
				BufferedImage imagen = Operaciones.imagenDiferencia(getPadre().getImagen(), otraImagen);
				getPadre().getPadre().mostrarImagen(imagen);
				
			}
		});
		
		setLayout(new FlowLayout());
		getContentPane().add(new JLabel("Calcular diferencia de '" + getPadre().getTitle() + "' con: "));
		getContentPane().add(combo);
		getContentPane().add(boton);
		
		setCombo(combo);
	}

	public JComboBox<String> getCombo() {
		return combo;
	}

	public void setCombo(JComboBox<String> combo) {
		this.combo = combo;
	}
}
