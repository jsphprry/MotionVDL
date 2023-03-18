package motionvdl.model;

import java.awt.Color;

import motionvdl.Debug;

/**
 * Wrapper class for a 3D Color array representing video data
 * @author Joseph
 */
public class Video extends Encoding {
	
	// video metadata
	public final int width;
	public final int height;
	public final int length;
	public final boolean greyscale;
	
	// video buffer
	private Color[][][] buffer;
	
	/**
	 * Constructor for Video instance from 3D Color array
	 * @param videoBuffer The 3D Color array to wrap as Video
	 */
	public Video(Color[][][] videoBuffer, boolean greyscaleFlag) {
		
		// setup metadata
		length = videoBuffer.length;
		height = videoBuffer[0].length;
		width  = videoBuffer[0][0].length;
		greyscale = greyscaleFlag;
		
		// setup buffer
		buffer = new Color[length][height][width];
		for (int d=0; d < length; d++) {
			for (int h=0; h < height; h++) {
				for (int w=0; w < width; w++) {
					buffer[d][h][w] = videoBuffer[d][h][w];
				}
			}
		}
	}
	
	
	/**
	 * Create a Video instance of noise
	 * @param width The resolution width
	 * @param height The resolution height
	 * @param frames The number of frames in the video
	 * @return Video instance
	 */
	public static Video noise(int width, int height, int frames) {
		
		// debug trace
		Debug.trace("Created noise video");
		
		Color[][][] videoBuffer = new Color[frames][height][width];
		
		for (int i=0; i < frames; i++) {
			for (int j=0; j < height; j++) {
				for (int k=0; k < width; k++) {
					
					// randomise color channels
					int r = (int) (255 * Math.random());
					int g = (int) (255 * Math.random());
					int b = (int) (255 * Math.random());
					
					// set color
					videoBuffer[i][j][k] = new Color(r, g, b);
				}
			}
		}
		
		return new Video(videoBuffer, false);
	}
	
	
	/**
	 * Load a video file into Video instance
	 * @param file The location of the file to load
	 * @return Video instance
	 */
	public static Video fromFile(String file) {
		
		// debug trace
		Debug.trace("Loaded video from "+file);
		
		throw new UnsupportedOperationException("fromFile is unimplemented");
	}
	
	
	/**
	 * Get video frame by index
	 * @param index Index of the frame
	 * @return Frame at index
	 */
	public Color[][] getFrame(int index) {
		return buffer[index];
	}
	
	
	/**
	 * Video resolution cropping
	 * @param x The top-left crop frame x coordinate 
	 * @param x The top-left crop frame y coordinate 
	 * @param size The crop frame edge size
	 * @return The cropped video
	 */
	public Video squareCrop(int x, int y, int size) {
		
		// throw invalid parameters
		if (0 > x || x >= width) throw new IllegalArgumentException("Invalid x coord");
		if (0 > y || y >= height) throw new IllegalArgumentException("Invalid y coord");
		if (0 > size || size >= Math.min(height,width)) throw new IllegalArgumentException("Invalid size");
		
		// setup work-buffer
		Color[][][] workBuffer = new Color[length][size][size];
		
		// copy buffer values within crop frame to work-buffer
		for (int i=0; i < length; i++) {
			for (int j=0; j < size; j++) {
				for (int k=0; k < size; k++) {
					workBuffer[i][j][k] = buffer[i][y+j][x+k];
				}
			}
		}
		
		// return work-buffer as Video
		Debug.trace(String.format("Video resolution cropped from %sx%s to %sx%s", width, height, size, size));
		return new Video(workBuffer, greyscale);
	}
	
	
	/**
	 * Video resolution downscaling
	 * @param target_w target resolution width
	 * @param target_h target resolution height
	 * @return The downscaled video
	 */
	public Video downScale(int target_w, int target_h) {
		
		// throw invalid parameters
		if (target_w < 0 || target_h < 0 || target_w > width || target_h > height) throw new IllegalArgumentException("Invalid target resolution");
		
		// initialise work-buffer
		Color[][][] workBuffer = new Color[length][target_h][target_w];
		
		// setup pool size
		int pool_h = height / target_h;
		int pool_w = width  / target_w;
		
		// use average pooling to downscale buffer into work-buffer
		for (int d=0; d < length; d++) {
			for (int h=0; h < target_h; h++) {
				for (int w=0; w < target_w; w++) {
					 
					// setup color channel sums
					int channelRed   = 0;
					int channelGreen = 0;
					int channelBlue  = 0;
					int poolVolume   = 0;
					
					// calculate color channel sums
					for (int ph=0; ph < pool_h; ph++) {
						for (int pw=0; pw < pool_w; pw++) {
							if (h*pool_h + ph < height && w*pool_w + pw < width) {
								
								// get color from buffer
								Color color = buffer[d][h*pool_h + ph][w*pool_w + pw];
								
								// add to color sum
								channelRed   += color.getRed();
								channelGreen += color.getGreen();
								channelBlue  += color.getBlue();
								poolVolume   += 1;
							}
						}
					}
					
					// write average color channels as Color to work-buffer
					workBuffer[d][h][w] = new Color(channelRed / poolVolume, channelGreen / poolVolume, channelBlue / poolVolume);
				}
			}
		}
		
		// return work-buffer as Video
		Debug.trace(String.format("Video resolution downscaled from %sx%s to %sx%s", width, height, target_w, target_h));
		return new Video(workBuffer, greyscale);
	}
	
	
	/**
	 * Convert video colors to greyscale
	 * @return The greyscale video
	 */
	public Video greyScale() {
		
		// initialise work-buffer
		Color[][][] workBuffer = new Color[length][height][width];
		
		// write greyscale buffer values to work-buffer
		for (int d=0; d < length; d++) {
			for (int h=0; h < height; h++) {
				for (int w=0; w < width; w++) {
					
					// calculate greyscale value
					Color color = buffer[d][h][w];
					int grey = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
					
					// write greyscale Color to work-buffer
					workBuffer[d][h][w] = new Color(grey, grey, grey);
				}
			}
		}
		
		// return work-buffer as Video
		Debug.trace("Video converted to greyscale");
		return new Video(workBuffer, true);
	}
	
	
	/**
	 * Encode the video as a byte array
	 * @return The encoded video
	 */
	public byte[] encode() {
		
		// setup metadata
		int z = length;              // The length of the video buffer
		int y = height;              // The height of the video buffer
		int x = width;               // The width of the video buffer
		int c = (greyscale) ? 1 : 3; // The number of channels in each color
		
		// throw invalid size
		if (x > 255 || y > 255 || z > 255) throw new ArrayIndexOutOfBoundsException("The video buffer is too large to export");
		
		// encode metadata
		byte[] encoding = new byte[4 + z*y*x*c];
		encoding[0] = (byte) z;
		encoding[1] = (byte) y;
		encoding[2] = (byte) x;
		encoding[3] = (byte) c;
		
		// encode each n-channel color in n-bytes
		// for greyscale video
		if (greyscale) {
			for (int i=0; i < z; i++) {
				for (int j=0; j < y; j++) {
					for (int k=0; k < x; k++) {
						encoding[4 + i*y*x + j*x + k] = (byte) buffer[i][j][k].getRed();
					}
				}
			}
		
		// for color video
		} else {
			for (int i=0; i < z; i++) {
				for (int j=0; j < y; j++) {
					for (int k=0; k < x; k++) {
						encoding[4 + i*y*x*c + j*x*c + k*c + 0] = (byte) buffer[i][j][k].getRed();
						encoding[4 + i*y*x*c + j*x*c + k*c + 1] = (byte) buffer[i][j][k].getGreen();
						encoding[4 + i*y*x*c + j*x*c + k*c + 2] = (byte) buffer[i][j][k].getBlue();
					}
				}
			}
		}
		
		// return byte encoding
		Debug.trace("Video encoded as byte sequence");
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