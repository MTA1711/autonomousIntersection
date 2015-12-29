package fr.utbm.tr54.autonomousIntersection;

import java.io.IOException;

import lejos.utility.Delay;
import fr.utbm.tr54.autonomousIntersection.communication.RobotCommunication;

/**
 * @author Achille
 *
 */
public class App2 {

	/**
	 * @param args
	 * @throws Exception 
	 */
	/*public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Robot r1 = new Robot();
		//r1.followLine();
		//r1.followLine2();
		r1.lineFollowingMove();
	}*/
	
	public static void main( String[] args )
    {
        int i = 0;
    	RobotCommunication c = new RobotCommunication("robotC");
    	try {
    		while(i < 5){
    			c.clientSendData(c.getNameRobot()+"-request", i + 1);   			
    			c.communicationWithServer();    			
    			Delay.msDelay(5000);			
    			c.clientSendData(c.getNameRobot()+"-out", i + 1);   			
    			Delay.msDelay(5000);
    			i++;   			
    		}		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
