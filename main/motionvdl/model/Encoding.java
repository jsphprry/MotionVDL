package motionvdl.model;

import java.io.FileOutputStream;

import motionvdl.Debug;

public abstract class Encoding {
	
	/**
	 * Encode data as a byte sequence
	 */
	public abstract byte[] encode();
	
	/**
	 * Encoding instance export method
	 * @param location
	 */
	public void export(String location) {
		export(this, location);
	}
	
	/**
	 * Write encoded data to filesystem location
	 * @param data The Encoding object
	 * @param location The filesystem location
	 */
	public static void export(Encoding data, String location) {
		
		// encode data
		byte[] encoding = data.encode();

		// try to write encoding to filesystem location
		try {
			String filename = location + ".mvdl";
			FileOutputStream stream = new FileOutputStream(filename);
			stream.write(encoding);
			stream.close();
			
			// debug trace
			Debug.trace("Encoding exported to " + filename);
		
		// catch and trace error
		} catch (Exception e) {
			Debug.trace("Error writing to file! " + e.getMessage());
		}
	}
}
