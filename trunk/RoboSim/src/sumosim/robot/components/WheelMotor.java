package sumosim.robot.components;

import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.DynamicShape;
import sumosim.robot.Robot;

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

	
	// TODO: find a good way of simulating this.. 
	public void setPower(float p) { }
}
