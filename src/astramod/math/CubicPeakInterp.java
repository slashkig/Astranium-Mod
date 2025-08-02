package astramod.math;

import arc.math.*;

/** Interpolation function that starts at 0, peaks at a middle point, then decreases to a set end value. (0-1-0.X)
 * @author Slashkig
*/
public class CubicPeakInterp implements Interp {
	/** x-intercept between 0 and 1 */
	final float maxPoint;
	/** x-intercept greater than 1 */
	final float endPoint;

	/**
	 * @param maxPoint Point where the return value peaks (a where R = 1)
	 * @param endValue Return value at the endpoint (R where a = 1)
	 * */
	public CubicPeakInterp(float maxPoint, float endValue) {
		this.maxPoint = maxPoint;
		this.endPoint = (float)Math.pow(1 - maxPoint, 2) / (maxPoint * (endValue * maxPoint - 2) + 1);
	}

	@Override public float apply(float a) {
		return (float)Math.pow(a - maxPoint, 2) * (a - endPoint) / (maxPoint * maxPoint * endPoint) + 1;
	}

}
