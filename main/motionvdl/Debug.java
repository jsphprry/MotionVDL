package motionvdl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Debug {
	
	// variables and components
	private static boolean enabled = false;
	private static DateTimeFormatter timeStamp;
	private static PrintWriter printWriter;
	
	
	/**
	 * Setup the debug trace
	 * @param verbose Enable or disable debug trace
	 */
	public static void setup(boolean verbose) {
		
		// set enabled flag
		enabled = verbose;
		
		if (enabled) {
			
			// setup time stamp formatter
			timeStamp = DateTimeFormatter.ofPattern("yyyy/MM/dd | HH:mm:ss");
			
			// setup file writer
			try {
				printWriter = new PrintWriter(new FileWriter("MVDL.log"));
				trace("Created log file");
			
			// if there is an issue
			} catch (IOException e) {
				System.out.println("Error: Problem setting up file writer. "+e.getMessage());
			}
		}
	}
	
	
	/**
	 * Record a debug trace
	 * @param message The message for the debug trace
	 */
	public static void trace(String message) {
		
		// if the trace is enabled record message
		if (enabled) {
			
			// format message with time stamp
			message = timeStamp.format(LocalDateTime.now()) +" | " + message;
			
			// print message to terminal
			System.out.println(message);
			
			// write to log file
			printWriter.println(message);
			printWriter.flush();
		}
	}
}
