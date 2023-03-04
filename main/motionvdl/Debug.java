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
	 * @param verbose Enable (true) or disable (false) debug trace
	 */
	public static void setup(boolean verbose) {
		
		// set flag
		enabled = verbose;
		
		// if the debug trace is enabled try to setup
		if (enabled) {
			try {
				timeStamp = DateTimeFormatter.ofPattern("yyyy/MM/dd | HH:mm:ss");
				printWriter = new PrintWriter(new FileWriter("MVDL.log"));
				trace("Created log file 'MVDL.log'");
			
			// disable trace and print warning if IO error
			} catch (IOException e) {
				enabled = false;
				System.out.println("Warning! Problem setting up file writer");
				System.out.println("Caught message: "+e.getMessage());
			}
		
		// otherwise free resources
		} else {
			timeStamp = null;
			printWriter = null;
		}
	}
	
	
	/**
	 * Record a debug trace to terminal and log file
	 * @param message The message string
	 */
	public static void trace(String message) {
		
		// if the debug trace is enabled
		if (enabled) {
			
			// reformat message with time stamp
			message = timeStamp.format(LocalDateTime.now())+" | "+message;
			
			// record message to terminal and log file
			System.out.println(message);
			printWriter.println(message);
			printWriter.flush();
		}
	}
}
