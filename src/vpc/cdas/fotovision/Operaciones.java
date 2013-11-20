package vpc.cdas.fotovision;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Operaciones {

	static final double NTSC_RED = 0.229;
	static final double NTSC_GREEN = 0.587;
	static final double NTSC_BLUE = 0.114;
	static final double PAL_RED = 0.222;
	static final double PAL_GREEN = 0.707;
	static final double PAL_BLUE = 0.071;
	static final int PIXELS = 256;

	/**
	 * Realiza una copia del objeto BufferedImage recibido
	 * @param imagen
	 * @return El objeto copia BufferedImage
	 */
	private static BufferedImage clona(final BufferedImage imagen) {

		BufferedImage copia = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());
		copia.setData(imagen.getData());

		return copia;
	}

	/**
	 * Genera una imagen a partir de la porición de la imagen origen. La porción la delimitan los dos puntos
	 * que recibe como parámetros.
	 * @param imagen - BufferedImage - Imagen origen.
	 * @param a - Punto - Primero punto del rectángulo que define la porción.
	 * @param b - Punto - Segundo punto del rectángulo que define la porción.
	 * @return BufferedImage - Subimagen generada a partir de la original.
	 */
	public static BufferedImage obtenerSubimagen(BufferedImage imagen, Punto a, Punto b) {

		int ax = a.getxPixel();
		int ay = a.getyPixel();
		int bx = b.getxPixel();
		int by = b.getyPixel();
		int difX = Math.abs(ax - bx);
		int difY = Math.abs(ay - by);
		int gris;
		int x2 = 0;
		int y2 = 0;

		BufferedImage nueva = new BufferedImage(difX, difY, imagen.getType());

		if (ax > bx) {
			ax -= difX;
			bx += difX;
		}

		if (ay > by) {
			ay -= difY;
			by += difY;
		}

		for (int x = ax; x < bx; x++) {
			y2 = 0;
			for (int y = ay; y < by; y++) {
				gris = new Color(imagen.getRGB(x, y)).getRed();
				nueva.setRGB(x2, y2, new Color(gris, gris, gris).getRGB());
				y2++;
			}
			x2++;
		}

		return nueva;
	}

	/**
	 * Crea una copia de la imagen recibida (BufferedImage) y le aplica filtro de escala de grises PAL.
	 * @param imagen - Objeto BufferedImage.
	 * @return Copia en escada de grises PAL.
	 */
	public static BufferedImage escalaDeGrisesPAL(final BufferedImage imagen) {

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

	/**
	 * Crea una copia de la imagen recibida (BufferedImage) y le aplica filtro de escala de grises NTCS.
	 * @param imagen - Objeto BufferedImage.
	 * @return Copia en escada de grises PAL.
	 */
	public static BufferedImage escalaDeGrisesNTSC(final BufferedImage imagen) {

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

	public static BufferedImage transformacionLinealB(final BufferedImage imagen, ArrayList<Coordenada> coordenadas) {

		double vout;
		Coordenada a, b;
		double m;

		// Construimos la lookuptable
		LookUpTable lut = new LookUpTable(0);
		for (int i = 0; i < coordenadas.size() - 1; i++) {
			a = coordenadas.get(i);
			b = coordenadas.get(i + 1);	
			m = (double) (b.getY() - a.getY()) / (double) (b.getX() - a.getX());

			for (int j = a.getX(); j <= b.getX(); j++) {

				if (m == 0) {
					vout = a.getY();
				}

				vout = m * j - m * a.getX() + a.getY();

				//System.out.println("j: " + j);

				lut.setValor(j, (int) vout);
			}	
		}

		BufferedImage copia = clona(imagen);

		for (int x = 0; x < copia.getWidth(); x++) {
			for (int y = 0; y < copia.getHeight(); y++) {
				Color color = new Color(copia.getRGB(x, y));
				int gris = lut.getValor(color.getRed());
				copia.setRGB(x, y, new Color(gris, gris, gris).getRGB());
			}
		}

		return copia;
	}
	/**
	 * Crea una copia de la imagen recibida (BufferedImage) y le aplica un transformación lineal por tramos.
	 * @param imagen - Objeto BufferedImage.
	 * @param tramos - Objeto Tramos que se utilizarán para la transformación.
	 * @return Copia con la tranformación aplicada.
	 */
	public static BufferedImage transformacionLineal(final BufferedImage imagen, Tramos tramos) {

		BufferedImage copia = clona(imagen);
		LookUpTable lut = new LookUpTable();

		//calculamos la look up table apartir de cada tramo
		for (int i = 1; i < tramos.getNumTramos(); i++) {
			//pedimos el punto del tramo actual
			int tramo[] = tramos.getTramo(i);
			//pedimos el punto del tramo anterior
			int tramo_anterior[] = tramos.getTramo(i - 1);
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
					lut.setValor(j, res);
				}
				//caso en el que la pendiente es cero
			} else if (((dividendo) == 0) && ((divisor) != 0)) {
				lut.setValor(tramo[1], tramo[0]);
				//caso en el que la pendiente es infinito
			} else if (((dividendo) != 0) && ((divisor) == 0)) {
				for (int j = tramo_anterior[1]; j < tramo[1]; j++) {
					lut.setValor(j, tramo[0]);
				}
			}
		}

		for (int x = 0; x < copia.getWidth(); x++) {
			for (int y = 0; y < copia.getHeight(); y++) {
				Color color = new Color(copia.getRGB(x, y));
				int Vin = color.getRed();
				int Vout = lut.getValor(Vin);
				copia.setRGB(x, y, new Color(Vout, Vout, Vout).getRGB());
			}
		}

		return copia;
	}

	/**
	 * Calcula el brillo (double) de la imagen (BufferedImage) recibida.
	 * @param imagen - Objeto BufferedImage.
	 * @return El brillo en formato double de la imagen.
	 */
	public static double getBrillo(final BufferedImage imagen) {
		double nu = 0;
		double size = imagen.getWidth() * imagen.getHeight();
		LookUpTable lut = calcularHistograma(imagen);

		for (int i = 0; i < lut.getSize(); i++) {
			double h = lut.getValor(i);
			nu = nu + (h * i);
		}
		nu = nu / size;

		return nu;
	}

	/**
	 * Calcula el contraste (double) de la imagen (BufferedImage) recibida.
	 * @param imagen - Objeto BufferedImage.
	 * @return El contraste en formato double de la imagen.
	 */
	public static double getContraste(final BufferedImage imagen) {
		double delta = 0;
		double nu = getBrillo(imagen);
		double size = imagen.getWidth() * imagen.getHeight();
		LookUpTable lut = calcularHistograma(imagen);

		for (int i = 0; i < lut.getSize(); i++) {
			double y = i;
			double h = lut.getValor(i);
			delta = delta + (h * Math.pow((y - nu), 2));
		}

		delta = delta / size;
		delta = Math.sqrt(delta);
		return delta;
	}

	/**
	 * Calcula el contraste (double) de la imagen (BufferedImage) recibida.
	 * @param imagen - Objeto BufferedImage.
	 * @return El contraste en formato double de la imagen.
	 */
	public static double getEntropia(final BufferedImage imagen) {
		double E = 0;
		double size = imagen.getWidth() * imagen.getHeight();
		LookUpTable lut = calcularHistograma(imagen);

		for (int i = 0; i < lut.getSize(); i++) {
			double h = lut.getValor(i);
			double p = h / size;
			if (p!= 0)
				E = E + (p * (Math.log10(p) / Math.log10(2)));
		}

		return E;
	}

	public static ArrayList<Double> histogramaNormalizado(final BufferedImage imagen) {

		ArrayList<Double> histograma = new ArrayList<Double>();
		double pixels = imagen.getWidth() * imagen.getHeight();
		LookUpTable lut = calcularHistograma(imagen);

		for (int i = 0; i < lut.getSize(); i++) {
			histograma.add((double) lut.getValor(i) / pixels);
		}

		return histograma;
	}

	/**
	 * Genera el histograma absoluto (ArrayList) de la imagen (BufferedImage) recibida.
	 * @param imagen - Objeto BufferedImage
	 * @return ArrayList con la frecuencia relativa de los niveles de grises.
	 */
	public static ArrayList<Integer> histogramaAbs(final BufferedImage imagen) {


		histogramaNormalizado(imagen);

		ArrayList<Integer> histograma = new ArrayList<Integer>();
		LookUpTable lut = calcularHistograma(imagen);

		for (int i = 0; i < lut.getSize(); i++)
			histograma.add(lut.getValor(i));

		return histograma;
	}

	/**
	 * Genera el histograma acumulado (ArrayList) de la imagen (BufferedImage) recibida.
	 * @param imagen - Objeto BufferedImage
	 * @return ArrayList con la frecuencia acumulada de los niveles de grises.
	 */
	public static ArrayList<Integer> histogramaAcu(final BufferedImage imagen) {

		ArrayList<Integer> histograma = histogramaAbs(imagen);

		for (int i = 1; i < histograma.size(); i++) {
			histograma.set(i, histograma.get(i - 1) + histograma.get(i));
		}

		return histograma;
	}

	/**
	 * Calcula el rango de valores de niveles de gris de la imagen (BufferedImage) recibida.
	 * @param imagen - Objeto BufferedImage
	 * @return ArrayList de tamaño 2. [0] => Limite inferior; [1] => Limite superior.
	 */
	public static ArrayList<Integer> rangoValores(final BufferedImage imagen) {

		ArrayList<Integer> histograma = histogramaAbs(imagen);

		int inferior = -1;
		int superior = -1;
		int index = 0;

		while (inferior == -1) {
			if (histograma.get(index++) > 0)
				inferior = index;
		}

		index = histograma.size() - 1;
		while (superior == -1) {
			if (histograma.get(index--) > 0)
				superior = index;
		}

		ArrayList<Integer> rango = new ArrayList<Integer>();		
		rango.add(inferior);
		rango.add(superior);

		return rango;
	}

	/**
	 * Esta función realiza un recuento de cada pixel dada una imagen.
	 * @param imagen - Objeto BufferedImage
	 * @return Se retorna un LookUpTable donde se guarda los contadores de cada pixel.
	 */
	public static LookUpTable calcularHistograma(BufferedImage imagen) {

		LookUpTable lut = new LookUpTable(0);	// Inicializamos una LookUpTable inicializada a 0

		for (int x = 0; x < imagen.getWidth(); x++) {
			for (int y = 0; y < imagen.getHeight(); y++) {
				Color color = new Color(imagen.getRGB(x, y));
				lut.incrementar(color.getRed());
			}
		}
		return lut;
	}

	/**
	 * Crea una copia de la imagen (BufferedImage) recibida aplicandole un cambio de brillo y contraste.
	 * @param imagen - Objeto BufferedImage
	 * @param contraste - Nivel de contraste que se aplicará
	 * @param brillo - Nivel de brillo que se aplicará
	 * @return Devuelve una imagen copia (BufferedImage) con el nuevo brillo y contraste aplicado.
	 */
	public static BufferedImage cambiarBrilloContraste(final BufferedImage imagen, double brillo, double contraste) {
		BufferedImage copia = clona(imagen);
		double brillo_actual = getBrillo(imagen);
		double contraste_actual = getContraste(imagen);
		double A = contraste / contraste_actual;
		double B = brillo - (A * brillo_actual);
		LookUpTable lut = new LookUpTable();

		for (double x = 0; x < lut.getSize(); x++) {
			double Vout = A * x + B;
			if (Vout < 0) {
				Vout = 0;
			} else if (Vout > 255) {
				Vout = 255;
			}

			int valor = (int) Math.round(Vout);
			lut.setValor((int) x, valor);
		}

		for (int x = 0; x < copia.getWidth(); x++) {
			for (int y = 0; y < copia.getHeight(); y++) {
				Color color = new Color(copia.getRGB(x, y));
				int Vin = color.getRed();
				int Vout = lut.getValor(Vin);
				copia.setRGB(x, y, new Color(Vout, Vout, Vout).getRGB());
			}
		}

		return copia;
	}

	/**
	 * Se comparan la dos imagenes especificadas por parametro y se marcará en rojo las zonas que
	 * no coinciden entre ambas. El usuario especificará un valor que sirve como umbral, dicho valor
	 * estará en el rango [0 - 255] los valores más cercanos a 0 del umbral serán más sensibles
	 * a las diferencias y los valores más cercanos a 255 serán menos sensibles a las diferencias.
	 * @param imagen1 - BufferedImage.
	 * @param imagen2 - BufferedImage.
	 * @param T - int - Umbral de diferencias.
	 * @return Retornará una imagen (BufferedImage) en la cual las zonas rojas representan las diferencias entre las imágenes.
	 */
	public static BufferedImage compararImagenes(BufferedImage imagen1, BufferedImage imagen2, int t) {
		BufferedImage copia = clona(imagen1);

		for (int x = 0; x < copia.getWidth(); x++) {
			for (int y = 0; y < copia.getHeight(); y++) {
				Color color1 = new Color(imagen1.getRGB(x, y));
				Color color2 = new Color(imagen2.getRGB(x, y));
				//sacamos el valor de gris de ambas imagenes
				int color_imagen1 = color1.getRed();
				int color_imagen2 = color2.getRed();

				//comprobamos si el valor generado sobrepasa el umbral especificado por el usuario
				if (Math.abs(color_imagen1 - color_imagen2) > t) {
					//ponemos a rojo los pixeles que se consideen que han cambiado
					copia.setRGB(x, y, new Color(255, 0, 0).getRGB());
				}
			}
		}

		return copia;
	}

	/**
	 * Está función aplica la fórmula de l ecualización a la imágen que recibe por parámetro. La función
	 * de ecualizado intenta lograr que exista la misma cantidad de pixels para todos los diferentes
	 * niveles de gris que existen en la imágen, aunque sea imposible en la práctica es lo más cercano
	 * que se puede lograr.
	 * @param imagen La imágen a la que se le desea aplicar la ecualización.
	 * @return Retornará la imágen con la ecualización aplicada.
	 */
	public static BufferedImage ecualizacion(BufferedImage imagen) {
		BufferedImage copia = clona(imagen);
		double size = imagen.getWidth() * imagen.getHeight();
		LookUpTable lut = calcularHistograma(imagen);
		double m = lut.getSize();
		//partimos la variedad de pixeles por la cantidad de pixeles
		double var = m / size;

		//creamos la look up table para el valor de salida
		LookUpTable lut_vout = new LookUpTable();
		for(int i = 0; i < lut.getSize(); i++) {
			//realizamos el calculo de Vout segu la formula max[0, round(M/size * C0(Vin)) - 1]
			int Vout = (int) Math.round((var * (double) lut.sumatorio(i)) - 1);
			if (Vout < 0) {
				Vout = 0;
			}
			lut_vout.setValor(i, Vout);
		}
		//actualizamos los valores de la nueva imagen segun la Look Up Table calculada
		for (int x = 0; x < copia.getWidth(); x++) {
			for (int y = 0; y < copia.getHeight(); y++) {
				Color color = new Color(copia.getRGB(x, y));
				int Vin = color.getRed();
				int Vout = lut_vout.getValor(Vin);
				copia.setRGB(x, y, new Color(Vout, Vout, Vout).getRGB());
			}
		}
		return copia;
	}

	public static BufferedImage especificacion(BufferedImage original, BufferedImage referencia) {
		
		BufferedImage copia = clona(original);
		ArrayList<Double> pOrigin = histogramaNormalizado(original);
		ArrayList<Double> pRefer = histogramaNormalizado(referencia);
		int[] T = new int[PIXELS];
		int j;
		int M = PIXELS;
		
		for (int a = 0; a < M; a++) {
			j = M - 1;
			do {
				T[a] = j;
				j--;

			} while(j >= 0 && pOrigin.get(a) <= pRefer.get(j));
		}
		
		for (int x = 0; x < copia.getWidth(); x++) {
			for (int y = 0; y < copia.getHeight(); y++) {
				Color color = new Color(original.getRGB(x, y));
				int Vin = color.getRed();
				int Vout = T[Vin];
				
				copia.setRGB(x, y, new Color(Vout, Vout, Vout).getRGB());
			}
		}
		return copia;
	}

	public static BufferedImage imagenDiferencia(BufferedImage imagen1, BufferedImage imagen2) {

		BufferedImage imagenDiferencia = new BufferedImage(imagen1.getWidth(), imagen1.getHeight(), imagen1.getType());

		for (int x = 0; x < imagenDiferencia.getWidth(); x++) {
			for (int y = 0; y < imagenDiferencia.getHeight(); y++) {

				Color color1 = new Color(imagen1.getRGB(x, y));
				Color color2 = new Color(imagen2.getRGB(x, y));

				int color = Math.abs(color1.getRed() - color2.getRed());

				imagenDiferencia.setRGB(x, y, new Color(color, color, color).getRGB());
			}
		}

		return imagenDiferencia;
	}

	/**
	 * La corrección gamma modifica el brillo y contraste de la imagen que recibe por parámetro, en
	 * función del parámetro gamma. Cuando el parametro será mayor que uno aumentará el brillo de la
	 * imágen, y cuando será menor que uno y mayor que cero disminuirá el brillo de la imágen.
	 * @param imagen La imagen a la que se le quiere aplicar la corrección gamma.
	 * @param parametro El parametro para la exponencial.
	 * @return Retona la imágen con la corección gamma aplicada.
	 */
	public static BufferedImage correcionGamma(BufferedImage imagen, double parametro) {
		BufferedImage copia = clona(imagen);
		//repasamos cada uno de los pixeles de la imagen le aplicamos la formula de la correccion
		//gama y lo almacenamos en el nueva imagen
		for (int x = 0; x < copia.getWidth(); x++) {
			for (int y = 0; y < copia.getHeight(); y++) {
				Color color = new Color(imagen.getRGB(x, y));
				double Vin = color.getRed();
				double a = Vin / 255;
				double b = 0;
				b = Math.pow(a, 1 / parametro);
				int Vout = (int) Math.round(b * 255);
				copia.setRGB(x, y, new Color(Vout, Vout, Vout).getRGB());
			}
		}
		return copia;
	}
}
