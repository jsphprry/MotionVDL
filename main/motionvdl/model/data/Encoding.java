package motionvdl.model.data;

import java.io.IOException;

import motionvdl.model.FileSystem;

/**
 * Superclass for objects that have byte encoded forms
 * Includes a collection of static utility methods for accessing the filesystem
 * @author joseph
 */
public abstract class Encoding {
	
	// constants
	public static final int SIXTEEN_BIT_LIMIT = 255*256+255;
	public static final int EIGHT_BIT_LIMIT = 255;
	
	/**
	 * Encode data as a byte sequence
	 * @return Byte sequence
	 * @throws ArrayIndexOutOfBoundsException Exceeding value limits
	 */
	public abstract byte[] getEncoding() throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Instance export method
	 * @param location Filesystem location
	 * @throws IOException Problem accessing filesystem
	 */
	public void export(String location) throws IOException {
		FileSystem.writeBytes(this.getEncoding(), location);
	}
}
