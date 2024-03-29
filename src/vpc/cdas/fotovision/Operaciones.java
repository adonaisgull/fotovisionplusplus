package vpc.cdas.fotovision;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Operaciones {

	private static final double NTSC_RED = 0.229;
	private static final double NTSC_GREEN = 0.587;
	private static final double NTSC_BLUE = 0.114;
	private static final double PAL_RED = 0.222;
	private static final double PAL_GREEN = 0.707;
	private static final double PAL_BLUE = 0.071;
	private static final int PIXELS = 256;
	public static final int SH = 1;
	public static final int SAH = 2;
	public static final int SH_90 = 1;
	public static final int SAH_90 = 2;
	public static final int SH_180 = 3;
	public static final int SH_270 = 4;
	public static final int SAH_270 = 5;
	public static final int INT_VMP = 1;
	public static final int INT_BL = 2;

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
	 * Genera una imagen a partir de la porici�n de la imagen origen. La porci�n la delimitan los dos puntos
	 * que recibe como par�metros.
	 * @param imagen - BufferedImage - Imagen origen.
	 * @param a - Punto - Primero punto del rect�ngulo que define la porci�n.
	 * @param b - Punto - Segundo punto del rect�ngulo que define la porci�n.
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
	
	public static BufferedImage voltearHorizontal(final BufferedImage imagen) {
		
		BufferedImage volteada = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());
		
		int xv = imagen.getWidth() - 1;
		for (int x = 0; x < imagen.getWidth(); x++){
			for (int y = 0; y < imagen.getHeight(); y++) {
				volteada.setRGB(xv, y, imagen.getRGB(x, y));				
			}
			xv--;
		}
		
		return volteada;
	}

	public static BufferedImage voltearVertical(final BufferedImage imagen) {
		
		BufferedImage volteada = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());
		
		int yv = imagen.getHeight() - 1;
		for (int y = 0; y < imagen.getHeight(); y++) {
			for (int x = 0; x < imagen.getWidth(); x++){
				volteada.setRGB(x, yv, imagen.getRGB(x, y));				
			}
			yv--;
		}
		
		return volteada;
	}
	
	public static BufferedImage imagenTraspuesta(final BufferedImage imagen) {
		
		BufferedImage traspuesta = new BufferedImage(imagen.getHeight(), imagen.getWidth(), imagen.getType());
		
		for (int x = 0; x < imagen.getWidth(); x++) {
			for (int y = 0; y < imagen.getHeight(); y++){
				traspuesta.setRGB(y, x, imagen.getRGB(x, y));				
			}
		}
		
		return traspuesta;
	}
	
	public static BufferedImage rotar90(final BufferedImage imagen, int sentido) {
		
		BufferedImage rotada = new BufferedImage(imagen.getHeight(), imagen.getWidth(), imagen.getType());
		
		
		if (sentido == SH) {
			int yr = imagen.getHeight() - 1;
			for (int y = 0; y < imagen.getHeight(); y++){
				for (int x = 0; x < imagen.getWidth(); x++) {
					rotada.setRGB(yr, x, imagen.getRGB(x, y));				
				}
				yr--;
			}
		}
		else {
			for (int y = 0; y < imagen.getHeight(); y++){
				for (int x = 0; x < imagen.getWidth(); x++) {
					rotada.setRGB(y, x, imagen.getRGB(x, y));				
				}
			}
		}
		
		return rotada;
		
	}
	
	public static BufferedImage rotarImagen(final BufferedImage imagen, final int angulo) {
		
		BufferedImage rotada = null;
		
		switch (angulo) {
		case SH_90:
			rotada = rotar90(imagen, SH);
			break;
		case SAH_90:
			rotada = rotar90(imagen, SAH);
			break;
		case SH_180:
			rotada = rotar90(rotar90(imagen, SH), SH);
			break;
		case SH_270:
			rotada = rotar90(rotar90(rotar90(imagen, SH), SH), SH);
			break;
		case SAH_270:
			rotada = rotar90(rotar90(rotar90(imagen, SAH), SAH), SAH);
			break;
		}
		
		return rotada;
	}
	
	public static BufferedImage transformacionLineal(final BufferedImage imagen, ArrayList<Coordenada> coordenadas) {

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
		ArrayList<Integer> histogramaInt = histogramaAcu(imagen);

		for (int i = 0; i < histogramaInt.size(); i++) {
			histograma.add((double) histogramaInt.get(i) / pixels);
		}

		return histograma;
	}

	/**
	 * Genera el histograma absoluto (ArrayList) de la imagen (BufferedImage) recibida.
	 * @param imagen - Objeto BufferedImage
	 * @return ArrayList con la frecuencia relativa de los niveles de grises.
	 */
	public static ArrayList<Integer> histogramaAbs(final BufferedImage imagen) {

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
	 * @return ArrayList de tama�o 2. [0] => Limite inferior; [1] => Limite superior.
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
	 * Esta funci�n realiza un recuento de cada pixel dada una imagen.
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
	 * @param contraste - Nivel de contraste que se aplicar�
	 * @param brillo - Nivel de brillo que se aplicar�
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
	 * Se comparan la dos imagenes especificadas por parametro y se marcar� en rojo las zonas que
	 * no coinciden entre ambas. El usuario especificar� un valor que sirve como umbral, dicho valor
	 * estar� en el rango [0 - 255] los valores m�s cercanos a 0 del umbral ser�n m�s sensibles
	 * a las diferencias y los valores m�s cercanos a 255 ser�n menos sensibles a las diferencias.
	 * @param imagen1 - BufferedImage.
	 * @param imagen2 - BufferedImage.
	 * @param T - int - Umbral de diferencias.
	 * @return Retornar� una imagen (BufferedImage) en la cual las zonas rojas representan las diferencias entre las im�genes.
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

	/**
	 * Modifica la distribucion de grises de la imagen original a partir de la imagen referencia
	 * @param original
	 * @param referencia
	 * @return
	 */
	public static BufferedImage especificacion(BufferedImage original, BufferedImage referencia) {
		
		BufferedImage copia = clona(original);
		ArrayList<Double> pOrigin = histogramaNormalizado(original);
		ArrayList<Double> pRefer = histogramaNormalizado(referencia);
		
		LookUpTable lut = new LookUpTable(0);
		
		int j;
		for (int a = 0; a < lut.getSize(); a++) {
			j = lut.getSize() - 1;
			do {
				lut.setValor(a, j);
				j--;
				
			} while(j >= 0 && pOrigin.get(a).doubleValue() <= pRefer.get(j).doubleValue());
			
		}
		
		for (int x = 0; x < copia.getWidth(); x++) {
			for (int y = 0; y < copia.getHeight(); y++) {
				Color color = new Color(original.getRGB(x, y));
				int gris = lut.getValor(color.getRed());
				copia.setRGB(x, y, new Color(gris, gris, gris).getRGB());
			}
		}
		return copia;
	}

	/**
	 * Calcula la imagen diferencia entre la imagen1 y la imagen2
	 * @param imagen1
	 * @param imagen2
	 * @return
	 */
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
	 * La correcci�n gamma modifica el brillo y contraste de la imagen que recibe por par�metro, en
	 * funci�n del par�metro gamma. Cuando el parametro ser� mayor que uno aumentar� el brillo de la
	 * im�gen, y cuando ser� menor que uno y mayor que cero disminuir� el brillo de la im�gen.
	 * @param imagen La imagen a la que se le quiere aplicar la correcci�n gamma.
	 * @param parametro El parametro para la exponencial.
	 * @return Retona la im�gen con la corecci�n gamma aplicada.
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
	
	
	public static int interpolacionVMP(BufferedImage imagen, double x, double y) {
		
		// Calculamos las cuatro coordenadas enteras envolventes
		ArrayList<Coordenada> envolventes = new ArrayList<Coordenada>();
		envolventes.add(new Coordenada((int) x, (int) y));
		envolventes.add(new Coordenada((int) x + 1, (int) y));
		envolventes.add(new Coordenada((int) x, (int) y + 1));
		envolventes.add(new Coordenada((int) x + 1, (int) y + 1));
		
		ArrayList<Double> distancias = new ArrayList<Double>();
		for (int i = 0; i < envolventes.size(); i++) {
			distancias.add(Math.sqrt(Math.pow((double) envolventes.get(i).getX() - x, 2) + Math.pow((double) envolventes.get(i).getY() - y, 2)));			
		}
		
		double min = distancias.get(0);
		int j = 0;
		for (int i = 1; i < distancias.size(); i++)
			if (distancias.get(i) < min) {
				min = distancias.get(i);
				j = i;
			}
		
		Color color = new Color(imagen.getRGB(envolventes.get(j).getX(), envolventes.get(j).getY()));
		
		return color.getRed();
	}
	
	public static int interpolacionBL(BufferedImage imagen, double x, double y) {
		
		// Calculamos las cuatro coordenadas enteras envolventes
		Coordenada a = new Coordenada((int) x, (int) y);
		Coordenada b = new Coordenada((int) x + 1, (int) y);
		Coordenada c = new Coordenada((int) x, (int) y + 1);
		Coordenada d = new Coordenada((int) x + 1, (int) y + 1);
		
		if (b.getX() >= imagen.getWidth()) b.setX(imagen.getWidth() - 1);
		if (c.getY() >= imagen.getHeight()) c.setY(imagen.getHeight() - 1);
		if (d.getX() >= imagen.getWidth()) d.setX(imagen.getWidth() - 1);
		if (d.getY() >= imagen.getHeight()) d.setY(imagen.getHeight() - 1);
		
		double q = Math.abs((double) c.getY() - y);
		double p = Math.abs((double) x - c.getX());
		
		int grisA = new Color(imagen.getRGB(a.getX(), a.getY())).getRed();
		int grisB = new Color(imagen.getRGB(b.getX(), b.getY())).getRed();
		int grisC = new Color(imagen.getRGB(c.getX(), c.getY())).getRed();
		int grisD = new Color(imagen.getRGB(d.getX(), d.getY())).getRed();
		
		double gris = grisC + (grisD - grisC) * p + (grisA - grisC) * q + (grisB + grisC - grisA - grisD) * p * q;
		
		return (int) Math.round(gris);
	}
	
	/**
	 * 
	 * @param imagen
	 * @param ancho
	 * @param alto
	 * @param interpolacion
	 * @return
	 */
	public static BufferedImage escalar(BufferedImage imagen, int ancho, int alto, int interpolacion) {
		
		BufferedImage nueva = new BufferedImage(ancho, alto, imagen.getType());
		double factorEscaladoX = (double) imagen.getWidth() / (double) nueva.getWidth();
		double factorEscaladoY = (double) imagen.getHeight() / (double) nueva.getHeight();
		
		if (interpolacion == INT_VMP) {
			for (int x = 0; x < nueva.getWidth(); x++) {
				for (int y = 0; y < nueva.getHeight(); y++) {
					int gris = interpolacionVMP(imagen, (double) x * factorEscaladoX, (double) y * factorEscaladoY);
					nueva.setRGB(x, y, new Color(gris, gris, gris).getRGB());
				}
			}
		}
		
		if (interpolacion == INT_BL) {
			for (int x = 0; x < nueva.getWidth(); x++) {
				for (int y = 0; y < nueva.getHeight(); y++) {
					int gris = interpolacionBL(imagen, (double) x * factorEscaladoX, (double) y * factorEscaladoY);
					nueva.setRGB(x, y, new Color(gris, gris, gris).getRGB());
				}
			}
		}
		
		return nueva;
	}
}
