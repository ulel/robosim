package sumosim.robot.components.sensors;

import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.DynamicShape;
import sumosim.robot.Robot;

/**
 * The GroundSensor is a sensor that checks for collisions with the arena floor.
 * Range: 1f (on the edge) OR 0f (in the arena).
 * 
 * @author Pier
 */
public class GroundSensor extends ContactSensor {

	public GroundSensor(World w, Robot r, float posX, float posY, float rotation, long groundBitmask) {
		super(w, r, posX, posY, rotation);
		this.setBitmask(groundBitmask);
	}
	
	
	@Override
	protected DynamicShape createDefaultComponentShape() {
		return new Box(2, 2);
	}
}