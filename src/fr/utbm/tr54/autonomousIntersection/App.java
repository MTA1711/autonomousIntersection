package fr.utbm.tr54.autonomousIntersection;

import java.io.IOException;

import fr.utbm.tr54.autonomousIntersection.communication.RobotCommunication;
import lejos.utility.Delay;

/**
 * Hello world!
 * 
 */
public class App 
{
	/**
	 * @param args
	 * @throws Exception 
	 */
    public static void main( String[] args )
    {
        int i = 0;
    	RobotCommunication c = new RobotCommunication("robotA");
    	try {
    		while(i < 5){
    			c.clientSendData(c.getNameRobot()+"-request", i + 1);  			
    			c.communicationWithServer();   			
    			Delay.msDelay(10000);			
    			c.clientSendData(c.getNameRobot()+"-out", i + 1);		
    			Delay.msDelay(10000);
    			i++;	
    		}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
