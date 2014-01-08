import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.contacts.*;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.callbacks.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

import fr.atis_lab.physicalworld.*;

public class Link implements ContactListener, Serializable {

	private PhysicalWorld world;
	
	
	private Ball neighbour1;
	private Ball neighbour2;
	private Body body;


	public Link(PhysicalWorld world, Ball neighbour1, Ball neighbour2){
		this.world = world;
		this.neighbour1 = neighbour1;
		this.neighbour2 = neighbour2;
		DistanceJointDef join = new DistanceJointDef();
		join.collideConnected = true;
		join.dampingRatio = 10f;
		join.frequencyHz = 10f;
		join.initialize(neighbour1.getBody(), neighbour2.getBody(), neighbour1.getPosition(), neighbour2.getPosition());
		world.getJBox2DWorld().createJoint(join);
	}
	
	


	/* Event when object are touching */
	public void beginContact(Contact contact) {
		//  System.out.println("Objects are touching "+Sprite.extractSprite(contact.getFixtureA().getBody()).getName() +" "+Sprite.extractSprite(contact.getFixtureB().getBody()).getName() );
	}

	/* Event when object are leaving */
	public void endContact(Contact contact) {
		//  System.out.println("Objects are leaving "+Sprite.extractSprite(contact.getFixtureA().getBody()).getName() +" "+Sprite.extractSprite(contact.getFixtureB().getBody()).getName() );
	}


	/* Advanced stuff */
	public void postSolve(Contact contact, ContactImpulse impulse) {}
	public void preSolve(Contact contact, Manifold oldManifold) {}


}
