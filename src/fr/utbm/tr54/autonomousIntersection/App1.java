package fr.utbm.tr54.autonomousIntersection;

import java.io.IOException;

import lejos.utility.Delay;
import fr.utbm.tr54.autonomousIntersection.communication.RobotCommunication;

/**
 * @author Achille
 *
 */
public class App1 {

	/**
	 * @param args
	 * @throws Exception 
	 */
	/*public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Sensor cs = new Sensor();
		
		int i = 0;
		while (i< 4){
			float[] couleur= cs.getColor();
			Color c = Sensor.determineColor(couleur);
			//LCD.drawString("couleur "+couleur[0] +" -- "+" "+couleur[1]+"--"+couleur[2], 0 , i);
			LCD.drawString("couleur "+couleur[0] ,0,1); //$NON-NLS-1$
			LCD.drawString("couleur "+couleur[1] ,0,2); //$NON-NLS-1$
			LCD.drawString("couleur "+couleur[2] ,0,3); //$NON-NLS-1$
			LCD.clear(4);
			LCD.drawString("couleur "+c ,0,4); //$NON-NLS-1$
			Utils.sauvegardeMesure("couleur "+couleur[0] +" -- "+" "+couleur[1]+"--"+couleur[2]+"---"+c, "couleur.txt");   //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$
			Delay.msDelay(5000);
			i++;	
		}
	}*/
	/**
	 * @param args
	 * @throws Exception 
	 */
	 public static void main( String[] args )
	    {
	        int i = 0;
	    	RobotCommunication c = new RobotCommunication("robotB");
	    	try {
	    		while(i < 5){
	    			c.clientSendData(c.getNameRobot()+"-request", i + 1);   			
	    			c.communicationWithServer();			
	    			Delay.msDelay(7000);	
	    			c.clientSendData(c.getNameRobot()+"-out", i + 1);	
	    			Delay.msDelay(7000);
	    			i++;
	    		}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

}
