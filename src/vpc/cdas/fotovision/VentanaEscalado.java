package vpc.cdas.fotovision;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class VentanaEscalado extends VentanaSecundaria {

	private static final long serialVersionUID = 1L;
	private static final int ANCHO = 300;
	private static final int ALTO = 200;
	
	private static final String INTERP_VMP = "Vecino más próximo";
	private static final String INTERP_BL = "Bilineal";
	private static final String ENT_PORCENTAJE = "Porcentajes";
	private static final String ENT_PIXELES = "Píxeles";
	
	private static final int PIXELES = 0;
	private static final int PORCENTAJE = 1;
	private static final int VMP = 0;
	private static final int BL = 1;
	
	private JTextField campoX;
	private JTextField campoY;
	private boolean relacionAspecto;
	private int tipoInterpolacion;
	private int tipoEntrada;
	
	private int anchoOriginal;
	private int altoOriginal;
	
	private boolean activoX;
	private boolean activoY;
	
	VentanaEscalado(VentanaImagen padre, BufferedImage imagen) {
		super(padre, "Escalar", ANCHO, ALTO);
		
		setTipoEntrada(PIXELES);
		setTipoInterpolacion(Operaciones.INT_VMP);
		
		setActivoX(true);
		setActivoY(true);
		
		setAnchoOriginal(imagen.getWidth());
		setAltoOriginal(imagen.getHeight());
		
		JComboBox<String> comboEntrada = new JComboBox<String>();
		comboEntrada.addItem(ENT_PIXELES);
		comboEntrada.addItem(ENT_PORCENTAJE);
		comboEntrada.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> combo = (JComboBox<String>) e.getSource();
				
				setActivoX(false);
				setActivoY(false);
				
				switch((String) combo.getSelectedItem()) {
					
				case ENT_PIXELES:
					setTipoEntrada(PIXELES);
					
					getCampoX().setText(String.valueOf(getAnchoOriginal()));
					getCampoY().setText(String.valueOf(getAltoOriginal()));
					break;
				case ENT_PORCENTAJE:
					setTipoEntrada(PORCENTAJE);
					getCampoX().setText("100");
					getCampoY().setText("100");
					break;
				}
				
				setActivoX(true);
				setActivoY(true);
			}
		});
		
		JComboBox<String> comboInterpolacion = new JComboBox<String>();
		comboInterpolacion.addItem(INTERP_VMP);
		comboInterpolacion.addItem(INTERP_BL);
		comboInterpolacion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JComboBox<String> combo = (JComboBox<String>) e.getSource();
				switch((String) combo.getSelectedItem()) {
					
				case INTERP_VMP:
					setTipoInterpolacion(Operaciones.INT_VMP);
					break;
				case INTERP_BL:
					setTipoInterpolacion(Operaciones.INT_BL);
					break;
				}
			}
		});
		
		JTextField campoX = new JTextField(String.valueOf(getAnchoOriginal()));
		campoX.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				if (isActivoX()) {
					
					setActivoY(false);
					
					if (isRelacionAspecto()) {
						if (getTipoEntrada() == PIXELES)
							cambiarYPixeles();
						else if (getTipoEntrada() == PORCENTAJE) 
							getCampoY().setText(getCampoX().getText());
					}
					
					setActivoY(true);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				
				if (isActivoX()) {
					
					setActivoY(false);
					if (isRelacionAspecto()) {
						if (getTipoEntrada() == PIXELES)
							cambiarYPixeles();
						else if (getTipoEntrada() == PORCENTAJE) 
							getCampoY().setText(getCampoX().getText());
					}
					setActivoY(true);
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				
			}
		});
		setCampoX(campoX);
		
		JTextField campoY = new JTextField(String.valueOf(getAltoOriginal()));
		campoY.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				
				if (isActivoY()) {
					setActivoX(false);
					if (isRelacionAspecto()) {
						if (getTipoEntrada() == PIXELES)
							cambiarXPixeles();
						else if (getTipoEntrada() == PORCENTAJE)
							getCampoX().setText(getCampoY().getText());
					}
					setActivoX(true);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// TODO Auto-generated method stub
				if (isActivoY()) {
					setActivoX(false);
					if (isRelacionAspecto()) {
						if (getTipoEntrada() == PIXELES)
							cambiarXPixeles();
						else if (getTipoEntrada() == PORCENTAJE)
							getCampoX().setText(getCampoY().getText());
					}
					setActivoX(true);
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				
			}
		});
		setCampoY(campoY);
		
		JCheckBox relacionAspecto = new JCheckBox("Restringir proporciones");
		relacionAspecto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				JCheckBox checkbox = (JCheckBox) e.getSource();
				
				setActivoX(false);
				setActivoY(false);
				if (checkbox.isSelected()) {
					setRelacionAspecto(true);
					if (getTipoEntrada() == PIXELES) {
						getCampoX().setText(String.valueOf(getAnchoOriginal()));
						getCampoY().setText(String.valueOf(getAltoOriginal()));
					} 
					else if (getTipoEntrada() == PORCENTAJE) {
						getCampoX().setText("100");
						getCampoY().setText("100");
					}
				}
				else {
					setRelacionAspecto(false);
					if (getTipoEntrada() == PIXELES) {
						getCampoX().setText(String.valueOf(getAnchoOriginal()));
						getCampoY().setText(String.valueOf(getAltoOriginal()));
					} 
					else if (getTipoEntrada() == PORCENTAJE) {
						getCampoX().setText("100");
						getCampoY().setText("100");
					}
				}
				
				setActivoX(true);
				setActivoY(true);
				
			}
		});
		
		
		JButton boton = new JButton("Aplicar");
		boton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int nuevoAncho = 0;
				int nuevoAlto = 0;
				
				if (getTipoEntrada() == PORCENTAJE) {
					double relX = Double.valueOf(getCampoX().getText()) / 100.0;
					nuevoAncho = (int) (getAnchoOriginal() * relX);
					
					double relY = Double.valueOf(getCampoY().getText()) / 100.0;
					nuevoAlto = (int) (getAltoOriginal() * relY);
				}
				else if (getTipoEntrada() == PIXELES){
					nuevoAncho = Integer.parseInt(getCampoX().getText());
					nuevoAlto = Integer.parseInt(getCampoY().getText());
				}
				
				BufferedImage imagen = getPadre().getImagen();
				getPadre().getPadre().mostrarImagen(Operaciones.escalar(imagen, nuevoAncho, nuevoAlto, getTipoInterpolacion()));
				
			}
		});
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(5, 2));
		
		panel.add(new JLabel("Ancho: "));
		panel.add(getCampoX());
		panel.add(new JLabel("Alto: "));
		panel.add(getCampoY());
		panel.add(new JLabel("Valores en: "));
		panel.add(comboEntrada);
		panel.add(new JLabel("Interpolación:"));
		panel.add(comboInterpolacion);
		panel.add(relacionAspecto);
		
		setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(boton, BorderLayout.SOUTH);
		
		setResizable(false);
		
	}
	
	private void cambiarXPixeles() {
		
		if (getCampoY().getText().length() > 0) {
		
			int altoNuevo = Integer.parseInt(getCampoY().getText());
			double rel = (double) altoNuevo / (double) getAltoOriginal();
			getCampoX().setText(String.valueOf((int) (getAnchoOriginal() * rel)));
		}
	}

	private void cambiarYPixeles() {
		
		if (getCampoX().getText().length() > 0) {
			int anchoNuevo = Integer.parseInt(getCampoX().getText());
			double rel = (double) anchoNuevo / (double) getAnchoOriginal();
				
			getCampoY().setText(String.valueOf((int) (getAltoOriginal() * rel)));
		}
		
	}

	public int getAnchoOriginal() {
		return anchoOriginal;
	}

	public void setAnchoOriginal(int anchoOriginal) {
		this.anchoOriginal = anchoOriginal;
	}

	public int getAltoOriginal() {
		return altoOriginal;
	}

	public void setAltoOriginal(int altoOriginal) {
		this.altoOriginal = altoOriginal;
	}

	public int getTipoInterpolacion() {
		return tipoInterpolacion;
	}

	public void setTipoInterpolacion(int tipoInterpolacion) {
		this.tipoInterpolacion = tipoInterpolacion;
	}

	public int getTipoEntrada() {
		return tipoEntrada;
	}

	public void setTipoEntrada(int tipoEntrada) {
		this.tipoEntrada = tipoEntrada;
	}

	public JTextField getCampoX() {
		return campoX;
	}

	public void setCampoX(JTextField campoX) {
		this.campoX = campoX;
	}

	public JTextField getCampoY() {
		return campoY;
	}

	public void setCampoY(JTextField campoY) {
		this.campoY = campoY;
	}

	public boolean isRelacionAspecto() {
		return relacionAspecto;
	}

	public void setRelacionAspecto(boolean relacionAspecto) {
		this.relacionAspecto = relacionAspecto;
	}

	public boolean isActivoX() {
		return activoX;
	}

	public void setActivoX(boolean activoX) {
		this.activoX = activoX;
	}

	public boolean isActivoY() {
		return activoY;
	}

	public void setActivoY(boolean activoY) {
		this.activoY = activoY;
	}
	
}
