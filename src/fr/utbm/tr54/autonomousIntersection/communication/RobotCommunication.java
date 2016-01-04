package fr.utbm.tr54.autonomousIntersection.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Semaphore;

import lejos.hardware.Wifi;
import lejos.hardware.lcd.LCD;
import fr.utbm.tr54.autonomousIntersection.util.NumberInt;


/**
 * @author Achille
 * Manage all wifi communication between EV3 robots
 */
public class RobotCommunication extends Wifi implements Runnable{
	final  int port = 8532;
	final  int periode = 500;
	final  int tailleBuffer = 1024; 
	final  byte buffer[] = new byte[this.tailleBuffer];
	final  int seuil = 8532; 
	
	//private InetAddress localAddress;
	private InetAddress broadcastAddress;	
	private InetAddress serverAddress;
	
	private DatagramSocket clientSocket ;
	private String nameRobot ;
	
	/*shared var*/
	private NumberInt sendRequestIn;
	private NumberInt sendRequestOut;
	private NumberInt rightToCross;
	
	/*mutex for synchronization*/
	private Semaphore semRequestIn;
	private Semaphore semRequestOut;
	private Semaphore semRightToCross;
	/**
	 * Default constructor of RobotCommunication class
	 * It initializes IP addresses and wifi device
	 * @param name the name of robot
	 */
	public RobotCommunication(String name){
		super();
		try 
		{
			//this.localAddress = InetAddress.getLocalHost();
			this.broadcastAddress = InetAddress.getByName("192.168.173.255"); 
			this.serverAddress = InetAddress.getByName("192.168.173.1"); 

			this.clientSocket = new DatagramSocket(this.port); 
			//LCD.drawString(""+this.localAddress, 0, 0); 
			LCD.drawString(""+this.broadcastAddress, 0, 0); 
		} 
		catch (Exception e) 
		{
			LCD.drawString("erreur: " + e.getMessage(), 0, 0); 
		}
		this.nameRobot = new String(name);
	}
	
	/**
	 * @param name
	 * @param sendRequestIn
	 * @param sendRequestOut
	 * @param rightToCross 
	 * @param semRequestIn 
	 * @param semRequestOut 
	 * @param semRightToCross
	 */
	public RobotCommunication(String name, NumberInt sendRequestIn, NumberInt sendRequestOut, NumberInt rightToCross, Semaphore semRequestIn, Semaphore semRequestOut,  Semaphore semRightToCross  ){
		super();
		try 
		{
			//this.localAddress = InetAddress.getLocalHost();
			this.broadcastAddress = InetAddress.getByName("192.168.173.255"); 
			this.serverAddress = InetAddress.getByName("192.168.173.1"); 
			this.clientSocket = new DatagramSocket(this.port); 
			
			//LCD.drawString(""+this.localAddress, 0, 0); 
			LCD.drawString(""+this.serverAddress, 0, 1); 
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		this.nameRobot = new String(name);
		this.semRequestIn = semRequestIn;
		this.semRequestOut = semRequestOut;
		this.semRightToCross = semRightToCross;
		
		this.sendRequestIn = sendRequestIn;
		this.sendRequestOut = sendRequestOut;
		this.rightToCross = rightToCross;
	}
	
	
	/**
	 * Send a request to the server
	 * @param data the data to send to the server
	 * @param nbe the number's of packet already sent
	 * @throws IOException
	 */
	
	public void clientSendData( String data, int nbe) throws IOException{
		//String envoi = "Robot1-request"; 
        byte[] buffer2 = data.getBytes(); 
        DatagramPacket packet = new DatagramPacket(buffer2,buffer2.length, this.serverAddress, this.port); 
        packet.setData(buffer2);
    
        this.clientSocket.send(packet);
        LCD.drawString("data send "+nbe, 0, 3  ); 
        //System.out.println("data send "+nbe); 
        //this.clientSocket.close();
	}
	
	/**
	 * receive a data from the server and determines if the robot can cross the intersection
	 * @return true if robot is in passing list and false in order case
	 * @throws IOException 
	 */
	public boolean communicationWithServer() throws IOException{
		DatagramPacket data = new DatagramPacket(this.buffer,this.buffer.length); 
	    this.clientSocket.receive(data);
	    boolean b = managePacket(data);
	    
	    LCD.clear(7);
	    LCD.drawString("pass: "+b, 0, 7  ); 
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
		
		//System.out.println("----> "+data.getLength());		
		String dataString = new String(data.getData(), StandardCharsets.ISO_8859_1);
		dataString = dataString.substring(0, data.getLength());
		//System.out.println("----> "+dataString);
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

	/**
	 * @return the nameRobot
	 */
	public String getNameRobot() {
		return this.nameRobot;
	}

	/**
	 * @param nameRobot the name Robot to set
	 */
	public void setNameRobot(String nameRobot) {
		this.nameRobot = nameRobot;
	}


	/**
	 * 
	 */
	public void run2() {
		// TODO Auto-generated method stub
		int nbPacket = 0;
		int firstIf = 0;
		while (true){
			firstIf = 0;
			try {
				this.semRequestIn.acquire();
			} catch (InterruptedException e) {e.printStackTrace();}
			
			if (this.sendRequestIn.getNum() == 1){
				//sent to the server a inRequest
				try {
					this.clientSendData(this.getNameRobot()+"-request", ++nbPacket);
				} catch (IOException e) {e.printStackTrace();}
				this.sendRequestIn.setNum(0);
				firstIf++;
			}
			this.semRequestIn.release();
			if (firstIf == 0){
				try {
					this.semRequestOut.acquire();
				} catch (InterruptedException e) {e.printStackTrace();}
				if (this.sendRequestOut.getNum() == 1){
					//sent to the server a outRequest
					try {
						this.clientSendData(this.getNameRobot()+"-out", ++nbPacket);
					} catch (IOException e) {e.printStackTrace();}
					this.sendRequestOut.setNum(0);
				}
				this.semRequestOut.release();
			}
			//receive passing list from server
			boolean cross = false;
			try {
				cross = this.communicationWithServer();
			} catch (IOException e) {e.printStackTrace();}
			
			try {
				this.semRightToCross.acquire();
			} catch (InterruptedException e) {e.printStackTrace();}
			if ( cross ){
				this.rightToCross.setNum(1);
			}else{
				this.rightToCross.setNum(0);
			}
			this.semRightToCross.release();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int nbPacket = 0;
		int firstIf = 0;
		while (true){
			firstIf = 0;
			int a = 0;
			int b = 0;
			try {
				this.semRequestIn.acquire();
				LCD.clear(3);
				LCD.drawString("sem In takes com", 0, 3); 
				if (this.sendRequestIn.getNum() == 1){
					//sent to the server a inRequest
					this.clientSendData(this.getNameRobot()+"-request", ++nbPacket);			
					this.sendRequestIn.setNum(0);
					this.semRequestIn.release();
					a++;
					
					firstIf++;
					
					//receive passing list from server
					boolean cross = false;
					cross = this.communicationWithServer();
					this.semRightToCross.acquire();
					LCD.drawString("sem RTC takes com", 0, 5); 
					if ( cross ){
						this.rightToCross.setNum(1);
					}else{
						this.rightToCross.setNum(0);
					}
					this.semRightToCross.release();
					LCD.drawString("sem RTC rel com", 0, 5); 	
				}
				
				if (a ==  0) this.semRequestIn.release();
				LCD.clear(3);
				LCD.drawString("sem In rel", 0, 3); 
				
				if (firstIf == 0){
					this.semRequestOut.acquire();
					LCD.clear(4);
					LCD.drawString("sem Out takes com", 0, 4); 
					if (this.sendRequestOut.getNum() == 1){
						//sent to the server a outRequest						
						this.clientSendData(this.getNameRobot()+"-out", ++nbPacket);
						this.sendRequestOut.setNum(0);
						this.semRequestOut.release();
						b++;
						
						//receive passing list from server
						boolean cross = false;
						cross = this.communicationWithServer();
						LCD.clear(5);
						this.semRightToCross.acquire();
						LCD.drawString("sem RTC takes com", 0, 5); 
						if ( cross ){
							this.rightToCross.setNum(1);
						}else{
							this.rightToCross.setNum(0);
						}
						this.semRightToCross.release();
						LCD.clear(5);
						LCD.drawString("sem RTC rel com", 0, 5); 
					}
					if (b==0)this.semRequestOut.release();
					LCD.clear(4);
					LCD.drawString("sem Out rel com", 0, 4); 				
				}
				
			} catch (Exception e) {e.printStackTrace();}
		}
	}
}
