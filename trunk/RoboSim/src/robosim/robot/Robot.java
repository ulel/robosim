package robosim.robot;

import java.util.List;
import java.util.LinkedList;

import robosim.robot.components.RobotComponent;
import robosim.robot.components.RobotHull;
import net.phys2d.raw.World;

/**
 * The Robot class provides an abstract baseclass for all robots.
 */
public abstract class Robot {

	protected World world;
	private RobotHull hull;
	private LinkedList<RobotComponent> components;
	
	public Robot(World w, float posX, float posY, float rotation, float mass) {
		this.world = w;
		this.components = new LinkedList<RobotComponent>();
		
		this.hull = new RobotHull(mass);
		this.addComponent(this.hull, posX, posY, rotation);
	}

	/**
	 * Returns the x position of the robot.
	 */
	public float getPosX() { return this.hull.getWorldPosX(); }
	
	/**
	 * Returns the x position of the robot.
	 */
	public float getPosY() { return this.hull.getWorldPosY(); }
	
	/**
	 * Returns the rotation of the robot.
	 * @return
	 */
	public float getRotation() { return this.hull.getWorldRotation(); }
	
	/**
	 * Returns the robot's hull.
	 */
	public RobotHull getHull() { return this.hull; }
	
	
	
	/**
	 * Returns a list of sensors of the robot.
	 */
	public List<RobotComponent> getComponents() {
		return this.components;
	}
	
	/**
	 * Adds a sensor to the robot.
	 */
	public void addComponent(RobotComponent c, float posX, float posY, float rotation) {
		if (!this.components.contains(c))
			this.components.add(c);
		
		c.addToRobot(this.world, this, this.hull, posX, posY, rotation);
	}
	
	/**
	 * Removes a component from the robot.
	 */
	public void removeComponent(RobotComponent c) {
		this.components.remove(c);
		
		c.removeFromRobot();
	}
	
	
	/**
	 * Updates the robot for each simulation step.
	 */
	public void step() {
		for (RobotComponent c : this.components)
			c.update();
		
		this.performBehavior();
	}
	
	/**
	 * This method is called for each simulation step and can be overridden to add robot behavior.
	 */
	public void performBehavior() {}
}
