package vpc.cdas.fotovision;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class VentanaCorreccionGamma extends VentanaSecundaria {
	
	private static final long serialVersionUID = 1L;
	private static final int ANCHO = 300;
	private static final int ALTO = 80;
	
	private JTextField campoValor;

	public VentanaCorreccionGamma(VentanaImagen padre) {
		
		super(padre, "Correción Gamma", ANCHO, ALTO);
		
		setLayout(new FlowLayout());
		
		getContentPane().add(new JLabel("Valor: "));
		setCampoValor(new JTextField(6));
		
		JButton boton = new JButton("Aplicar");
		boton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				if (getCampoValor().getText().length() > 0) {
					
					double valor = Double.parseDouble(getCampoValor().getText());
					
					BufferedImage imagen = Operaciones.correcionGamma(getPadre().getImagen(), valor);
					getPadre().getPadre().mostrarImagen(imagen);
				}
			}
		});
		
		getContentPane().add(getCampoValor());
		getContentPane().add(boton);
		
	}

	public JTextField getCampoValor() {
		return campoValor;
	}

	public void setCampoValor(JTextField campoValor) {
		this.campoValor = campoValor;
	}
	
	
}
