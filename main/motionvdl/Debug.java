package motionvdl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Debug {
	
	// utility
	private static boolean enabled = false;
	private static DateTimeFormatter timeStamp;
	private static PrintWriter printWriter;
	
	
	/*
	 * Setup file access
	 */
	public static void setup(boolean verbose) {
		
		// set init flag
		enabled = verbose;
		
		if (enabled) {
			
			// setup time stamp formatter
			timeStamp = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			
			// setup file writer
			try {
				printWriter = new PrintWriter(new FileWriter("mvdl.log"));
			
			// if there is an issue
			} catch (IOException e) {
				System.out.println("Problem setting up file writer. "+e.getMessage());
			}
		}
	}
	
	
	/**
	 * Record a debug trace
	 * @param message The message for the debug trace
	 */
	public static void trace(String message) {
		
		// if the trace is enabled
		if (enabled) {
			
			// format message with time stamp
			message = timeStamp.format(LocalDateTime.now()) + message;
			
			// print message to terminal
			System.out.println(message);
			
			// write to log file
			printWriter.print(message);
		}
	}
}
