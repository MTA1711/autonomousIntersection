package fr.utbm.tr54.autonomousIntersection.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import lejos.hardware.Wifi;
import lejos.hardware.lcd.LCD;

/**
 * This class manages the communication ( robot -> Server ) on the robot.
 * @author Achille
 * 
 */
public class RobotSender extends Wifi{
	final  int port = 8532;
	final  int tailleBuffer = 1024; 
	private InetAddress serverAddress;
	private DatagramSocket clientSocket ;
	
	/**
	 * Default constructor
	 */
	public RobotSender(){
		super();
		try 
		{
			this.serverAddress = InetAddress.getByName("192.168.173.1"); 
			this.clientSocket = new DatagramSocket(this.port); 
		} 
		catch (Exception e) 
		{	LCD.drawString("erreur: " + e.getMessage(), 0, 0); }
	}
	
	/**
	 * Send a request to the server
	 * @param data the data to send to the server
	 * @throws IOException
	 */
	public void clientSendData( String data) throws IOException{
        byte[] buffer2 = data.getBytes(); 
        DatagramPacket packet = new DatagramPacket(buffer2,buffer2.length, this.serverAddress, this.port); 
        packet.setData(buffer2);
        this.clientSocket.send(packet);
	}
}
