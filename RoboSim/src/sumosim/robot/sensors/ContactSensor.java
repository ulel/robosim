package sumosim.robot.sensors;

import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.Collide;
import net.phys2d.raw.Contact;
import net.phys2d.raw.World;
import sumosim.robot.Robot;

/**
 * Sensor that gives an on/off value to indicate whether it is colliding with another robot.
 * Range: 1f (contact) OR 0f (no contact).
 * 
 * @author Pier
 */
public class ContactSensor extends Sensor {
	public ContactSensor(World w, Robot r) {
		super(w, r);
	}
	
	
	@Override
	protected float calcSensorValue(){
		//Reset contact points.
		contactPoints[0] = new Contact();
		contactPoints[1] = new Contact();
		
		//Get all robot hulls.
		BodyList bdList = this.world.getBodies().getBodiesByName(Robot.ROBOT_HULL); 
		
		int numBodies = bdList.size();
		for (int i = 0; i < numBodies; i++) {
			Body bd = bdList.get(i);
			
			//Don't check the body of the sensor's robot.
			if (bd == this.robot.getHull())
				continue;
			
			if (Collide.collide(contactPoints, this.sensorBody, bd, 1f) > 0)
				return 1f;
		}
		
		return 0f;
	}

}
