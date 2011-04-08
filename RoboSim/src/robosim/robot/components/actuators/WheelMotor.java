package robosim.robot.components.actuators;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.DynamicShape;
import robosim.robot.Robot;
import robosim.robot.components.RobotComponent;

public class WheelMotor extends RobotComponent {
	public static final long BODY_BITMASK = 0; //no collisions.
	
	public WheelMotor(World w, Robot r, float posX, float posY, float rotation, float mass) {
		super(w, r, posX, posY, rotation, mass);
		
		this.componentBody.setBitmask(BODY_BITMASK);
		this.componentBody.setDamping(0.1f);
	}
	
	@Override
	protected DynamicShape createDefaultComponentShape() {
		return new Box(5, 20);
	}

	/**
	 * Sets the power on for this motor.
	 */
	public void setPower(float p) {
		float r = this.robot.getRotation();
		float x = (float) (-Math.sin(r) * p);
		float y = (float) (Math.cos(r) * p);
		
		this.componentBody.addForce(new Vector2f(x, y));
	}
}
