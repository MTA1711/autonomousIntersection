package fr.utbm.tr54.autonomousIntersection.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Achille
 *
 */
public class Utils {
	/**
	 * Write a string in a csv file
	 * @param dist string to write
	 * @param file name of file
	 */
	@SuppressWarnings("resource")
	public static void sauvegardeMesure(String dist,String file){
		File f = new File (file);
		try
		{
		    PrintWriter pw = new PrintWriter (new BufferedWriter (new FileWriter (f, true) ));
		    pw.println (dist); //$NON-NLS-1$
		    pw.close();
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage()); //$NON-NLS-1$
		}
	}
	
	/**
	 * Save a measure in a csv file
	 * @param dist string to write
	 * @param file name of file
	 */
	@SuppressWarnings("resource")
	public static void sauvegardeMesure(Float dist, String file){
		File f = new File (file);
		try
		{
		    PrintWriter pw = new PrintWriter (new BufferedWriter (new FileWriter (f, true) ));
		    pw.println (dist);
		    pw.close();
		}
		catch (IOException exception)
		{
			exception.printStackTrace();
		    System.out.println ("Erreur lors de la lecture : " + exception.getMessage()); //$NON-NLS-1$
		}
	}
}
