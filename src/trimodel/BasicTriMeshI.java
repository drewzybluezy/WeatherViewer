package trimodel; // use a package - Not default!

public interface BasicTriMeshI {

	/**
	 * Adds a vertex to the vertex list.
	 * @param x x coordinate of vertex to be added
	 * @param y y coordinate of vertex to be added
	 * @param z z coordinate of vertex to be added
	 * @return index number of the vertex just added
	 */
	public int addVertex(double x, double y, double z);


	/**
	 * Returns the current number of vertices
	 * @return index of the last vertex (which is the number of vertices)
	 */
	public int getVertexCnt();

	/**
	 * Gets the coordinate values for the specified vertex
	 * @param item the index 1..N for the vertex desired
	 * @return an array of 3 items, x,y,z for the desired vertex
	 * @throws IndexOutOfBoundsException if an invalid vertex item is requested
	 */
	public double[] getVertex(int item) throws IndexOutOfBoundsException;

	/**
	 * Add a triangle face to the face list (vertex indices start with 1)
	 * @param a index of triangle's 1st vertex
	 * @param b index of triangle's 2nd vertex
	 * @param c index of triangle's 3rd vertex
	 * @return this face's index number which is also total number of faces
	 */
	public int addTriFace(int a, int b, int c);

	/**
	 * Returns the current number of faces
	 * @return index of the last face (which is the number of faces)
	 */
	public int getFaceCnt();

	
	/**
	 * Gets the indexes of the vertices (in order) specifying the given face
	 * @param item - index of the face to get the vertices for
	 * @return an array giving the indices of the vertices which specify this face
	 * @throws IndexOutOfBoundsException
	 */
	public int[] getFaceVertices(int item) throws IndexOutOfBoundsException;

	/*
	 * Clears all the data associated with this mesh object
	 */
	public void clearAll();

}