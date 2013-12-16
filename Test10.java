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

/* import ... */
public class Test10 implements Serializable {

    /* PhysicalWorld => contains the World and walls (as PhysicalObject) */
    private PhysicalWorld world;
    /* PhysicalObject => contains the Body, Shape & Fixture */
    //private PhysicalObject ball, ramp, door;
    transient private Body canon, ball1, ball2;
    /* Custom Panel for drawing purpose */
    private DrawingPanel panel;

    public Test10()  {
    
        /* Allocation of the PhysicalWorld (gravity, dimensions, color of the walls */
        world = new PhysicalWorld(new Vec2(0,-9.81f), -42.5f, 42.5f, 0, 64, Color.RED);

        try {

		  /* The canon is a static rectangular shape that will rotate and throw bullet from both sides */
            canon = world.addRectangularObject(2f, 10f, BodyType.STATIC, new Vec2(0, 22), MathUtils.PI/4, new Sprite("canon", 0, Color.BLACK, null));

            
            /* The balls are simple static circular sensors that will follow each ends of the rectangle */
            ball1 = world.addCircularObject(1f, BodyType.STATIC, canon.getWorldPoint(new Vec2(0,5)) , 0, new Sprite("ball1", 1, Color.RED, null));
            ball1.getFixtureList().setSensor(true);
            ball2 = world.addCircularObject(1f, BodyType.STATIC, canon.getWorldPoint(new Vec2(0,-5)) , 0, new Sprite("ball2", 1, Color.GREEN, null));
            ball2.getFixtureList().setSensor(true);


        } catch (InvalidSpriteNameException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        /* Allocation of the drawing panel of  size 640x480 and of scale x10 */
        /* The DrawingPanel is a window on the world and can be of a different size than the world. (with scale x10, the world is currently 960x640) */
        /* The DrawingPanel panel is centered around the camera position (0,0) by default */
        this.panel = new DrawingPanel(world, new Dimension(640,480), 7.5f);
        /* Setting the color of the background */
        this.panel.setBackGroundColor(Color.BLACK);
        /* Setting an image as background */
        this.panel.setBackGroundIcon(new ImageIcon("./img/paysage.png"));
        this.panel.setCameraPosition(new Vec2(0,32));
        
        /* Wrapping JFrame */
        JFrame frame = new JFrame("JBox2D GUI");
        frame.setMinimumSize(this.panel.getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this.panel, BorderLayout.CENTER); // Add DrawingPanel Panel to the frame
        frame.pack();
        frame.setVisible(true);
   }

    /*
    * Simulation loop
    */
    public void run() {
        try {
            float timeStep = 1/30.0f; // Each turn, we advance of 1/6O of second
            int msSleep = Math.round(1000*timeStep); // timeStep in milliseconds
            world.setTimeStep(timeStep); // Set the timeStep of the PhysicalWorld
	       int i = 0;
	       
            /* Launch the simulation */
            while(true) { // Infinite loop
            	 
            	 // Manually rotate the static canon
            	 // You can adjust speed as you want :)
                canon.setTransform(canon.getPosition(), canon.getAngle()+0.01f);
                
                // Manually translate the balls at the ends of the rectangle
                // As the rectangle is of size 2x10 and that the orgin is centered (1,5)
                // the extrema are (0, 5) and (0, -5) in the object referential
                // To have their position in the world referential, we use the
                // getWorldPoint method.
                ball1.setTransform(canon.getWorldPoint(new Vec2(0,5)),0);
                ball2.setTransform(canon.getWorldPoint(new Vec2(0,-5)),0);
                
                
                // To avoid to outrun the processor, we will throw a ball every 4 step (you can change this)
                if(world.getStepIdx()%4==0) {

			 // Create two bullets
	           try {
	           	 // First bullet (watchout for the unique name condition)
	           	 // We place the bullet at the extrema of the canon (but outside the canon, to avoid collision !)
		           Body bullet = world.addCircularObject(1f, BodyType.DYNAMIC, canon.getWorldPoint(new Vec2(0,6)) , 0, new Sprite("bullet"+(i++), 1, Color.YELLOW, null));
		           // We set the set the speed of the bullet to be 30, colinear to the canon
		           // Colinearity can be seen as the vertical in the object referential
		           // So we use the method getWorldVector to have the vector in the World referential
				bullet.setLinearVelocity(canon.getWorldVector(new Vec2(0,30)));
				// Second bullet, from the other side, with inverted speed
				 bullet = world.addCircularObject(1f, BodyType.DYNAMIC, canon.getWorldPoint(new Vec2(0,-6)) , 0, new Sprite("bullet"+(i++), 1, Color.BLUE, null));
				bullet.setLinearVelocity(canon.getWorldVector(new Vec2(0,-30)));

		           } catch (InvalidSpriteNameException ex) {
		           	System.err.println(ex.getMessage());
		           	System.exit(-1);
		           }
                }
                
                world.step(); // Move all objects
                Thread.sleep(msSleep); // Synchronize the simulation with real time
                this.panel.updateUI(); // Update graphical interface
            }
        } catch(InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Test10().run();
    }
}
