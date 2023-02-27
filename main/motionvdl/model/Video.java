package motionvdl.model;

import java.awt.Color;

import motionvdl.Debug;

/**
 * Wrapper class for a video represented by a 3D Color array
 * @author Joseph
 */
public class Video {
	
	// video metadata
	private int width;
	private int height;
	private int frames;
	private boolean greyscale;
	
	// video buffer
	private Color[][][] buffer;
	
	/**
	 * Constructor for Video instance from 3D Color array
	 * @param videoBuffer The 3D Color array to wrap as Video
	 */
	public Video(Color[][][] videoBuffer, boolean greyscale) {
		
		// setup metadata
		this.frames  = videoBuffer.length;
		this.height = videoBuffer[0].length;
		this.width  = videoBuffer[0][0].length;
		this.greyscale = greyscale;
		
		// setup buffer
		this.buffer = new Color[this.frames][this.height][this.width];
		for (int d=0; d < this.frames; d++) {
			for (int h=0; h < this.height; h++) {
				for (int w=0; w < this.width; w++) {
					this.buffer[d][h][w] = videoBuffer[d][h][w];
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
		Debug.trace("Created video of noise");
		
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
	 * Get video resolution width
	 * @return Video width
	 */
	public int getWidth() {
		return this.width;
	}
	
	
	/**
	 * Get video resolution height
	 * @return Video height
	 */
	public int getHeight() {
		return this.height;
	}
	
	
	/**
	 * Get video frame count
	 * @return Video frames
	 */
	public int getFrames() {
		return this.frames;
	}
	
	
	/**
	 * Get video frame by index
	 * @param index Index of the frame
	 * @return Frame at index
	 */
	public Color[][] getFrame(int index) {
		return this.buffer[index];
	}
	
	
	/**
	 * Video resolution cropping
	 * @param origin_x crop origin coordinate
	 * @param origin_y crop origin coordinate
	 * @param target_w target resolution width
	 * @param target_h target resolution height
	 * @return The cropped video
	 */
	public Video crop(int origin_x, int origin_y, int target_w, int target_h) {
		
		// debug trace
		Debug.trace(String.format("Cropped video resolution from %sx%s to %sx%s", this.width, this.height, target_w, target_h));
		
		// throw invalid parameters
		if (origin_x < 0 || origin_y < 0 || origin_x >= this.width || origin_y >= this.height) throw new IllegalArgumentException("Invalid origin coordinate");
		if (target_w <= 0 || target_h <= 0 || origin_x+target_w > this.width || origin_y+target_h > this.height) throw new IllegalArgumentException("Invalid target resolution");
		
		// initialise work-buffer
		Color[][][] workBuffer = new Color[this.frames][target_h][target_w];
		
		// write crop-frame to work-buffer
		for (int d=0; d < this.frames; d++) {
			for (int h=0; h < target_h; h++) {
				for (int w=0; w < target_w; w++) {
					workBuffer[d][h][w] = this.buffer[d][origin_y+h][origin_x+w];
				}
			}
		}
		
		// return work-buffer as Video
		return new Video(workBuffer, this.greyscale);
	}
	
	
	/**
	 * Video resolution downscaling
	 * @param target_w target resolution width
	 * @param target_h target resolution height
	 * @return The downscaled video
	 */
	public Video downScale(int target_w, int target_h) {
		
		// debug trace
		Debug.trace(String.format("Downscaled video resolution from %sx%s to %sx%s", this.width, this.height, target_w, target_h));
		
		// throw invalid parameters
		if (target_w < 0 || target_h < 0 || target_w > this.width || target_h > this.height) throw new IllegalArgumentException("Invalid target resolution");
		
		// initialise work-buffer
		Color[][][] workBuffer = new Color[this.frames][target_h][target_w];
		
		// setup pool size
		int pool_h = this.height / target_h;
		int pool_w = this.width  / target_w;
		
		// use average pooling to downscale buffer into work-buffer
		for (int d=0; d < this.frames; d++) {
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
							if (h*pool_h + ph < this.height && w*pool_w + pw < this.width) {
								
								// get color from buffer
								Color color = this.buffer[d][h*pool_h + ph][w*pool_w + pw];
								
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
		return new Video(workBuffer, this.greyscale);
	}
	
	
	/**
	 * Convert video colors to greyscale
	 * @return The greyscale video
	 */
	public Video greyScale() {
		
		// debug trace
		Debug.trace("Converted video to greyscale");
		
		// initialise work-buffer
		Color[][][] workBuffer = new Color[this.frames][this.height][this.width];
		
		// write greyscale buffer values to work-buffer
		for (int d=0; d < this.frames; d++) {
			for (int h=0; h < this.height; h++) {
				for (int w=0; w < this.width; w++) {
					
					// calculate greyscale value
					Color color = this.buffer[d][h][w];
					int grey = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
					
					// write greyscale Color to work-buffer
					workBuffer[d][h][w] = new Color(grey, grey, grey);
				}
			}
		}
		
		// return work-buffer as Video
		return new Video(workBuffer, true);
	}
	
	
	/**
	 * Encode the video as a byte array
	 * @return The encoded video
	 */
	public byte[] encode() {
		
		// debug trace
		Debug.trace("Encoded video");
		
		// setup metadata
		int z = this.frames;              // The depth of the video buffer
		int y = this.height;              // The height of the video buffer
		int x = this.width;               // The width of the video buffer
		int c = (this.greyscale) ? 1 : 3; // The number of channels in each color
		
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
		if (this.greyscale) {
			for (int i=0; i < z; i++) {
				for (int j=0; j < y; j++) {
					for (int k=0; k < x; k++) {
						encoding[4 + i*y*x + j*x + k] = (byte) this.buffer[i][j][k].getRed();
					}
				}
			}
		
		// for color video
		} else {
			for (int i=0; i < z; i++) {
				for (int j=0; j < y; j++) {
					for (int k=0; k < x; k++) {
						encoding[4 + i*y*x*c + j*x*c + k*c + 0] = (byte) this.buffer[i][j][k].getRed();
						encoding[4 + i*y*x*c + j*x*c + k*c + 1] = (byte) this.buffer[i][j][k].getGreen();
						encoding[4 + i*y*x*c + j*x*c + k*c + 2] = (byte) this.buffer[i][j][k].getBlue();
					}
				}
			}
		}
		
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