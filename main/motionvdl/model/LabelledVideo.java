package motionvdl.model;

import java.awt.Color;
import java.awt.image.BufferedImage;

import motionvdl.Debug;
import motionvdl.model.util.Label;
import motionvdl.model.util.Video;

/**
 * Immutable container for video and label data
 * @author Joseph
 */
public class LabelledVideo extends Encoding {

	// constants
	public static final int LABEL_POINTS = 11;
	public final static int[] CONNECTOR_SEQUENCE = new int[] {0,0,1,2,1,4,1,6,7,6,9};

	// components
	public final Video video;
	public final Label label;

	/**
	 * Construct labelled video with arbitrary point count
	 */
	public LabelledVideo(Video video) {

		// setup components
		this.video = video;
		this.label = new Label(video.length, LABEL_POINTS);

		// debug trace
		Debug.trace("Created LabelledVideo");
	}

	/**
	 * Construct model from byte sequence
	 * @param encoding Byte sequence
	 * @throws IllegalArgumentException Malformed byte sequence
	 */
	public LabelledVideo(byte[] encoding) throws IllegalArgumentException {

		try {

			// read metadata
			int z = encoding[0] & 0xFF;
			int y = encoding[1] & 0xFF;
			int x = encoding[2] & 0xFF;
			int videoVolume = 3 + z*y*x;
			int labelVolume = encoding.length - videoVolume;

			// split encoding
			byte[] videoEncoding = new byte[videoVolume];
			byte[] labelEncoding = new byte[labelVolume];
			for (int i=0; i < videoVolume; i++) {videoEncoding[i] = encoding[i];}
			for (int i=0; i < labelVolume; i++) {labelEncoding[i] = encoding[videoVolume+i];}

			// decode
			video = new Video(videoEncoding);
			label =	new Label(labelEncoding);

		} catch (Exception e) {
			throw new IllegalArgumentException("LabelledVideo error: "+e.getMessage());
		}

		// debug trace
		Debug.trace("Created LabelledVideo");
	}

	/**
	 * Construct labelled video from prepared images
	 * @param frames Array of BufferedImages
	 * @throws ArrayIndexOutOfBoundsException Empty frames array
	 */
	public LabelledVideo(BufferedImage[] frames) throws ArrayIndexOutOfBoundsException {

		// convert BufferredImage array into video buffer
		int z = frames.length;
		int y = frames[0].getHeight();
		int x = frames[0].getWidth();
		Color[][][] videoBuffer = new Color[z][y][x];
		for (int i=0; i < z; i++) {
			for (int j=0; j < y; j++) {
				for (int k=0; k < x; k++) {
					int col = frames[i].getRGB(k,j);
					videoBuffer[i][j][k] = new Color((col >> 16) & 255, (col >> 8) & 255, col & 255);
				}
			}
		}

		// setup components
		video = new Video(videoBuffer);
		label = new Label(z, LABEL_POINTS);

		// debug trace
		Debug.trace("Created LabelledVideo");
	}


	/**
	 * Process labelled video by cropping to square, downscaling, then greyscaling video. Resets label component.
	 * @param x Crop frame x coordinate
	 * @param y Crop frame y coordinate
	 * @param crop Crop frame edge size
	 * @param target Target resolution
	 * @return Processed LabelledVideo
	 */
	public LabelledVideo process(int x, int y, int crop, int target) {
		try {
			return new LabelledVideo(video.crop(x,y,crop,crop).downscale(target,target).greyscale());
		} catch (IllegalArgumentException e) {
			return this;
		}
	}
	
	
	/**
	 * Concatenate video and label encodings
	 * @return Byte encoding
	 */
	@Override
	public byte[] encode() {
		
		// encode video and label
		byte[] encodedVideo = video.encode();
		byte[] encodedLabel = label.encode();
		int videoVolume = encodedVideo.length;
		int labelVolume = encodedLabel.length;
		
		// concatenate encodings
		byte[] encoding = new byte[videoVolume+labelVolume];
		for (int i=0; i < videoVolume; i++) {encoding[i] = encodedVideo[i];}
		for (int i=0; i < labelVolume; i++) {encoding[videoVolume+i] = encodedLabel[i];}
		
		// debug trace
		Debug.trace("LabelledVideo: encoded as byte sequence");
		
		return encoding;
	}
}