package no.hvl.dat100ptc.oppgave2;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import org.w3c.dom.DOMImplementation;

public class GPSData {

	private GPSPoint[] gpspoints;
	protected int antall = 0;


	public GPSData(int n) {
		gpspoints= new GPSPoint[n];
	}

	public GPSPoint[] getGPSPoints() {
		return this.gpspoints;
	}
	
	protected boolean insertGPS(GPSPoint gpspoint) {
		if (antall < gpspoints.length){
			gpspoints[antall] = gpspoint;
			antall++;
			return true;
		}
		return false;
	}

	public boolean insert(String time, String latitude, String longitude, String elevation) {
		GPSPoint gpspoint;
		int timeInt = GPSDataConverter.toSeconds(time);
		double latitudeDouble = Double.parseDouble(latitude);
		double longitudeDouble = Double.parseDouble(longitude);
		double elevationDouble = Double.parseDouble(elevation);
		gpspoint = new GPSPoint(timeInt,latitudeDouble,longitudeDouble,elevationDouble);

		if(insertGPS(gpspoint)){
			return true;
		}
		return false;
	}

	public void print() {

		System.out.println("====== Konvertert GPS Data - START ======");
		for (GPSPoint gpsPoint : gpspoints){
			System.out.print(gpsPoint.toString());
		}

		
		System.out.println("====== Konvertert GPS Data - SLUTT ======");

	}
}
