import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.callbacks.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

import fr.atis_lab.physicalworld.*;

/*
 * javac -cp ./lib/*:. TestXX.java
 *
 * java -cp ./lib/*:. TestXX
 */
/* import ... */
public class Poc implements ContactListener, Serializable {

	private PhysicalWorld world;
	private DrawingPanel panel;

	/* Temporary reference to the objects */
	private transient Body ball, ramp, door, ball2, ball3;
	// pourra peut etre serveir plus tard : private <LinkedList>Ball ballList; // ! \\ the ID of elt in list != the ID of a Ball object !
	private Ball objetBall, objetBall2;

	private JFrame frame;

	public Poc()  {

		/* ... */
		/* Allocation of the PhysicalWorld (gravity, dimensions, color of the walls */
		world = new PhysicalWorld(new Vec2(0,-9.81f), -48, 48, 0, 64, Color.RED);
		/* Add ContactListener to the PhysicalWorld */
		world.setContactListener(this);

		try {

			/* CREATION BALLES */

			/* Allocation of the ball : radius of 3, position (0, 10), yellow, with an Image */
			/* PhysicalObject are automatically added to the PhysicalWorld */
			//ball = world.addCircularObject(3f, BodyType.DYNAMIC, new Vec2(0, 10), 0, new Sprite("ball", 1, Color.YELLOW, new ImageIcon("./img/emosmile.png")));
			/*objetBall = new Ball(world, new Vec2(0,10), "ball", 3f, new ImageIcon("./img/emosmile.png"), 0.4f);
			objetBall2 = new Ball(world, new Vec2(0,30), "ball2", 3f, new ImageIcon("./img/emosmile.png"), 0.4f);
			ball3 = world.addCircularObject(3f, BodyType.DYNAMIC, new Vec2(0, 20), 0, new Sprite("ball3", 1, Color.YELLOW, new ImageIcon("./img/emosmile.png")));*/
			
			//création d'objets Ball (fait maison)
			objetBall = new Ball(world, new Vec2(0,10));
			objetBall2 = new Ball(world, new Vec2(0,30));
			Ball objetBall3 = new Ball(world, new Vec2(0, 20));
			

			/* Changing the restitution parameter of the PhysicalObject */
			//ball.getFixtureList().setRestitution(0.4f);
			/* FIN BALLES */


			/*  CREATION POLYGONE */
			/* Complex polygon should be set as a list of points in COUNTERCLOCKWISE order */
			/* Here, we want a simple triangle. */
			/* Note that the (0,0) coordinate correspond to the rotation center */
			LinkedList<Vec2> vertices = new LinkedList<Vec2>();
			vertices.add(new Vec2(0,0));
			vertices.add(new Vec2(10,0));
			vertices.add(new Vec2(0,5));
			/* We create a PhysicalObject based on this polygon. It is a static object in (-5, 0), green with an image */
			/* In case of invalid polygon (less than 3 points, or too much) an InvalidPolygonException is raised */
			try {
				ramp = world.addPolygonalObject(vertices, BodyType.STATIC, new Vec2(-5, 0), 0, new Sprite("ramp", 0, Color.GREEN, new ImageIcon("./img/triangle.png")));
			} catch(InvalidPolygonException ex) {
				System.err.println(ex.getMessage());
				System.exit(-1);
			}
			/* FIN POLYGONE */


			/* CREATION SENSOR */
			/* Simple rectangle, the Door is a sensor (the other object can go through, but are detected) */
			door = world.addRectangularObject(1f, 6f, BodyType.STATIC, new Vec2(14, 3), 0, new Sprite("door", 2, Color.BLUE, null));
			door.getFixtureList().setSensor(true);
			/* FIN SENSOR */

		} catch (InvalidSpriteNameException ex) {
			ex.printStackTrace();
			System.exit(-1);
		}

		/* INIT AFFICHAGE */
		/* Allocation of the drawing panel of  size 640x480 and of scale x10 */
		/* The DrawingPanel is a window on the world and can be of a different size than the world. (with scale x10, the world is currently 960x640) */
		/* The DrawingPanel panel is centered around the camera position (0,0) by default */
		this.panel = new DrawingPanel(world, new Dimension(640,480), 10f);
		/* Setting the color of the background */
		this.panel.setBackGroundColor(Color.BLACK);
		/* Setting an image as background */
		this.panel.setBackGroundIcon(new ImageIcon("./img/paysage.png"));

		/* Wrapping JFrame */
		frame = new JFrame("JBox2D GUI");
		frame.setMinimumSize(this.panel.getPreferredSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this.panel, BorderLayout.CENTER); // Add DrawingPanel Panel to the frame
		frame.pack();
		frame.setVisible(true);
		/* FIN INIT AFFICHAGE */
	}

	/*
	* If we put the simulation loop in the constructor, it would be played before showing the GUI
	* So we put it in an external loop
	*/
	public void run() {
		try {
			frame.setVisible(true);
			float timeStep = 1/60.0f; // Each turn, we advance of 1/6O of second
			int nbTurn = Math.round(5f/timeStep); // 5 second of simulation => 300 turn
			int msSleep = Math.round(1000*timeStep); // timeStep in milliseconds
			world.setTimeStep(timeStep); // Set the timeStep of the PhysicalWorld

			/* Launch the simulation */
			for(int i = 0 ; i < nbTurn ; i++) { // 300 turn = 5 seconds
				world.step(); // Move all objects
				//panel.setCameraPosition(objetBall.getPosition().add(new Vec2(0,20))); // The camera will follow the ball
				panel.setCameraPosition(new Vec2(15,25));//position fixée de la caméra fixée au pifometre
				Thread.sleep(msSleep); // Synchronize the simulation with real time
				this.panel.updateUI(); // Update graphical interface
			}
			System.out.println(world);
		} catch(InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
	}

	/* Event when object are touching */
	public void beginContact(Contact contact) {
		System.out.println("Objects are touching "+Sprite.extractSprite(contact.getFixtureA().getBody()).getName() +" "+Sprite.extractSprite(contact.getFixtureB().getBody()).getName() );
	}

	/* Event when object are leaving */
	public void endContact(Contact contact) {
		System.out.println("Objects are leaving "+Sprite.extractSprite(contact.getFixtureA().getBody()).getName() +" "+Sprite.extractSprite(contact.getFixtureB().getBody()).getName() );
	}

	/* Advanced stuff */
	public void postSolve(Contact contact, ContactImpulse impulse) {}
	public void preSolve(Contact contact, Manifold oldManifold) {}

	public static void main(String[] args) {
		new Poc().run();
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		// Read all non-transient fields => Load 99% of the instance state
		in.defaultReadObject();
		// Relink the current instance with the JBox2D World
		world.setContactListener(this);
		// Relink the objects reference with the JBox2D Bodies
		try {
			ball = world.getObject("ball");
			ball2 = world.getObject("ball2");
			ball3 = world.getObject("ball3");
			ramp = world.getObject("ramp");
			door = world.getObject("door");
		} catch(ObjectNameNotFoundException ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
	}


}
