package ldtk.demo1;

import ldtk.Kernel;
import ldtk.Camera;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class LandscapeRenderer {
	
	private static final int VERTS_PER_QUAD = 20;
	private static final int TILE_WIDTH = 64;
	private static final float SCALING = 4.0f;
	private static final float LANDSCAPE_TOP = 180;
	private static final float LANDSCAPE_BOTTOM = -180;
	
	private Texture texture;
	private float[] lowerVertices;
	private float[] upperVertices;

	public LandscapeRenderer(Landscape landscape, Texture texture, Color color) {
		lowerVertices = createLowerLandscapeAppearance(landscape.lower(), color);
		upperVertices = createUpperLandscapeAppearance(landscape.upper(), color);
		this.texture = texture;
	}
	
	private float[] createLowerLandscapeAppearance(float[] heights, Color color) {
		// Points are defined anti-clockwise from top left.
		// Each point is: x, y, colour, u, v.
		float colorBits = color.toFloatBits();
		float[] landscape = new float[VERTS_PER_QUAD * (heights.length - 1)];
		for (int i = 0, j = 0; i < heights.length - 1; i++, j += VERTS_PER_QUAD) {
			// Top left.
			landscape[j + 0] = i * TILE_WIDTH;															// x
			landscape[j + 1] = heights[i];																// y
			landscape[j + 2] = colorBits;																// colour
			landscape[j + 3] = i / SCALING;																// u
			landscape[j + 4] = 1 - ((LANDSCAPE_BOTTOM - landscape[j + 1]) / TILE_WIDTH) / SCALING;		// v
			
			// Bottom left.
			landscape[j + 5] = i * TILE_WIDTH;															// x
			landscape[j + 6] = LANDSCAPE_BOTTOM;														// y
			landscape[j + 7] = colorBits;																// colour
			landscape[j + 8] = i / SCALING;																// u
			landscape[j + 9] = 1;																		// v
			
			// Bottom right.
			landscape[j + 10] = (i + 1) * TILE_WIDTH;													// x
			landscape[j + 11] = LANDSCAPE_BOTTOM;														// y
			landscape[j + 12] = colorBits;																// colour
			landscape[j + 13] = (i + 1) / SCALING;														// u
			landscape[j + 14] = 1;																		// v
			
			// Top right.
			landscape[j + 15] = (i + 1) * TILE_WIDTH;													// x
			landscape[j + 16] = heights[i + 1];															// y
			landscape[j + 17] = colorBits;																// colour
			landscape[j + 18] = (i + 1) / SCALING;														// u
			landscape[j + 19] = 1 - ((LANDSCAPE_BOTTOM - landscape[j + 16]) / TILE_WIDTH) / SCALING;	// v
		}
		
		return landscape;
	}
	
	private float[] createUpperLandscapeAppearance(float[] heights, Color color) {
		// Points are defined anti-clockwise from top left.
		// Each point is: x, y, colour, u, v.
		float colorBits = color.toFloatBits();
		float[] landscape = new float[VERTS_PER_QUAD * (heights.length - 1)];
		for (int i = 0, j = 0; i < heights.length - 1; i++, j += VERTS_PER_QUAD) {
			// Top left.
			landscape[j + 0] = i * TILE_WIDTH;															// x
			landscape[j + 1] = LANDSCAPE_TOP;															// y
			landscape[j + 2] = colorBits;																// colour
			landscape[j + 3] = i / SCALING;																// u
			landscape[j + 4] = 0;																		// v
			
			// Bottom left.
			landscape[j + 5] = i * TILE_WIDTH;															// x
			landscape[j + 6] = heights[i];																// y
			landscape[j + 7] = colorBits;																// colour
			landscape[j + 8] = i / SCALING;																// u
			landscape[j + 9] = ((heights[i] - LANDSCAPE_TOP) / TILE_WIDTH) / SCALING;					// v
			
			// Bottom right.
			landscape[j + 10] = (i + 1) * TILE_WIDTH;													// x
			landscape[j + 11] = heights[i + 1];															// y
			landscape[j + 12] = colorBits;																// colour
			landscape[j + 13] = (i + 1) / SCALING;														// u
			landscape[j + 14] = ((heights[i + 1] - LANDSCAPE_TOP) / TILE_WIDTH) / SCALING;				// v
			
			// Top right.
			landscape[j + 15] = (i + 1) * TILE_WIDTH;													// x
			landscape[j + 16] = LANDSCAPE_TOP;															// y
			landscape[j + 17] = colorBits;																// colour
			landscape[j + 18] = (i + 1) / SCALING;														// u
			landscape[j + 19] = 0;																		// v
		}
		
		return landscape;
	}

	public void draw(Camera cam) {
		float left = cam.x() - cam.width() / 2;
		float right = cam.x() + cam.width() / 2;
		
		// Go right until we find the first visible strip of landscape.
		int start;
		for (start = 0; start < lowerVertices.length; start += VERTS_PER_QUAD) {
			if (lowerVertices[start + 15] >= left) {
				break;
			}
		}
		
		// Go right from that point until we find the first non visible strip of landscape.
		int end;
		for (end = start; end < lowerVertices.length; end += VERTS_PER_QUAD) {
			if (lowerVertices[end] >= right) {
				break;
			}
		}
		
		// Figure out how many strips we need to draw.
		int count = end - start;
		
		// Finally, draw the landscape.
		Kernel.batch.draw(texture, lowerVertices, start, count);
		Kernel.batch.draw(texture, upperVertices, start, count);	// Assumption!!!
	}
}