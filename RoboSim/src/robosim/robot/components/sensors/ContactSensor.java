package robosim.robot.components.sensors;

import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.Collide;
import net.phys2d.raw.Contact;
import net.phys2d.raw.World;
import robosim.robot.Robot;

/**
 * Sensor that gives an on/off value to indicate whether it is colliding with another robot.
 * Range: 1f (contact) OR 0f (no contact).
 * 
 * @author Pier
 */
public class ContactSensor extends Sensor {

	public ContactSensor(World w, Robot r, float posX, float posY, float rotation) {
		super(w, r, posX, posY, rotation);
	}

	@Override
	protected float calcSensorValue() {
		//Reset contact points.
		this.contactPoints[0] = new Contact();
		this.contactPoints[1] = new Contact();
		
		//Get all robot hulls.
		BodyList bdList = this.world.getBodies();
		
		int numBodies = bdList.size();
		for (int i = 0; i < numBodies; i++) {
			Body bd = bdList.get(i);
			
			if ((bd.getBitmask() & this.getBitmask()) == 0)
				continue;
			
			if (this.getExcludedBodies().contains(bd))
				continue;

			if (Collide.collide(contactPoints, this.componentBody, bd, 1f) > 0)
				return 1f;
		}
		
		return 0f;
	}

}