package trimodel;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Maintains vertex and face data to describe object geometry.
 * Faces are limited to triangles. Faces refer to vertices
 * by an index beginning with 1 based upon the order given.
 * Jan 2013, Feb add normals to your version from Lab2
 * @author wainer
 *
 */
public class TriObjMesh implements TriMeshI {

	private ArrayList<vec3> vertexBuffer = new ArrayList<vec3>();
	private ArrayList<i3> faceBuffer = new ArrayList<i3>();
	private ArrayList<vec3> normalBuffer = new ArrayList<vec3>();
	private ArrayList<vec2> texBuffer = new ArrayList<vec2>();

	private class vec2 {
		double v[] = {0.0, 0.0};
	}
	
	private class vec3 {
		double v[] = {0.0, 0.0, 0.0};
	}
	
	private class i3 {
		int v[] = {0, 0, 0};
		int n[] = {0, 0, 0,};
		int t[] = {0, 0, 0};
		String part;
	}
	
	@Override
	public int addVertex(double x, double y, double z) {
		vec3 v3 = new vec3();
		v3.v[0] = x;
		v3.v[1] = y;
		v3.v[2] = z;
		
		vertexBuffer.add(v3);
		return vertexBuffer.size();
	}

	@Override
	public int getVertexCnt() {
		return vertexBuffer.size();
	}

	@Override
	public double[] getVertex(int item) throws IndexOutOfBoundsException {
		return vertexBuffer.get(item - 1).v;
	}

	@Override
	public int addTriFace(int a, int b, int c) {
		i3 i = new i3();
		i.v[0] = a;
		i.v[1] = b;
		i.v[2] = c;
		faceBuffer.add(i);
		return faceBuffer.size();
	}

	@Override
	public int getFaceCnt() {
		return faceBuffer.size();
	}

	@Override
	public int[] getFaceVertices(int item) throws IndexOutOfBoundsException {
		return faceBuffer.get(item - 1).v;
	}

	@Override
	public void clearAll() {
		vertexBuffer.clear();
		faceBuffer.clear();
		normalBuffer.clear();
	}

	@Override
	public int addNormal(double x, double y, double z) {
		vec3 v3 = new vec3();
		v3.v[0] = x;
		v3.v[1] = y;
		v3.v[2] = z;
		
		normalBuffer.add(v3);
		return normalBuffer.size();
	}

	@Override
	public int getNormalCnt() {
		return normalBuffer.size();
	}

	@Override
	public double[] getNormal(int item) throws IndexOutOfBoundsException {
		return normalBuffer.get(item - 1).v;
	}

	@Override
	public int setNormalsOfTriFace(int face, int a, int b, int c) {
		i3 i = faceBuffer.get(face - 1);
		i.n[0] = a;
		i.n[1] = b;
		i.n[2] = c;
		
		faceBuffer.set(face - 1, i);
		return face;
	}

	@Override
	public int[] getFaceNormals(int item) throws IndexOutOfBoundsException {
		return faceBuffer.get(item - 1).n;
	}

	@Override
	public int addTexCoord(double s, double t) {
		vec2 v3 = new vec2();
		v3.v[0] = s;
		v3.v[1] = t;
		
		texBuffer.add(v3);
		return texBuffer.size();
	}

	@Override
	public int getTexCoordCnt() {
		return texBuffer.size();
	}

	@Override
	public double[] getTexCoord(int i) throws IndexOutOfBoundsException {
		return texBuffer.get(i - 1).v;
	}

	@Override
	public int setTexCoordsOfTriFace(int face, int a, int b, int c) {
		i3 i = faceBuffer.get(face - 1);
		i.t[0] = a;
		i.t[1] = b;
		i.t[2] = c;
		
		faceBuffer.set(face - 1, i);
		return face;
	}

	@Override
	public int[] getFaceTexCoords(int face) {
		return faceBuffer.get(face - 1).t;
	}
	
	@Override
	public String getName(int face) {
		i3 i = faceBuffer.get(face - 1);
		return i.part;
	}
	
	@Override
	public void setFacePart(int face, String name) {
		i3 i = faceBuffer.get(face - 1);
		i.part = name;
		faceBuffer.set(face - 1, i);
	}

}
