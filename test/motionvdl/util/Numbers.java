package motionvdl.util;

public final class Numbers {
	
	/**
	 * Biased random number 
	 * @param bias The bias added to the random number
	 * @param range The maximum value of the randon number
	 * @return Biased random number
	 */
	public static int biasRand(int bias, int range) {
		return bias + (int) (Math.random() * range);
	}
}
