import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;

/*
 * javac -cp ./lib/*:. TestXX.java
 *
 * java -cp ./lib/*:. TestXX
 */

public class Test01 {

    public Test01() {
        /** Create World **/
        Vec2 gravity = new Vec2(0.0f, -9.81f);
        World world = new World(gravity);

	   /* Intermediary variables*/
        BodyDef bodyDef;
        PolygonShape poly;
        CircleShape circ;
        FixtureDef fixDef;
        /* Object's body */
        Body floorBody, ballBody;

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

	   float timeStep = 1/6.0f; // Each turn, we advance of 1/6 of second
	   int nbTurn = Math.round(2/timeStep); // 2 second of simulation => 12 turn

        /* Launch the simulation */
        for(int i = 0 ; i < nbTurn ; i++) {
            world.step(timeStep, 8, 3); // Move all objects
            System.out.println(ballBody.getPosition()); // Position of the ball
        }
    }

    public static void main(String[] args) {
        new Test01();

    }
}
