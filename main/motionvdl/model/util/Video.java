package motionvdl.model.util;

import java.awt.Color;

import motionvdl.Debug;
import motionvdl.model.Encoding;

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
	private final Color[][][] buffer;

	/**
	 * Construct video from 3d Color array
	 * @param videoBuffer Video data as 3d Color array
	 * @throws ArrayIndexOutOfBoundsException videoBuffer dimensions less than 1
	 */
	public Video(Color[][][] videoBuffer) throws ArrayIndexOutOfBoundsException {

		// debug trace
		Debug.trace("Created video from buffer");

		// set metadata
		length = videoBuffer.length;
		height = videoBuffer[0].length;
		width  = videoBuffer[0][0].length;

		// throw exceptions
		if (length < 1 || height < 1 || width < 1) throw new ArrayIndexOutOfBoundsException("Video error: buffer cannot have zero shape");

		// initialise buffer
		buffer = new Color[length][height][width];

		// populate buffer
		for (int i=0; i < length; i++) {
			for (int j=0; j < height; j++) {
				for (int k=0; k < width; k++) {
					buffer[i][j][k] = videoBuffer[i][j][k];
				}
			}
		}
	}

	/**
	 * Construct video from byte sequence
	 * @param encoding Video data as byte sequence
	 * @throws IllegalArgumentException Malformed byte sequence
	 */
	public Video(byte[] encoding) throws IllegalArgumentException {

		// debug trace
		Debug.trace("Created video from byte sequence");

		try {

			// decode metadata
			int z = encoding[0] & 0xFF;
			int y = encoding[1] & 0xFF;
			int x = encoding[2] & 0xFF;
			int msize = 3;

			// initialise workBuffer
			Color[][][] workBuffer = new Color[z][y][x];

			// decode into buffer
			for (int i=0; i < z; i++) {
				for (int j=0; j < y; j++) {
					for(int k=0; k < x; k++) {
						int grey = encoding[msize + i*y*x + j*x + k] & 0xFF;
						workBuffer[i][j][k] = new Color(grey,grey,grey);
					}
				}
			}

			// setup video
			length = z;
			height = y;
			width  = x;
			buffer = workBuffer;

		} catch (Exception e) {
			throw new IllegalArgumentException("Video error: problem decoding bytes");
		}
	}


	/**
	 * Create instance of noise
	 * @param width Resolution width
	 * @param height Resolution height
	 * @param length Length of video in #frames
	 */
	public static Video noise(int width, int height, int length) {
		
		// debug trace
		Debug.trace(String.format("Created (%d,%d,%d) noise buffer",width,height,length));
		
		// setup buffer
		Color[][][] videoBuffer = new Color[length][height][width];
		
		// populate buffer
		for (int i=0; i < length; i++) {
			for (int j=0; j < height; j++) {
				for (int k=0; k < width; k++) {
					int r = (int) (255 * Math.random());
					int g = (int) (255 * Math.random());
					int b = (int) (255 * Math.random());
					videoBuffer[i][j][k] = new Color(r, g, b);
				}
			}
		}

		// return wrapped buffers
		return new Video(videoBuffer);
	}


	/**
	 * Get video frame by index
	 * @param frameIndex Index of the frame
	 * @return Frame at index
	 */
	public Color[][] getFrame(int frameIndex) {
		return buffer[frameIndex];
	}


	/**
	 * Crop video resolution
	 * @param x Crop frame top left  x coordinate
	 * @param y Crop frame top left y coordinate
	 * @param w Crop frame width
	 * @param h Crop frame height
	 */
	public Video crop(int x, int y, int w, int h) {

		// debug trace
		Debug.trace(String.format("Video: resolution cropped from %sx%s to %sx%s from (%s,%s)",width,height,w,h,x,y));

		// throw invalid parameters
		if (0 > x || x >= width || 0 > y || y >= height) throw new IllegalArgumentException(String.format("Video error: invalid crop coordinate '(%d,%d)'",x,y));
		if (0 > w || w >= width-x || 0 > h || h >= height-y) throw new IllegalArgumentException(String.format("Video error: invalid crop resolution '%dx%d'",w,h));

		// initialise workBuffer
		Color[][][] workBuffer = new Color[length][h][w];

		// copy buffer values within crop frame into workBuffer
		for (int i=0; i < length; i++) {
			for (int j=0; j < h; j++) {
				for (int k=0; k < w; k++) {
					workBuffer[i][j][k] = buffer[i][y+j][x+k];
				}
			}
		}

		// return wrapped workBuffer
		return new Video(workBuffer);
	}


	/**
	 * Downscaling video resolution
	 * @param w Target resolution width
	 * @param h Target resolution height
	 */
	public Video downscale(final int w, final int h) {
		
		// debug trace
		Debug.trace(String.format("Video: resolution downscaled from %sx%s to %sx%s", width, height, w, h));
		
		// throw invalid parameters
		if (w < 0 || h < 0 || w > width || h > height) throw new IllegalArgumentException(String.format("Video error: downscale got invalid resolution '%dx%d'",w,h));
		
		// initialise workBuffer
		Color[][][] workBuffer = new Color[length][h][w];
		
		// determine pool properties
		final double pool_w = width / (double) w;   // pool width
		final double pool_h = height / (double) h;  // pool height
		final int pool_ixlim = (int) pool_w;        // samples on x axis
		final int pool_iylim = (int) pool_h;        // samples on y axis
		final int _volume = pool_ixlim*pool_iylim;  // total samples
		final double pool_ix = pool_w / pool_ixlim; // x axis sample seperation
		final double pool_iy = pool_h / pool_iylim; // y axis sample seperation
		
		// use average pooling to downscale buffer into workBuffer
		for (int i=0; i < length; i++) {
			for (int j=0; j < h; j++) {
				for (int k=0; k < w; k++) {
					
					// initialise RGB sums
					int channelR = 0;
					int channelG = 0;
					int channelB = 0;
					
					// pool RGB buffer samples
					for (double py=0; py < pool_iylim; py += pool_iy) {
						for (double px=0; px < pool_ixlim; px += pool_ix) {
							
							// map pool-space coordinate to buffer index
							int bx = (int) (k*pool_w + px);
							int by = (int) (j*pool_h + py);
							
							// add color channels at buffer index to RGB sums
							Color color = buffer[i][by][bx];
							channelR += color.getRed();
							channelG += color.getGreen();
							channelB += color.getBlue();
						}
					}
					
					// write pool RGB average to workBuffer
					workBuffer[i][j][k] = new Color(channelR / _volume, channelG / _volume, channelB / _volume);
				}
			}
		}
		
		// return wrapped workBuffer
		return new Video(workBuffer);
	}


	/**
	 * Convert video colors to greyscale
	 * @return The greyscale video
	 */
	public Video greyscale() {
		
		// debug trace
		Debug.trace("Video: converted to greyscale");
		
		// initialise workBuffer
		Color[][][] workBuffer = new Color[length][height][width];
		
		// write greyscale buffer Colors to workBuffer
		for (int d=0; d < length; d++) {
			for (int h=0; h < height; h++) {
				for (int w=0; w < width; w++) {
					Color color = buffer[d][h][w];
					int grey = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
					workBuffer[d][h][w] = new Color(grey, grey, grey);
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
	public byte[] encode() {
		
		// debug trace
		Debug.trace("Video: encoded as byte sequence");
		
		// determine metadata
		int z = length;
		int y = height;
		int x = width;
		int msize = 3;     // metadata volume
		int vsize = z*y*x; // video volume
		
		// throw invalid shape
		if (x > MAX_DIMENSION || y > MAX_DIMENSION || z > MAX_DIMENSION) throw new ArrayIndexOutOfBoundsException("Video error: buffer dimensions are too large to export");
		
		// encode metadata
		byte[] encoding = new byte[msize+vsize];
		encoding[0] = (byte) z;
		encoding[1] = (byte) y;
		encoding[2] = (byte) x;
		
		// encode greyscale video buffer
		for (int i=0; i < z; i++) {
			for (int j=0; j < y; j++) {
				for (int k=0; k < x; k++) {
					encoding[msize + i*y*x + j*x + k] = (byte) ((buffer[i][j][k].getRed() + buffer[i][j][k].getGreen() + buffer[i][j][k].getBlue()) / 3.0);
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
