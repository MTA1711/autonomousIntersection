package fr.utbm.tr54.autonomousIntersection;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import fr.utbm.tr54.autonomousIntersection.communication.ServerReceiver;
import fr.utbm.tr54.autonomousIntersection.communication.ServerSender;

/**
 * Program run by server that manage the intersection
 * @author Achille
 */
public class ServerCom {

	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args) {
		Semaphore access = new Semaphore(1);
		ArrayList<String> passingList = new ArrayList<>();
		
		ServerReceiver rec = new ServerReceiver(access,passingList);
		ServerSender send = new ServerSender(access , passingList);
		rec.start();
		send.start();
		
	}

}
