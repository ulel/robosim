package robosim.robot;

import java.util.ArrayList;

import robosim.robot.components.RobotHull;
import robosim.robot.components.sensors.Sensor;
import net.phys2d.raw.World;

/**
 * The Robot class provides an abstract baseclass for all robots.
 */
public abstract class Robot {

	protected World world;
	private RobotHull hull;
	private ArrayList<Sensor> sensors;
	
	public Robot(World w, float posX, float posY, float rotation, float mass) {
		this.world = w;
		this.hull = new RobotHull(w, this, posX, posY, rotation, mass);
		this.sensors = new ArrayList<Sensor>();
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
	public ArrayList<Sensor> getSensors() {
		return this.sensors;
	}
	
	/**
	 * Adds a sensor to the robot.
	 */
	public void addSensor(Sensor s) {
		if (!this.sensors.contains(s))
			this.sensors.add(s);
	}
	
	/**
	 * Removes a sensor from the robot.
	 */
	public void removeSensor(Sensor s) {
		if (this.sensors.contains(s))
			this.sensors.remove(s);
	}
	
	
	/**
	 * Updates the robot for each simulation step.
	 */
	public void step() {
		int numSensors = this.sensors.size();
		for (int i = 0; i < numSensors; i++) {
			this.sensors.get(i).update();
		}
		
		this.performBehavior();
	}
	
	/**
	 * This method is called for each simulation step and can be overridden to add robot behavior.
	 */
	public void performBehavior() {}
}
