package edu.stanford.math.plex_viewer.gl;

import java.lang.Math;

import javax.media.opengl.GL;
import com.sun.opengl.util.GLUT;

import edu.stanford.math.plex4.homology.chain_basis.Simplex;
import edu.stanford.math.plex4.metric.interfaces.AbstractObjectMetricSpace;
import edu.stanford.math.plex_viewer.color.ColorScheme;

public class GLUtility {
	public static  void drawPoint(GL gl, double[] point, float[] color) {
		gl.glColor3fv(color, 0);

		if (point.length == 1) {
			gl.glVertex2d(point[0], 0);
		} else if (point.length == 2) {
			gl.glVertex2d(point[0], point[1]);
		} else if (point.length >= 3) {
			gl.glVertex3d(point[0], point[1], point[2]);
		}
	}
	
	public static  void drawPoint(GL gl, double[] point, ColorScheme<double[]> colorScheme) {
		drawPoint(gl, point, colorScheme.computeColor(point));
	}
	
	public static void drawSimplex(GL gl, Simplex simplex, ColorScheme<Simplex> colorScheme, AbstractObjectMetricSpace<double[]> metricSpace) {
		drawSimplex(gl, simplex, colorScheme.computeColor(simplex), metricSpace);
	}
	
	public static void drawSimplex(GL gl, Simplex simplex, float[] color, AbstractObjectMetricSpace<double[]> metricSpace) {
		
		int glShapeCode = 0;
		if (simplex.getDimension() == 0) {
			glShapeCode = GL.GL_POINTS;
		} else if (simplex.getDimension() == 1) {
			glShapeCode = GL.GL_LINES;
		} else if (simplex.getDimension() == 2) {
			glShapeCode = GL.GL_TRIANGLES;
		}
		
		int[] vertices = simplex.getVertices();
		gl.glBegin(glShapeCode);
		for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
			double[] point = metricSpace.getPoint(vertices[vertexIndex]);
			gl.glColor3fv(color, 0);
			//gl.glLineWidth(50);     // not implemented in jogl1.1
			if (point.length == 1) {
				gl.glVertex2d(point[0], 0);
			} else if (point.length == 2) {
				gl.glVertex2d(point[0], point[1]);
			} else if (point.length >= 3) {
				gl.glVertex3d(point[0], point[1], point[2]);
			}
		}
		gl.glEnd();
	}

	/*
	 * "Thick" versions of simplex drawing.
	 * Makes the points into balls, the lines into cylinders, and etc.
	 */
	
	public static  void drawPointT(GL gl, double[] point, float[] color, float thickness) {
		gl.glColor3fv(color, 0);

		if (point.length == 1) {
			gl.glVertex2d(point[0], 0);
		} else if (point.length == 2) {
			gl.glVertex2d(point[0], point[1]);
		} else if (point.length >= 3) {
			gl.glVertex3d(point[0], point[1], point[2]);
		}
	}
	
	public static  void drawPointT(GL gl, double[] point, ColorScheme<double[]> colorScheme, float thickness) {
		drawPointT(gl, point, colorScheme.computeColor(point), thickness);
	}
	
	public static void drawSimplexT(GL gl, Simplex simplex, ColorScheme<Simplex> colorScheme,
									  AbstractObjectMetricSpace<double[]> metricSpace, float thickness) {
		drawSimplexT(gl, simplex, colorScheme.computeColor(simplex), metricSpace, thickness);
	}
	
	public static void drawSimplexT(GL gl, Simplex simplex, float[] color,
									  AbstractObjectMetricSpace<double[]> metricSpace, float thickness) {
		GLUT glut = new GLUT();
		
		// radius of 0-simplices
		float pw = thickness*0.05f;
		// line width and height; for 1-simplices
		float lw = thickness*0.008f;
		float lh = thickness*0.03f;
		// thickness for 3-simplices
		float t3 = thickness*0.001f;
		
		int glShapeCode = 0;
		if (simplex.getDimension() == 0) {
			glShapeCode = GL.GL_POINTS;
			
			double p1[] = new double[3];
			int[] vertices = simplex.getVertices();
			double[] point = metricSpace.getPoint(vertices[0]);
			if (point.length == 1) {
				p1[0] = point[0]; p1[1]=0; p1[2]=0;
			} else if (point.length == 2) {
				p1[0] = point[0]; p1[1]=point[1]; p1[2]=0;
			} else if (point.length >= 3) {
				p1[0] = point[0]; p1[1]=point[1]; p1[2]=point[2];
			}
			gl.glColor3fv(color,0);
			gl.glPushMatrix();
			gl.glTranslated(p1[0], p1[1], p1[2]);
            glut.glutSolidSphere(pw, 4, 4);
            gl.glPopMatrix();
			
		} else if (simplex.getDimension() == 1) {
			// maybe figure this out later
			// need to rotate the coord axes
			//glut.glutSolidCylinder(0.1, 0, 25, 25);

			glShapeCode = GL.GL_QUADS;
			double p1[] = new double[3];
			double p2[] = new double[3];
			int[] vertices = simplex.getVertices();

			// one end
			double[] point = metricSpace.getPoint(vertices[0]);
			if (point.length == 1) {
				p1[0] = point[0]; p1[1]=0; p1[2]=0;
			} else if (point.length == 2) {
				p1[0] = point[0]; p1[1]=point[1]; p1[2]=0;
			} else if (point.length >= 3) {
				p1[0] = point[0]; p1[1]=point[1]; p1[2]=point[2];
			}

			// other end
			point = metricSpace.getPoint(vertices[1]);
			if (point.length == 1) {
				p2[0] = point[0]; p2[1]=0; p2[2]=0;
			} else if (point.length == 2) {
				p2[0] = point[0]; p2[1]=point[1]; p2[2]=0;
			} else if (point.length >= 3) {
				p2[0] = point[0]; p2[1]=point[1]; p2[2]=point[2];
			}

			// compute the normals
			float dx = (float)(p2[0]-p1[0]);
			float dy = (float)(p2[1]-p1[1]);
			float nx = lw*-Math.signum(dy);
			float ny = lw*Math.signum(dx);

			gl.glBegin(glShapeCode);
			gl.glColor3fv(color,0);
			// Top Face
			gl.glVertex3f((float)p1[0]-nx, (float)p1[1]-ny, (float)p1[2]+lh);
			gl.glVertex3f((float)p1[0]+nx, (float)p1[1]+ny, (float)p1[2]+lh);
			gl.glVertex3f((float)p2[0]+nx, (float)p2[1]+ny, (float)p2[2]+lh);
			gl.glVertex3f((float)p2[0]-nx, (float)p2[1]-ny, (float)p2[2]+lh);
			// Side Face
			gl.glVertex3f((float)p1[0]-nx, (float)p1[1]-ny, (float)p1[2]+lh);
			gl.glVertex3f((float)p1[0]-nx, (float)p1[1]-ny, (float)p1[2]-lh);
			gl.glVertex3f((float)p2[0]-nx, (float)p2[1]-ny, (float)p2[2]-lh);
			gl.glVertex3f((float)p2[0]-nx, (float)p2[1]-ny, (float)p2[2]+lh);
			// Bottom Face
			gl.glVertex3f((float)p1[0]-nx, (float)p1[1]-ny, (float)p1[2]-lh);
			gl.glVertex3f((float)p1[0]+nx, (float)p1[1]+ny, (float)p1[2]-lh);
			gl.glVertex3f((float)p2[0]+nx, (float)p2[1]+ny, (float)p2[2]-lh);
			gl.glVertex3f((float)p2[0]-nx, (float)p2[1]-ny, (float)p2[2]-lh);
			// Other Side Face
			gl.glVertex3f((float)p1[0]+nx, (float)p1[1]+ny, (float)p1[2]+lh);
			gl.glVertex3f((float)p1[0]+nx, (float)p1[1]+ny, (float)p1[2]-lh);
			gl.glVertex3f((float)p2[0]+nx, (float)p2[1]+ny, (float)p2[2]-lh);
			gl.glVertex3f((float)p2[0]+nx, (float)p2[1]+ny, (float)p2[2]+lh);
			gl.glEnd();

		} else if (simplex.getDimension() == 2) {
			glShapeCode = GL.GL_TRIANGLES;
			int[] vertices = simplex.getVertices();
			gl.glBegin(glShapeCode);
			for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
				double[] point = metricSpace.getPoint(vertices[vertexIndex]);
				gl.glColor3fv(color, 0);
				if (point.length == 1) {
					gl.glVertex2d(point[0], 0);
				} else if (point.length == 2) {
					gl.glVertex2d(point[0], point[1]);
				} else if (point.length >= 3) {
					gl.glVertex3d(point[0], point[1], point[2]);
				}
			}
			gl.glEnd();	
		} else if (simplex.getDimension() == 3) {
			// to be efficient we would figure out if the convex hull
			// of the points is a triangle or a quad, but just to be q&d
			// we can simply draw all four possible triangles
			glShapeCode = GL.GL_TRIANGLES;
			int[] vertices = simplex.getVertices();
			for (int excludeV=0; excludeV<vertices.length; excludeV++) {
				// top face
				gl.glBegin(glShapeCode);
				for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
					if (vertexIndex!=excludeV) {
						double[] point = metricSpace.getPoint(vertices[vertexIndex]);
						gl.glColor3fv(color,0);
						//gl.glColor3fv(color, 0);
						if (point.length == 1) {
							gl.glVertex3d(point[0], 0, t3);
						} else if (point.length == 2) {
							gl.glVertex3d(point[0], point[1], t3);
						} else if (point.length >= 3) {
							gl.glVertex3d(point[0], point[1], point[2]+t3);
						}
					}
				}
				gl.glEnd();
				// bottom face
				gl.glBegin(glShapeCode);
				for (int vertexIndex = 0; vertexIndex < vertices.length; vertexIndex++) {
					if (vertexIndex!=excludeV) {
						double[] point = metricSpace.getPoint(vertices[vertexIndex]);
						gl.glColor3fv(color,0);
						if (point.length == 1) {
							gl.glVertex3d(point[0], 0, -t3);
						} else if (point.length == 2) {
							gl.glVertex3d(point[0], point[1], -t3);
						} else if (point.length >= 3) {
							gl.glVertex3d(point[0], point[1], point[2]-t3);
						}
					}
				}
				gl.glEnd();
			}
		}
	}


}
