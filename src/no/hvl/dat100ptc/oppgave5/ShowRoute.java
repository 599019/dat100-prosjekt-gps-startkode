package no.hvl.dat100ptc.oppgave5;

import javax.swing.JOptionPane;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class ShowRoute extends EasyGraphics {

	private static int MARGIN = 50;
	private static int MAPXSIZE = 800;
	private static int MAPYSIZE = 800;

	private GPSPoint[] gpspoints;
	private GPSComputer gpscomputer;
	
	public ShowRoute() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		makeWindow("Route", MAPXSIZE + 2 * MARGIN, MAPYSIZE + 2 * MARGIN);

		showRouteMap(MARGIN + MAPYSIZE);
		
		showStatistics();
	}

	// antall x-pixels per lengdegrad
	public double xstep() {

		double maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));

		double xstep = MAPXSIZE / (Math.abs(maxlon - minlon)); 

		return xstep;
	}

	// antall y-pixels per breddegrad
	public double ystep() {

		double maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		double ystep = MAPYSIZE / (Math.abs(maxlat - minlat));

		return ystep;
	}

	public void showRouteMap(int ybase) {

		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		int r = 5;

		setColor(0, 255, 0);
		for (int i = 0; i < gpspoints.length; i++) {
			int x1 = (int) (xstep() * (gpspoints[i].getLongitude() - minlon));
			int y1 = (int) (ystep() * (gpspoints[i].getLatitude() - minlat));

			int x2;
			int y2;

			if (i < gpspoints.length-1) {
				x2 = (int) (xstep() * (gpspoints[i + 1].getLongitude() - minlon));
				y2 = (int) (ystep() * (gpspoints[i + 1].getLatitude() - minlat));
			} else {
				x2 = (int) (xstep() * (gpspoints[i].getLongitude() - minlon));
				y2 = (int) (ystep() * (gpspoints[i].getLatitude() - minlat));
			}



			drawLine(MARGIN + x1, ybase - y1, MARGIN + x2, ybase - y2);
			fillCircle(MARGIN + x1, ybase - y1, r);
		}

	}

	public void showStatistics() {

		int TEXTDISTANCE = 20;
		setColor(0, 0, 0);
		setFont("Courier", 12);

		String[] outstr = new String[6];
		outstr[0] = String.format("%-15s: %8s %n", "Total time", GPSUtils.formatTime(gpscomputer.totalTime()));
		outstr[1] = String.format("%-15s: %8.2f %-4s %n", "Total distance", gpscomputer.totalDistance() / 1000, "km");
		outstr[2] = String.format("%-15s: %8.2f %-4s %n", "Total elecation", gpscomputer.totalElevation(), "m");
		outstr[3] = String.format("%-15s: %8.2f %-4s %n", "Max speed", gpscomputer.maxSpeed(), "km/t");
		outstr[4] = String.format("%-15s: %8.2f %-4s %n", "Average speed", gpscomputer.averageSpeed(), "km/t");
		outstr[5] = String.format("%-15s: %8.2f %-4s %n", "Energy", gpscomputer.totalKcal(80), "kcal");

		for (int i = 0; i < outstr.length; i++) {
			drawString(outstr[i], MARGIN, 50 + TEXTDISTANCE * i);
		}
	}
}
