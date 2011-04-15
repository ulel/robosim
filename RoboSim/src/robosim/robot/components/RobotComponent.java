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
	protected float localX;
	protected float localY;
	protected float rotation;
	
	public RobotComponent(float mass) {
		this.componentBody = new Body(this.createDefaultComponentShape(), mass);
	}
	
	/**
	 * Returns the default shape for the component.
	 */
	protected DynamicShape createDefaultComponentShape() {
		return new Box(5, 5);
	}
	
	
	/**
	 * Called when adding the component to the robot. Sets up the transform and joint.
	 */
	public void addToRobot(World w, Robot r, RobotComponent joinToComponent, float posX, float posY, float rotation) {
		this.world = w;
		this.robot = r;
		
		this.setTransform(posX, posY, rotation);
		
		this.robotJoint = this.createRobotJoint(joinToComponent);
		if (this.robotJoint != null)
			this.world.add(this.robotJoint);
		
		this.world.add(this.componentBody);
	}
	
	public void removeFromRobot() {
		if (this.robotJoint != null)
			this.world.remove(this.robotJoint);
		
		this.world.remove(this.componentBody);
	}
	
	/**
	 * Returns a Joint between the component's body and the robot's component to join to.
	 * If the component should not be joined, return null. 
	 */
	protected Joint createRobotJoint(RobotComponent joinToComponent) {
		return new FixedJoint(this.componentBody, this.robot.getHull().getComponentBody());
	}
	
	protected void setTransform(float posX, float posY, float rotation) {
		this.localX = posX;
		this.localY = posY;
		this.rotation = rotation;
		
		AffineTransform transform = new AffineTransform();
		transform.translate(this.robot.getPosX(), this.robot.getPosY());
		transform.rotate(this.robot.getRotation());
		transform.translate(posX, posY);
		transform.rotate(rotation);
		
		this.componentBody.setPosition((float)transform.getTranslateX(), (float)transform.getTranslateY());
		this.componentBody.setRotation(this.robot.getRotation() + this.rotation);
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
	public float getWorldPosX() { return this.componentBody.getPosition().getX(); } //(float)this.transform.getTranslateX(); }
	
	/**
	 * Returns the components y position in relation to the world.
	 */
	public float getWorldPosY() { return this.componentBody.getPosition().getY(); } //(float)this.transform.getTranslateY(); }
	
	/**
	 * Returns the components rotation in relation to the world.
	 */
	public float getWorldRotation() { return this.componentBody.getRotation(); } //this.robot.getRotation() + this.rotation; }
	
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
	
	
	
	/**
	 * Update function called at every simulation step (by the Robot it is added to).
	 * Override to add component specific updates.
	 */
	public void update() { }
	
	
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
