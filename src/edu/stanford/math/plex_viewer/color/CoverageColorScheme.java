package edu.stanford.math.plex_viewer.color;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;

public class CoverageColorScheme extends ColorScheme<Simplex> {
	/*
	float[][] colors = new float[][]{
			{ 0.81f, 0.24f, 0.24f },	// red
			{ 0.72f, 0.46f, 0.0f },		// orange
			{ 0.72f, 1.0f, 0.61f },		// light green
			{ 0.55f, 1.0f, 0.37f },		// dark green
	};
	*/
	float[][] colors = new float[][]{
			{ 0.81f, 0.24f, 0.24f },	// red
			{ 0.72f, 0.46f, 0.0f },		// orange
			{ 0.40f, 0.80f, 0.0f },		// light green
			{ 0.40f, 0.90f, 0.0f },		// dark green
	};

	
	public CoverageColorScheme() {
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
