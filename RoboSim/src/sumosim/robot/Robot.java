package sumosim.robot;

import sumosim.robot.components.RobotHull;
import net.phys2d.raw.World;

/**
 * The Robot class provides an abstract baseclass for all robots.
 */
public abstract class Robot {

	protected World world;
	private RobotHull hull;
	
	public Robot(World w, float posX, float posY, float rotation, float mass) {
		this.world = w;
		this.hull = new RobotHull(w, this, posX, posY, rotation, mass);
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
	 * This method is called for each simulation step and can be overridden to add robot behavior.
	 */
	public void step() {}
}
