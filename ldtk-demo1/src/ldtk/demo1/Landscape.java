package ldtk.demo1;

import com.badlogic.gdx.math.MathUtils;

class Landscape {

	private static final int LANDSCAPE_POINTS = 1000;
	private static final int TILE_WIDTH = 64;
	private static final float LOWER_MAX = 0;
	private static final float LOWER_MIN = -170;
	private static final float UPPER_MAX = 180;
	private static final float UPPER_MIN = LOWER_MAX - 64;

	private float[] lower;
	private float[] upper;

	public Landscape() {
		lower = createLowerLandscape();
		upper = createUpperLandscape();
	}

	// TODO: consider having this as a static factory and making the constructor private.
	private float[] createLowerLandscape() {
		float[] heights = new float[LANDSCAPE_POINTS];
		float mid = (LOWER_MAX + LOWER_MIN) / 2;
		for (int i = 0; i < heights.length; i++) {
			heights[i] = mid + MathUtils.sinDeg(i * 10f) * TILE_WIDTH;
			heights[i] = Math.max(heights[i], LOWER_MIN);
			heights[i] = Math.min(heights[i], LOWER_MAX);
		}
		return heights;
	}

	// TODO: consider having this as a static factory and making the constructor private.
	private float[] createUpperLandscape() {
		float[] heights = new float[LANDSCAPE_POINTS];
		for (int i = 0; i < heights.length; i++) {
			heights[i] = lower[i] + 100;
			heights[i] = Math.max(heights[i], UPPER_MIN);
			heights[i] = Math.min(heights[i], UPPER_MAX);
		}
		return heights;
	}

	public float[] lower() {
		return lower;
	}

	public float[] upper() {
		return upper;
	}
}
