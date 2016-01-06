package fr.utbm.tr54.autonomousIntersection.robot;

import java.util.concurrent.Semaphore;

import lejos.hardware.lcd.LCD;
import lejos.utility.Stopwatch;
import fr.utbm.tr54.autonomousIntersection.communication.RobotSender;
import fr.utbm.tr54.autonomousIntersection.motor.MotorControl;
import fr.utbm.tr54.autonomousIntersection.sensor.Color;
import fr.utbm.tr54.autonomousIntersection.sensor.Sensor;
import fr.utbm.tr54.autonomousIntersection.util.NumberInt;

/**
 * This class manages the robot's movement 
 * @author Achille
 * 
 */
public class RobotWifi extends Thread{
	
	protected MotorControl robotMotors;
	protected Sensor robotSensors;
	protected RobotSender commucationModule ;
	final private int DISTANCE_CRITICAL_ZONE  = 950 ;
	final private int DISTANCE_STOCKAGE_ZONE  = 1350 ;
	/*mutex for synchronization*/
	private Semaphore semRightToCross;
	/*shared var*/
	private NumberInt rightToCross;
	protected String requestIn;
	protected String requestOut;
	protected String name;
	
	/**
	 * @param name 
	 * @param rightToCross
	 * @param semRightToCross
	 */
	public RobotWifi( String name, NumberInt rightToCross ,  Semaphore semRightToCross) {
		// TODO Auto-generated constructor stub
		this.robotMotors = new MotorControl();
		this.robotSensors = new Sensor();
		this.semRightToCross = semRightToCross;
		this.rightToCross = rightToCross;
		
		this.name = new String(name);
		this.requestIn  = new String( this.name+"-request" );
		this.requestOut = new String( this.name+"-out" );
		
		this.commucationModule = new RobotSender();
	}
	
	public RobotWifi() {
		// TODO Auto-generated constructor stub
		this.robotMotors = new MotorControl();
		this.robotSensors = new Sensor();
	}
	
	/**
	 * get Maximal motor's speed
	 * @return Maximal motor's speed
	 */
	public float getSpeedMax(){
		float speedMax;
		speedMax = this.robotMotors.getMaxSpeedMotor();
		return speedMax;
	}
	/**
	 * Stop all robot's components
	 */
	public void shutdown(){
		this.robotMotors.close();
		this.robotSensors.close();
	}
	
	/**
	 * determine the robot's speed thanks to the color
	 * @param c
	 * @return the speed of robot
	 */
	public float speedByColor(Color c){
		float v = 0;
		float vi = this.getSpeedMax();
		
		if (c ==  Color.ORANGE || c == Color.BLUE) {
			v = (float) (vi * 0.5);
		}else{
			if(c ==  Color.BLACK ){
				v = (float) (vi * 0.1);
			}else if( c ==  Color.WHITE){
				v = vi * 0;
			}else{
				v = -1;
			}
		}	
		return v;
	}
	/**
	 * check if there is an obstacle in front of robot
	 * @return true if there is no obstacle
	 */
	public boolean checkObstacle(){
		double dist = this.robotSensors.distance();
		return (dist > 0.15)?true:false;
	}
	
	/**
	 * Move robot while the time in parameter
	 * @param s
	 * @param endTime
	 */
	public void move(Stopwatch s , long endTime){
		float[] couleur = null;
		float v = 0;
		float vi = this.getSpeedMax();
		
		while( s.elapsed() < endTime ){			
			if( checkObstacle() ){				
				couleur = this.robotSensors.getColor();
				Color c = Sensor.determineColor(couleur);
				if (c ==  Color.ORANGE || c == Color.BLUE) {//if blue or orange forward
					v = (float) (vi * 0.4);
					this.robotMotors.moveSpeed(v);
					this.robotMotors.moveForward();
				}else if (c ==  Color.WHITE ){//if white go right until bleu or black
					while (c == Color.WHITE){
						this.robotMotors.stop();
						if ( checkObstacle() ) this.robotMotors.goRight();
						couleur = this.robotSensors.getColor();
						c = Sensor.determineColor(couleur);
					}
				}else if (c ==  Color.BLACK){//if black go left until bleu or orange or white
					while (c == Color.BLACK){
						this.robotMotors.stop();
						if ( checkObstacle() ) this.robotMotors.goLeft();	
						couleur = this.robotSensors.getColor();
						c = Sensor.determineColor(couleur);
					}
				}else{
					v = (float) (vi * 0.1);
					this.robotMotors.moveSpeed(v);
					this.robotMotors.moveForward();
				}		
			}
			else{
				this.robotMotors.stop();
			}
		}
	}
	
	/**
	 * Move robot while the time in parameter
	 * @param s
	 * @param endTime
	 */
	public void move(int distance){
		float[] couleur = null;
		float v = 0;
		float vi = this.getSpeedMax();
		
		int valTacho = 0;
		this.robotMotors.resetTachoCount(); 
		while( valTacho < distance ){			
			if( checkObstacle() ){				
				couleur = this.robotSensors.getColor();
				Color c = Sensor.determineColor(couleur);
				if (c ==  Color.ORANGE || c == Color.BLUE) {//if blue or orange forward
					v = (float) (vi * 0.4);
					this.robotMotors.moveSpeed(v);
					this.robotMotors.moveForward();
				}else if (c ==  Color.WHITE ){//if white go right until bleu or black
					while (c == Color.WHITE){
						this.robotMotors.stop();
						if ( checkObstacle() ) this.robotMotors.goRight();
						couleur = this.robotSensors.getColor();
						c = Sensor.determineColor(couleur);
					}
				}else if (c ==  Color.BLACK){//if black go left until bleu or orange or white
					while (c == Color.BLACK){
						this.robotMotors.stop();
						if ( checkObstacle() ) this.robotMotors.goLeft();	
						couleur = this.robotSensors.getColor();
						c = Sensor.determineColor(couleur);
					}
				}else{
					v = (float) (vi * 0.1);
					this.robotMotors.moveSpeed(v);
					this.robotMotors.moveForward();
				}		
			}
			else{
				this.robotMotors.stop();
			}
			
			valTacho = this.robotMotors.getTachoCount() ;
			/*LCD.clear(0);
			LCD.drawInt(valTacho, 0, 0);*/
		}
		this.robotMotors.stop();
	}
	@Override
	public void run(){
		int inStockageZone = 0;
		int nearBoundary = 0;

		
		int a ;
		int b = 0;
		int k = 0;
		int j = 0;
		
		float[] couleur = null;
		float v = 0;
		float vi = this.getSpeedMax();
		Stopwatch s = new Stopwatch();
		
		while (true){
			a = 0;
			b = 0;
			k = 0;
			j = 0;
			if ( this.checkObstacle() ) {
				try{
					this.semRightToCross.acquire();
					if ( inStockageZone == 0 && this.rightToCross.getNum() == 0 ){
						this.semRightToCross.release();
						a++;
						//get color
						couleur = this.robotSensors.getColor();
						Color c = Sensor.determineColor(couleur);
						if (c ==  Color.ORANGE || c == Color.BLUE) {//if blue or orange forward
							v = (float) (vi * 0.4);
							if (c == Color.ORANGE ){
								this.commucationModule.clientSendData(this.requestIn);
								inStockageZone = 1;
							}
							this.robotMotors.moveSpeed(v);
							this.robotMotors.moveForward();							
						}else if (c ==  Color.WHITE ){//if white go right until blue or black
							while (c == Color.WHITE){
								this.robotMotors.stop();
								if ( this.checkObstacle() )this.robotMotors.goRight();
								couleur = this.robotSensors.getColor();
								c = Sensor.determineColor(couleur);
							}	
						}else if (c ==  Color.BLACK){//if black go left until blue or orange or white
							while (c == Color.BLACK){
								this.robotMotors.stop();
								if ( this.checkObstacle() )this.robotMotors.goLeft();
								couleur = this.robotSensors.getColor();
								c = Sensor.determineColor(couleur);
							}
						}else{
							v = (float) (vi * 0.1);
							this.robotMotors.moveSpeed(v);
							this.robotMotors.moveForward();
						}
					}
					if (a == 0) this.semRightToCross.release();
					this.semRightToCross.acquire();
					if ( this.rightToCross.getNum() == 1 && inStockageZone == 1){
						this.semRightToCross.release();
						b++;
						s.reset();
						if (nearBoundary == 1){
							//this.move(s, this.TIME_IN);
							LCD.clear(4);
							LCD.drawString("move 1", 0, 4);
							this.move(this.DISTANCE_STOCKAGE_ZONE);
							
						}else{
							//this.move(s, this.TIME_IN * 2);
							LCD.clear(4);
							LCD.drawString("move 2", 0, 4);
							this.move(this.DISTANCE_CRITICAL_ZONE + this.DISTANCE_STOCKAGE_ZONE);
						}						
						this.commucationModule.clientSendData(this.requestOut);
						inStockageZone = 0;
						nearBoundary = 0;
						
						this.semRightToCross.acquire();
							this.rightToCross.setNum(0);
						this.semRightToCross.release();
						LCD.clear(6);
					}
					if (b == 0) this.semRightToCross.release();
					
					this.semRightToCross.acquire();
					if ( this.rightToCross.getNum() == 0 && inStockageZone == 1){
						this.semRightToCross.release();
						k++;
						if ( nearBoundary == 0 ){
							s.reset();
							//this.move(s, this.TIME_IN);
							LCD.clear(4);
							LCD.drawString("move 3", 0, 4);
							this.move(this.DISTANCE_CRITICAL_ZONE);
							this.robotMotors.stop();
							nearBoundary = 1;	
						}
						this.robotMotors.stop();
						LCD.clear(6);
						LCD.drawString("critical zone", 0, 6); 						
						//send request to server for reception of passingList
						this.commucationModule.clientSendData(this.requestIn);
					}
					if (k == 0) this.semRightToCross.release();
					
					this.semRightToCross.acquire();
					if ( inStockageZone == 0 && this.rightToCross.getNum() == 1 ){
						this.semRightToCross.release();
						LCD.clear(6);
						LCD.drawString("wrong case", 0, 6); 
						//this.robotMotors.stop();
						j++;
					}
					if (j == 0) this.semRightToCross.release();
				}catch (Exception e) {e.printStackTrace();}
			}else{
				this.robotMotors.stop();
			}
		}
	}
}
