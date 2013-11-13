package vpc.cdas.fotovision;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DialogTransformacionLineal extends JDialog{

	private static final long serialVersionUID = 1L;
	private static final int MAX_TRAMOS = 10;
	private ArrayList<JLabel> arrayLabel;
	private ArrayList<JTextField> arrayX;
	private ArrayList<JTextField> arrayY;
	private JComboBox<Integer> combo;
	private JLabel label;
	private int tramos;

	DialogTransformacionLineal() {
		super();
		setTitle("Transformación Lineal por Tramos");
		arrayLabel = new ArrayList<JLabel>();
		arrayX = new ArrayList<JTextField>();
		arrayY = new ArrayList<JTextField>();

		//declarar primera linea
		label = new JLabel("Número de tramos: ");
		combo = new JComboBox<Integer>();
		for (int i = 0; i <= MAX_TRAMOS; i++) {
			combo.addItem(i);
		}
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				set_tramos(Integer.parseInt(combo.getSelectedItem().toString()));
				setVisibilidad();
			}
		});
		for (int i = 0; i < MAX_TRAMOS; i++) {
			int j = i + 1;
			arrayLabel.add(new JLabel("Tramo " + j + " :"));
			arrayX.add(new JTextField(5));
			arrayY.add(new JTextField(5));
		}
		//seleccionar el layout
		setLayout(new FlowLayout());
		//añadir elementos
		add(label);
		add(combo);
		for (int i = 0; i < MAX_TRAMOS; i++) {
			add(arrayLabel.get(i));
			add(arrayX.get(i));
			add(arrayY.get(i));
		}
		//opciones de ventana
		setSize(245, 400);
		setResizable(false);
		setVisibilidad();
	}

	public void setVisibilidad() {
		for (int i = 0; i < 10; i++) {
			if (i < tramos) {
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

	public void set_tramos(int t) {
		tramos = t;
	}

	public int get_tramos() {
		return tramos;
	}

	public int get_x(int posi) {
		try {
			return Integer.parseInt(arrayX.get(posi).getText());
		} catch (Exception e) {
			return -1;
		}
	}

	public int get_y(int posi) {
		return Integer.parseInt(arrayY.get(posi).getText());
	}
}