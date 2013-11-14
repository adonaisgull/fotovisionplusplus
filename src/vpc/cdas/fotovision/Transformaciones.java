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
		
		BufferedImage copia = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());
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
                double dividendo = tramo[1] - tramo_anterior[1];
                double divisor = tramo[0] - tramo_anterior[0];
                //caso general en el que existe pendiente y no es horizontal ni vertical
                if (((dividendo) != 0) && ((divisor) != 0)) {
                        //calculo de la pendiente de la recta
                        double m = dividendo / divisor;
                        //calculo de la constante de la recta
                        double c = tramo[1] - m * tramo[0];
                        //este bucle calcula el Vout
                        for (int j = tramo_anterior[1]; j < tramo[1]; j++) {
                                //el calculo se realiza mediante la formula de la recta
                                double resultado = m * j + c;
                                if (resultado > 255) {
                                        resultado = 255;
                                } else if (resultado < 0) {
                                        resultado = 0;
                                }
                                int res = (int) Math.round(resultado);
                                lut.set_valor(j, res);
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
	
	public static double brillo(BufferedImage imagen) {
		double nu = 0;
		double size = imagen.getWidth() * imagen.getHeight();
		LookUpTable lut = calcularH(imagen);
		for (int i = 0; i < LookUpTable.VAR_PIXELS; i++) {
			double h = lut.get_valor(i);
			nu = nu + (h * i);
		}
		nu = nu / size;
		return nu;
	}
	
	public static double contraste(BufferedImage imagen) {
		double delta = 0;
		double nu = brillo(imagen);
		double size = imagen.getWidth() * imagen.getHeight();
		LookUpTable lut = calcularH(imagen);
		for (int i = 0; i < LookUpTable.VAR_PIXELS; i++) {
			double y = i;
			double h = lut.get_valor(i);
			delta = delta + (h * Math.pow((y - nu), 2));
		}
		delta = delta / size;
		delta = Math.sqrt(delta);
		return delta;
	}
	
	public static double entropia(BufferedImage imagen) {
		double E = 0;
		double size = imagen.getWidth() * imagen.getHeight();
		LookUpTable lut = calcularH(imagen);
		for (int i = 0; i < LookUpTable.VAR_PIXELS; i++) {
			double h = lut.get_valor(i);
			double p = h / size;
			if (h != 0)
				E = E + (p * (Math.log10(p) / Math.log10(2)));
		}
		return E;
	}
	
	public static LookUpTable calcularH(BufferedImage imagen) {
		LookUpTable lut = new LookUpTable();
		lut.set_zero();
		for (int x = 0; x < imagen.getWidth(); x++) {
            for (int y = 0; y < imagen.getHeight(); y++) {
            	Color color = new Color(imagen.getRGB(x, y));
            	lut.incrementar(color.getRed());
            }
		}
		return lut;
	}
	
	public static BufferedImage cambiarBrilloContraste(BufferedImage imagen,
			double contraste, double brillo) {
		BufferedImage copia = clona(imagen);
		double brillo_actual = brillo(imagen);
		double contraste_actual = contraste(imagen);
		double A = contraste / contraste_actual;
		double B = brillo - (A * brillo_actual);
		LookUpTable lut = new LookUpTable();
		for (double x = 0; x < LookUpTable.VAR_PIXELS; x++) {
			double Vout = A * x + B;
			if (Vout < 0) {
				Vout = 0;
			} else if (Vout > 255) {
				Vout = 255;
			}
			int valor = (int) Math.round(Vout);
			lut.set_valor((int) x, valor);
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
	
	public static BufferedImage compararImagenes(BufferedImage imagen1, BufferedImage imagen2, int T) {
		BufferedImage copia = clona(imagen1);
		for (int x = 0; x < copia.getWidth(); x++) {
            for (int y = 0; y < copia.getHeight(); y++) {
            	Color color1 = new Color(imagen1.getRGB(x, y));
            	Color color2 = new Color(imagen2.getRGB(x, y));
            	int color_imagen1 = color1.getRed();
            	int color_imagen2 = color2.getRed();
            	int nuevo_color = Math.abs(color_imagen1 - color_imagen2);
            	if (nuevo_color > T) {
            		copia.setRGB(x, y, new Color(255, 0, 0).getRGB());
            	}
            }
		}
		return copia;
	}
	
	public static BufferedImage equalizacion(BufferedImage imagen) {
		BufferedImage copia = clona(imagen);
		double size = imagen.getWidth() * imagen.getHeight();
		double m = LookUpTable.VAR_PIXELS;
		double var = m / size;
		LookUpTable lut = calcularH(imagen);
		LookUpTable lut_vout = new LookUpTable();
		for(int i = 0; i < LookUpTable.VAR_PIXELS; i++) {
			int Vout = (int) Math.round((var * lut.get_valor(i)) - 1);
			if (Vout < 0) {
				Vout = 0;
			}
			lut_vout.set_valor(i, Vout);
		}
		for (int x = 0; x < copia.getWidth(); x++) {
            for (int y = 0; y < copia.getHeight(); y++) {
                    Color color = new Color(copia.getRGB(x, y));
                    int Vin = color.getRed();
                    int Vout = lut_vout.get_valor(Vin);
                    copia.setRGB(x, y, new Color(Vout, Vout, Vout).getRGB());
            }
		}
		return copia;
	}
}


