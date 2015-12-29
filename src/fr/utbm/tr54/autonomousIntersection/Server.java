package fr.utbm.tr54.autonomousIntersection;

import java.io.IOException;

import fr.utbm.tr54.autonomousIntersection.communication.ServerCommunication;

/**
 * @author Achille
 * Program run by server that manage the intersection
 */
public class Server {

	/**
	 * Main function
	 * @param args
	 */
	public static void main(String[] args) {
		ServerCommunication s = new ServerCommunication();
		try {
			s.communicationWithRobot();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
