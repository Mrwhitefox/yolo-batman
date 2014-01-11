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
    private DistanceJointDef join,join2;
    private WeldJointDef weld;
    private WeldJointDef weld2;
    private static int currentId = 0;
    private int id;

    public Link(PhysicalWorld world, Ball neighbour1, Ball neighbour2) throws InvalidSpriteNameException{
	this.world = world;
	this.neighbour1 = neighbour1;
	this.neighbour2 = neighbour2;
	currentId++;
	this.id = currentId;
	



	

	body = world.addLine(neighbour1.getPosition(),neighbour2.getPosition(),BodyType.DYNAMIC,0, new Sprite("link"+id,1,Color.BLACK,null));
	System.out.println(neighbour1.getPosition()+" "+neighbour2.getPosition()+" "+body.getPosition());
	body.getFixtureList().setSensor(true);


	/*this.join = new DistanceJointDef();
	join.collideConnected = true;
	join.dampingRatio = 10f;
	join.frequencyHz = 10f;
	join.initialize(neighbour1.getBody(), body, neighbour1.getPosition(), new Vec2(0,0));
	world.getJBox2DWorld().createJoint(join);*/
	   
	this.weld = new WeldJointDef();
	weld.collideConnected = true;
	weld.dampingRatio = 10f;
	weld.frequencyHz = 10f;
	//weld.bodyA = neighbour1.getBody();
	//weld.bodyB = body;
	//weld.localAnchorA.set(neighbour1.getPosition());
	//weld.localAnchorB.set(new Vec2(0,0));
	weld.initialize(neighbour1.getBody(), body,neighbour1.getPosition());
	this.weld2 = new WeldJointDef();
	weld2.collideConnected = true;
	weld2.dampingRatio = 0.1f;
	weld2.frequencyHz = 0.1f;
	//weld2.bodyA = neighbour2.getBody();
	//weld2.bodyB = body;
	//weld2.localAnchorA.set(neighbour2.getPosition());
	//weld2.localAnchorB.set(new Vec2(0,0));
	weld2.initialize(neighbour2.getBody(), body,neighbour2.getPosition());
	world.getJBox2DWorld().createJoint(weld);
	world.getJBox2DWorld().createJoint(weld2);


    }
    
    public void updateLink() throws InvalidSpriteNameException{
	try{world.destroyObject(body);}
	catch(LockedWorldException ex){System.err.println(ex.getMessage());}
	body = world.addLine(neighbour1.getPosition(),neighbour2.getPosition(),BodyType.DYNAMIC,0, new Sprite("link",1,Color.BLACK,null));
	System.out.println(neighbour1.getPosition()+" "+neighbour2.getPosition());
    }
	
    public Body getBody(){
	return this.body;
    }

    public Vec2 getStart(){
	return body.getPosition();
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
