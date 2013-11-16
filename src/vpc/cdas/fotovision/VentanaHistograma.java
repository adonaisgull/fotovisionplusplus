package vpc.cdas.fotovision;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class VentanaHistograma extends VentanaSecundaria {

	private static final long serialVersionUID = 1L;
	public static final String ABSOLUTO = "Absoluto";
	public static final String ACUMULADO = "Acumulado";
	private static final int ANCHO = 600;
	private static final int ALTO = 400;

	public VentanaHistograma(VentanaImagen padre, ArrayList<Integer> histograma, String tipo) {
		super(padre, "Imagen " + (padre.getId() + 1) + " - " + tipo, ANCHO, ALTO);
		
		double [] datos = new double [histograma.size()];		

		for (int i = 0; i < histograma.size(); i++)
			datos[i] = histograma.get(i);

		final XYSeries serie = new XYSeries("");
		for (int i = 0; i < histograma.size(); i++)
			serie.add(i, histograma.get(i));

		final XYSeriesCollection collection = new XYSeriesCollection();
		collection.addSeries(serie);

		final JFreeChart chart = ChartFactory.createXYLineChart("Histograma " + tipo, "Nivel de gris", "Número de pixels", 
				collection,
				PlotOrientation.VERTICAL, 
				true, 	// uso de leyenda
				false, 	// uso de tooltips  
				false 	// uso de urls
				);

		final XYPlot plot = (XYPlot) chart.getPlot();
		plot.setDomainGridlinePaint(Color.black);
		plot.setRangeGridlinePaint(Color.black);

		final NumberAxis domainAxis = (NumberAxis)plot.getDomainAxis();
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		domainAxis.setTickUnit(new NumberTickUnit(10));

		JPanel panel = new ChartPanel(chart);
		getContentPane().add(panel);
	}
}
