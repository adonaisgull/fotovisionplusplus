package vpc.cdas.fotovision;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Transformaciones {
	
	static final double NTSC_RED = 0.229;
	static final double NTSC_GREEN = 0.587;
	static final double NTSC_BLUE = 0.114;
	static final double PAL_RED = 0.222;
	static final double PAL_GREEN = 0.707;
	static final double PAL_BLUE = 0.071;
	
	private static BufferedImage clona(BufferedImage imagen) {
		
		BufferedImage copia=new BufferedImage (imagen.getWidth(),imagen.getHeight(),imagen.getType());
		copia.setData(imagen.getData());
		
		return copia;
	}
	
	public static BufferedImage escalaDeGrisesPAL(BufferedImage imagen) {

		BufferedImage copia = clona(imagen);
		int gris;
		Color color;
		
		for (int x = 0; x < copia.getWidth(); x++) {
			for (int y = 0; y < copia.getHeight(); y++) {
				color = new Color(copia.getRGB(x, y));
				gris = (int) ((color.getRed() * PAL_RED) + (color.getGreen() * PAL_GREEN) + (color.getBlue() * PAL_BLUE));
				
				copia.setRGB(x, y, new Color(gris, gris, gris).getRGB());
			}
		}
		
		return copia;
	}
	
	public static BufferedImage escalaDeGrisesNTSC(BufferedImage imagen) {

		BufferedImage copia = clona(imagen);
		int gris;
		Color color;
		
		for (int x = 0; x < copia.getWidth(); x++) {
			for (int y = 0; y < copia.getHeight(); y++) {
				color = new Color(copia.getRGB(x, y));
				gris = (int) ((color.getRed() * NTSC_RED) + (color.getGreen() * NTSC_GREEN) + (color.getBlue() * NTSC_BLUE));
				
				copia.setRGB(x, y, new Color(gris, gris, gris).getRGB());
			}
		}
		
		return copia;
	}
}
