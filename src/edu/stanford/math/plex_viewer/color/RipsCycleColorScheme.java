package edu.stanford.math.plex_viewer.color;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;

public class RipsCycleColorScheme extends ColorScheme<Simplex> {
	float[][] colors = new float[][]{
			{ 1.0f, 0.4f, 1.0f },		// yellow
			{ 1.0f, 0.4f, 1.0f },		// yellow
	};

	public RipsCycleColorScheme() {
	}

	/**
	 * 0-simplices yellow
	 * 1-simplices yellow
	 */
	public float[] computeColor(Simplex simplex) {
		if (simplex==null || simplex.getDimension()>1) {
			return new float[]{ 0.0f , 0.0f , 0.0f };
		}
		return colors[simplex.getDimension()];
	}
}
