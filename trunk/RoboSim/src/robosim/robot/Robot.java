package robosim.robot;

import java.util.LinkedList;
import java.util.List;

import net.phys2d.raw.World;
import robosim.robot.components.RobotComponent;
import robosim.robot.components.RobotHull;
import robosim.robot.strategy.Strategy;

/**
 * The Robot class provides an abstract baseclass for all robots.
 */
public abstract class Robot {

	protected World world;
	protected Strategy strategy;
	private RobotHull hull;
	private LinkedList<RobotComponent> components;
	
	public Robot(World w, float posX, float posY, float rotation, float mass, Strategy strategy) {
		this.world = w;
		this.components = new LinkedList<RobotComponent>();
		
		this.hull = new RobotHull(mass);
		this.addComponent(this.hull, posX, posY, rotation);
		
		this.strategy = strategy;
		this.strategy.initStrategy(this);
	}

	/**
	 * Returns the x position of the robot.
	 */
	public float getPosX() { return this.hull.getWorldPosX(); }
	
	/**
	 * Returns the y position of the robot.
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
	public void step(boolean[] keys) {
		for (RobotComponent c : this.components)
			c.update();
		
		if (this.strategy != null)
			strategy.step(this);
		
		this.performBehavior(keys);
	}
	
	/**
	 * This method is called for each simulation step and can be overridden to add robot behavior.
	 */
	public void performBehavior(boolean[] keys) { }
}
