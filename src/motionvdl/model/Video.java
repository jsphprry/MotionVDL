package motionvdl.model;

import java.awt.Color;

/**
 * Wrapper class for a video represented as a 3D Color array
 * @author Joseph
 */
public class Video{
	
	// video metadata
	private int width;
	private int height;
	private int depth;
	private boolean greyscale;
	
	// video buffer
	private Color[][][] buffer;
	
	/**
	 * Constructor for Video instance from 3D Color array
	 * @param videoBuffer The 3D Color array to wrap as Video
	 */
	public Video(Color[][][] videoBuffer, boolean greyscale) {
		
		// setup metadata
		this.depth  = videoBuffer.length;
		this.height = videoBuffer[0].length;
		this.width  = videoBuffer[0][0].length;
		this.greyscale = greyscale;
		
		// setup buffer
		this.buffer = new Color[this.depth][this.height][this.width];
		for (int d=0; d < this.depth; d++) {
			for (int h=0; h < this.height; h++) {
				for (int w=0; w < this.width; w++) {
					this.buffer[d][h][w] = videoBuffer[d][h][w];
				}
			}
		}
	}
	
	
	/**
	 * Constructor for Video instance from encoded video file on disk
	 * @param file The location of the file to load
	 */
	public Video(String file) {
		throw new UnsupportedOperationException("Constructor for Video instance from file is unimplemented");
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
	 * Get video depth (frame-count)
	 * @return Video depth
	 */
	public int getDepth() {
		return this.depth;
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
		
		// throw invalid parameters
		if (origin_x < 0 || origin_y < 0 || origin_x >= this.width || origin_y >= this.height) throw new IllegalArgumentException("Invalid origin coordinate");
		if (target_w <= 0 || target_h <= 0 || origin_x+target_w > this.width || origin_y+target_h > this.height) throw new IllegalArgumentException("Invalid target resolution");
		
		// initialise work-buffer
		Color[][][] workBuffer = new Color[this.depth][target_h][target_w];
		
		// write crop-frame to work-buffer
		for (int d=0; d < this.depth; d++) {
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
		
		// throw invalid parameters
		if (target_w < 0 || target_h < 0 || target_w > this.width || target_h > this.height) throw new IllegalArgumentException("Invalid target resolution");
		
		// initialise work-buffer
		Color[][][] workBuffer = new Color[this.depth][target_h][target_w];
		
		// setup pool size
		int pool_h = this.height / target_h;
		int pool_w = this.width  / target_w;
		
		// use average pooling to downscale buffer into work-buffer
		for (int d=0; d < this.depth; d++) {
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
		
		// initialise work-buffer
		Color[][][] workBuffer = new Color[this.depth][this.height][this.width];
		
		// write greyscale buffer values to work-buffer
		for (int d=0; d < this.depth; d++) {
			for (int h=0; h < this.height; h++) {
				for (int w=0; w < this.width; w++) {
					
					// calculate greyscale value
					Color color = this.buffer[d][h][w];
					int grey = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
					
					// write greyscale Color to work-buffer
					this.buffer[d][h][w] = new Color(grey, grey, grey);
				}
			}
		}
		
		// return work-buffer as Video
		return new Video(workBuffer, true);
	}
	
	
	/**
	 * Export the video
	 * @return The exported video
	 */
	public boolean[] export() {
		
		// TODO implement video export
		throw new UnsupportedOperationException("Video export is unimplemented");
	}
}
