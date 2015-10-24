package edu.stanford.math.plex_viewer.color;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;

public class RipsColorScheme extends ColorScheme<Simplex> {
	float[][] colors = new float[][]{
			{ 0.0f, 0.0f, 0.0f },		// black
			{ 0.0f, 0.0f, 0.0f },		// black
			{ 0.55f, 0.84f, 1.0f },		// light blue
			{ 0.24f, 0.60f, 0.81f },		// dark blue
	};

	public RipsColorScheme() {
	}

	/**
	 * 0-simplices red
	 * 1-simplices orange
	 * 2-simplices light green
	 * 3-simplices dark green
	 */
	public float[] computeColor(Simplex simplex) {
		if (simplex==null || simplex.getDimension()>3) {
			return new float[]{ 0.0f , 0.0f , 0.0f };
		}
		return colors[simplex.getDimension()];
	}

}

