package motionvdl.model.data;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import motionvdl.Debug;

/**
 * Immutable wrapper for video data
 * @author Joseph
 */
public class Video extends Encoding {
	
	// metadata
	public final int width;
	public final int height;
	public final int length;
	
	// video buffer
	private final BufferedImage[] buffer;
	
	/**
	 * Construct video from buffered image array
	 * @param buffer Video data as buffered image array
	 * @throws ArrayIndexOutOfBoundsException workBuffer dimensions less than 1
	 */
	public Video(BufferedImage[] buffer) throws ArrayIndexOutOfBoundsException {
		
		// throw exceptions
		if (buffer.length < 1 || buffer[0].getHeight() < 1 || buffer[0].getWidth() < 1) throw new ArrayIndexOutOfBoundsException("Video error: buffer cannot have zero shape");
		
		// set metadata
		length = buffer.length;
		height = buffer[0].getHeight();
		width  = buffer[0].getWidth();
		
		// setup buffer;
		this.buffer = buffer;
		
		// debug trace
		Debug.trace(String.format("Created (%d,%d,%d) video from buffer",length,height,width));
	}

	/**
	 * Construct video from byte sequence
	 * @param encoding Video data as byte sequence
	 * @throws IllegalArgumentException Malformed byte sequence
	 */
	public Video(byte[] encoding) throws IllegalArgumentException {
		try {
			
			// read metadata
			int b0 = encoding[0] & 0xFF;
			int b1 = encoding[1] & 0xFF;
			int b2 = encoding[2] & 0xFF;
			int b3 = encoding[3] & 0xFF;
			int b4 = encoding[4] & 0xFF;
			int b5 = encoding[5] & 0xFF;
			
			// decode metadata
			length = b0*256+b1;
			height = b2*256+b3;
			width  = b4*256+b5;
			
			// decode buffer
			buffer = new BufferedImage[length];
			int offset = 6;
			for (int i=0; i < length; i++) {
				buffer[i] = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
				for (int j=0; j < height; j++) {
					for(int k=0; k < width; k++) {
						int g = (int) encoding[offset] & 0xFF;
						buffer[i].setRGB(k, j, new Color(g,g,g).getRGB());
						offset += 1;
					}
				}
			}
			
			// debug trace
			Debug.trace(String.format("Created (%d,%d,%d) video from byte sequence",length,height,width));

		} catch (Exception e) {
			throw new IllegalArgumentException("Video error: problem decoding bytes "+e.getMessage());
		}
	}
	
	
	/**
	 * Create video instance of noise
	 * @param width Resolution width
	 * @param height Resolution height
	 * @param length Length of video in #workBuffer
	 */
	public static Video noise(int width, int height, int length) {
		
		// debug trace
		Debug.trace(String.format("Created (%d,%d,%d) noise buffer",length,height,width));
		
		// initialise workBuffer
		BufferedImage[] workBuffer = new BufferedImage[length];
		
		// populate workBuffer
		for (int i=0; i < length; i++) {
			workBuffer[i] = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			for (int j=0; j < height; j++) {
				for (int k=0; k < width; k++) {
					int r = (int) (255 * Math.random());
					int g = (int) (255 * Math.random());
					int b = (int) (255 * Math.random());
					workBuffer[i].setRGB(k, j, new Color(r,g,b).getRGB());
				}
			}
		}
		
		// return wrapped workBuffer
		return new Video(workBuffer);
	}
	
	
	/**
	 * Get video frame by index
	 * @param index Index of the frame
	 * @return Image of frame
	 */
	public Image getFrame(int index) {
		return buffer[index];
	}
	
	
	/**
	 * Get video instance cropped to rectangular region
	 * @param x Top left x coordinate of the rectangular region
	 * @param y Top left y coordinate of the rectangular region
	 * @param w Width of the rectangular region
	 * @param h Height of the rectangular region
	 * @return Cropped video
	 */
	public Video getCropped(int x, int y, int w, int h) {
		
		// debug trace
		Debug.trace(String.format("Video: buffer resolution cropped from %sx%s to %sx%s from (%s,%s)",width,height,w,h,x,y));
		
		// throw invalid parameters
		if (0 > x || x >= width   || 0 > y || y >= height) throw new IllegalArgumentException(String.format("Video error: invalid crop coordinate '(%d,%d)'",x,y));
		if (0 > w || w > width-x || 0 > h || h > height-y) throw new IllegalArgumentException(String.format("Video error: invalid crop resolution '%dx%d'",w,h));
		
		// initialise workBuffer
		BufferedImage[] workBuffer = new BufferedImage[length];
		
		// populate workBuffer
		for (int i=0; i < length; i++) {
			
			// get sub image
			BufferedImage crop = buffer[i].getSubimage(x, y, w, h);
			
			// copy sub image into workBuffer
			workBuffer[i] = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			for (int j=0; j < h; j++){
				for (int k=0; k < w; k++){
					workBuffer[i].setRGB(k,j,crop.getRGB(k,j));
				}
			}
		}
		
		// return wrapped workBuffer
		return new Video(workBuffer);
	}
	
	
	/**
	 * Get video instance scaled to a target resolution
	 * @param w Target resolution width
	 * @param h Target resolution height
	 * @return Scaled video
	 */
	public Video getScaled(int w, int h) throws IllegalArgumentException {
		
		// debug trace
		Debug.trace(String.format("Video: buffer resolution scaled from %sx%s to %sx%s", width, height, w, h));
		
		// throw invalid parameters
		if (w < 1 || h < 1) throw new IllegalArgumentException(String.format("Video error: invalid scale resolution '%dx%d'",w,h));
		
		// initialise workBuffer
		BufferedImage[] workBuffer = new BufferedImage[length];
		
		// populate workBuffer
		for (int i=0; i < length; i++) {
			workBuffer[i] = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
			workBuffer[i].getGraphics().drawImage(buffer[i].getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING),0,0,null);
		}
		
		// return wrapped workBuffer
		return new Video(workBuffer);
	}


	/**
	 * Get video instance with colors converted to greyscale
	 * @return Greyscale video
	 */
	public Video getGreyscaled() {
		
		// debug trace
		Debug.trace("Video: buffer converted to greyscale");
		
		// initialise workBuffer
		BufferedImage[] workBuffer = new BufferedImage[length];
		
		// write greyscale buffer Colors to workBuffer
		for (int i=0; i < length; i++) {
			workBuffer[i] = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			for (int j=0; j < height; j++) {
				for (int k=0; k < width; k++) {
					int col   = buffer[i].getRGB(k,j);
					int red   = (col >> 16) & 255;
					int green = (col >> 8) & 255;
					int blue  = (col) & 255;
					int grey  = (red+green+blue) / 3;
					workBuffer[i].setRGB(k, j, new Color(grey,grey,grey).getRGB());
				}
			}
		}
		
		// return wrapped workBuffer
		return new Video(workBuffer);
	}


	/**
	 * Encode as byte sequence
	 * @return Byte sequence
	 */
	@Override
	public byte[] getEncoding() throws ArrayIndexOutOfBoundsException {
		
		// debug trace
		Debug.trace("Video: buffer encoded as byte sequence");
		
		// throw invalid shape
		if (length > SIXTEEN_BIT_LIMIT || 
			height > SIXTEEN_BIT_LIMIT || 
			width  > SIXTEEN_BIT_LIMIT
		) throw new ArrayIndexOutOfBoundsException("Video error: buffer dimensions exceed value limits");
		
		// determine volume
		int msize = 6;                           // metadata volume
		int bsize = length*height*width;         // buffer volume
		byte[] encoding = new byte[msize+bsize]; // total volume
		
		// encode metadata
		encoding[0] = (byte) Math.floor(length / 256);
		encoding[1] = (byte) (length % 256);
		encoding[2] = (byte) Math.floor(height / 256);
		encoding[3] = (byte) (height % 256);
		encoding[4] = (byte) Math.floor(width / 256);
		encoding[5] = (byte) (width % 256);
		
		// encode greyscale buffer
		int offset = msize;
		for (int i=0; i < length; i++) {
			for (int j=0; j < height; j++) {
				for (int k=0; k < width; k++) {
					
					// convery rgb to greyscale 255
					int rgb = buffer[i].getRGB(k,j);
					int grey = ((rgb >> 16 & 255)+(rgb >> 8 & 255)+(rgb & 255)) / 3;
					
					// write greyscale 255 to encoding as byte
					encoding[offset] = (byte) grey;
					offset += 1;
				}
			}
		}
		
		// return byte encoding
		return encoding;
	}
}



// this is a note for how to cast bytes into int 0-255
// will be useful for decoding the byte encoding

// https://stackoverflow.com/questions/28752835/is-possible-have-byte-from-0-to-255-in-java
//int a = 255;
//byte b = (byte) a;
//int c = (int) b & 0xFF;
//System.out.println(a+" "+b+" "+c);
