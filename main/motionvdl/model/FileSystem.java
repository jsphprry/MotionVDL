package motionvdl.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.imageio.ImageIO;

import motionvdl.Debug;

/**
 * Collection of static utility methods for accessing the filesystem
 * @author Joseph
 */
public final class FileSystem {
	
	// constants
	public static final int IMPORT_LIMIT = Encoding.MAX_DIMENSION;
	
	// uninstantiable object
	private FileSystem() {}
	
	
	/**
	 * Write encoded instance to filesystem location
	 * @param data The Encoding object
	 * @param location The filesystem location
	 * @throws IOException Problem accessing filesystem
	 */
	public static void writeBytes(byte[] encoding, String location) throws IOException {
		
		// debug trace
		Debug.trace(String.format("FileSystem: write bytes '%s'",location));
		
		try {
			
			FileOutputStream output = new FileOutputStream(location);
			output.write(encoding);
			output.close();
			
		} catch (Exception e) {
			throw new IOException("FileSystem error: "+e.getMessage());
		}
	}
	
	
	/**
	 * Read bytes from filesystem location
	 * @param location File system location
	 * @return Byte sequence
	 * @throws IOException Problem accessing filesystem
	 */
	public static byte[] readBytes(String location) throws IOException {
		
		// debug trace
		Debug.trace(String.format("FileSystem: read bytes '%s'",location));
		
		try {
			
			// initialise encoding
			File file = new File(location);
			byte[] encoding = new byte[(int) file.length()]; // default zero
			
			// read bytes
			FileInputStream input = new FileInputStream(file);
			input.read(encoding);
			input.close();
			
			// return byte sequence
			return encoding;
			
		} catch (Exception e) {
			throw new IOException("FileSystem error: "+e.getMessage());
		}
	}
	
	
	/**
	 * Load images from directory on filesystem
	 * @param location Filesystem directory location
	 * @return BufferedImage array
	 * @throws IOException Problem accessing filesystem
	 */
	public static BufferedImage[] readImages(String location) throws IOException {
		
		// debug trace
		Debug.trace(String.format("FileSystem: read images '%s'",location));
		
		// check if the path is a directory
		File directory = new File(location);
		if (!directory.isDirectory()) throw new IOException("FileSystem error: not a directory");
		
		try {
			
			// get numerically sorted array of files in the directory
			File[] files = directory.listFiles();
			Arrays.sort(files, new Comparator<File>() {
				@Override
				public int compare(File aFile, File bFile) {
					int a = Integer.parseInt(aFile.getName().split("\\.")[0]);
					int b = Integer.parseInt(bFile.getName().split("\\.")[0]);
					return a - b;
				}
			});
			
			// read each file into BufferedImage array
			BufferedImage[] frames = new BufferedImage[Math.min(IMPORT_LIMIT,files.length)];
			for (int i=0; i < frames.length; i++) {
				frames[i] = ImageIO.read(files[i]);
			}
			
			// return BufferedImage array
			return frames;
			
		} catch (Exception e) {
			throw new IOException("FileSystem error: "+e.getMessage());
		}
	}


//	/**
//	 * Load up to 255 frames from a video file
//	 * @param location Filesystem video location
//	 * @return BufferedImage array
//	 * @throws IOException Problem accessing filesystem
//	 */
//	public static BufferedImage[] readFrames(String location) throws IOException {
//
//		// debug trace
//		Debug.trace(String.format("FileSystem: read video '%s'",location));
//
//		try {
//
//			// setup
//			FFmpegFrameGrabber player = new FFmpegFrameGrabber(location);
//			Java2DFrameConverter converter = new Java2DFrameConverter();
//			BufferedImage[] frames = new BufferedImage[IMPORT_LIMIT];
//
//			// convert video frames into BufferedImage array
//			player.start();
//			for (int i=0; i < frames.length; i++) {
//				frames[i] = converter.convert(player.grabImage());
//			}
//			player.stop();
//
//			// close resources and return
//			player.close();
//			converter.close();
//			return frames;
//
//		} catch (Exception e) {
//			throw new IOException("FileSystem error: "+e.getMessage());
//		}
//	}
}
