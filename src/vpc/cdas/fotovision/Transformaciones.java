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
	static final int VAR_PIXELS = 256;
	//tabla de transformaciones
	private static int lut[];
	
	Transformaciones() {
		//inicializacion de la tabla de transformaciones
		lut = new int[VAR_PIXELS];
		for (int i = 0; i < VAR_PIXELS; i++) {
			lut[i] = i;
		}
	}
	
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
	
	public static BufferedImage transormacionLineal(BufferedImage imagen, Tramos tramos) {
		BufferedImage copia = clona(imagen);
		//calculamos la look up table apartir de cada tramo
		for (int i = 1; i < tramos.get_num_tramos(); i++) {
			//pedimos el punto del tramo actual
			int tramo[] = tramos.get_tramo(i);
			//pedimos el punto del tramo anterior
			int tramo_anterior[] = tramos.get_tramo(i - 1);
			//caso general en el que existe pendiente y no es horizontal ni vertical
			if (((tramo[1] - tramo_anterior[1]) != 0) && ((tramo[0] - tramo_anterior[0]) != 0)) {
				//calculo de la pendiente de la recta
				int m = (tramo[1] - tramo_anterior[1]) / (tramo[0] - tramo_anterior[0]);
				//calculo de la constante de la recta
				int c = tramo[1] - m * tramo[0];
				//este bucle calcula el Vout
				for (int j = tramo_anterior[1]; j < tramo[1]; j++) {
					//el calculo se realiza mediante la formula de la recta
					lut[j] = m * j + c;
				}
			} else if (((tramo[1] - tramo_anterior[1]) == 0) && ((tramo[0] - tramo_anterior[0]) != 0)) {
				
			}
		}
		return copia;
	}
}
