package fr.utbm.tr54.autonomousIntersection.test;

import fr.utbm.tr54.autonomousIntersection.robot.RobotWifi;
import lejos.utility.Delay;

/**
 * Testing the Robot Moves
 * @author Youssoupha
 *
 */
public class robotMove {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		RobotWifi r1 = new RobotWifi();
			//r1.followLine();
			//r1.followLine2();
			for (int i =0; i< 2 ; i++){
				r1.move( 950);			
				Delay.msDelay(5000);
				
				r1.move(1350);
				Delay.msDelay(5000);
			}
			
	}
	
}
