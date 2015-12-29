package fr.utbm.tr54.autonomousIntersection.robot;

import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import fr.utbm.tr54.autonomousIntersection.enums.Direction;
import fr.utbm.tr54.autonomousIntersection.enums.RobotState;
import fr.utbm.tr54.autonomousIntersection.sensor.Color;
import fr.utbm.tr54.autonomousIntersection.sensor.Sensor;

public class RobotEnhance {
	
	protected DifferentialPilot pilot ;
	protected Sensor robotSensors;
	private RobotState state;
	/**
	 * Diameter of the wheels
	 */
	protected static final double WHEEL_DIAMETER = 0.055f;
	
	/**
	 * Distance between wheels
	 */
	protected static final double DISTANCE_BTWN_WHEEL_AXIS = 0.12f;
	
	public RobotEnhance(){
		//pilot = new DifferentialPilot(WHEEL_DIAMETER, DISTANCE_BTWN_WHEEL_AXIS, MotorPort.B, MotorPort.C, true);		
		pilot = new DifferentialPilot(WHEEL_DIAMETER, DISTANCE_BTWN_WHEEL_AXIS,Motor.B, Motor.C);
		this.robotSensors = new Sensor();
		this.state = RobotState.FORWARDING;
	}
	
	public void forward(){
		this.pilot.forward();
	}
	
	public void stop(){
		this.pilot.stop();
	}
	
	public void backward(){
		this.pilot.backward();
	}
	
	/**
	 * Use the pilot to set the speed of the robot
	 * @param speed the robot speed
	 */
	public void setSpeed(double speed) {
		pilot.setTravelSpeed(speed);
	}
	
	/**
	 * @return max speed of the robot
	 */
	public double getMaxSpeed() {
		return pilot.getMaxTravelSpeed();
	}
	
	public void steer(Direction dir) {
		double radius = 0;
		double distance = 0.5; 
		switch (dir) {
		case RIGHT:
			radius = -0.6;
			break;
		case LEFT:
			radius = 0.6;
			break;
		}
		
		this.pilot.travelArc(radius, distance);
		
	}
	
	public Color getColor() {
		float[] color = this.robotSensors.getColor();
		return Sensor.determineColor(color);
	}
	
	public boolean searchFwdColor() {
		int coeff = 1;
		
		for(int i = 0; i < 30; i += 3) {
			pilot.rotate(coeff * 3);
			
			Color c = getColor();
			
			if(c == Color.BLUE && c == Color.ORANGE) {
				return true;
			}
			
			if(i == 15 || i == 30) {
				if(i == 15)
					coeff = -1;
				else
					coeff = 1;
				
				pilot.rotate(coeff * 15);
			}
		}
		
		return false;
	} 
	
	public Direction searchDir() {
		pilot.travel(0.035f);
		
		if(getColor() == Color.BLACK)
			return Direction.RIGHT;
		
		return Direction.LEFT;
	} 

	public RobotState getState() {
		return state;
	}

	public void setState(RobotState state) {
		this.state = state;
	}
	
	
	
}
