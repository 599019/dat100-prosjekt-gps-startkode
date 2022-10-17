package no.hvl.dat100ptc.oppgave4;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;

public class GPSComputer {
	
	private GPSPoint[] gpspoints;
	
	public GPSComputer(String filename) {

		GPSData gpsdata = GPSDataFileReader.readGPSFile(filename);
		gpspoints = gpsdata.getGPSPoints();

	}

	public GPSComputer(GPSPoint[] gpspoints) {
		this.gpspoints = gpspoints;
	}
	
	public GPSPoint[] getGPSPoints() {
		return this.gpspoints;
	}
	
	// beregn total distances (i meter)
	public double totalDistance() {

		double distance = 0;

		for (int i=0; i< gpspoints.length-1;i++){
			distance += GPSUtils.distance(gpspoints[i], gpspoints[i+1]);
		}

		return distance;
	}

	// beregn totale høydemeter (i meter)
	public double totalElevation() {

		double elevation = 0;

		for (int i = 0; i < gpspoints.length - 1; i++) {
			double tmp = (gpspoints[i + 1].getElevation() - gpspoints[i].getElevation());
			elevation += (tmp > 0) ? tmp : 0;
		}

		return elevation;
	}

	// beregn total tiden for hele turen (i sekunder)
	public int totalTime() {
		int time=gpspoints[gpspoints.length - 1].getTime() - gpspoints[0].getTime();

		return time;
	}
		
	// beregn gjennomsnitshastighets mellom hver av gps punktene

	public double[] speeds() {
		double[] speeds = new double[gpspoints.length - 1];

		for (int i = 0; i < gpspoints.length - 1; i++) {
			speeds[i] = GPSUtils.speed(gpspoints[i], gpspoints[i + 1]);
		}

		return speeds;
	}
	
	public double maxSpeed() {
		double maxspeed = GPSUtils.findMax(speeds());

		return maxspeed;
	}

	public double averageSpeed() {
		double average = totalDistance() * 3.6 / totalTime();;
		
		return average;
	}

	/*
	 * bicycling, <10 mph, leisure, to work or for pleasure 4.0 bicycling,
	 * general 8.0 bicycling, 10-11.9 mph, leisure, slow, light effort 6.0
	 * bicycling, 12-13.9 mph, leisure, moderate effort 8.0 bicycling, 14-15.9
	 * mph, racing or leisure, fast, vigorous effort 10.0 bicycling, 16-19 mph,
	 * racing/not drafting or >19 mph drafting, very fast, racing general 12.0
	 * bicycling, >20 mph, racing, not drafting 16.0
	 */

	// conversion factor m/s to miles per hour
	public static double MS = 2.236936;

	// beregn kcal gitt weight og tid der kjøres med en gitt hastighet
	public double kcal(double weight, int secs, double speed) {

		double kcal;

		// MET: Metabolic equivalent of task angir (kcal x kg-1 x h-1)
		double[][] mets = { { 0, 4.0 }, { 10, 6.0 }, { 12, 8.0 }, { 14, 10.0 }, { 16, 12.0 }, { 20, 16.0 }, { Double.MAX_VALUE } };
		double met = 0;
		double speedmph = speed * MS;


		for (int i = 0; i < mets.length - 1; i++) {
			if (speedmph > mets[i][0] && speedmph < mets[i + 1][0]) {
				met = mets[i][1];
			}
		}
		kcal = met * weight * ((double) secs / 3600);
		// System.out.println("Result: " + res + " Weight: " + weight + " Secs: " + secs
		// + " Speed: " + speed + " MET: " + met);
		return kcal;
	}

	public double totalKcal(double weight) {

		double totalkcal = 0;

		for (int i = 0; i < gpspoints.length - 1; i++) {
			double speed = GPSUtils.speed(gpspoints[i], gpspoints[i + 1]);
			int secs = gpspoints[i + 1].getTime() - gpspoints[i].getTime();
			totalkcal += kcal(weight, secs, speed);
		}

		return totalkcal;
	}
	
	private static double WEIGHT = 80.0;
	
	public void displayStatistics() {

		String outstr = "";

		outstr += "==============================================\n";
		outstr += String.format("%-15s: %10s %n", "Total time", GPSUtils.formatTime(totalTime()));
		outstr += String.format("%-15s: %10.2f %-4s %n", "Total distance", totalDistance() / 1000, "km");
		outstr += String.format("%-15s: %10.2f %-4s %n", "Total elevation", totalElevation(), "m");
		outstr += String.format("%-15s: %10.2f %-4s %n", "Max speed", maxSpeed(), "km/t");
		outstr += String.format("%-15s: %10.2f %-4s %n", "Average speed", averageSpeed(), "km/t");
		outstr += String.format("%-15s: %10.2f %-4s %n", "Energy", totalKcal(WEIGHT), "kcal");

		System.out.print(outstr);
	}

}
