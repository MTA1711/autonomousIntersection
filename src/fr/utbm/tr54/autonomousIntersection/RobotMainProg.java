package fr.utbm.tr54.autonomousIntersection;

import java.util.concurrent.Semaphore;

import fr.utbm.tr54.autonomousIntersection.communication.RobotCommunication;
import fr.utbm.tr54.autonomousIntersection.robot.Robot;
import fr.utbm.tr54.autonomousIntersection.util.NumberInt;

/**
 * @author Achille
 * Program run by EV3 robot. It has two thread one for wifi communication and another for the move's of robot
 */
public class RobotMainProg {

	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NumberInt sendRequestIn = new NumberInt();
		NumberInt sendRequestOut = new NumberInt();
		NumberInt rightToCross = new NumberInt();
		
		/*mutex for synchronization*/
		Semaphore semRequestIn = new Semaphore(1);
		Semaphore semRequestOut = new Semaphore(1);
		Semaphore semRightToCross = new Semaphore(1);
		
		Robot r2d2 = new Robot(sendRequestIn,sendRequestOut, rightToCross, semRequestIn, semRequestOut, semRightToCross);
		RobotCommunication r2d2Com = new RobotCommunication("robot1",sendRequestIn,sendRequestOut, rightToCross, semRequestIn, semRequestOut, semRightToCross);
		r2d2.start();
		Thread comThread = new Thread(r2d2Com);
		comThread.start();
		
	}

}
