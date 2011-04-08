package sumosim.robot.sensors;

import java.awt.geom.AffineTransform;

import sumosim.robot.*;
import net.phys2d.raw.*;
import net.phys2d.raw.shapes.*;

/**
 * Abstract baseclass for all sensors. Defines a number of useful properties and functions common for all/most sensors.
 * 
 * @author Pier 
 */
public abstract class Sensor {
	public static final long SENSOR_BITMASK = 0;
	
	protected World world;
	protected Robot robot;
	protected Body sensorBody;
	protected Contact[] contactPoints;
	
	private FixedJoint sensorJoint; 
	
	public Sensor(World w, Robot r) {
		this.world = w;
		this.robot = r;
		
		this.contactPoints = new Contact[] { new Contact(), new Contact() };
		
		this.sensorBody = new Body(this.createDefaultSensorShape(), 0.00000001f);
		sensorBody.setBitmask(SENSOR_BITMASK);
		
		this.sensorBody.setPosition(this.robot.getPosX(), this.robot.getPosY() + 1);
		this.sensorBody.setRotation(this.robot.getRotation());
		
		world.add(sensorBody);
		this.addSensorJoint();
	}
	
	
	/**
	 * Returns an array of contact points for this sensor.
	 */
	//TODO: check if this returns a reference or a copy of the array. (Should be a copy?).
	public Contact[] getContactPoints() {
		return this.contactPoints;
	}
	
	/**
	 * Returns the sensor's body.
	 */
	public Body getSensorBody() {
		return this.sensorBody;
	}
	
	

	
	
	/**
	 * Returns the position of the sensor.
	 */
	//TODO: should return position relative to robot, not relative to world.
	public float getPosX() {
		return this.sensorBody.getPosition().getX();
	}
	public float getPosY() {
		return this.sensorBody.getPosition().getY();
	}
	
	/**
	 * Sets the position of the sensor relative to the robot.
	 */
	public void setPosition(float x, float y) {
		if (x == 0 && y == 0)
			throw new IllegalArgumentException("Sensor x and y can't both be 0, (joint rotation issue)."); //TODO: fix the actual bug.
		
		this.removeSensorJoint();
		AffineTransform t = new AffineTransform();
		t.translate(this.robot.getPosX(), this.robot.getPosY());
		t.rotate(this.robot.getRotation());
		t.translate(x, y);
		this.sensorBody.setPosition((float)t.getTranslateX(), (float)t.getTranslateY());
		//this.sensorBody.setPosition(this.robot.getPosX() + x, this.robot.getPosY() + y);
		this.addSensorJoint();
	}
	
	/**
	 * Returns the rotation of the sensor, relative to the robot.
	 */
	public float getRotation() {
		return this.sensorBody.getRotation() - this.robot.getHull().getRotation();
	}
	/**
	 * Sets the rotation of the sensor, relative to the robot.
	 */
	public void setRotation(float r) {
		this.removeSensorJoint();
		this.sensorBody.setRotation(this.robot.getRotation() + r);
		this.addSensorJoint();
	}
	
	/**
	 * Returns the shape of the sensor's body.
	 */
	public Shape getSensorShape() {
		return this.sensorBody.getShape();
	}
	/**
	 * Sets the shape of the sensor's body.
	 */
	public void setSensorShape(Shape s) {
		this.sensorBody.setShape(s);
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
	 * Returns the default shape for the sensor.
	 */
	protected DynamicShape createDefaultSensorShape() {
		return new Box(5, 5);
	}
	
	
	
	/**
	 * Adds a joint between the sensor's body and the robot's hull.
	 */
	private void addSensorJoint() {
		this.sensorJoint = new FixedJoint(this.robot.getHull(), sensorBody);
		this.world.add(this.sensorJoint);
	}
	/**
	 * Removes the sensor's joint, to be used before setting the position or rotation of the sensor's body through code.
	 */
	private void removeSensorJoint() {
		this.world.remove(this.sensorJoint);
		this.sensorJoint = null;
	}
}
