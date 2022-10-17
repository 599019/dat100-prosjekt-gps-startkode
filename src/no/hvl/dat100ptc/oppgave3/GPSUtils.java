package no.hvl.dat100ptc.oppgave3;

import static java.lang.Math.*;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class GPSUtils {

	public static double findMax(double[] da) {

		double max; 
		
		max = da[0];
		
		for (double d : da) {
			if (d > max) {
				max = d;
			}
		}
		
		return max;
	}

	public static double findMin(double[] da) {

		double min;

		min = da[0];

		for (double d : da) {
			if (d < min) {
				min = d;
			}
		}
		return min;
	}

	public static double[] getLatitudes(GPSPoint[] gpspoints) {
		double[] latitudes = new double[gpspoints.length];
		for (int i=0; i < gpspoints.length; i++){
			latitudes[i]=gpspoints[i].getLatitude();
		}
		return latitudes;
	}

	public static double[] getLongitudes(GPSPoint[] gpspoints) {
		double[] longitude = new double[gpspoints.length];
		for (int i=0; i < gpspoints.length; i++){
			longitude[i]=gpspoints[i].getLongitude();
		}
		return longitude;
	}

	private static int R = 6371000; // jordens radius

	public static double distance(GPSPoint gpspoint1, GPSPoint gpspoint2) {
		double d;
		double latitude1, longitude1, latitude2, longitude2;

		latitude1= gpspoint1.getLatitude();
		longitude1= gpspoint1.getLongitude();
		latitude2= gpspoint2.getLatitude();
		longitude2= gpspoint2.getLongitude();

		double q1=toRadians(latitude1);
		double q2=toRadians(latitude2);
		double t1=q2-q1;
		double t2=toRadians(longitude2)-toRadians(longitude1);
		double a=(pow(sin(t1/2),2))+(cos(q1)*cos(q2)*(pow(sin(t2/2),2)));
		double c=2*atan2(sqrt(a),sqrt(1-a));
		d=R*c;
		return d;
	}

	public static double speed(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		int secs;
		double speed;

		double distance = distance(gpspoint1,gpspoint2);
		secs= gpspoint2.getTime()- gpspoint1.getTime();

		speed = (distance/secs)*3.6;
		return speed;
	}

	public static String formatTime(int secs) {

		String timestr;
		String TIMESEP = ":";

		int hh= secs/3600;
		int mm= secs % 3600 / 60;
		int ss= secs%60;

		timestr=("  "+String.format("%02d", hh)+TIMESEP+String.format("%02d", mm)+TIMESEP+String.format("%02d", ss));
		return timestr;
	}
	private static int TEXTWIDTH = 10;

	public static String formatDouble(double d) {

		String str;

		str=(String.format("%10.2f", d));
		return str;
	}
}
