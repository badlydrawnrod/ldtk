package ldtk;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Assets {

	public static final String DEFAULT_PATH = "";

	private AssetManager assetManager;
	private Images images;
	private Fonts fonts;
	private Sounds sounds;

	/**
	 * Creates the Assets object, telling it where to store loaded assets.
	 * 
	 * @param images
	 *            where to store images (loaded from textures and atlases).
	 * @param fonts
	 *            where to store fonts.
	 * @param sounds
	 *            where to store sounds.
	 */
	public Assets(Images images, Fonts fonts, Sounds sounds) {
		this.images = images;
		this.fonts = fonts;
		this.sounds = sounds;
		assetManager = new AssetManager();
	}

	/**
	 * Loads all default assets. Default assets are those without a path prefix.
	 * Their subpaths are located off the root path. For example, default sounds
	 * are in "sounds", not "level1/sounds".
	 */
	public void loadDefaults() {
		load(DEFAULT_PATH);
	}

	/**
	 * Loads all assets from the given path.
	 * 
	 * @param path
	 *            the base path of the assets, eg, "level1".
	 */
	public void load(String path) {
		loadAtlases(path);
		loadTextures(path);
		loadSounds(path);
		loadFonts(path);
	}

	/**
	 * Unloads all assets with the given path.
	 * 
	 * @param path
	 *            the base path of the assets, eg, "level1".
	 */
	public void unload(String path) {
		unloadAtlases(path);
		unloadTextures(path);
		unloadSounds(path);
		unloadFonts(path);
	}

	/**
	 * Loads all texture atlas assets from the given path. Texture atlas assets
	 * are expected to be in the "atlases" subpath of the asset path, eg,
	 * "level1/atlases".
	 * 
	 * @param path
	 *            the base path, eg, "level1".
	 */
	public void loadAtlases(String path) {
		List<String> atlasFilenames = loadAtlasesAsync(path);
		assetManager.finishLoading();
		populateAtlases(atlasFilenames);
	}

	/**
	 * Unloads all texture atlas assets from the given path.
	 * 
	 * @param path the base path, eg, "level1".
	 */
	public void unloadAtlases(String path) {
		List<String> atlasFilenames = getAssetFilenames(atlasDir(path), ".atlas");
		depopulateAtlases(atlasFilenames);
		unloadAssets(atlasFilenames);
	}

	/**
	 * Loads all texture assets from the given path. Texture assets are expected
	 * to be in the "textures" subpath of the asset path, eg, "level1/textures".
	 * 
	 * @param path
	 *            the base path, eg, "level1".
	 */
	public void loadTextures(String path) {
		List<String> textureFilenames = loadTexturesAsync(path);
		assetManager.finishLoading();
		populateTextures(textureFilenames);
	}

	/**
	 * Unloads all texture assets from the given path.
	 * 
	 * @param path the base path, eg, "level1".
	 */
	public void unloadTextures(String path) {
		List<String> textureFilenames = getAssetFilenames(textureDir(path), ".png");
		depopulateTextures(textureFilenames);
		unloadAssets(textureFilenames);
	}

	/**
	 * Loads all sound assets from the given path. Sound assets are expected to
	 * be in the "sounds" subpath of the asset path, eg, "level1/sounds".
	 * 
	 * @param path
	 *            the base path, eg, "level1".
	 */
	public void loadSounds(String path) {
		List<String> soundFilenames = loadSoundsAsync(path);
		assetManager.finishLoading();
		populateSounds(soundFilenames);
	}

	/**
	 * Unloads all sound assets from the given path.
	 * 
	 * @param path the base path, eg, "level1".
	 */
	public void unloadSounds(String path) {
		List<String> soundFilenames = getAssetFilenames(soundDir(path), ".ogg");
		depopulateSounds(soundFilenames);
		unloadAssets(soundFilenames);
	}

	/**
	 * Loads all font assets from the given path. Font assets are expected to be
	 * in the "fonts" subpath of the asset path, eg, "level1/fonts".
	 * 
	 * @param path
	 *            the base path, eg, "level1".
	 */
	public void loadFonts(String path) {
		List<String> fontFilenames = loadFontsAsync(path);
		assetManager.finishLoading();
		populateFonts(fontFilenames);
	}

	/**
	 * Unloads all font assets from the given path.
	 * 
	 * @param path the base path, eg, "level1".
	 */
	public void unloadFonts(String path) {
		List<String> fontFilenames = getAssetFilenames(fontDir(path), ".fnt");
		depopulateFonts(fontFilenames);
		unloadAssets(fontFilenames);
	}

	private List<String> loadAtlasesAsync(String path) {
		// Load the texture atlases.
		List<String> atlasFilenames = loadAssets(atlasDir(path), ".atlas", TextureAtlas.class);
		return atlasFilenames;
	}

	private String atlasDir(String path) {
		return normalizePath(path) + "atlases";
	}

	private List<String> loadTexturesAsync(String path) {
		// Load the textures.
		List<String> textureFilenames = loadAssets(textureDir(path), ".png", Texture.class);
		return textureFilenames;
	}

	private String textureDir(String path) {
		return normalizePath(path) + "textures";
	}

	private List<String> loadSoundsAsync(String path) {
		// Load the sounds.
		List<String> soundFilenames = loadAssets(soundDir(path), ".ogg", com.badlogic.gdx.audio.Sound.class);
		return soundFilenames;
	}

	private String soundDir(String path) {
		return normalizePath(path) + "sounds";
	}

	private List<String> loadFontsAsync(String path) {
		// Load the fonts.
		List<String> fontFilenames = loadAssets(fontDir(path), ".fnt", BitmapFont.class);
		return fontFilenames;
	}

	private String fontDir(String path) {
		return normalizePath(path) + "fonts";
	}

	private String normalizePath(String path) {
		if (path.equalsIgnoreCase(DEFAULT_PATH) || path.endsWith("/")) {
			return path;
		} else {
			return path + "/";
		}
	}

	private void populateAtlases(List<String> atlasFilenames) {
		// Create images from the texture atlases.
		for (String filename : atlasFilenames) {
			TextureAtlas atlas = assetManager.get(filename, TextureAtlas.class);

			// Lop off the "*data/" prefix and the ".atlas" suffix.
			int cut = filename.indexOf("data/") + 5;
			String shortname = filename.substring(cut);
			shortname = shortname.substring(0, shortname.length() - 6);

			Array<AtlasRegion> regions = atlas.getRegions();
			for (AtlasRegion region : regions) {
				String imageName = shortname + "/" + region.name;
				images.add(imageName, region);
			}
		}
	}

	private void depopulateAtlases(List<String> atlasFilenames) {
		for (String filename : atlasFilenames) {
			TextureAtlas atlas = assetManager.get(filename, TextureAtlas.class);

			// Lop off the "*data/" prefix and the ".atlas" suffix.
			int cut = filename.indexOf("data/") + 5;
			String shortname = filename.substring(cut);
			shortname = shortname.substring(0, shortname.length() - 6);

			Array<AtlasRegion> regions = atlas.getRegions();
			for (AtlasRegion region : regions) {
				String imageName = shortname + "/" + region.name;
				images.dispose(imageName);
			}
		}
	}

	private void populateTextures(List<String> textureFilenames) {
		// Create images from the textures.
		for (String filename : textureFilenames) {
			Texture texture = assetManager.get(filename, Texture.class);
			TextureRegion region = new TextureRegion(texture);

			// Lop off the "*data/" prefix and the ".png" suffix.
			int cut = filename.indexOf("data/") + 5;
			String imageName = filename.substring(cut);
			imageName = imageName.substring(0, imageName.length() - 4);

			images.add(imageName, region);
		}
	}

	private void depopulateTextures(List<String> textureFilenames) {
		for (String filename : textureFilenames) {
			// Lop off the "*data/" prefix and the ".png" suffix.
			int cut = filename.indexOf("data/") + 5;
			String imageName = filename.substring(cut);
			imageName = imageName.substring(0, imageName.length() - 4);
			images.dispose(imageName);
		}
	}

	private void populateSounds(List<String> soundFilenames) {
		// Create sounds from the sounds.
		for (String filename : soundFilenames) {
			Sound sound = assetManager.get(filename, Sound.class);

			// Lop off the "*data/" prefix and the ".ogg" suffix.
			int cut = filename.indexOf("data/") + 5;
			String soundName = filename.substring(cut);
			soundName = soundName.substring(0, soundName.length() - 4);
			sounds.add(soundName, sound);
		}
	}

	private void depopulateSounds(List<String> soundFilenames) {
		for (String filename : soundFilenames) {
			// Lop off the "*data/" prefix and the ".ogg" suffix.
			int cut = filename.indexOf("data/") + 5;
			String soundName = filename.substring(cut);
			soundName = soundName.substring(0, soundName.length() - 4);
			sounds.dispose(soundName);
		}
	}

	private void populateFonts(List<String> fontFilenames) {
		// Create fonts from the fonts.
		for (String filename : fontFilenames) {
			BitmapFont gdxFont = assetManager.get(filename, BitmapFont.class);
			gdxFont.setUseIntegerPositions(false);

			// Lop off the "*data/" prefix and the ".fnt" suffix.
			int cut = filename.indexOf("data/") + 5;
			String fontName = filename.substring(cut);
			fontName = fontName.substring(0, fontName.length() - 4);
			fonts.add(fontName, gdxFont);
		}
	}

	private void depopulateFonts(List<String> fontFilenames) {
		for (String filename : fontFilenames) {
			// Lop off the "*data/" prefix and the ".fnt" suffix.
			int cut = filename.indexOf("data/") + 5;
			String fontName = filename.substring(cut);
			fontName = fontName.substring(0, fontName.length() - 4);
			fonts.dispose(fontName);
		}
	}

	private List<String> loadAssets(String assetDir, String extension, Class<?> klass) {
		List<String> filenames = getAssetFilenames(assetDir, extension);

		for (String filename : filenames) {
			assetManager.load(filename, klass);
		}

		return filenames;
	}

	private void unloadAssets(List<String> filenames) {
		for (String filename : filenames) {
			assetManager.unload(filename);
		}
	}

	private List<String> getAssetFilenames(String assetDir, String extension) {
		List<String> filenames = new ArrayList<String>();

		String basePath = getBasePath();
		FileHandle directory = Gdx.files.internal(basePath + assetDir);
		FileHandle[] files = directory.list(extension);
		for (FileHandle file : files) {
			filenames.add(file.path());
		}

		return filenames;
	}

	private String getBasePath() {
		// To run this on the desktop from within Eclipse, add
		// "-Dldtk.assetPath=bin/data" to VM arguments in the
		// desktop run configuration.
		String assetPath = System.getProperty("ldtk.assetPath", "data");
		return assetPath + "/";
	}
}
