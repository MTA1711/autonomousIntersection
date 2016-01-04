package fr.utbm.tr54.autonomousIntersection.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Represent server which manages the intersection
 * @author amongatc
 *
 */
public class ServerCommunication {
	
	private ArrayList<String> listRequest; 
	private ArrayList<String> passingList ;
	
	final  int port = 8532;
	final  int tailleBuffer = 20; 
	final  byte buffer[] = new byte[this.tailleBuffer];
	
	//private InetAddress localAddress;
	private InetAddress broadcastAddress;
	private DatagramSocket socketServer;
	private InetAddress myAddress;
	
	/**
	 * Constructor, it initializes the different lists 
	 */
	public ServerCommunication(){
		this.listRequest = new ArrayList<>();
		this.passingList = new ArrayList<>();
		
		try 
		{
			//this.localAddress = InetAddress.getLocalHost();
			this.broadcastAddress = InetAddress.getByName("192.168.173.255"); 
			this.myAddress = InetAddress.getByName("192.168.173.1"); 
			this.socketServer = new DatagramSocket(this.port);
			//this.socketServer = new DatagramSocket(this.port, this.myAddress);
			
			//System.out.println("localAddress "+this.localAddress);
			System.out.println("my address "+this.myAddress); 
			System.out.println("broadcastAddress "+this.broadcastAddress); 
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			System.out.println("erreur: " + e.getMessage()); 
		}
	}
	
	/**
	 * Build Passing list to send to robot
	 * With this function only one robot can go throught the intersection
	 */
	public void buidPassingList(){
		this.passingList.removeAll(this.passingList);
		if (this.listRequest.size() > 0)
		this.passingList.add( this.listRequest.get(0) );
	}
	

	/**
	 * Build datagram to send to all robots
	 * @return Datagram to send
	 */
	public DatagramPacket buildResponse () {
		String dataToSend =""; 
		int i = 0;
		for (String d : this.passingList){
			if ( i == 0){
				dataToSend += d;
				i++;
			}else{
				dataToSend += "--"+d; 
			}
		}
		System.out.println("data sent: " + dataToSend);
		DatagramPacket packet = new DatagramPacket(dataToSend.getBytes(), dataToSend.getBytes().length, this.broadcastAddress, this.port); 
        packet.setData(dataToSend.getBytes());
        return packet;	
	}

	/**
	 * Manage a packet received from a robot. Packet is a string that contain: Robot's name, request|out
	 * @param data datagram received by server
	 */
	public void managePacket(DatagramPacket data){			
		int index ;		
		System.out.println("length: "+data.getLength());
		
		String dataString = new String(data.getData(),StandardCharsets.ISO_8859_1);
		dataString = dataString.substring(0, data.getLength());
		
		System.out.println("received: "+dataString);		
		System.out.println("address: " +data.getAddress().toString() ); 
				
		String[] arrayData = dataString.split("-");			
		if (arrayData[1].equals("request")){
			//add robot to listRequest
			index = this.listRequest.indexOf( (arrayData[0]) ); 
			if (index < 0 )this.listRequest.add(arrayData[0]); //if doesn't exist in listRequest, add it
		}else if ( arrayData[1].equals("out") ){
			//remove robot in request list
			index = this.listRequest.indexOf( (arrayData[0]) );
			if (index >= 0 ) this.listRequest.remove(index);
		} 
		
	}

	/**
	 * Implements communication between server and the robots
	 * @throws IOException
	 */
	public void communicationWithRobot() throws IOException{
		int nbe = 0;
		int nbReceive = 0;
		 
		System.out.println("Begin listenning...\n"); 
		
	    while(true) 
		{ 
		    DatagramPacket data = new DatagramPacket(this.buffer,this.buffer.length); 
		    this.socketServer.receive(data);
			
		    if ( ! data.getAddress().equals(this.myAddress)){
		    	++nbReceive;
			    System.out.println("data receive:"+nbReceive); 
			    this.managePacket(data);
			    this.buidPassingList();
				//send broadcast to all robots
				this.socketServer.send( this.buildResponse() );
				
				++nbe;
			    System.out.println("data sent number: "+nbe); 
			    this.printArrayList(this.listRequest);
			    System.out.println(""); 
		    }
		 } 
	    
	    
	}
	
	/**
	 * @param list
	 */
	public void printArrayList(ArrayList<String> list){
		String listString = "";
		for (String s : list)
		{
		    listString += s + "\t";
		}
		System.out.println(listString);
	}
	
}


