package fr.utbm.tr54.autonomousIntersection.motor;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

/**
 * @author Achille
 * Manage motors's of robot
 */
public class MotorControl {
	private  EV3LargeRegulatedMotor motorC;
	private  EV3LargeRegulatedMotor motorB;
	
	/**
	 * Default constructor 
	 */
	public MotorControl(){
		this.motorC = new EV3LargeRegulatedMotor(MotorPort.C);
		this.motorB = new EV3LargeRegulatedMotor(MotorPort.B);

	}
	/**
	 * move forward robot
	 */
	public void moveForward(){
		/* Motor forward */
		this.motorB.forward();
		this.motorC.forward();
	}
	/**
	 * stop all motors
	 */
	public void stop(){
		this.motorB.stop(true);
		this.motorC.stop(true);
	}
	/**
	 * set speed's of motor
	 * @param speed
	 */
	public void moveSpeed(float speed) {
		this.motorB.setSpeed(speed);
		this.motorC.setSpeed(speed);
	}
	/**
	 * Manage rotation of robot 
	 * @param angle 
	 */
	public void rotation(int angle){
		this.motorB.rotate(angle, true);
		this.motorC.rotate(-1 * angle, true);
	}
	
	/**
	 * get speed max of motor
	 * @return speed max of motor
	 */
	public float getMaxSpeedMotor(){
		return this.motorB.getMaxSpeed();
	}
	
	/**
	 * get current speed's of motors
	 * @return current speed's of motor
	 */
	public float getMoveSpeed(){
		return this.motorB.getSpeed();
	}
	
	/**
	 * close all motors
	 */
	public  void close(){
		this.motorB.close();
		this.motorC.close();
	}
	
	/**
	 * manage rotation's of robot
	 * @param angle
	 * @param multiplicateur
	 */
	public void rotation (int angle, int multiplicateur){
		int a = angle * multiplicateur;
		this.motorB.rotate(a, true);
	}
	
	/**
	 * manage rotation forward Left of robot
	 * @param speed
	 * @param delay
	 */
	public void rotationForwardLeft (int speed, int delay){
		
		this.motorB.setSpeed(0);
		this.motorC.setSpeed(speed);
		this.motorC.forward();
		Delay.msDelay(500 * delay);
		this.motorC.stop(true);
	}
	
	
	/**
	 * manage rotation forward Left of robot
	 * @param speed
	 * @param delay
	 */
	public void rotationForwardLeft (int speed, float delay){
		
		this.motorB.setSpeed(speed/2);
		this.motorC.setSpeed(speed);
		this.motorC.forward();
		Delay.msDelay( (long)(500 * delay) );
		this.motorB.stop(true);
		this.motorC.stop(true);
	}
	
	/**
	 * manage rotation Backward Left of robot
	 * @param speed
	 * @param delay
	 */
	public void rotationBackwardLeft (int speed, int delay){
		
		this.motorB.setSpeed(0);
		this.motorC.setSpeed(speed);
		this.motorC.backward();
		Delay.msDelay(500 * delay);
		this.motorC.stop(true);
	}
	
	/**
	 * manage rotation Backward Left of robot
	 * @param speed
	 * @param delay
	 */
	public void rotationBackwardLeft (int speed, float delay){
		
		this.motorB.setSpeed(speed/2);
		this.motorC.setSpeed(speed);
		this.motorC.backward();
		Delay.msDelay( (long)(500 * delay) );
		this.motorB.stop(true);
		this.motorC.stop(true);
	}
	
	/**
	 * manage rotation forward Right of robot
	 * @param speed
	 * @param delay
	 */
	public void rotationForwardRight(int speed, int delay){
		this.motorB.setSpeed(speed);
		this.motorC.setSpeed(0);
		this.motorB.forward();
		Delay.msDelay(500 * delay);
		this.motorB.stop(true);
	}
	
	/**
	 * manage rotation forward Right of robot
	 * @param speed
	 * @param delay
	 */
	public void rotationForwardRight(int speed, float delay){
		this.motorB.setSpeed(speed);
		this.motorC.setSpeed(speed/2);
		this.motorB.forward();
		Delay.msDelay( (long)(500 * delay) );
		this.motorB.stop(true);
		this.motorC.stop(true);
	}
	
	/**
	 * manage rotation Backward Right of robot
	 * @param speed
	 * @param delay
	 */
	public void rotationBackwardRight(int speed, int delay){
		this.motorB.setSpeed(speed);
		this.motorC.setSpeed(0);
		this.motorB.backward();
		Delay.msDelay(500 * delay);
		this.motorB.stop(true);
	}
	
	/**
	 * manage rotation Backward Right of robot 
	 * @param speed
	 * @param delay
	 */
	public void rotationBackwardRight(int speed, float delay){
		this.motorB.setSpeed(speed);
		this.motorC.setSpeed(speed/2);
		this.motorB.backward();
		Delay.msDelay( (long)(250 * delay) );
		this.motorB.stop(true);
		this.motorC.stop(true);
	}
	
	/**
	 * move robot to the left
	 */
	public void goLeft(){
		this.motorC.setSpeed(this.getMaxSpeedMotor() * 0.3f);
		this.motorB.setSpeed(this.getMaxSpeedMotor() * 0.1f);
		
		this.moveForward();
		
		Delay.msDelay( (300) );
		
		this.stop();
	}
	
	/**
	 * Move robot to the right
	 */
	public void goRight(){
		this.motorB.setSpeed(this.getMaxSpeedMotor() * 0.1f);
		this.motorC.setSpeed(0);
		
		this.moveForward();
		Delay.msDelay( (300) );
		this.stop();
	}
}
