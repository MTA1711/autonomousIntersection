package fr.utbm.tr54.autonomousIntersection;

import java.util.concurrent.Semaphore;

import fr.utbm.tr54.autonomousIntersection.communication.RobotReceiver;
import fr.utbm.tr54.autonomousIntersection.robot.RobotWifi;
import fr.utbm.tr54.autonomousIntersection.util.NumberInt;

/**
 * Main program executed by all robots. We have to update the robot's name during the deployment
 * @author Achille
 * 
 */
public class RobotMain {
	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args) {
		NumberInt rightToCross = new NumberInt();
		Semaphore semRightToCross = new Semaphore(1);
		
		RobotWifi r2d2 = new RobotWifi("robot3", rightToCross, semRightToCross);
		RobotReceiver r2d2Receive  = new RobotReceiver("robot3", rightToCross, semRightToCross);
		r2d2.start();
		Thread recThread = new Thread(r2d2Receive);
		recThread.start();
	}

}
