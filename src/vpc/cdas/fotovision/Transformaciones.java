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
		
		BufferedImage copia = new BufferedImage (imagen.getWidth(),imagen.getHeight(),imagen.getType());
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
		LookUpTable lut = new LookUpTable();
		//calculamos la look up table apartir de cada tramo
		for (int i = 1; i < tramos.get_num_tramos(); i++) {
			//pedimos el punto del tramo actual
			int tramo[] = tramos.get_tramo(i);
			//pedimos el punto del tramo anterior
			int tramo_anterior[] = tramos.get_tramo(i - 1);
			int dividendo = tramo[1] - tramo_anterior[1];
			int divisor = tramo[0] - tramo_anterior[0];
			//caso general en el que existe pendiente y no es horizontal ni vertical
			if (((dividendo) != 0) && ((divisor) != 0)) {
				//calculo de la pendiente de la recta
				double m = (tramo[1] - tramo_anterior[1]) / (tramo[0] - tramo_anterior[0]);
				//calculo de la constante de la recta
				double c = tramo[1] - m * tramo[0];
				//este bucle calcula el Vout
				for (int j = tramo_anterior[1]; j < tramo[1]; j++) {
					//el calculo se realiza mediante la formula de la recta
					int resultado = (int) Math.round(m * j + c);
					if (resultado > 255) {
						resultado = 255;
					} else if (resultado < 0) {
						resultado = 0;
					}
					lut.set_valor(j, resultado);
				}
			//caso en el que la pendiente es cero
			} else if (((dividendo) == 0) && ((divisor) != 0)) {
				lut.set_valor(tramo[1], tramo[0]);
			//caso en el que la pendiente es infinito
			} else if (((dividendo) != 0) && ((divisor) == 0)) {
				for (int j = tramo_anterior[1]; j < tramo[1]; j++) {
					lut.set_valor(j, tramo[0]);
				}
			}
		}
		
		for (int x = 0; x < copia.getWidth(); x++) {
			for (int y = 0; y < copia.getHeight(); y++) {
				Color color = new Color(copia.getRGB(x, y));
				int Vin = color.getRed();
				int Vout = lut.get_valor(Vin);
				copia.setRGB(x, y, new Color(Vout, Vout, Vout).getRGB());
			}
		}
		
		return copia;
	}
}
