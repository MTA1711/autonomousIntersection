package fr.utbm.tr54.autonomousIntersection.robot;

import java.util.concurrent.Semaphore;

public class SpeedManager implements Runnable {

	private RobotEnhance robot;
	private Semaphore sem;
	
	public SpeedManager(RobotEnhance robot, Semaphore sem) {
		this.robot = robot;
		this.sem = sem;
	}

	@Override
	public void run() {
		float dist;
		
		while(true){
			dist = this.robot.robotSensors.distance();
			if(dist > 0.15){
				this.robot.setSpeed(robot.getMaxSpeed() * 0.5);		
				sem.release();
			}
			else{
				try {
					sem.acquire();
					this.robot.setSpeed(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}

}
