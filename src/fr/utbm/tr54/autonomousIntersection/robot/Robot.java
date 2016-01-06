package fr.utbm.tr54.autonomousIntersection.robot;

import java.util.concurrent.Semaphore;

import lejos.hardware.lcd.LCD;
import lejos.utility.Stopwatch;
import fr.utbm.tr54.autonomousIntersection.motor.MotorControl;
import fr.utbm.tr54.autonomousIntersection.sensor.Color;
import fr.utbm.tr54.autonomousIntersection.sensor.Sensor;
import fr.utbm.tr54.autonomousIntersection.util.NumberInt;
import fr.utbm.tr54.autonomousIntersection.util.Utils;
/**
 * This is an old version of the class RobotWifi.
 * This class manages the robot's movement 
 * @author Achille
 */
public class Robot extends Thread {
	
	protected MotorControl robotMotors;
	protected Sensor robotSensors;
	
	@SuppressWarnings("unused")
	private boolean progress;
	
	final long TIME_IN  = 5000 ;
	final long TIME_OUT = 15000 ;
	/*shared var*/
	private NumberInt sendRequestIn;
	private NumberInt sendRequestOut;
	private NumberInt rightToCross;
	
	/*mutex for synchronization*/
	private Semaphore semRequestIn;
	private Semaphore semRequestOut;
	private Semaphore semRightToCross;
	
	/**
	 * Default constructor
	 */
	public Robot() {
		// TODO Auto-generated constructor stub
		this.robotMotors = new MotorControl();
		this.robotSensors = new Sensor();
		this.progress = false;
	}
	
	/**
	 * @param sendRequestIn
	 * @param sendRequestOut
	 * @param rightToCross
	 * @param semRequestIn
	 * @param semRequestOut
	 * @param semRightToCross
	 */
	public Robot( NumberInt sendRequestIn, NumberInt sendRequestOut, NumberInt rightToCross, Semaphore semRequestIn, Semaphore semRequestOut,  Semaphore semRightToCross) {
		// TODO Auto-generated constructor stub
		this.robotMotors = new MotorControl();
		this.robotSensors = new Sensor();
		this.progress = false;
		
		this.semRequestIn = semRequestIn;
		this.semRequestOut = semRequestOut;
		this.semRightToCross = semRightToCross;
		
		this.sendRequestIn = sendRequestIn;
		this.sendRequestOut = sendRequestOut;
		this.rightToCross = rightToCross;
	}
	
	/**
	 * get Maximal motor's speed
	 * @return Maximal motor's speed
	 */
	float getSpeedMax(){
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
	 * Move robot. it manages the robot's movement  
	 */
	public void moveEight(){
		float[] couleur = null;
		float[] couleur2 = null;
		
		float vi = this.getSpeedMax();
		float v = 0;
		int angle = 100;
		while(true){
			couleur = this.robotSensors.getColor();
			// calcul vitesse en fonction du pourcentage de RGB
			if (couleur[0]<15 && couleur[0]<10 && couleur[0]<15 ){
				v = (float) (vi * 0.6);
			}else{
				if(couleur[0]<25 && couleur[0]<25 && couleur[0]<25){
					v = (float) (vi * 0.3);
				}else if(couleur[0]<35 && couleur[0]<35 && couleur[0]<35){
					v = vi * 0;
				}
			}
			
			if (v == 0 ){
				this.robotMotors.stop();
				this.robotMotors.rotationForwardLeft(angle,1);
				couleur2 = this.robotSensors.getColor();
				if(couleur[0]<couleur2[0] && couleur[1]<couleur2[1] && couleur[2]<couleur2[2]){
					this.robotMotors.rotationForwardRight(angle, 3);
				}
			}else{
				this.robotMotors.moveSpeed(v);
				this.robotMotors.moveForward();
			}
						
			LCD.drawInt( (int)v, 0 ,0 );
		}
	}
	
	/**
	 * Move robot. it manages the robot's movement  
	 */
	public void moveEight2(){
		float[] couleur = null;
		float[] couleur2 = null;
		
		float vi = this.getSpeedMax();
		float v = 0;
		int angle = 100;
		int increment = 0;
		while(true){
			couleur = this.robotSensors.getColor();
			// calcul vitesse en fonction du pourcentage de RGB
			float moy = (couleur[0] + couleur[1] +couleur[2]) / 3;
			
			if (moy < 10){
				v = (float) (vi * 0.6);
			}else{
				if(moy < 20){
					v = (float) (vi * 0.3);
				}else {
					v = vi * 0;
				}
			}
			
			if (v == 0 ){
				this.robotMotors.stop();
				
				int k = 1;
				increment = 1;
				
				while (k > 0 ){
					this.robotMotors.rotationForwardLeft(angle,increment);
					couleur2 = this.robotSensors.getColor();
					float moy2 = (couleur2[0] + couleur2[1] +couleur2[2]) / 3;
					
					if(moy2 >20 ){
						this.robotMotors.rotationBackwardLeft(angle,increment);
						this.robotMotors.rotationForwardRight(angle,increment);
						couleur2 = this.robotSensors.getColor();
						moy2 = (couleur2[0] + couleur2[1] +couleur2[2]) / 3;					
						if(moy2 >20 ){
							this.robotMotors.rotationForwardRight(angle, increment);
						}else{
							k++;
						}
						increment++;
					}else{
						k++;
					}
				}
				
			}else{
				this.robotMotors.moveSpeed(v);
				this.robotMotors.moveForward();
			}
						
			LCD.drawInt( increment, 0 ,0 );
		}
	}
	
	/**
	 * Move robot. it manages the robot's movement  
	 */
	public void followLine(){
		float[] couleur = null;
		float[] couleur2 = null;
		
		float vi = this.getSpeedMax();
		float v = 0;
		int angle = 100;
		
		while(true){
			couleur = this.robotSensors.getColor();
			Color c = Sensor.determineColor(couleur);
			
			LCD.clear(1);
			LCD.drawString("couleur "+c ,0,1); //$NON-NLS-1$
			Utils.sauvegardeMesure("couleur "+couleur[0] +" -- "+" "+couleur[1]+"--"+couleur[2]+"---"+c, "couleur_test.txt");
			
			if (c ==  Color.ORANGE || c == Color.BLUE) {
				v = (float) (vi * 0.6);
			}else{
				if(c ==  Color.BLACK ){
					v = (float) (vi * 0.3);
				}else if( c ==  Color.WHITE){
					v = vi * 0;
				}else{
					//indetermine color
					v = vi * 0;
				}
			}
			
			if (v == 0 ){
				this.robotMotors.stop();
				this.robotMotors.rotationForwardLeft(angle,1);
				couleur2 = this.robotSensors.getColor();
				if(couleur[0]<couleur2[0] && couleur[1]<couleur2[1] && couleur[2]<couleur2[2]){
					this.robotMotors.rotationForwardRight(angle, 3);
				}
			}else{
				this.robotMotors.moveSpeed(v);
				this.robotMotors.moveForward();
			}
						
		}
	}
	
	/**
	 * Move robot. it manages the robot's movement  
	 */
	public void followLine2(){
		float[] couleur = null;
		//float[] couleur2 = null;
		Color previousColor = Color.BLACK;

		float v = 0;
		int angle = 100;

		couleur = this.robotSensors.getColor();
		Color c = Sensor.determineColor(couleur);
		
		LCD.clear(1);
		LCD.drawString("couleur "+c ,0,1); //$NON-NLS-1$
		Utils.sauvegardeMesure("couleur "+couleur[0] +" -- "+" "+couleur[1]+"--"+couleur[2]+"---"+c, "couleur_test.txt");
		
		v = this.speedByColor(c);
		if (v == -1){
			v = this.speedByColor(previousColor);
			c = previousColor;
		}
		
		if (c == Color.ORANGE ){
			LCD.clear(2);
			LCD.drawString("zone stockage "+c ,0,2);
		}else{
			LCD.clear(2);
			LCD.drawString("hors zone stockage "+c ,0,2);
		}
		if (c == Color.BLACK){				
			this.robotMotors.rotationForwardLeft(angle, 0.5f);
		
		}else if(c == Color.WHITE){
			this.robotMotors.rotationForwardRight(angle, 0.5f);				
		}
		
		this.robotMotors.moveSpeed(v);
		this.robotMotors.moveForward();
			
						
		
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
				//indetermine color
				v = -1;
			}
		}	
		return v;
	}
	
	/**
	 * Move robot. it manages the robot's movement  
	 */
	public void lineFollowingMove(){
		float dist;
		
		while(true){
			dist = this.robotSensors.distance();
			if(dist >0.15){				
				this.followLine();
			}
			else{
				this.robotMotors.stop();
			}
			
		}
	}
	
	
	/**
	 * Move robot. it manages the robot's movement  
	 */
	public void lineFollowingMove2(){
		float[] couleur = null;
		float dist;
		float v = 0;
		float vi = this.getSpeedMax();
		
		while(true){
			dist = this.robotSensors.distance();
			if(dist >0.15){				
				//getcolor
				couleur = this.robotSensors.getColor();
				Color c = Sensor.determineColor(couleur);
				
				if (c ==  Color.ORANGE || c == Color.BLUE) {//if blue or orange forward
					v = (float) (vi * 0.4);
					this.robotMotors.moveSpeed(v);
					this.robotMotors.moveForward();
				}else if (c ==  Color.WHITE ){//if white go right until bleu or black
					
					while (c == Color.WHITE){
						dist = this.robotSensors.distance();
						this.robotMotors.stop();
						
						if ( dist > 0.15){						
							this.robotMotors.goRight();
						}
						couleur = this.robotSensors.getColor();
						c = Sensor.determineColor(couleur);
					}
					
				}else if (c ==  Color.BLACK){//if black go left until bleu or orange or white
					
					while (c == Color.BLACK){
						this.robotMotors.stop();
						dist = this.robotSensors.distance();
						
						if ( dist > 0.15){	
							this.robotMotors.goLeft();
						}
						
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
	
	private boolean checkObstacle(){
		double dist = this.robotSensors.distance();
		return (dist > 0.15)?true:false;
	}
	
	private void move(Stopwatch s , long endTime){
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
	
	@Override
	public void run(){
		int inStockageZone = 0;
		int nearBoundary = 0;
		
		int a ;
		int b = 0;
		int k = 0;
		
		float[] couleur = null;
		float v = 0;
		float vi = this.getSpeedMax();
		Stopwatch s = new Stopwatch();
		
		while (true){
			a = 0;
			b = 0;
			k = 0;
			if ( this.checkObstacle() ) {
				try{
					this.semRightToCross.acquire();
					if ( inStockageZone == 0 && this.rightToCross.getNum() == 0 ){
						this.semRightToCross.release();
						a++;
						//getcolor
						couleur = this.robotSensors.getColor();
						Color c = Sensor.determineColor(couleur);
						if (c ==  Color.ORANGE || c == Color.BLUE) {//if blue or orange forward
							v = (float) (vi * 0.4);
							if (c == Color.ORANGE ){
								this.semRequestIn.acquire();
								LCD.drawString("semRequestIn takes move", 0, 3);
								this.sendRequestIn.setNum(1);
								inStockageZone = 1;
								this.semRequestIn.release();
								LCD.drawString("semRequestIn releases", 0, 3); 
							}
							this.robotMotors.moveSpeed(v);
							this.robotMotors.moveForward();							
						}else if (c ==  Color.WHITE ){//if white go right until bleu or black
							while (c == Color.WHITE){
								this.robotMotors.stop();
								if ( this.checkObstacle() )this.robotMotors.goRight();
								couleur = this.robotSensors.getColor();
								c = Sensor.determineColor(couleur);
							}	
						}else if (c ==  Color.BLACK){//if black go left until bleu or orange or white
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
							this.move(s, this.TIME_IN);
						}else{
							this.move(s, this.TIME_IN * 2);
						}
						
						this.semRequestOut.acquire();
							this.sendRequestOut.setNum(1);
						this.semRequestOut.release();
						inStockageZone = 0;
						nearBoundary = 0;
						this.semRightToCross.acquire();
							this.rightToCross.setNum(0);
						this.semRightToCross.release();
					}
					if (b == 0) this.semRightToCross.release();
					
					this.semRightToCross.acquire();
					if ( this.rightToCross.getNum() == 0 && inStockageZone == 1){
						this.semRightToCross.release();
						k++;
						if ( nearBoundary == 0 ){
							s.reset();
							this.move(s, this.TIME_IN);
							this.robotMotors.stop();
							nearBoundary = 1;	
						}
						this.robotMotors.stop();
						LCD.clear(6);
						LCD.drawString("critical zone", 0, 6); 
						
						//send request to server for reception of passingList
						this.semRequestIn.acquire();
						this.sendRequestIn.setNum(1);
						this.semRequestIn.release();
					}
					if (k == 0) this.semRightToCross.release();
					
				}catch (Exception e) {e.printStackTrace();}
			}else{
				this.robotMotors.stop();
			}
		}
	}
}