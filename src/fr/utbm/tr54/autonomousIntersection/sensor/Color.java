package fr.utbm.tr54.autonomousIntersection.sensor;

/**
 * @author Achille
 * Enum for color 
 */
public enum Color{BLACK, BLUE, WHITE, ORANGE, BLUE_BLACK ;
	 @Override
	  public String toString() {
		String color = null;
	    switch(this) {
	      case BLACK:  color = new String("BLACK");
	      break;
	      case BLUE:   color = new String("BLUE");
	      break;
	      case WHITE:  color = new String("WHITE");
	      break;
	      case ORANGE: color = new String("ORANGE");
	      break;
	      case BLUE_BLACK: color = new String("BLUE_BLACK");
	      break;
		  default:
		  break;  
	    }
	    return color;
	  }
}
