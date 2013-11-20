package vpc.cdas.fotovision;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VentanaTransformacionLineal extends VentanaSecundaria {

	private static final long serialVersionUID = 1L;
	private static final int MAX_TRAMOS = 10;
	private static final int MIN_TRAMOS = 1;
	private static final int ANCHO = 250;
	private static final int ALTO = 400;
	
	private ArrayList<JLabel> arrayLabel;
	private ArrayList<JTextField> arrayX;
	private ArrayList<JTextField> arrayY;
	private JComboBox<Integer> combo;
	private int tramos;
	
	private VentanaImagen padre;

	VentanaTransformacionLineal(VentanaImagen padre) {
		super(padre, "Transformacion Lineal por Tramos", ANCHO, ALTO);
		setTramos(MIN_TRAMOS);
		
		arrayLabel = new ArrayList<JLabel>();
		arrayX = new ArrayList<JTextField>();
		arrayY = new ArrayList<JTextField>();
	
		JComboBox<Integer> combo = new JComboBox<Integer>();
		for (int i = 1; i <= MAX_TRAMOS; i++) {
			combo.addItem(i);
		}
		
		combo.setSelectedIndex(0);
		setCombo(combo);
		
		getCombo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTramos(Integer.parseInt(getCombo().getSelectedItem().toString()));
				setVisibilidad();
			}
		});
		
		JPanel contenedor = new JPanel();
		contenedor.setLayout(new GridLayout(11, 3));
		
		for (int i = 0; i < MAX_TRAMOS; i++) {
			int j = i + 1;
			arrayLabel.add(new JLabel("Punto " + j + " :"));
			arrayX.add(new JTextField(5));
			arrayY.add(new JTextField(5));
		}
		
		setLayout(new BorderLayout());
		setSize(250, 400);
		
		contenedor.add(new JLabel("Tramos: "));
		contenedor.add(getCombo());
		contenedor.add(new JLabel(""));
		
		for (int i = 0; i < MAX_TRAMOS; i++) {
			contenedor.add(arrayLabel.get(i));
			contenedor.add(arrayX.get(i));
			contenedor.add(arrayY.get(i));
		}

		JButton boton = new JButton("Aplicar");

		boton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<Coordenada> puntos = new ArrayList<Coordenada>();
				
				//int tr = getTramos();
				//Tramos tramos = new Tramos(tr);
				
				for (int i = 0; i <= getTramos(); i++) {
				     //tramos.setTramo(i, getX(i), getY(i));
				     
				     puntos.add(new Coordenada(getX(i), getY(i)));
				}
				
				BufferedImage imagen = getPadre().getImagen();
				
				getPadre().getPadre().mostrarImagen(Operaciones.transformacionLinealB(imagen, puntos));
				//getPadre().getPadre().mostrarImagen(Operaciones.transformacionLineal(imagen, tramos));

			}
		});
		
		getContentPane().add(contenedor, BorderLayout.CENTER);
		getContentPane().add(boton, BorderLayout.SOUTH);
		
		setResizable(false);
		setVisibilidad();
	}

	public void setVisibilidad() {
		for (int i = 0; i < 10; i++) {
			if (i <= tramos) {
				arrayLabel.get(i).setVisible(true);;
				arrayX.get(i).setVisible(true);
				arrayY.get(i).setVisible(true);
			} else {
				arrayLabel.get(i).setVisible(false);;
				arrayX.get(i).setVisible(false);
				arrayY.get(i).setVisible(false);
			}
		}
	}

	public JComboBox<Integer> getCombo() {
		return combo;
	}

	public void setCombo(JComboBox<Integer> combo) {
		this.combo = combo;
	}

	public VentanaImagen getPadre() {
		return padre;
	}

	public void setPadre(VentanaImagen padre) {
		this.padre = padre;
	}

	public void setTramos(int t) {
		tramos = t;
	}

	public int getTramos() {
		return tramos;
	}

	public int getX(int index) {
		try {
			return Integer.parseInt(arrayX.get(index).getText());
		} catch (Exception e) {
			return -1;
		}
	}

	public int getY(int index) {
		return Integer.parseInt(arrayY.get(index).getText());
	}
}