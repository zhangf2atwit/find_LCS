package edu.wit.cs.comp2350.tests;

import java.awt.Color;
import java.util.Random;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import edu.wit.cs.comp2350.A7;

public class ChartMaker extends ApplicationFrame {
	private static final long serialVersionUID = 1L;
	private static final int DATASETS = 2;

	/**
	 *
	 * @param title  the frame title.
	 */
	public ChartMaker(String title) {
		super(title);
		JPanel chartPanel = createDemoPanel();
		chartPanel.setPreferredSize(new java.awt.Dimension(850, 500));
		setContentPane(chartPanel);
	}

	/**
	 * Creates a chart with two datasets of algorithm runtimes and
	 * sets appearance of chart objects.
	 *
	 * @return A chart instance.
	 */
	private static JFreeChart createChart() {
		XYDataset[] data = new XYDataset[DATASETS];

		data[0] = createDataset("dfs", "depth-first search");
		data[1] = createDataset("dyn", "dynamic programming");


		JFreeChart chart = ChartFactory.createXYLineChart(
				"DNA alignment runtimes",  // chart title
				"number of basepairs",     // x axis label
				"time(ms)",                // y axis label
				data[0],                   // data
				PlotOrientation.VERTICAL, 
				true,                      // include legend
				true,                      // tooltips
				false                      // urls
				);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.getDomainAxis().setLowerMargin(0.0);
		plot.getDomainAxis().setUpperMargin(0.0);
		XYLineAndShapeRenderer renderer[] = new XYLineAndShapeRenderer[DATASETS];

		for (int i = 0; i < DATASETS; i++) {
			plot.setDataset(i, data[i]);
			renderer[i] = new XYLineAndShapeRenderer(); 
			plot.setRenderer(i, renderer[i]);
		}

		plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red); 
		plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(0, Color.blue);

		return chart;
	}

	/**
	 * Creates a dataset based on algorithm runtimes.
	 *
	 * @param algo  The string used to represent algorithm
	 * @param name  The name of algorithm to appear in the legend
	 * 
	 * @return A sample dataset.
	 */
	public static XYDataset createDataset(String algo, String name) {
		XYDataset result = DatasetUtils.sampleFunction2D(new AddRuns(algo),
				7.0, 14.0, 8, name);
		return result;
	}

	/**
	 * Creates a panel for the demo (used by SuperDemo.java).
	 *
	 * @return A panel.
	 */
	public static JPanel createDemoPanel() {
		JFreeChart chart = createChart();
		return new ChartPanel(chart);
	}

	/**
	 * Generates all the runtimes for the graph.
	 */
	static class AddRuns implements Function2D {
		private String algo;

		public AddRuns(String a) { algo = a; }

		// t is the number of characters in each string
		public double getValue(double t) {
			double sec = 0;

			if (t == 0)
				return 0;

			String text1 = randDNA(Double.valueOf(t).intValue());
			String text2 = randDNA(Double.valueOf(t).intValue());

			long start = System.nanoTime();

			switch (algo) {
			case "dfs":
				A7.findLCSdfs(text1, text2);
				break;
			case "dyn":
				A7.findLCSdyn(text1, text2);
				break;
			default:
				System.out.println("Invalid algorithm");
				System.exit(0);
				break;
			}

			long end = System.nanoTime();

			sec += (end-start)/1E6;

			return sec;
		}

		private String randDNA(int intValue) {
			String alphabet = "TCGA";
			int len = alphabet.length();
			String ret = "";
			Random r = new Random();

			for (int i = 0; i < intValue; i++) {
				ret += alphabet.charAt(r.nextInt(len));
			}
			return ret;
		}

	}

	/**
	 * Starting point for the chartmaker.
	 *
	 * @param args  ignored.
	 */
	public static void main(String[] args) {
		ChartMaker cm = new ChartMaker(
				"Chart Window");
		cm.pack();
		RefineryUtilities.centerFrameOnScreen(cm);
		cm.setVisible(true);
	}

}
