package fr.utbm.tr54.autonomousIntersection.sensor;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;

/**
 * Manage sensor of robot
 * @author Achille
 */
public class Sensor {
	private EV3UltrasonicSensor distanceSensor;
	private EV3ColorSensor colorSensor;
	
	/**
	 * default constructor, it initializes distance sensor and color sensor
	 */
	public Sensor() {
		this.distanceSensor = new EV3UltrasonicSensor(SensorPort.S3);
		this.colorSensor  = new EV3ColorSensor(SensorPort.S2);
	}

	/**
	 * @return color on top off color sensor
	 */
	public int getCouleurSurface() {
		int a;
		a = this.colorSensor.getColorID();
		return a;
	}

	/**
	 * compute distance between robot and an obstacle
	 * @return distance between an obstacle and robot
	 */
	public float distance() {
		float[] dist = new float[1];
		this.distanceSensor.getDistanceMode().fetchSample(dist, 0);
		return dist[0];
	}

	/**
	 * @param n number of sample use to compute de distance
	 * @return distance between an obstacle and robot
	 */
	public float distance(int n) {
		float[] dist = new float[n];
		float distMoy = 0;
		for (int i = 0; i < n; i++) {
			this.distanceSensor.getDistanceMode().fetchSample(dist, i);
		}
		// calcul de la moyenne
		for (int i = 0; i < n; i++) {
			distMoy = distMoy + dist[i] / n;
		}
		return distMoy;
	}

	/**
	 * Close all sensors opened on robot 
	 */
	public void close() {
		this.distanceSensor.close();
		this.colorSensor.close();
	}
	/**
	 * get RGB color 
	 * @return the RGB color in a array of float
	 */
	public float[] getColor(){
		float[] color = new float[3];
		SensorMode a = this.colorSensor.getRGBMode();
		a.fetchSample(color, 0);
		
		for (int i=0; i<3; i++)
			 color[i] *= 100;
		return color;
	}
	/**
	 * Determine color
	 * @param color array of RGB values
	 * @return  color's name
	 */
	public static Color determineColor(float[] color){
		Color c =null;
		int i = 0;
		
		if (i == 0 &&  color[0]<=12f && color[1]<=12f && color[2]<=10f ){
			c = Color.BLACK;
			i++;
		} 
		
		if( i == 0 && color[0] >15f && color[1]<10f && color[2]<10f){
			c = Color.ORANGE;
			i++;
		}
		
		if( i == 0 && color[0]<=13f && color[1]>10f && color[2]>=9f ){
			c = Color.BLUE;
			i++;
		}
		
		if( i == 0 && color[0]>=13f && color[1]>=15f && color[2]>=10f){
			c = Color.WHITE;
			i++;
		}
		
		//couleur 10.09804 --  22.843138--21.27451---null
		//19.313726 --  22.941177--17.254902---null
		
		return c;
	}
}
