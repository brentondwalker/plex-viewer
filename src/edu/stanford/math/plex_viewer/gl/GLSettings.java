package edu.stanford.math.plex_viewer.gl;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class GLSettings {

	public static final float defaultPointSize = 5.0f;
	public static final float[] defaultPointColor = new float[]{0.1f, 0.1f, 0.1f};
	public static final float defaultBackgroundIntensity = 1.0f;
	
	private static final int defaultScreenWidth = 750;
	private static final int defaultScreenHeight = 750;
	
	private static final GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
	private static final GraphicsDevice device = environment.getDefaultScreenDevice();
	private static final DisplayMode displaymode = device.getDisplayMode();
	
	private static final int fullScreenWidth = displaymode.getWidth()/2;
	private static final int fullScreenHeight = displaymode.getHeight()/2;
	
	public static boolean useFullScreen = false;
	
	public static int getScreenWidth() {
		if (useFullScreen) {
			return fullScreenWidth;
		} else {
			return defaultScreenWidth;
		}
	}
	
	public static int getScreenHeight() {
		if (useFullScreen) {
			return fullScreenHeight;
		} else {
			return defaultScreenHeight;
		}
	}
}
