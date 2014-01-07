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
public class MyGame implements ContactListener, MouseListener, Serializable {

    private PhysicalWorld world;
    private DrawingPanel panel;
    
    /* Temporary reference to the objects */
    private transient Body ball, ramp, door, ball2, ball3, line;
    private Ball ball4;
    private ArrayList<Ball> listBalls;
    private JFrame frame;

    public MyGame(){
    
	/* ... */
        /* Allocation of the PhysicalWorld (gravity, dimensions, color of the walls */
        world = new PhysicalWorld(new Vec2(0,-9.81f), -48, 48, 0, 64, Color.RED);
        /* Add ContactListener to the PhysicalWorld */
        world.setContactListener(this);
        
        try {

	    /* Allocation of the ball : radius of 3, position (0, 10), yellow, with an Image */
	    /* PhysicalObject are automatically added to the PhysicalWorld */
	    ball = world.addCircularObject(3f, BodyType.DYNAMIC, new Vec2(40, 10), 0, new Sprite("ball", 1, Color.YELLOW, new ImageIcon("./img/emosmile.png")));
	    //ball3 = world.addCircularObject(3f, BodyType.DYNAMIC, new Vec2(0, 20), 0, new Sprite("ball3", 1, Color.YELLOW, new ImageIcon("./img/emosmile.png")));
	    //ball2 = world.addCircularObject(3f, BodyType.DYNAMIC, new Vec2(0, 30), 0, new Sprite("ball2", 1, Color.YELLOW, new ImageIcon("./img/emosmile.png")));
	    ball4 = new Ball(world, new Vec2(-20, 20)); 



	    Vec2 s = new Vec2(10,10);
	    Vec2 e = new Vec2(30,30);
	    //line = new EdgeShape(s,e,null,null,false,false);



	    //*******************************************************//
	    //*******************************************************//
	    //************* POUR M. GADEMER **************************//
	    //*******************************************************//
	    //*******************************************************//


	    line = world.addRectangularObject(0.0000001f, 480f, BodyType.STATIC, new Vec2(0, 0), 0, new Sprite("line", 2, Color.BLUE, null));
	    line.getFixtureList().setSensor(true);

	    float x = 3;
	    float y = 10;
	    EdgeShape shape = new EdgeShape();
	    shape.set(s, e);
	    //line = world.createObject(shape, BodyType.DYNAMIC, new Vec2(x,y), 0, new Sprite("line", 2, Color.BLUE, null));
	    

	    /*BodyDef bodyDef = new BodyDef();
	    bodyDef.type = BodyType.DYNAMIC;
	    bodyDef.position.set(x, y);
	    

	    World umad = world.getJBox2DWorld();
	    Body body = world.getJBox2DWorld().createBody(bodyDef);

	    /* Changing the restitution parameter of the PhysicalObject */
	    ball.getFixtureList().setRestitution(0.4f);

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
        this.panel = new DrawingPanel(world, new Dimension(1000,700), 10f);
        /* Setting the color of the background */
        this.panel.setBackGroundColor(Color.BLACK);
        /* Setting an image as background */
        this.panel.setBackGroundIcon(new ImageIcon("./img/paysage.png"));
	
	/* Mouse listener */
	this.panel.addMouseListener(this);

        /* Wrapping JFrame */
        frame = new JFrame("World Of POO");
        frame.setMinimumSize(this.panel.getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this.panel, BorderLayout.CENTER); // Add DrawingPanel Panel to the frame
        frame.pack();
        frame.setVisible(true);
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
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    double width = screenSize.getWidth();
	    double height = screenSize.getHeight();
            MouseInfo.getPointerInfo().getLocation().setLocation(0,10);
	    for(int i = 0 ; i > -1 ; i++) { // 300 turn = 5 seconds
                world.step(); // Move all objects
		//System.out.println(MouseInfo.getPointerInfo().getLocation());
		
		panel.setCameraPosition(new Vec2(0,30));
		//panel.setCameraPosition(new Vec2((float)((MouseInfo.getPointerInfo().getLocation().getX()-320)/9),(float)((MouseInfo.getPointerInfo().getLocation().getY()-130)/7))); // The camera will follow the ball
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
        new MyGame().run();
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
	    line = world.getObject("line");
	} catch(ObjectNameNotFoundException ex) {
	    ex.printStackTrace();
	    System.exit(-1);
	}
    }
    
    public void mouseExited(MouseEvent e){
    }
    public void mouseEntered(MouseEvent e){
    }
    public void mouseReleased(MouseEvent e){
    }
    public void mousePressed(MouseEvent e){
    }
    public void mouseClicked(MouseEvent e){
	System.out.println("Mouse pressed "+e.getButton());
	if(e.getButton() == MouseEvent.BUTTON1){
	    System.out.println("Youpi "+e.getX()+" "+e.getY());
	    try{
		Ball n = new Ball(world, new Vec2(e.getX()/10 - 50,65-e.getY()/10));
		System.out.println((e.getX()/10)+" "+(e.getY()/10));
	    } catch (InvalidSpriteNameException ex) {
		ex.printStackTrace();
		System.exit(-1);
	    }
	}
    }
}
