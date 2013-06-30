package meshViewGL;

import java.io.IOException;

import trimodel.TriMeshI;

public interface Entity {

	public abstract TriMeshI getMesh();

	/**
	 * Create a suitable 3d mesh object to display
	 * @return the mesh object created
	 */
	public abstract TriMeshI defineMeshObj(String part);

	public abstract void init() throws IOException;

	/**
	 * Defines a display list representing the mesh object surface
	 * @param mesh - the mesh object to create the display list for
	 * @return the display list ID generated
	 */
	public abstract int mesh2DisplayList(TriMeshI mesh, String part);

	/**
	 * Initializes the lighting to use for the Mesh Object
	 */
	public abstract void initLights();

	/**
	 * Turns lighting and light0 on or off
	 * @param toggle - True to turn lighting on; False to turn it off
	 */
	public abstract void enableLighting(boolean toggle);

	public abstract void renderWithRotation(float time, float angle, float x,
			float y, float z);


}