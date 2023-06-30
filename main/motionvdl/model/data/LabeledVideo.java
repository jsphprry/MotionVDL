package motionvdl.model.data;

import motionvdl.Debug;

/**
 * Immutable container for video and label data
 * @author Joseph
 */
public class LabeledVideo extends Encoding {
	
	// constants
	public static final int NODE_COUNT = 11;
	public static final int[] CONNECTOR_SEQUENCE = new int[] {0,0,1,2,1,4,1,6,7,6,9};
	
	// components
	public final Video video;
	public final Label label;
	
	/**
	 * Construct Labeled video with default node count
	 * @param video Video instance to contain
	 */
	public LabeledVideo(Video video) {
		this(video, NODE_COUNT);
	}
	
	/**
	 * Construct labeled video with arbitrary point count
	 * @param video Video instance to contain
	 * @param nodes Nodes per frame label
	 */
	public LabeledVideo(Video video, int nodes) {
		
		// setup components
		this.video = video;
		this.label = new Label(video.length, sequence);
		
		// debug trace
		Debug.trace("Created LabelledVideo");
	}

	/**
	 * Construct model from byte sequence
	 * @param encoding Byte sequence
	 * @throws IllegalArgumentException Malformed byte sequence
	 */
	public LabeledVideo(byte[] encoding) throws IllegalArgumentException {
		
		// read metadata
		int b0 = encoding[0] & 0xFF;
		int b1 = encoding[1] & 0xFF;
		int b2 = encoding[2] & 0xFF;
		int b3 = encoding[3] & 0xFF;
		int b4 = encoding[4] & 0xFF;
		int b5 = encoding[5] & 0xFF;
		
		// decode volume
		int videoVolume = 6 + (b0*256+b1)*(b2*256+b3)*(b4*256+b5);
		int labelVolume = encoding.length - videoVolume;

		// split encoding
		byte[] videoEncoding = new byte[videoVolume];
		byte[] labelEncoding = new byte[labelVolume];
		for (int i=0; i < videoVolume; i++) {videoEncoding[i] = encoding[i];}
		for (int i=0; i < labelVolume; i++) {labelEncoding[i] = encoding[videoVolume+i];}

		// decode components
		video = new Video(videoEncoding);
		label =	new Label(labelEncoding);

		// debug trace
		Debug.trace("Created LabelledVideo");
	}
	
	
	/**
	 * Process labelled video by cropping to square, scaling to target resolution, then greyscaling video. Resets label component.
	 * @param x Top left x coordinate of the square crop region
	 * @param y Top left y coordinate of the square crop region
	 * @param crop Width and height of the square crop region
	 * @param target Scale target resolution width and height
	 * @return Processed instance
	 */
	public LabeledVideo getProcessed(int x, int y, int crop, int target) throws IllegalArgumentException {
		return new LabeledVideo(video.getCropped(x,y,crop,crop).getScaled(target,target).getGreyscaled());
	}
	
	
	/**
	 * Concatenate video and label encodings
	 * @return Byte encoding
	 */
	@Override
	public byte[] getEncoding() {
		
		// encode video and label
		byte[] encodedVideo = video.getEncoding();
		byte[] encodedLabel = label.getEncoding();
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
