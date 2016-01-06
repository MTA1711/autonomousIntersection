package fr.utbm.tr54.autonomousIntersection.util;

/**
 * Represents an integer. It will useful to use that class to share an integer between several threads
 * @author Achille
 */
public class NumberInt {
	int num ;
	/**
	 * Default constructor, it initializes the number with 0
	 */
	public NumberInt(){
		this.num = 0;
	}
	/**
	 * @return the number
	 */
	public int getNum() {
		return this.num;
	}
	/**
	 * @param num the number to set
	 */
	public void setNum(int num) {
		this.num = num;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + this.num + "]";
	}
	
	
}
