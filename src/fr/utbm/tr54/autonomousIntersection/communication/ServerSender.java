package fr.utbm.tr54.autonomousIntersection.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Represent server which manages the intersection. It is used for sending data to all robots.
 * @author amongatc
 *
 */
public class ServerSender extends Thread{
	private ArrayList<String> passingList ;
	
	final  int portSend = 8533;


	
	//private InetAddress localAddress;
	private InetAddress broadcastAddress;
	private DatagramSocket socketServerSender;
	private InetAddress myAddress;
	/*mutex for synchronization*/
	private Semaphore accessPassingList;
	/**
	 * Constructor, it initializes the passing list
	 * @param access 
	 * @param l 
	 */
	public ServerSender(Semaphore access , ArrayList<String> l){

		this.passingList = l;
		this.accessPassingList = access;
		try 
		{
			//this.localAddress = InetAddress.getLocalHost();
			this.broadcastAddress = InetAddress.getByName("192.168.173.255"); 
			this.myAddress = InetAddress.getByName("192.168.173.1"); 
			this.socketServerSender = new DatagramSocket();		
			System.out.println("start sender... "+this.myAddress); 
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			System.out.println("erreur: " + e.getMessage()); 
		}
	}
	

	/**
	 * Build datagram to send to all robots
	 * @return Datagram to send
	 */
	public DatagramPacket buildResponse () {
		String dataToSend =""; 
		int i = 0;
		try {
			this.accessPassingList.acquire();
			for (String d : this.passingList){
				if ( i == 0){
					dataToSend += d;
					i++;
				}else{
					dataToSend += "--"+d; 
				}
			}
			this.accessPassingList.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//System.out.println("data sent by ServerSender: " + dataToSend);
		DatagramPacket packet = new DatagramPacket(dataToSend.getBytes(), dataToSend.getBytes().length, this.broadcastAddress, this.portSend); 
        packet.setData(dataToSend.getBytes());
        return packet;	
	}

	
	/**
	 * print an array list
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
	@Override
	public void run(){
		
		while(true){
			try {
				//send broadcast to all robots
				this.socketServerSender.setBroadcast(true);
				this.socketServerSender.send( this.buildResponse() );
				Thread.sleep(500);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
}


