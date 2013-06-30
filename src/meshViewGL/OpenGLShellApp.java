package meshViewGL;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Timer;

public class OpenGLShellApp implements ShellProperties {

	boolean pause = false;
	boolean wireframe = false;
	boolean showNormals = false;
	
	float[] main = {0.0f, 1.0f, 0.0f};
	float[] bot = {1.0f, 2.0f, 0.0f};
	float[] top = {1.0f, 0.0f, 0.0f};
	
	final float VIEW_CD = 1.0f;
	float VIEW_REMAINING = 0;
	
	final int WINDOW_WIDTH = 600;
	final int WINDOW_HEIGHT = 600;
	
	boolean isSnowing = true;
	
	ArrayList<Entity> entityList = new ArrayList<Entity>();
	ArrayList<Particle> particleList = new ArrayList<Particle>();
	
	int numParticles = 4000;
	
	float dTime = 0;
	float lastTime = 0;
	
	float[] temp;
	
	Timer secTimer; // lwjgl Timer - returns time in seconds
	Timer persTimer;
	float cycleTime;
	float[] bkgColor = { 0.01f, 0.1f, 0.2f, 1.0f };
		
	/**
	 * Create a new LWJGL Pane for OpenGL graphics
	 */
	public OpenGLShellApp() {
		try {
			// configure and create the LWJGL display
			Display.setTitle("Weather Machine");
			Display.setDisplayMode(new DisplayMode(WINDOW_WIDTH,WINDOW_HEIGHT));
			Display.setFullscreen(false);
			Display.create();	
		} catch (LWJGLException e) {
			e.printStackTrace();
			Sys.alert("Error", "Failed: Display Creation!! "+e.getMessage());
		}
	}

	
	/**
	 * Initialize resources, graphics state etc
	 * @throws IOException 
	 * @throws LWJGLException 
	 */
	public void init() throws IOException, LWJGLException {
		Mouse.create();
		
		entityList.add((Entity)new Particle(this));
		
		
		secTimer = new Timer();
		persTimer = new Timer();
		persTimer.resume();
		cycleTime = 0f;
		
		glClearColor(bkgColor[0],bkgColor[1],bkgColor[2],bkgColor[3]);
		glShadeModel(GL11.GL_FLAT);
		
		glMatrixMode(GL11.GL_PROJECTION);		
		GL11.glViewport(0, 0, 600, 600);
		GL11.glScissor(0, 0, 600, 600);
		glLoadIdentity();		
		gluPerspective(78,1,5,40); // fovy, aspect w/h, near, far
		
		glMatrixMode(GL11.GL_MODELVIEW); // MODELVIEW is default matrix mode
		glLoadIdentity();
		
		for (int i = 0; i < entityList.size(); i++){
			entityList.get(i).init();
		}
		glEnable(GL_DEPTH_TEST);
	}
	
	public void splitRender(float cycleTime) {
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		//MAIN VIEW
		glMatrixMode(GL11.GL_PROJECTION);		
		GL11.glViewport(0, 0, 600, 600);
		GL11.glScissor(0, 0, 600, 600);
		glLoadIdentity();		
		gluPerspective(100,1,0.01f,100); // fovy, aspect w/h, near, far
		renderWithRotation(cycleTime, 90, main[0], main[1], main[2]);
	}


	/**
	 * Program starts here.
	 * @param argv command line arguments are ignored
	 * @throws IOException 
	 * @throws LWJGLException 
	 */
	public static void main(String argv[]) throws IOException, LWJGLException {
		OpenGLShellApp shellApp = new OpenGLShellApp();
		shellApp.init(); // initialize data, graphics etc. before main loop
		shellApp.displayInfo(); // display info before dropping into main loop
		shellApp.mainLoop();
		Display.destroy();
	}


	/**
	 * The main loop - checks events, updates & renders
	 */
	public void mainLoop() {		
		boolean keepRunning = true;
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Sys.alert("Error", "Couldn't Create KEYBOARD!! "+e.getMessage());
		}
		
		Random r = new Random();
		for (int i = 0; i < 4000; i++) {
			particleList.add(new Particle());
			particleList.get(i).setCoords(r.nextFloat()*10, r.nextFloat()*10, r.nextFloat()*10);
		}
		
		while (keepRunning) {
			dTime = persTimer.getTime() - lastTime;
			lastTime = persTimer.getTime();
			keyboardInteraction();
			
			if (Keyboard.isKeyDown(Keyboard.KEY_UP))Particle.setGlobalAccel(0.3f);
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))Particle.setGlobalAccel(-0.3f);
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))addParticles();
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))removeParticles();
			
			if (!pause) { // advance timer
				cycleTime = secTimer.getTime();
			} else {
				secTimer.set(cycleTime); // keep timer stuck until unpaused
			}
			
			for (int i = 0; i < particleList.size(); i++) {
				particleList.get(i).updateParticle(-1*dTime);
				if (particleList.get(i).isDeleted()) {
					particleList.remove(i);
				    particleList.add(new Particle(this));
				    particleList.get(particleList.size() - 1).setCoords(r.nextFloat()*10, r.nextFloat()*10, r.nextFloat()*10);
				}
			}
			splitRender(cycleTime); // render the model
			
			Display.update(); // lwjgl required to scan inputs, switch double buffer etc.
			Timer.tick(); // lwjgl required to advance the timer
			Display.sync(60); // ideally this works to reduce CPU load (may not on some systems)
			if (Display.isCloseRequested()) {
				keepRunning = false;
				System.exit(0);
			}
			VIEW_REMAINING -= dTime;
		}
	}
	
	private void renderWithRotation(float cycleTime, float angle, float x, float y, float z) {
		
		// clears colors and depth values
		for (int i = 0; i < entityList.size(); i++) {
			entityList.get(i).renderWithRotation(cycleTime, angle, x, y, z);
		}
		
		for (int i = 0; i < numParticles; i++) {
			particleList.get(i).renderWithRotation(cycleTime, angle, x, y, z);
		}
	}


	/**
	 * Prints Java Path info as well as mesh object in text form to stdout.
	 * 
	 */
	private void displayInfo() {
		System.out.println("java.library.path: "+System.getProperty("java.library.path"));
		System.out.println("java.class.path: "+System.getProperty("java.class.path"));
		//meshViewer.displayInfo(true);
	}


	/**
	 * Reads the keyboard and sets state variables: pause, wireframe, showNormals
	 */
	private void keyboardInteraction() {
		int key;
		if (Keyboard.next()) {
			key = Keyboard.getEventKey();
			if (!Keyboard.isKeyDown(key)) {
				switch (key) {
				case Keyboard.KEY_SPACE:
					pause = !pause;
					break;
				case Keyboard.KEY_T:
					Particle.toggleRain();
				}
			}
	
		}
	}

	// Used to let other objects query status of properties
	// which might be set by user input
	public boolean isPaused() {
		return pause;
	}


	public boolean isShowNormals() {
		return showNormals;
	}


	public boolean isWireframe() {
		return wireframe;
	}
	
	public void addParticles() {
		if (numParticles < 4000) numParticles += 100;
		System.out.println(numParticles + "");
	}
	
	public void removeParticles() {
		if (numParticles > 0) numParticles -= 100;
		System.out.println(numParticles + "");
	}

}
