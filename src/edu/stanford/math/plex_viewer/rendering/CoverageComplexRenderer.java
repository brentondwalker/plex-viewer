package edu.stanford.math.plex_viewer.rendering;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.impl.EuclideanMetricSpace;
import edu.stanford.math.plex4.metric.interfaces.AbstractObjectMetricSpace;
import edu.stanford.math.plex4.streams.impl.GeometricSimplexStream;
import edu.stanford.math.plex4.streams.interfaces.AbstractFilteredStream;
import edu.stanford.math.plex_viewer.color.*;
import edu.stanford.math.plex_viewer.gl.GLSettings;
import edu.stanford.math.plex_viewer.gl.GLUtility;


/**
 * This class draws coverage complexes on top of their Rips complexes.
 *
 */
public class CoverageComplexRenderer implements ObjectRenderer {

	protected final GeometricSimplexStream ripsStream;
	protected final GeometricSimplexStream coverageStream;
	protected final GeometricSimplexStream cycleStream;
	protected final GeometricSimplexStream ripsCycleStream;
	protected final int maxFiltrationIndex;
	protected final int minFiltrationIndex;

	protected int currentFiltrationIndex = 0;
	protected ColorScheme<Simplex> ripsColorScheme = null;
	protected ColorScheme<Simplex> coverageColorScheme = null;
	protected ColorScheme<Simplex> cycleColorScheme = null;
	protected ColorScheme<Simplex> ripsCycleColorScheme = null;
	protected int maxNumSimplices = 500000;
	protected int maxDimension = 10;
	
	// whether or not to display the various complexes
	protected boolean showRips = true;
	protected boolean showCoverage = true;
	protected boolean showCycles = true;
	protected boolean showRipsCycles = true;
	
	/**
	 * This constructor initializes the class with a given GeometricSimplexStream
	 * object.
	 * 
	 * @param geometricSimplexStream the GeometricSimplexStream object to initialize with
	 */
	public CoverageComplexRenderer(GeometricSimplexStream ripsStream, GeometricSimplexStream coverageStream,
			GeometricSimplexStream cycleStream, GeometricSimplexStream ripsCycleStream) {
		this.ripsStream = ripsStream;
		this.coverageStream = coverageStream;
		this.cycleStream = cycleStream;
		this.ripsCycleStream = ripsCycleStream;
		this.maxFiltrationIndex = this.ripsStream.getMaximumFiltrationIndex();
		this.minFiltrationIndex = this.ripsStream.getMinimumFiltrationIndex();
		this.ripsColorScheme = new RipsColorScheme();
		this.coverageColorScheme = new CoverageColorScheme();
		this.cycleColorScheme = new CycleColorScheme();
		this.ripsCycleColorScheme = new RipsCycleColorScheme();
		this.currentFiltrationIndex = this.minFiltrationIndex;
	}

	/**
	 * This constructor initializes the class with an abstract simplicial complex as well as a
	 * set of points in Euclidean space.
	 * 
	 * @param stream the abstract simplicial complex
	 * @param points the points of the vertices in Euclidean space (should be in R^2 or R^3)
	 */
	public CoverageComplexRenderer(AbstractFilteredStream<Simplex> ripsStream, AbstractFilteredStream<Simplex> coverageStream,
			AbstractFilteredStream<Simplex> cycleStream,  AbstractFilteredStream<Simplex> ripsCycleStream, double[][] points) {
		this(ripsStream, coverageStream, cycleStream, ripsCycleStream, new EuclideanMetricSpace(points));
	}

	/**
	 * This constructor initializes the class with an abstract simplicial complex as well as
	 * a Euclidean metric space.
	 * 
	 * @param stream the abstract simplicial complex
	 * @param metricSpace the geometric points in Euclidean space (should be in R^2 or R^3)
	 */
	public CoverageComplexRenderer(AbstractFilteredStream<Simplex> ripsStream,
									AbstractFilteredStream<Simplex> coverageStream,
									AbstractFilteredStream<Simplex> cycleStream,
									AbstractFilteredStream<Simplex> ripsCycleStream,
									AbstractObjectMetricSpace<double[]> ems) {
		this(ripsStream!=null ? new GeometricSimplexStream(ripsStream,ems) : null,
				coverageStream!=null ? new GeometricSimplexStream(coverageStream,ems) : null,
				cycleStream!=null ? new GeometricSimplexStream(cycleStream,ems) : null,
				ripsCycleStream!=null ? new GeometricSimplexStream(ripsCycleStream,ems) : null);
	}

	/**
	 * This function sets the color scheme.
	 * 
	 * @param colorScheme the new color scheme
	 */
	public void setRipsColorScheme(ColorScheme<Simplex> colorScheme) {
		this.ripsColorScheme = colorScheme;
	}
	public void setCoverageColorScheme(ColorScheme<Simplex> colorScheme) {
		this.coverageColorScheme = colorScheme;
	}
	public void setCycleColorScheme(ColorScheme<Simplex> colorScheme) {
		this.cycleColorScheme = colorScheme;
	}
	public void setRipsCycleColorScheme(ColorScheme<Simplex> colorScheme) {
		this.ripsCycleColorScheme = colorScheme;
	}

	public ColorScheme<Simplex> getRipsColorScheme() {
		return this.ripsColorScheme;
	}
	public ColorScheme<Simplex> getCoverageColorScheme() {
		return this.coverageColorScheme;
	}
	public ColorScheme<Simplex> getCycleColorScheme() {
		return this.cycleColorScheme;
	}
	public ColorScheme<Simplex> getRipsCycleColorScheme() {
		return this.ripsCycleColorScheme;
	}

	public void setMaxDimension(int dimension) {
		this.maxDimension = dimension;
	}

	public void init(GL gl) {
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_POINT_SMOOTH);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glPointSize(GLSettings.defaultPointSize);
	}

	public void renderShape(GL gl) {
		int simplexCount = 0;
		if (this.ripsStream!=null && this.showRips) {
			for (Simplex simplex: this.ripsStream) {
				if (simplexCount > this.maxNumSimplices) {
					break;
				}

				if (ripsStream.getFiltrationIndex(simplex) > currentFiltrationIndex) {
					break;
				}

				if (simplex.getDimension() > maxDimension) {
					continue;
				}

				if (simplex.getDimension() <= 3) {
					GLUtility.drawSimplexT(gl, simplex, this.ripsColorScheme, this.ripsStream, 0.5f);
				}

				simplexCount++;
			}
		}
		if (this.coverageStream!=null && this.showCoverage) {
			for (Simplex simplex: this.coverageStream) {
				if (simplexCount > this.maxNumSimplices) {
					break;
				}

				if (coverageStream.getFiltrationIndex(simplex) > currentFiltrationIndex) {
					break;
				}

				if (simplex.getDimension() > maxDimension) {
					continue;
				}

				if (simplex.getDimension() <= 3) {
					GLUtility.drawSimplexT(gl, simplex, this.coverageColorScheme, this.coverageStream, 0.8f);
				}

				simplexCount++;
			}
		}
		if (this.ripsCycleStream!=null && this.showRipsCycles) {
			for (Simplex simplex: this.ripsCycleStream) {
				if (simplexCount > this.maxNumSimplices) {
					break;
				}

				if (ripsCycleStream.getFiltrationIndex(simplex) > currentFiltrationIndex) {
					break;
				}

				if (simplex.getDimension() > maxDimension) {
					continue;
				}

				if (simplex.getDimension() <= 3) {
					GLUtility.drawSimplexT(gl, simplex, this.ripsCycleColorScheme, this.ripsCycleStream, 2.4f);
				}

				simplexCount++;
			}
		}
		if (this.cycleStream!=null && this.showCycles) {
			for (Simplex simplex: this.cycleStream) {
				if (simplexCount > this.maxNumSimplices) {
					break;
				}

				if (cycleStream.getFiltrationIndex(simplex) > currentFiltrationIndex) {
					break;
				}

				if (simplex.getDimension() > maxDimension) {
					continue;
				}

				if (simplex.getDimension() <= 3) {
					GLUtility.drawSimplexT(gl, simplex, this.cycleColorScheme, this.cycleStream, 2.5f);
				}

				simplexCount++;
			}
		}
	}

	public void processSpecializedKeys(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyCode() == KeyEvent.VK_1) {
			this.showRips ^= true;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_2) {
			this.showCoverage ^= true;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD3 || e.getKeyCode() == KeyEvent.VK_3) {
			this.showCycles ^= true;
		} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyCode() == KeyEvent.VK_4) {
			this.showRipsCycles ^= true;
		}
	}


}

