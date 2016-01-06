package fr.utbm.tr54.autonomousIntersection.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;

import fr.utbm.tr54.autonomousIntersection.util.NumberInt;
import lejos.hardware.Wifi;
import lejos.hardware.lcd.LCD;

/**
 * This class manages the WIFI communication ( Server -> robot  ) on the robot.
 * @author Achille
 * 
 */
public class RobotReceiver extends Wifi implements Runnable{

	final  int portDest = 8533;
	final  int tailleBuffer = 1024; 
	final  byte buffer[] = new byte[this.tailleBuffer];

	//private InetAddress localAddress;
	private DatagramSocket clientSocketRec ;
	private String nameRobot ;
	/*shared var*/
	private NumberInt rightToCross;
	/*mutex for synchronization*/
	private Semaphore semRightToCross;
	
	/**
	 * Default constructor
	 * @param name
	 * @param rightToCross 
	 * @param semRightToCross
	 */
	public RobotReceiver(String name, NumberInt rightToCross,  Semaphore semRightToCross  ){
		super();
		try 
		{
			this.clientSocketRec = new DatagramSocket(this.portDest); 
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		this.nameRobot = new String(name);
		this.semRightToCross = semRightToCross;
		this.rightToCross = rightToCross;
	}
	/**
	 * receive a data from the server and determines if the robot can cross the intersection
	 * @return true if robot is in passing list and false in order case
	 * @throws IOException 
	 */
	public boolean communicationWithServer() throws IOException{
		DatagramPacket data = new DatagramPacket(this.buffer,this.buffer.length); 
	    this.clientSocketRec.receive(data);
	    boolean b = managePacket(data);
	    LCD.clear(7);
	    LCD.drawString("passing: "+b, 0, 7  ); 
	    //System.out.println(b);
	    return b;
	}
	
	/**
	 * Manage a packet received from the server. Packet is the passing list
	 * @param data datagram received by server
	 * @return true if robot is in passing list and false in order case
	 */
	public boolean managePacket(DatagramPacket data){	
		int i =0;
		boolean find = false;	
		String dataString = new String(data.getData(), StandardCharsets.ISO_8859_1);
		dataString = dataString.substring(0, data.getLength());
		String[] arrayData = dataString.split("--");
		//looking for the name of robot in the passing list
		while (i < arrayData.length  && find == false){
			if( arrayData[i].equals(this.nameRobot) ){
				find = true;
			}
			i++;
		}	
		return find;	
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean cross = false;
		while (true){
			try {	
				cross = this.communicationWithServer();
				this.semRightToCross.acquire();
				if ( cross ){
					this.rightToCross.setNum(1);
				}else{
					this.rightToCross.setNum(0);
				}
				this.semRightToCross.release(); 
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
