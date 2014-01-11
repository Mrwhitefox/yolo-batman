import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.callbacks.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

import fr.atis_lab.physicalworld.*;

/*
 * javac -cp ./lib/*:. TestXX.java
 *
 * java -cp ./lib/*:. TestXX
 */

public class Test11 implements KeyListener, Serializable {

    /* PhysicalWorld => contains the World and walls (as PhysicalObject) */
    private PhysicalWorld world;
    /* PhysicalObject => contains the Body, Shape & Fixture */
    //private PhysicalObject ball, ramp, door;
    transient private Body ball, ramp, door;
    /* Custom Panel for drawing purpose */
    private DrawingPanel panel;

    public Test11()  {
    
	    /* ... */

        /* Allocation of the PhysicalWorld (gravity, dimensions, color of the walls */
        world = new PhysicalWorld(new Vec2(0,-9.81f), -48, 48, 0, 64, Color.RED);

        try {

            /* Allocation of the ball : radius of 3, position (0, 10), yellow, with an Image */
            /* PhysicalObject are automatically added to the PhysicalWorld */
            
		
            try {
            AnimatedSprite ballSprite = new AnimatedSprite("ball", 1, Color.YELLOW);
		  /* Basic animation for the Sprite, no duration */
            ballSprite.addNewAction("Rest", new ImageIcon("./img/emosmile.png"), -1, null);
            ballSprite.setCurrentAction("Rest");
            
            /* Simple animation, after 30 step, it will return to "Rest" */
            ballSprite.addNewAction("Wink", new ImageIcon("./img/emowink.png"), 30, "Rest");
            
            /* Animation chain : Sick will last 30 step, then automatically pass to Sad (Sad will last until the animation is actively changed) */
            ballSprite.addNewAction("Sick", new ImageIcon("./img/emosick.png"), 30, "Sad");
            ballSprite.addNewAction("Sad", new ImageIcon("./img/emosad.png"), -1, null);
  
            /* AnimatedSprite extends Sprite, so it is does not change anything for all other class */
            ball = world.addCircularObject(3f, BodyType.DYNAMIC, new Vec2(0, 10), 0, ballSprite);
            
            } catch (InvalidActionNameException ex) {
            	 ex.printStackTrace();
	            System.exit(-1);
            }
            

            /* Changing the restitution parameter of the PhysicalObject */
            ball.getFixtureList().setRestitution(0.4f);

       /* Changing the friction parameter of the PhysicalObject */
       ball.getFixtureList().setFriction(10f);

		  /* ... */

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

            /* Simple rectangle, the Door is a sensor (the other object can go through, but are detected) */
            door = world.addRectangularObject(1f, 6f, BodyType.STATIC, new Vec2(14, 3), 0, new Sprite("door", 2, Color.BLUE, null));
            door.getFixtureList().setSensor(true);


        } catch (InvalidSpriteNameException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        /* Allocation of the drawing panel of  size 640x480 and of scale x10 */
        /* The DrawingPanel is a window on the world and can be of a different size than the world. (with scale x10, the world is currently 960x640) */
        /* The DrawingPanel panel is centered around the camera position (0,0) by default */
        this.panel = new DrawingPanel(world, new Dimension(640,480), 10f);
        /* Setting the color of the background */
        this.panel.setBackGroundColor(Color.BLACK);
        /* Setting an image as background */
        this.panel.setBackGroundIcon(new ImageIcon("./img/paysage.png"));
        
        /* Add KeyListener */
        this.panel.addKeyListener(this);
        	

        /* Wrapping JFrame */
        JFrame frame = new JFrame("JBox2D GUI");
        frame.setMinimumSize(this.panel.getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this.panel, BorderLayout.CENTER); // Add DrawingPanel Panel to the frame
        frame.pack();
        frame.setVisible(true);
        /* Request focus for panel MUST BE LAST LINE*/
        this.panel.requestFocus();	
    }

    /*
    * Simulation loop
    */
    public void run() {
    		System.out.println("You may use the arrows of the keyboard !");
        try {
            float timeStep = 1/60.0f; // Each turn, we advance of 1/6O of second
            int msSleep = Math.round(1000*timeStep); // timeStep in milliseconds
            world.setTimeStep(timeStep); // Set the timeStep of the PhysicalWorld

            /* Launch the simulation */
            while(true) { // Infinite loop
           
                world.step(); // Move all objects
                panel.setCameraPosition(ball.getPosition().add(new Vec2(0,20))); // The camera will follow the ball
                Thread.sleep(msSleep); // Synchronize the simulation with real time
                this.panel.updateUI(); // Update graphical interface
            }
        } catch(InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Test11().run();
    }

    /*
     * When a key is pressed
     * CTRL+Q	: Quit the program
     * UP 	: Jump
     * LEFT	: Roll left
     * RIGHT	: Roll right
     */
    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed "+ e.getKeyCode());
        boolean ctrl_pressed = false;
        if ((KeyEvent.CTRL_MASK & e.getModifiers()) != 0) { // Detect the CTRL modifier
            ctrl_pressed = true;
        }
        switch (e.getKeyCode()) {
        case KeyEvent.VK_Q:
        	if(ctrl_pressed) { // If Q AND CTRL are both pressed
        		System.out.println("Bye bye");
        		System.exit(-1);
        		}
            break;
        case KeyEvent.VK_UP:
        	  if(ball.getContactList() != null) { // If the ball is touching something
        	  	ball.applyForceToCenter(new Vec2(0,3000)); // Apply a vertical force of 3000 Newton to the ball

			/* If jump, make the emo wink */
        	  	try {
        	  	AnimatedSprite.extractAnimatedSprite(ball).setCurrentAction("Wink");
        	  	} catch(InvalidActionNameException ex) {
        	  		ex.printStackTrace();
        	  		System.exit(-1);
        	  	}
        	  }
            break;

        case KeyEvent.VK_RIGHT:
        	  ball.setAngularVelocity(-20); // Directly set the angular velocity
        	  
		  /* If rotate, make the emo sick */
        	  try {
        	  	AnimatedSprite.extractAnimatedSprite(ball).setCurrentAction("Sick");
        	  	} catch(InvalidActionNameException ex) {
        	  		ex.printStackTrace();
        	  		System.exit(-1);
        	  	}
            break;
        case KeyEvent.VK_LEFT:
        	  ball.setAngularVelocity(20); // Directly set the angular velocity
        	  
 		  /* If rotate, make the emo sick */
        	  try {
        	  	AnimatedSprite.extractAnimatedSprite(ball).setCurrentAction("Sick");
        	  	} catch(InvalidActionNameException ex) {
        	  		ex.printStackTrace();
        	  		System.exit(-1);
        	  	}
            break;
        }
    }
    public void keyTyped(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }
}
