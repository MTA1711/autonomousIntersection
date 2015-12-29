package fr.utbm.tr54.autonomousIntersection;

import fr.utbm.tr54.autonomousIntersection.enums.Direction;
import fr.utbm.tr54.autonomousIntersection.enums.RobotState;
import fr.utbm.tr54.autonomousIntersection.robot.RobotEnhance;
import fr.utbm.tr54.autonomousIntersection.sensor.Color;

public class TestPathfinding {

	public static void main(String[] args) {
		
		RobotEnhance robot = new RobotEnhance();
		Direction direction = null;
	
		while(true) {
			Color c;
			c = robot.getColor();

			if (c != Color.BLUE && c != Color.ORANGE){
				System.out.println("Entering non forward seq");
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
				System.out.println("Entering forward seq");
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
