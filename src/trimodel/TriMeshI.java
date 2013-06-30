package trimodel; // use a package - Not default!

public interface TriMeshI extends BasicTriMeshI {

	/**
	 * Adds an outgoing normal (unit length) to the normal list.
	 * @param x x coordinate of normal to be added
	 * @param y y coordinate of normal to be added
	 * @param z z coordinate of normal to be added
	 * @return index number of the normal just added
	 */
	public int addNormal(double x, double y, double z);

	/**
	 * Returns the current number of normals
	 * @return index of the last normal (which is the number of normals)
	 */
	public int getNormalCnt();

	/**
	 * Gets the coordinate values for the specified normal
	 * @param item the index 1..N for the normal desired
	 * @return an array of 3 items, x,y,z for the desired normal
	 * @throws IndexOutOfBoundsException if an invalid vertex item is requested
	 */
	public double[] getNormal(int item) throws IndexOutOfBoundsException;

	/**
	 * Add the vertex normals for a triangle face to the face list (all indices start with 1)
	 * @param face index of face to set normals for
	 * @param a index of normal for triangle's 1st vertex
	 * @param b index of normal for triangle's 2nd vertex
	 * @param c index of normal for triangle's 3rd vertex
	 * @return this face's index number
	 */
	public int setNormalsOfTriFace(int face, int a, int b, int c);

	/**
	 * Gets the indexes of the normals (in order) specified for the given face
	 * @param item - index of the face to get the normals for
	 * @return an array giving the indices of the normals for this face (or null)
	 * @throws IndexOutOfBoundsException
	 */
	public int[] getFaceNormals(int item) throws IndexOutOfBoundsException;
	
	public int addTexCoord(double s, double t);
	public int getTexCoordCnt();
	public double[] getTexCoord(int i);
	public int setTexCoordsOfTriFace(int face, int a, int b, int c);
	public int[] getFaceTexCoords(int face);
	String getName(int face);
	void setFacePart(int face, String name);


}