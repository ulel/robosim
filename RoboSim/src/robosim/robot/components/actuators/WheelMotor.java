package robosim.robot.components.actuators;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.DynamicShape;
import robosim.robot.Robot;
import robosim.robot.components.RobotComponent;

public class WheelMotor extends RobotComponent {
	public static final long BODY_BITMASK = 0; //no collisions.
	private float maxPower;
	private float power;
	private Vector2f motorForce;
	
	public WheelMotor(World w, Robot r, float posX, float posY, float rotation, float mass, float maxPower) {
		super(w, r, posX, posY, rotation, mass);
		
		this.componentBody.setBitmask(BODY_BITMASK);
		this.componentBody.setDamping(0.1f);
		
		this.maxPower = maxPower;
		this.power = 0f;
		this.motorForce = new Vector2f();
	}
	
	@Override
	protected DynamicShape createDefaultComponentShape() {
		return new Box(5, 20);
	}

	
	/**
	 * Returns the power set for this motor.
	 */
	public float getPower() { return this.power; }
	/**
	 * Sets the power on for this motor.
	 * The value set will be clamped between MaxPower and -MaxPower.
	 */
	public void setPower(float p) {
		if (p < -this.maxPower) 
			this.power = -this.maxPower;
		else if (p > this.maxPower) 
			this.power = this.maxPower;
		else
			this.power = p;
	}
	
	/**
	 * Returns the maximum power value.
	 */
	public float getMaxPower() { return this.maxPower; }
	/**
	 * Sets the maximum power value.
	 * When setting the power value, the value will be clamped between MaxPower and -MaxPower.
	 */
	public void setMaxPower(float p) {
		this.maxPower = p;
	}
	
	
	
	@Override
	public void update()
	{
		if (this.power != 0) {
			float r = this.getWorldRotation();
			float x = (float) (-Math.sin(r) * this.power);
			float y = (float) (Math.cos(r) * this.power);
			
			this.motorForce.set(x, y);
			this.componentBody.addForce(this.motorForce);
		}
	}
}
