package fr.utbm.tr54.autonomousIntersection;

import java.util.concurrent.Semaphore;

import fr.utbm.tr54.autonomousIntersection.robot.Pathfinding;
import fr.utbm.tr54.autonomousIntersection.robot.RobotEnhance;
import fr.utbm.tr54.autonomousIntersection.robot.SpeedManager;

public class IntersectMain {

	public static void main(String[] args) {
		RobotEnhance robot = new RobotEnhance();
		Semaphore sem = new Semaphore(1);
		
		Thread pathfinding = new Thread(new Pathfinding(robot));
		Thread speedManager = new Thread(new SpeedManager(robot, sem));

		speedManager.start();
		pathfinding.start();
		
	}

}
