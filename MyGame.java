import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.callbacks.*;
import org.jbox2d.dynamics.joints.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

import fr.atis_lab.physicalworld.*;

/*
  LUCAS IL RACONTE N'IMPORTE QUOI
 * javac -cp ./lib/*:. TestXX.java
 *
 * java -cp ./lib/*:. TestXX
 */
/* import ... */
public class MyGame implements ContactListener, MouseListener, Serializable {

    private PhysicalWorld world;
    private DrawingPanel panel;

    private static DebugDraw debugDraw;    
    /* Temporary reference to the objects */
    private transient Body ramp, door, ball2, ball3, line;
    private Ball ball4, ball;
    private ArrayList<Ball> listBalls = new ArrayList<Ball>();
    // normalement pas besoin de la liste des Links vu que les liens appartiennent à l'objet Ball
    // private ArrayList<Link> listLinks = new ArrayList<Link>();
    private JFrame frame;
    private float ex,ey;
    public MyGame(){
    
	/* ... */
        /* Allocation of the PhysicalWorld (gravity, dimensions, color of the walls */
        world = new PhysicalWorld(new Vec2(0,-9.81f), -48, 48, 0, 64, Color.RED);
        /* Add ContactListener to the PhysicalWorld */
        world.setContactListener(this);
        
        try {

	    /* Allocation of the ball : radius of 3, position (0, 10), yellow, with an Image */
	    /* PhysicalObject are automatically added to the PhysicalWorld */
	    
	    ball = new Ball(world, new Vec2(-30,10));
	    ball4 = new Ball(world, new Vec2(-10,10));
	    //listBalls.add(ball);
	    //listBalls.add(ball);
	    Link li = new Link(world,ball, ball4);
	    //listLinks.add(li);
	    //test = world.addLine(new Vec2(0,0), new Vec2(20,20),BodyType.STATIC, 0, new Sprite("test",1,Color.RED,null));
	    // RIP AU BAZOOKA
	    //debugDraw.setFlags(debugDraw.e_jointBit);
	    //world.getJBox2DWorld().setDebugDraw(debugDraw);
	    

	    System.out.println(ball.getPosition()+" "+ball4.getPosition());
	    

	    line = world.addRectangularObject(0.0000001f, 480f, BodyType.STATIC, new Vec2(0, 0), 0, new Sprite("line", 2, Color.BLUE, null));
	    line.getFixtureList().setSensor(true);

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
	    
	    

	    for(int i = 0 ; i > -1 ; i++) { // 300 turn = 5 seconds
                world.step(); // Move all objects
		//daoustFunction(daoust);
		panel.setCameraPosition(new Vec2(0,30));
                Thread.sleep(msSleep); // Synchronize the simulation with real time
		
                this.panel.updateUI(); // Update graphical interface
		
	    }
	    
            System.out.println(world);
        } catch(InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }
	
	
    private Ball getBallUnderPosition(int x, int y) throws NoBallHereException{
    		for(Ball ball:this.listBalls){
		    if(((x/10 - 50) <= ball.getPosition().x +3)
		       &&((x/10 - 50) >= ball.getPosition().x -3)
		       &&((x/10 - 50) >= ball.getPosition().y -3)
		       &&((65-y/10) <= ball.getPosition().y+3)){ //if a ball exists under the cursor
			
			return (ball);
			}
		}
		throw(new NoBallHereException());
    		
    }
    
    
    /* Event when object are touching */
    public void beginContact(Contact contact) {
        //System.out.println("Objects are touching "+Sprite.extractSprite(contact.getFixtureA().getBody()).getName() +" "+Sprite.extractSprite(contact.getFixtureB().getBody()).getName() );
    }

    /* Event when object are leaving */
    public void endContact(Contact contact) {
        //System.out.println("Objects are leaving "+Sprite.extractSprite(contact.getFixtureA().getBody()).getName() +" "+Sprite.extractSprite(contact.getFixtureB().getBody()).getName() );
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
	    // ball = world.getObject("ball");
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
	if(e.getButton() == MouseEvent.BUTTON3){
	    if(listBalls.isEmpty()){
	    }
	    else{
		ArrayList<Ball> copy = new ArrayList<Ball>(listBalls);
		for(Ball ball:copy){
		    if(((e.getX()/10 - 50) <= ball.getPosition().x +3)
		       &&((e.getX()/10 - 50) >= ball.getPosition().x -3)
		       &&((e.getX()/10 - 50) >= ball.getPosition().y -3)
		       &&((65-e.getY()/10) <= ball.getPosition().y+3)){ //if a ball exists under the cursor
			
			ex = (e.getX()/10 - 50);
			ey = (65-e.getY()/10);
			
			int pos = copy.indexOf(ball);
			System.out.println("Ball "+ball.getId());
			listBalls.get(pos).setPosition(new Vec2(ex,ey));
		    }
		}
	    }
	}
	else{
	}
    }
   
    public void mouseClicked(MouseEvent e){
	System.out.println("Mouse pressed "+e.getButton());
	boolean isIn = false;
	if(e.getButton() == MouseEvent.BUTTON1){
	    System.out.println("Youpi "+e.getX()+" "+e.getY());
	    if(listBalls.isEmpty())
		isIn = false;
	    else{
		for(Ball ball:listBalls){
		    if(((e.getX()/10 - 50) < ball.getPosition().x +3) &&((65-e.getY()/10) < ball.getPosition().y+3)){
			isIn = true;
			System.out.println("Ball "+ball.getId());
		    }
		}
	    }
	    try{
		if(!isIn){
		    Ball n = new Ball(world, new Vec2(e.getX()/10 - 50,65-e.getY()/10));
		    listBalls.add(n);
		}
		System.out.println((e.getX()/10)+" "+(e.getY()/10));
	    } catch (InvalidSpriteNameException ex) {
		ex.printStackTrace();
		System.exit(-1);
	    }
	}
    }

    /*	
    public void drawString(float x, float y, String s, Color3f color){}
    public void drawTransform(Transform xf) {}
    public void drawSolidPolygon(Vec2[] vertices, int vertexCount, Color3f color) {}
    public void drawSolidCircle(Vec2 center, float radius, Vec2 axis, Color3f color) {}
    public void drawSegment(Vec2 p1, Vec2 p2, Color3f color) {}
    public void drawPoint(Vec2 argPoint, float argRadiusOnScreen, Color3f argColor) {}
    public void drawCircle(Vec2 center, float radius, Color3f color) {}
    */
}
