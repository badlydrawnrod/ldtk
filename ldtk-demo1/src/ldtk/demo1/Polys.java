package ldtk.demo1;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

/**
 * Some functions that operate on groups of polygons.
 */
public class Polys {
	/**
	 * Tests if the given polygon is in collision with any of the other polygons.
	 * 
	 * @param poly the polygon.
	 * @param polygroup the other polygons.
	 * @return true if the polygon is in collision, otherwise false.
	 */
	public static boolean hitAny(Polygon poly, Polygon[] polygroup) {
		for (int i = polygroup.length - 1; i >= 0; i--) {
			Polygon other = polygroup[i];
			if (Intersector.overlapConvexPolygons(poly, other)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Tests if any polygon in on group of polygons is in collision with any of the polygons in the other group.
	 *   
	 * @param polygroupA the first group of polygons.
	 * @param polygroupB the second group of polygons.
	 * @return true if the polygons are in collision, otherwise false.
	 */
	public static boolean hitAny(Polygon[] polygroupA, Polygon[] polygroupB) {
		for (int i = polygroupA.length - 1; i >=0; i--) {
			Polygon poly = polygroupA[i];
			if (Polys.hitAny(poly,  polygroupB)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calculates the bounding rectangle of a group of polygons.
	 * 
	 * @param polygroup the group of polygons.
	 * @return the bounding rectangle of the polygons.
	 */
	public static Rectangle bounds(Polygon[] polygroup) {
		Rectangle result = new Rectangle(polygroup[0].getBoundingRectangle());
		for (int i = 1; i < polygroup.length; i++) {
			result.merge(polygroup[i].getBoundingRectangle());
		}
		return result;
	}
}
