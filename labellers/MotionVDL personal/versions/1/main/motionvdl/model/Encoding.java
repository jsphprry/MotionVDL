package motionvdl.model;

import java.io.IOException;

/**
 * Superclass for objects that have byte encoded forms
 * @author joseph
 */
public abstract class Encoding {

	// constants
	public static final int MAX_DIMENSION = 255;

	/**
	 * Encode data as a byte sequence
	 */
	public abstract byte[] encode();

	/**
	 * Instance export method
	 * @param location Filesystem location
	 * @throws IOException Problem accessing filesystem
	 */
	public void export(String location) throws IOException {
		FileSystem.writeBytes(this.encode(), location);
	}
}
