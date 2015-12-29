package fr.utbm.tr54.autonomousIntersection.robot;

import fr.utbm.tr54.autonomousIntersection.enums.Direction;
import fr.utbm.tr54.autonomousIntersection.enums.RobotState;
import fr.utbm.tr54.autonomousIntersection.sensor.Color;

public class Pathfinding implements Runnable {
	private RobotEnhance robot;
	private Direction direction = null;
	
	public Pathfinding(RobotEnhance robot) {
		this.robot = robot;
	}

	@Override
	public void run() {
		while(true) {
			Color c;
			c = robot.getColor();
			if (c != Color.BLUE && c != Color.ORANGE){
				robot.stop();
				
				if (robot.searchFwdColor()) {
					continue;
				}

				robot.setState(RobotState.STEERING);
				
				if(direction == null) {
					direction = robot.searchDir();
				}
				
				robot.steer(direction);
				robot.searchFwdColor();
			} else {
				if(robot.getState() != RobotState.FORWARDING) {
					robot.forward();
					robot.setState(RobotState.FORWARDING);
				}
				
				if(c == Color.ORANGE && direction != null) {
					switch (direction) {
					case LEFT:
						direction = Direction.RIGHT;
						break;
					case RIGHT:
						direction = Direction.LEFT;
						break;

					default:
						break;
					}
				}
			}
		
		}
	}
}
