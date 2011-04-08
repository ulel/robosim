package robosim.robot.components;

import java.awt.geom.AffineTransform;

import robosim.robot.Robot;

import net.phys2d.raw.*;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.DynamicShape;
import net.phys2d.raw.shapes.Shape;

/**
 * The RobotComponent class provides an abstract baseclass for all robot components. 
 * It provides a standard body and joins it with the robot's hull. It also handles 
 * (initial) translation and rotation of the component in relation to the robot.
 */
public abstract class RobotComponent {

	protected World world;
	protected Robot robot;
	protected Body componentBody;
	protected Joint robotJoint;
	protected AffineTransform transform;
	private float localX;
	private float localY;
	private float rotation;
	
	public RobotComponent(World w, Robot r, float posX, float posY, float rotation, float mass) {
		this.world = w;
		this.robot = r;
		this.localX = posX;
		this.localY = posY;
		this.rotation = rotation;
		this.componentBody = new Body(this.createDefaultComponentShape(), mass);
		
		this.setTransform(posX, posY, rotation);
		this.createRobotJoint();
		
		world.add(this.componentBody);
	}
	
	/**
	 * Returns the default shape for the component.
	 */
	protected DynamicShape createDefaultComponentShape() {
		return new Box(5, 5);
	}
	
	
	/**
	 * Creates a FixedJoint between the component and the robot's hull.
	 */
	protected void createRobotJoint() {
		this.robotJoint = new FixedJoint(this.componentBody, this.robot.getHull().getComponentBody());
		this.world.add(this.robotJoint);
	}
	
	protected void setTransform(float posX, float posY, float rotation) {
		this.transform = new AffineTransform();
		this.transform.translate(this.robot.getPosX(), this.robot.getPosY());
		this.transform.rotate(this.robot.getRotation());
		this.transform.translate(posX, posY);
		this.transform.rotate(rotation);
		
		this.componentBody.setPosition(this.getWorldPosX(), this.getWorldPosY());
		this.componentBody.setRotation(this.getWorldRotation());
	}
	
	/**
	 * Returns the components x position in relation to the robot.
	 */
	public float getLocalPosX() { return this.localX; }
	
	/**
	 * Returns the components y position in relation to the robot.
	 */
	public float getLocalPosY() { return this.localY; }
	
	/**
	 * Returns the components rotation in relation to the robot.
	 */
	public float getLocalRotation() { return this.rotation; }
	
	/**
	 * Returns the components x position in relation to the world.
	 */
	public float getWorldPosX() { return (float)this.transform.getTranslateX(); }
	
	/**
	 * Returns the components y position in relation to the world.
	 */
	public float getWorldPosY() { return (float)this.transform.getTranslateY(); }
	
	/**
	 * Returns the components rotation in relation to the world.
	 */
	public float getWorldRotation() { return this.robot.getRotation() + this.rotation; }
	
	/**
	 * Returns the body of this component.
	 */
	public Body getComponentBody() { return this.componentBody; }
	
	/**
	 * Sets the component body's mass.
	 */
	public void setComponentMass(float m) {
		this.componentBody.set(this.componentBody.getShape(), m);
	}
	
	/**
	 * Sets the component body's shape.
	 */
	public void setComponentShape(Shape s) {
		this.componentBody.setShape(s);
	}
	
	
	
	/*
	 * Setting the position and rotation after the component has been initialized is not supported (yet?).
	 * This is because it is a little tricky to implement when it comes to joints.
	 * 
	 * So if you really need to move your robot somewhere, make it drive there ;-)
	 * 
	public void setLocalPosX(float x) {
		this.setTransform(x, this.getLocalPosY(), this.getLocalRotation());
	}
	public void setLocalPosY(float y) {
		this.setTransform(this.getLocalPosX(), y, this.getLocalRotation());
	}
	public void setLocalPosition(float x, float y) {
		this.setTransform(x, y, this.getLocalRotation());
	}
	public void setLocalRotation(float r) {
		this.setTransform(this.getLocalPosX(), this.getLocalPosY(), r);
		this.rotation = r;
	}
	*/
}
