package sumosim.robot.sensors;

import net.phys2d.raw.Body;
import net.phys2d.raw.Collide;
import net.phys2d.raw.Contact;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.DynamicShape;
import sumosim.SumoRobot;
import sumosim.robot.Robot;

/**
 * The GroundSensor is a sensor that checks for collisions with the arena floor.
 * Range: 1f (on the edge) OR 0f (in the arena).
 * 
 * @author Pier
 */
public class GroundSensor extends Sensor {
	
	public GroundSensor(World w, Robot r) {
		super(w, r);
	}
	
	@Override
	protected DynamicShape createDefaultSensorShape() {
		return new Box(2, 2);
	}
	
	// Returns 1f if the sensor is no longer 'above' the dohyo's arena body. 0f otherwise.
	@Override
	protected float calcSensorValue() {
		Body arenaBody = this.world.getBodies().getBodyByName(SumoRobot.DOHYO_ARENA);
		if (arenaBody == null)
			return 1f;
		
		this.contactPoints[0] = new Contact();
		this.contactPoints[1] = new Contact();
		
		if (Collide.collide(contactPoints, this.sensorBody, arenaBody, 1f) > 0)
			return 0f;
		else
			return 1f;
	}

	
}