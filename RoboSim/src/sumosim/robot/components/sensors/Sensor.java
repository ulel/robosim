package sumosim.robot.components.sensors;

import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.Contact;
import net.phys2d.raw.World;
import sumosim.robot.Robot;
import sumosim.robot.components.RobotComponent;

/**
 * Abstract baseclass for all sensors. Defines a number of useful properties and functions common for all/most sensors.
 * 
 * @author Pier 
 */
public abstract class Sensor extends RobotComponent {
	public static final long BODY_BITMASK = 0;
	private static final float SENSOR_WEIGHT = 0.00000001f; //weight of 0 would mess up physics simulation.
	
	protected Contact[] contactPoints;
	private long bitmask = Long.MAX_VALUE; // All bits set.
	private BodyList excludedBodies;
	
	public Sensor(World w, Robot r, float posX, float posY, float rotation) {
		super(w, r, posX, posY, rotation, SENSOR_WEIGHT);
		
		this.contactPoints = new Contact[] { new Contact(), new Contact() };
		
		this.componentBody.setBitmask(BODY_BITMASK);
		
		this.excludedBodies = new BodyList();
	}
	
	
	public long getBitmask() {
		return this.bitmask;
	}
	public void setBitmask(long m) {
		this.bitmask = m;
	}
	public void removeBit(long m) {
		this.bitmask -= m & this.bitmask;
	}
	public void addBit(long m) {
		this.bitmask |= m;
	}
	
	
	public BodyList getExcludedBodies() {
		return excludedBodies;
	}
	public void addExcludedBody(Body b) {
		if (!this.excludedBodies.contains(b))
			this.excludedBodies.add(b);
	}
	
	
	
	/**
	 * Returns the sensor's value.
	 */
	public float getSensorValue() {
		return this.calcSensorValue();
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

}