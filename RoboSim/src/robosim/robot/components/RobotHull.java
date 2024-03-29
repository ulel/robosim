package robosim.robot.components;

import net.phys2d.raw.Joint;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.DynamicShape;

/*
 * The robot hull is the main body of the robot, used to track the robot's position and rotation.
 * All other robot components will be constrained to this body.
 */
public class RobotHull extends RobotComponent {
	public static final long BODY_BITMASK = 5; //Set bit 1 and 4, in case some body wants to collide with the hull, but not with the standard bodies. 
	
	public RobotHull(float mass) {
		super(mass);
		this.componentBody.setBitmask(BODY_BITMASK);
		this.componentBody.setFriction(0.2f);
	}
	
	@Override
	protected DynamicShape createDefaultComponentShape() {
		return new Box(30, 30);
	}
	
	@Override
	protected Joint createRobotJoint(RobotComponent joinToComponent) {
		// Don't create a joint for robot's hull itself.
		return null;
	}
	
	@Override
	protected void setTransform(float posX, float posY, float rotation) {
		this.localX = posX;
		this.localY = posY;
		this.rotation = rotation;
		
		
		this.componentBody.setPosition(this.getLocalPosX(), this.getLocalPosY());
		this.componentBody.setRotation(this.getLocalRotation());
	}

	@Override
	public float getWorldPosX() { return this.componentBody.getPosition().getX(); }
	
	@Override
	public float getWorldPosY() { return this.componentBody.getPosition().getY(); }
	
	@Override
	public float getWorldRotation() { return this.componentBody.getRotation(); }
}
