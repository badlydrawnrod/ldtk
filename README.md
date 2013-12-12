# LDTK - a Ludum Dare Toolkit

_LDTK is a toolkit that makes it easy to get your LibGDX game up and running
for Ludum Dare._

[LibGDX] is a hugely powerful library and a great toolkit for building games and game frameworks. However, it takes time and thought to build a good framework, because there are so many ways of doing things, and time is the one luxury that you don't have during a [Ludum Dare] weekend.

That's where LDTK comes in.

LDTK makes it even easier to use [LibGDX], offering the following benefits:

* __Easy state management.__ You no longer need to worry about switching between
  screens. LDTK does this for you. For example, simply tell it that you want
  to change state from _Playing_ to _Menu_ and it will automate that process for you.

* __Convention-based asset management.__ Assets can be loaded automatically, or on
  demand, then later retrieved by name. Unused assets can be unloaded when they
  are no longer needed.

* __Simplified APIs.__ [LibGDX] has a hugely flexible API, offering a huge variety
  of options, but often all you want to do is display an image on the screen,
  centred on a particular coordinate. LDTK focuses on those common use cases,
  but it doesnt get in your way if you want to drop down to something more
  complicated.

* __An adaptive viewport.__ LDTK automatically adjusts the camera to fit the
  window, scaling it and changing the aspect ratio to fit your requirements.

* __A kernel.__ All of LDTKs functionality is driven by a simple, easily extended
  kernel.

* __A microframework not a wrapper.__ LDTK is a microframework that lets you use
  as much or as little of LibGDX as youd like. It doesnt attempt to hide
  [LibGDX] - it merely makes it easier to use.

## Philosophy
The whole idea behind LDTK is that it embodies some of the boilerplate decisions that have to be made when writing games in the 48 hour timeframe required by [Ludum Dare].

It isn't a replacement for [LibGDX], nor is it a wrapper that tries to hide it. It is simply a micro-framework that makes a few choices about how to use [LibGDX] to make it easier to create games in a short time frame.

## State Management
The LDTK kernel knows how to manage the transitions between game states, such as exiting from _Playing_ and entering into _Menu_, but it does not know about these states directly. It delegates the decision of choosing the next state to a _StateSelector_, which is injected via the kernel's constructor.

### Example
In the following example, _App_ implements _StateSelector_. It takes responsibility for creating two states, _Playing_ and _Menu_, and it overrides _select()_ so that the kernel knows what the next state will be.

When _App_ creates a state, it injects itself via the state's constructor, so that the state can call it back. This way, the _Menu_ state can request a transition to the _Playing_ state, without ever having to know about _Playing_ and vice-versa.

```java
public class App implements StateSelector {

    private Playing playing;
	private Menu menu;
	private State state;

	public App() {
		playing = new Playing(this);
		menu = new Menu(this);
		state = menu;
	}

	@Override
	public State select() {
		return state;
	}
	
	public void requestPlaying() {
		if (state == menu) {
			state = playing;
		}
	}
	
	public void requestMenu() {
		if (state == playing) {
			state = menu;
		}
	}
}
```
### State Logic
Just before updating the current state, the kernel calls the _StateSelector_ to determine the next state. If the state has changed, then the kernel will call the current state's _exit()_ method to exit that state, then it will call the new state's _enter()_ method before making that state current. Classes derived from _State_ can override _enter()_ and _exit()_ to perform state-specific setup and teardown.

```java
    State nextState = stateSelector.select();
    if (currentState != nextState) {
    	if (currentState != null) {
    		currentState.exit();
	   	}
	   	if (nextState != null) {
	   		nextState.enter();
	   	}
	    currentState = nextState;
    }
	if (currentState != null) {
	  	currentState.update();
	}
```

## Running a game with LDTK
When you run a game with [LibGDX], you pass it a class that implements  _ApplicationListener_. LDTK is no different in that respect, but every LDTK game always starts off by telling [LibGDX] to run the LDTK kernel, passing it an game-specific _StateSelector_ that the kernel calls to switch states as described above.

### Android Example
In Android, create an instance of _Kernel_ and pass it to _initialize()_.
```java
    StateSelector gameStateSelector = new App();
	Kernel kernel = new Kernel(gameStateSelector);
	initialize(kernel, cfg);
```

### Desktop Example
From the desktop, create an instance of _Kernel_ and pass it to _LwjglApplication()_.
```java
    StateSelector gameStateSelector = new App();
	Kernel kernel = new Kernel(gameStateSelector);
	new LwjglApplication(kernel, cfg);
```

## Asset Loading and Unloading
All assets in LDTK are loaded and unloaded using the _Assets_ class exposed via _Kernel.assets_. Assets follow a convention by which assets of different types are loaded from appropriately named paths.

* all __fonts__ are loaded from the _fonts_ path.
* all __sounds__ are loaded from the _sounds_ path.
* all __texture atlases__ are loaded from the  _atlases_ path.
* all __textures__ are loaded from the _textures_ path.

### Loading Assets at Startup
LDTK treats assets whose paths are immediately off the root path as _default_ assets to be loaded by the kernel at start time. You do not have to do anything to load these assets - if they're present then LDTK will load them automatically.

### Loading and Unloading Assets on Demand
Assets that are not immediately off the root path are not automatically loaded. They can be loaded through the _Assets_ class's _load()_ method and unloaded through its _unload()_ method.

Both of these methods operate on all asset types under a particular path, but it is also possible to load and unload specific asset types, eg, _loadTextures()_ will load only textures.

The following example shows the kernel being asked to load all textures under the "monkeys" path, ie, from "monkeys/textures".
```java
    Kernel.assets.loadTextures("monkeys");
```

## Accessing Assets
Once assets have been loaded, they can be accessed by name. The kernel exposes each asset store type via an appropriately named static member.

* the _Fonts_ class looks after all fonts and is accessed via _Kernel.fonts_.
* the _Images_ class looks after all images loaded from textures and texture atlases
  and is accessed via _Kernel.images_.
* the _Sounds_ class looks after all sounds and is accessed via _Kernel.sounds_.

Assets are named according to the path that they came from. In the following example, "RobotScan3" came from a texture atlas called "png1" that was loaded at startup, because its path is immediately off the root path.

```java
    Image image = Kernel.images.get("atlases/png1/RobotScan3");
```        

## Simplified APIs
LDTK attempts to keep APIs as simple as possible while still allowing full access to the underlying [LibGDX] APIs. A good example of this is the _Image_ class, which simply knows how to draw images centred on coordinates, optionally rotated. Questions such as what _SpriteBatch_ to use, have been take care of, as images are always drawn to _Kernel._batch_, but _Image_ still exposes the underlying [LibGDX] _TextureRegion_ object as it is sometimes useful to have a little more control.

### The Camera Class
The _Camera_ class is an orthographic camera that knows how to resize itself to the current window, optionally scissoring the viewport. There is only one camera active at a time, and the act of activating a camera disables the others. When a camera is activated then the kernel's _SpriteBatch_ is updated to reflect this and all subsequent drawing operations.

Cameras are activated with the _activate()_ method.
```java
    public activate();
```

Cameras can be rotated, moved and zoomed using the following methods.
```java
    public float angle();
	public void setAngle(float degrees);
	public Vector2 position();
	public float x();
	public float y();
	public void moveTo(float x, float y);
	public float zoom();
	public void setZoom(float zoom);
```

A camera's dimensions can be queried with the following methods, all of which take the zoom into account.
```java
    public float width();
    public float height();
    public float windowWidth();
    public float windowHeight();
```

And here are the methods that control scissoring.
```java
    public void enableScissoring();
    public void disableScissoring();
    public void setScissor(boolean isScissored);
    public boolean isScissored();
```

### The Font Class
The _Font_ class knows how to draw text to the screen at a point representing the bottom left corner of the text's bounding box. And that's it.

To draw a string with the _Font_ class, use the following.
```java
    public void draw(String text, float x, float y, Color color);
```

To get the size of text prior to drawing, use _bounds()_ as follows.
```java
    public Rectangle bounds(String text);
```

And to query the line height of a font, use this method.
```java
    public float height();
```

### The Image Class
The _Image_ class has one main function, to draw an image onto the screen. It assumes that all images are centred on their coordinates, and that no scaling ever takes place. However, images can be rotated about their coordinates.

It provides the following methods to do this.
```java
    public void draw(float x, float y);
    public void draw(float x, float y, float ccwDegrees);
```

### The Sound Class
The _Sound_ class's API is almost identical to that of [LibGDX].

  [LibGDX]: http://libgdx.badlogicgames.com/
  [Ludum Dare]: http://http://www.ludumdare.com/
  