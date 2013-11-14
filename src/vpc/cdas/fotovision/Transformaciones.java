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
		//calculamos el tama�o de la imagen
		double size = imagen.getWidth() * imagen.getHeight();
		//sacamos el recuento de los pixeles de la imagen
		LookUpTable lut = calcularH(imagen);
		for (int i = 0; i < LookUpTable.VAR_PIXELS; i++) {
			double h = lut.get_valor(i);
			//acumulamos todos los valores segun la formula
			nu = nu + (h * i);
		}
		//dividimos la acumulacion por el tama�o de la imagen
		nu = nu / size;
		return nu;
	}
	
	public static double contraste(BufferedImage imagen) {
		double delta = 0;
		//hallamos el brillo que equivale a la media de pixeles
		double nu = brillo(imagen);
		//calculamos el tama�o de la imagen
		double size = imagen.getWidth() * imagen.getHeight();
		//sacamos el recuento de los pixeles de la imagen
		LookUpTable lut = calcularH(imagen);
		for (int i = 0; i < LookUpTable.VAR_PIXELS; i++) {
			double y = i;
			double h = lut.get_valor(i);
			//calculamos el acumulativo delta segun la formula de la varianza
			delta = delta + (h * Math.pow((y - nu), 2));
		}
		//el valor acumuladolo dividimos por el tama�o de la imagen
		delta = delta / size;
		delta = Math.sqrt(delta);
		return delta;
	}
	
	public static double entropia(BufferedImage imagen) {
		double E = 0;
		//calculamos el tama�o de la imagen
		double size = imagen.getWidth() * imagen.getHeight();
		//sacamos el recuento de los pixeles de la imagen
		LookUpTable lut = calcularH(imagen);
		for (int i = 0; i < LookUpTable.VAR_PIXELS; i++) {
			double h = lut.get_valor(i);
			//dividimos la cantidad de veces del pixel x que aparece en la imagen por el tama�ode la imagen
			double p = h / size;
			if (h != 0) {
				//se calcula la entropia segun su formula
				E = E + (p * (Math.log10(p) / Math.log10(2)));
			}
		}
		return E;
	}
	/**
	 * Esta funci�n realiza un recuento de cada pixel dada una im�gen.
	 * @param imagen Es la imagen sobre la que se va a realizar el recuento.
	 * @return Se retorna una Look Up Table donde se guarda los contadores de cada pixel.
	 */
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
	/**
	 * El usuario especificar� el nuevo valor de brillo y contraste que se calcular� sobre la
	 * im�gen que se recibe como par�metro.
	 * @param imagen La im�gen sobre la que se realizar� el cambio.
	 * @param contraste Nuevo contraste especificado por el usuario.
	 * @param brillo Nuevo brillo especificado por el usuario.
	 * @return Se retorna la im�gen con los nuevos valores de brillo y contraste.
	 */
	public static BufferedImage cambiarBrilloContraste(BufferedImage imagen,
			double contraste, double brillo) {
		BufferedImage copia = clona(imagen);
		//sacamos el brillo de la imagen
		double brillo_actual = brillo(imagen);
		//sacamos el contraste de la imagen
		double contraste_actual = contraste(imagen);
		//   Calculamos los paramentros de la funcion
		//contraste es el valor que ha pasado el usuario y contraste_actual es el de la imagen
		double A = contraste / contraste_actual;
		double B = brillo - (A * brillo_actual);
		LookUpTable lut = new LookUpTable();
		for (double x = 0; x < LookUpTable.VAR_PIXELS; x++) {
			//hallamos el valor de salida segun la formula de la funcion
			double Vout = A * x + B;
			//comprobacion para que no se salga del rango de 8 bits
			if (Vout < 0) {
				Vout = 0;
			} else if (Vout > 255) {
				Vout = 255;
			}
			//se redondea el valor final ya que el valor de un pixel no puede ser decimal
			int valor = (int) Math.round(Vout);
			lut.set_valor((int) x, valor);
		}
		//actualizamos los valores de la nueva imagen segun la Look Up Table calculada
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
	/**
	 * Se comparan la dos imagenes especificadas por parametro y se marcar� en rojo las zonas que
	 * no coinciden entre ambas. El usuario especificar� un valor que sirve como umbral, dicho valor
	 * estar� en el rango [0 - 255] los valores m�s cercanos a 0 del umbral ser�n m�s sensibles
	 * a las diferencias y los valores m�s cercanos a 255 ser�n menos sensibles a las diferencias.
	 * @param imagen1 Primera im�gen a considerar.
	 * @param imagen2 Segunda im�gen a considerar.
	 * @param T El umbral de diferencias.
	 * @return Retornar� una im�gen d�nde estar� marcado en rojo las regiones que no son iguales
	 * 			en ambas imagenes.
	 */
	public static BufferedImage compararImagenes(BufferedImage imagen1, BufferedImage imagen2, int T) {
		BufferedImage copia = clona(imagen1);
		for (int x = 0; x < copia.getWidth(); x++) {
            for (int y = 0; y < copia.getHeight(); y++) {
            	Color color1 = new Color(imagen1.getRGB(x, y));
            	Color color2 = new Color(imagen2.getRGB(x, y));
            	//sacamos el valor de gris de ambas imagenes
            	int color_imagen1 = color1.getRed();
            	int color_imagen2 = color2.getRed();
            	//hacemos la resta Id = |I1 - i2|
            	int nuevo_color = Math.abs(color_imagen1 - color_imagen2);
            	//comprobamos si el valor generado sobrepasa el umbral especificado por el usuario
            	if (nuevo_color > T) {
            		//ponemos a rojo los pixeles que se consideen que han cambiado
            		copia.setRGB(x, y, new Color(255, 0, 0).getRGB());
            	}
            }
		}
		return copia;
	}
	/**
	 * Est� funci�n aplica la f�rmula de l ecualizaci�n a la im�gen que recibe por par�metro. La funci�n
	 * de ecualizado intenta lograr que exista la misma cantidad de pixels para todos los diferentes
	 * niveles de gris que existen en la im�gen, aunque sea imposible en la pr�ctica es lo m�s cercano
	 * que se puede lograr.
	 * @param imagen La im�gen a la que se le desea aplicar la ecualizaci�n.
	 * @return Retornar� la im�gen con la ecualizaci�n aplicada.
	 */
	public static BufferedImage ecualizacion(BufferedImage imagen) {
		BufferedImage copia = clona(imagen);
		double size = imagen.getWidth() * imagen.getHeight();
		double m = LookUpTable.VAR_PIXELS;
		//partimos la variedad de pixeles por la cantidad de pixeles
		double var = m / size;
		//sacamos el recuento de los pixeles de la imagen
		LookUpTable lut = calcularH(imagen);
		//creamos la look up table para el valor de salida
		LookUpTable lut_vout = new LookUpTable();
		for(int i = 0; i < LookUpTable.VAR_PIXELS; i++) {
			//realizamos el calculo de Vout segu la formula max[0, round(M/size * C0(Vin)) - 1]
			int Vout = (int) Math.round((var * lut.get_valor(i)) - 1);
			if (Vout < 0) {
				Vout = 0;
			}
			lut_vout.set_valor(i, Vout);
		}
		//actualizamos los valores de la nueva imagen segun la Look Up Table calculada
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
	/**
	 * La correcci�n gamma modifica el brillo y contraste de la imagen que recibe por par�metro, en
	 * funci�n del par�metro gamma. Cuando el parametro ser� mayor que uno aumentar� el brillo de la
	 * im�gen, y cuando ser� menor que uno y mayor que cero disminuir� el brillo de la im�gen.
	 * @param imagen La imagen a la que se le quiere aplicar la correcci�n gamma.
	 * @param paramentro El parametro para la exponencial.
	 * @return Retona la im�gen con la corecci�n gamma aplicada.
	 */
	public static BufferedImage correcionGamma(BufferedImage imagen, double paramentro) {
		BufferedImage copia = clona(imagen);
		//repasamos cada uno de los pixeles de la imagen le aplicamos la formula de la correccion
		//gama y lo almacenamos en el nueva imagen
		for (int x = 0; x < copia.getWidth(); x++) {
            for (int y = 0; y < copia.getHeight(); y++) {
            	Color color = new Color(imagen.getRGB(x, y));
                double Vin = color.getRed();
                double a = Vin / 255;
                double b = 0;
                b = Math.pow(a, 1 / paramentro);
                int Vout = (int) Math.round(b * 255);
                copia.setRGB(x, y, new Color(Vout, Vout, Vout).getRGB());
            }
		}
		return copia;
	}
}


