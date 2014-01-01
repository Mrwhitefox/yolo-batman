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

public class Ball implements ContactListener, Serializable {

	private PhysicalWorld world;
	private static int currentID = 0;
	private int id;
	private static float DEFAULT_RADIUS = 3f;
	private static String DEFAULT_IMAGE = "./img/emosmile.png";
	private static float DEFAULT_RESTITUTION = 0.3f;

	/* Temporary reference to the objects */
	private Body ball ;



	public Ball(PhysicalWorld world, Vec2 position, String name, Float radius, ImageIcon image, Float restitution) throws InvalidSpriteNameException   {
		this.world = world;
		ball = world.addCircularObject(radius, BodyType.DYNAMIC, position, 0, new Sprite(name, 1, Color.YELLOW, image));
		ball.getFixtureList().setRestitution(restitution);
		currentID++;
		this.id = currentID;
	}
	
	public Ball(PhysicalWorld world, Vec2 position) throws InvalidSpriteNameException{
		
		this.world = world;
		currentID++;
		this.id = currentID;
		ball = world.addCircularObject(DEFAULT_RADIUS, BodyType.DYNAMIC, position, 0, new Sprite("ball"+this.id, 1, Color.YELLOW, new ImageIcon(DEFAULT_IMAGE)));
		ball.getFixtureList().setRestitution(DEFAULT_RESTITUTION);
	}

	public Vec2 getPosition() {
		return this.ball.getPosition();
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
