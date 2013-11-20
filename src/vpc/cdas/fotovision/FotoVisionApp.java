package vpc.cdas.fotovision;

import javax.swing.UIManager;

public class FotoVisionApp {
	
	public static void main(String[] args) {
		
		try {
	          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		new VentanaPrincipal().setVisible(true);
	
	}

}
