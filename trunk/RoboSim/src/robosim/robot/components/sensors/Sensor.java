package robosim.robot.components.sensors;

import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.Contact;
import net.phys2d.raw.World;
import robosim.robot.Robot;
import robosim.robot.components.RobotComponent;

/**
 * Abstract baseclass for all sensors. Defines a number of useful properties and functions common for all/most sensors.
 * 
 * @author Pier 
 */
public abstract class Sensor extends RobotComponent {
	public static final long BODY_BITMASK = 0;
	private static final float SENSOR_WEIGHT = 0.00000001f; //weight of 0 would mess up physics simulation.
	
	protected float sensorValue;
	protected Contact[] contactPoints;
	private long bitmask = Long.MAX_VALUE; // All bits set.
	private BodyList excludedBodies;
	
	public Sensor(World w, Robot r, float posX, float posY, float rotation) {
		super(w, r, posX, posY, rotation, SENSOR_WEIGHT);
		
		this.sensorValue = 0f;
		this.contactPoints = new Contact[] { new Contact(), new Contact() };
		this.componentBody.setBitmask(BODY_BITMASK);
		this.excludedBodies = new BodyList();
	}
	
	
	/**
	 * Returns the bitmask used for this sensor.
	 */
	public long getBitmask() {
		return this.bitmask;
	}
	
	/**
	 * Sets the bitmask used for this sensor.
	 */
	public void setBitmask(long m) {
		this.bitmask = m;
	}
	
	/**
	 * Removes the provided bit(s) from the sensor's bitmask.
	 */
	public void removeBit(long m) {
		this.bitmask -= m & this.bitmask;
	}
	
	/**
	 * Adds the provided bit(s) from the sensor's bitmask.
	 * @param m
	 */
	public void addBit(long m) {
		this.bitmask |= m;
	}
	
	
	/**
	 * Returns a list of excluded bodies.
	 */
	public BodyList getExcludedBodies() {
		return excludedBodies;
	}
	/**
	 * Adds a body to be excluded from this sensor's detection.
	 */
	public void addExcludedBody(Body b) {
		if (!this.excludedBodies.contains(b))
			this.excludedBodies.add(b);
	}
	
	
	
	/**
	 * Returns the sensor's value.
	 */
	public float getSensorValue() {
		return this.sensorValue;
	}
	
	/**
	 * Calculates and returns the sensor's value.
	 */
	protected abstract float calcSensorValue();
	
	
	/**
	 * Returns an array of contact points for this sensor.
	 */
	public Contact[] getContactPoints() {
		return this.contactPoints;
	}
	
	public void update() {
		this.sensorValue = this.calcSensorValue();
	}

}