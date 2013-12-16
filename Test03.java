import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.callbacks.*;
import java.awt.*;
import javax.swing.*;

/*
 * javac -cp ./lib/*:. TestXX.java
 *
 * java -cp ./lib/*:. TestXX
 */

public class Test03 extends JPanel implements ContactListener {

    /* JBox2D World */
    World world;
    /* Object's body */
    private Body floorBody, ballBody;

    public Test03()  {
        /** Create World **/
        Vec2 gravity = new Vec2(0.0f, -9.81f);
        world = new World(gravity);
        /* Add the ContactListener to the World */
        world.setContactListener(this); 

        /* Intermediary variables*/
        /* ... */
        BodyDef bodyDef;
        PolygonShape poly;
        CircleShape circ;
        FixtureDef fixDef;

        /** Create a horizontal polygon (the floor) **/
        /* Define body properties */
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC; // Gravity is not applied to static objects
        bodyDef.position.set(0,0);
        bodyDef.angle = 0f;
        /* Create body from the world and body properties */
        floorBody = world.createBody(bodyDef);
        /* Define shape as polygon */
        poly = new PolygonShape();
        poly.setAsBox(200f, 0.01f, new Vec2(0,0), 0);
        /* Define the fixture properties */
        fixDef = new FixtureDef();
        fixDef.shape = poly; // The fixture is linked to the shape
        /* Create fixture from the body and fixture properties */
        floorBody.createFixture(fixDef);

        /** Create a ball (object) **/
        /* Define body properties */
        bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC; // Gravity is applied to dynamic objects
        bodyDef.position.set(0, 10);
        bodyDef.angle = 0f;
        /* Create body from the world and body properties */
        ballBody = world.createBody(bodyDef);
        /* Define shape as polygon */
        circ = new CircleShape();
        circ.setRadius(3);
        /* Define the fixture properties */
        fixDef = new FixtureDef();
        fixDef.shape = circ; // The fixture is linked to the shape
        fixDef.restitution=0.4f; // Bouncing ball
        /* Create fixture from the body and fixture properties */
        ballBody.createFixture(fixDef);

        /* Wrapping JFrame */
        JFrame frame = new JFrame("JBox2D GUI");
        frame.setMinimumSize(new Dimension(640,480));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER); // Add Test03 Panel to the frame

        frame.pack();
        frame.setVisible(true);

    }

    /*
    * If we put the simulation loop in the constructor, it would be played before showing the GUI
    * So we put it in an external loop
    */
    public void run() {
        try {

            float timeStep = 1/6.0f; // Each turn, we advance of 1/6 of second
            int nbTurn = Math.round(2/timeStep); // 2 second of simulation => 12 turn
            int msSleep = Math.round(1000*timeStep); // timeStep in milliseconds

            /* Launch the simulation */
            for(int i = 0 ; i < nbTurn ; i++) { // 12 turn = 2 seconds
                world.step(timeStep, 8, 3); // Move all objects
                Thread.sleep(msSleep); // Synchronize the simulation with real time
                this.updateUI(); // Update graphical interface
            }
        } catch(InterruptedException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public int getWidth() {
        return 640;
    }
    @Override
    public int getHeight() {
        return 480;
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);


        float scale = 10;
        Vec2 objectPos;
        float radius;
        Point drawingPos, drawingSize;
        // Custom drawing by method calls to the Graphics instance (see java.awt.Graphics)
        g.setColor(Color.BLACK);     // Set the color to Black
        g.fillRect(0,0, getWidth(), getHeight()); // Fill a rectangle where top-left corner is at (0,0) and of the same size than the component
        g.setColor(Color.YELLOW);    // Set the color to Yellow

        objectPos= new Vec2(ballBody.getPosition()); // Position of the ball, in the world coordinate, centered
        // We need to take a copy (through the constructor) so we can change the coordinate without modifiying the simulation
        radius = ballBody.getFixtureList().getShape().getRadius(); // Get Radius of the circle
        // Coordinate conversion from the center to the top-left corner (we know the radius is 3)
        objectPos.x -= radius;
        objectPos.y += radius;
        // Scaling (and rounding) conversion => Drawing will be 10 times bigger
        drawingPos = new Point(Math.round(objectPos.x*scale), Math.round(objectPos.y*scale));
        // For a circle => width = height = diameter = 2*radius
        drawingSize = new Point(Math.round(2*radius*scale), Math.round(2*radius*scale));
        // Reference change from X-Centered, Y-Bottom Positive up (World ref) to X-Left,Y-Top Positive down (Drawing ref)
        drawingPos.x += getWidth()/2;
        drawingPos.y = getHeight() - drawingPos.y;
        // Actual drawing of the circle
        g.fillOval(drawingPos.x,drawingPos.y, drawingSize.x, drawingSize.y);
    }

    /* PREVIOUS CODE */
    /* Event when object are touching */
    public void beginContact(Contact contact) {
        System.out.print("Objects are touching ");
        System.out.println(contact.getFixtureA() +" "+contact.getFixtureB() );
    }

    /* Event when object are leaving */
    public void endContact(Contact contact) { 
        System.out.print("Objects are leaving ");
        System.out.println(contact.getFixtureA() +" "+contact.getFixtureB() );
    }
    
    /* Advanced stuff */
    public void postSolve(Contact contact, ContactImpulse impulse) { }
    public void preSolve(Contact contact, Manifold oldManifold) { }

    public static void main(String[] args) {
        new Test03().run();
    }
}
