package ldtk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * A VirtualViewport is used to create a viewport that fits on the screen. The original was written by the Gemserk
 * guys.
 * See:  http://blog.gemserk.com/2013/02/13/our-solution-to-handle-multiple-screen-sizes-in-android-part-two/
 */
public class VirtualViewport {  
    
    private static final float EPSILON = 0.01f;
	private float virtualWidth;
    private float virtualHeight;
  
    /**
     * Creates a virtual viewport.
     * 
     * @param virtualWidth the viewport's width, not including additions for letterboxing.
     * @param virtualHeight the viewport's height, not including additions for letterboxing.
     */
    public VirtualViewport(float virtualWidth, float virtualHeight) {  
        this.virtualWidth = virtualWidth;  
        this.virtualHeight = virtualHeight;  
    }

    /**
     * Changes the viewport's size.
     */
    public void setSize(Vector2 size) {
    	setSize(size.x, size.y);
    }
    
    /**
     * Changes the viewport's size.
     */
    public void setSize(float virtualWidth, float virtualHeight) {
        this.virtualWidth = virtualWidth;  
        this.virtualHeight = virtualHeight;  
    }
  
    /**
     * Returns the viewport's virtual width, not including any letterboxing.
     * 
     * @return the virtual width.
     */
    public float virtualWidth() {  
        return virtualWidth;  
    }  
  
    /**
     * Returns the viewport's virtual height, not including any letterboxing.
     * 
     * @return the virtual height.
     */
    public float virtualHeight() {  
        return virtualHeight;  
    }
    
    /**
     * Returns the viewport's virtual aspect, not including any letterboxing.
     * 
     * @return the virtual aspect ratio.
     */
    public float virtualAspect() {
    	return virtualWidth / virtualHeight;
    }
  
    /** 
     * Returns the viewport width required to let all the virtual viewport be
     * shown on the screen.
     * 
     * @return the viewport's width, including letterboxing.
     */  
    public float width() {  
        return width(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  
    }  
  
    /** 
     * Returns the viewport height required to let all the virtual viewport be shown on the screen.
     * 
     * @return the viewport's height, including letterboxing.
     */
    public float height() {  
        return height(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  
    }  
  
    /** 
     * Returns the viewport width required to let all the virtual viewport be shown on the screen. 
     *  
     * @param screenWidth the screen width. 
     * @param screenHeight the screen height. 
     * 
     * @return the viewport's width, including letterboxing.
     */  
    public float width(float screenWidth, float screenHeight) {  
        float virtualAspect = virtualWidth / virtualHeight;  
        float aspect = screenWidth / screenHeight;  
        if (aspect > virtualAspect || (Math.abs(aspect - virtualAspect) < EPSILON)) {  
            return virtualHeight * aspect;  
        }
        else {  
            return virtualWidth;  
        }  
    }  
  
    /** 
     * Returns the viewport height required to let all the virtual viewport be shown on the screen. 
     *  
     * @param screenWidth the screen width. 
     * @param screenHeight the screen height. 
     * 
     * @return the viewport's height, including letterboxing.
     */  
    public float height(float screenWidth, float screenHeight) {  
        float virtualAspect = virtualWidth / virtualHeight;  
        float aspect = screenWidth / screenHeight;  
        if (aspect > virtualAspect || (Math.abs(aspect - virtualAspect) < EPSILON)) {  
            return virtualHeight;  
        }
        else {  
            return virtualWidth / aspect;  
        }  
    }
    
    @Override
    public String toString() {
    	return VirtualViewport.class.getSimpleName() + ": width x height = " + width() + " x " + height() +
    			", virtualWidth x virtualHeight = " + virtualWidth + " x " + virtualHeight +
    			", pixelWidth x pixelHeight = " + Gdx.graphics.getWidth() + " x " + Gdx.graphics.getHeight();
    }
}
