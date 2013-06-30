package meshViewGL;

import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import trimodel.TriMeshI;
import trimodel.TriObjMesh;

public class Particle implements Entity {
	String buf;
	String[] temp;
	String[] temp2;
	String restemp;

	static TriMeshI mesh;
	static float[] color = { 1f,1f,1f,1 };

	static int id;
	static ShellProperties shellProp;
	
	float x;
	float y;
	float z;
	
	boolean deleted;
	
	float accel;
	static float globalAccel = 1;
	
	public Particle() {
		Random r = new Random();
		accel = r.nextFloat()+1;
	}
	
	public Particle(ShellProperties shellProp) {
		this.shellProp = shellProp; // to access shell properties
		mesh = defineMeshObj("");
		id = mesh2DisplayList(mesh, "");
		Random r = new Random();
		accel = r.nextFloat()+1;
	}

	@Override
	public TriMeshI getMesh() {
		return mesh;
	}

	@Override
	public TriMeshI defineMeshObj(String part) {
		TriMeshI mesh = new TriObjMesh();
		
		File meshFile = new File("res/snowflake.obj");
		FileInputStream is = null;
		
		try {
			is = new FileInputStream(meshFile);
			InputStreamReader inputreader = new InputStreamReader(is);
			BufferedReader buffreader = new BufferedReader(inputreader);
			
			while((buf = buffreader.readLine()) != null) {
				if(buf != null && (buf.length() > 1)) {
					temp = buf.split(" ");
					switch(buf.charAt(0)) {
						case 'v':
							 switch(buf.charAt(1)) {
								case ' ':
									double x = Float.valueOf(temp[1].trim()).doubleValue();
									double y = Float.valueOf(temp[2].trim()).doubleValue();
									double z = Float.valueOf(temp[3].trim()).doubleValue();
									mesh.addVertex(x, y, z);
									break;
								case 'n':
									double xn = Float.valueOf(temp[1].trim()).doubleValue();
									double yn = Float.valueOf(temp[2].trim()).doubleValue();
									double zn = Float.valueOf(temp[3].trim()).doubleValue();
									mesh.addNormal(xn, yn, zn);
									break;
								case 't':
									double xt = Float.valueOf(temp[1].trim()).doubleValue();
									double yt = Float.valueOf(temp[2].trim()).doubleValue();
									mesh.addTexCoord(xt, yt);
									break;
									
							} 
							break;
						case 'f':
						  if (temp[1].contains("//")) {
								temp2 = temp[1].split("//"); 
								int a = Integer.parseInt(temp2[0].trim());
								int a2 = Integer.parseInt(temp2[1].trim());
								
								temp2 = temp[2].split("//");
								int b = Integer.parseInt(temp2[0].trim());
								int b2 = Integer.parseInt(temp2[1].trim());
								
								temp2 = temp[3].split("//");
								int c = Integer.parseInt(temp2[0].trim());
								int c2 = Integer.parseInt(temp2[1].trim());
								
								int i = mesh.addTriFace(a,b,c);
								mesh.setNormalsOfTriFace(i, a2, b2, c2);
								break;
							}
							else {
								temp2 = temp[1].split("/"); 
								int a = Integer.parseInt(temp2[0].trim());
								int a2 = Integer.parseInt(temp2[1].trim());
								int a3 = Integer.parseInt(temp2[2].trim());
								
								temp2 = temp[2].split("/");
								int b = Integer.parseInt(temp2[0].trim());
								int b2 = Integer.parseInt(temp2[1].trim());
								int b3 = Integer.parseInt(temp2[2].trim());
								
								temp2 = temp[3].split("/");
								int c = Integer.parseInt(temp2[0].trim());
								int c2 = Integer.parseInt(temp2[1].trim());
								int c3 = Integer.parseInt(temp2[2].trim());
								
								int i = mesh.addTriFace(a,b,c);
								mesh.setTexCoordsOfTriFace(i, a2, b2, c2);
								System.out.println(i-1 + " " + a2 + " " + b2 + " " + c2);
								mesh.setNormalsOfTriFace(i, a3, b3, c3);
								break;
							}
					}
				}
			}
			
			is.close();
			buffreader.close();
			
		} catch (IOException e) {
			System.err.println("Something went wrong.");
		}
		
		return mesh;
	}

	@Override
	public void init() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int mesh2DisplayList(TriMeshI mesh, String part) {
		int listID = GL11.glGenLists(1);
		GL11.glNewList(listID, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_TRIANGLES);
		
		for (int i = 1; i <= mesh.getFaceCnt(); i++) {
			int[] facevert = mesh.getFaceVertices(i);
			int[] facenorm = mesh.getFaceNormals(i);
			int[] facetex = mesh.getFaceTexCoords(i);
			
		for (int j = 0; j < 3; j++) {
				double[] vertex = mesh.getVertex(facevert[j]);
				double[] normal = mesh.getNormal(facenorm[j]);
				
				//double[] tex = mesh.getTexCoord(facetex[j]);
					//GL11.glTexCoord2d(tex[0], tex[1]);
				
				GL11.glVertex3d(vertex[0], vertex[1], vertex[2]);
				GL11.glNormal3d(normal[0], normal[1], normal[2]);	
		
			}
		}
		GL11.glEnd();
		GL11.glEndList();
			
		return listID; // TODO
	}

	@Override
	public void initLights() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableLighting(boolean toggle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderWithRotation(float time, float angle, float x, float y,
			float z) {
				glPushMatrix();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glColor4f(color[0], color[1], color[2], color[3]);
				
				glTranslatef(0, 0, -15); // #3 move triangle back to position
				
				glScalef(2.0f, 2.0f,2.0f);
				if (color[0] == 0) glScalef(1.0f,10.0f,1.0f);
				glFrontFace(GL_CCW);
				
				glRotatef(angle, x, y, z);
				
				glTranslatef(this.x, this.y, this.z); // #3 move triangle back to position
				
				glPushMatrix();
				
				GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDisable(GL_LIGHTING);
				glScalef(0.1f, 0.1f,0.1f);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glCallList(id);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				
				GL11.glPopAttrib();
				glPopMatrix();
				
				// needs a section to check/display normals
				glPopMatrix();
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void updateParticle(float dt) {
		y += dt*accel*globalAccel;
		if (y < -6) deleted = true;
	}
	
	public void setCoords(float x, float y, float z) {
		Random r = new Random();
		int i = r.nextInt(2);
		
		if (i == 0) {
			this.x = x;
		}
		else {
			this.x = x*-1;
		}
		
		i = r.nextInt(2);
		
		if (i == 0) {
			this.y = y;
		}
		else {
			this.y = y*-1;
		}
		
		i = r.nextInt(2);
		
		if (i == 0) {
			this.z = z;
		}
		else {
			this.z = z*-1;
		}	
	}
	
	public static void setGlobalAccel(float dx) {
		globalAccel += dx;
		System.out.println(globalAccel + "");
	}
	
	public static void toggleRain() {
		if (color[0] == 1) {
			color[0] = 0;
			color[1] = 0;
			color[2] = 1;
			color[3] = 0.4f;
		}
		else {
			color[0] = 1;
			color[1] = 1;
			color[2] = 1;
			color[3] = 1f;
		}
	}

}
