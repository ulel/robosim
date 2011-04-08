package sumosim.robot.sensors;

import net.phys2d.raw.Body;
import net.phys2d.raw.Contact;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.DynamicShape;
import sumosim.SumoRobot;
import sumosim.robot.Robot;

/**
 * A sensor which is using a bit of cheating, to get a factor of the distance to the center of the arena.
 * Range: 1f (on the edge) - 0f (in the center).
 * 
 * @author Pier
 */
public class DistanceToCenterSensor extends Sensor {
	
	public DistanceToCenterSensor(World w, Robot r) {
		super(w, r);
	}
	
	@Override
	protected DynamicShape createDefaultSensorShape() {
		return new Box(0, 0);
	}
	
	@Override
	protected float calcSensorValue() {
		Body arenaBody = this.world.getBodies().getBodyByName(SumoRobot.DOHYO_ARENA);
		if (arenaBody == null)
			return 1f;
		
		this.contactPoints[0] = new Contact();
		this.contactPoints[1] = new Contact();
		
		float dArenaCenter = arenaBody.getPosition().distance(this.sensorBody.getPosition());
		float offCenterFactor = dArenaCenter / (arenaBody.getShape().getBounds().getWidth() / 2); 
		return Math.max(0, Math.min(offCenterFactor, 1));
	}
}
