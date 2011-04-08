package sumosim.robot.sensors;
import sumosim.robot.Robot;
import net.phys2d.raw.*;
import net.phys2d.raw.shapes.*;
import net.phys2d.math.*;

/**
 * Sensor that can be used to detect nearby robots. Use hRange and vRange to set the sensor's scope. 
 * Range: 1f (object very close) - 0f (no object in range).
 *  
 * @author Pier
 */
public class ProximitySensor extends Sensor {
	protected float hRange = 4f;
	protected float vRange = 4f;
	
	public ProximitySensor(World w, Robot r) {
		super(w, r);
	}
	
	
	/**
	 * Returns the horizontal range of the sensor.
	 */
	public float getHRange() { return this.hRange; }
	
	/**
	 * Sets the horizontal range of the sensor.
	 */
	public void setHRange(float r) {
		this.hRange = r;
		this.sensorBody.setShape(this.createDefaultSensorShape());
	}
	
	/**
	 * Returns the vertical range of the sensor.
	 */
	public float getVRange() { return this.vRange; }
	
	/**
	 * Sets the vertical range of the sensor.
	 */ 
	public void setVRange(float d) {
		this.vRange = d;
		this.sensorBody.setShape(this.createDefaultSensorShape());
	}
	
	
	@Override
	public void setSensorShape(Shape s) {
		throw new UnsupportedOperationException("setSensorShape is not supported for the ProximitySensor.");
	}
	
	
	@Override
	protected DynamicShape createDefaultSensorShape() {
		if (this.hRange < 2) this.hRange = 2;
		if (this.vRange < 2) this.vRange = 2;
		
		return new ConvexPolygon(new Vector2f[] { new Vector2f(0, 0), 
				  				 				  new Vector2f(this.hRange / 2, this.vRange), 
				  				 				  new Vector2f(-(this.hRange / 2), this.vRange)
												});
	}
	

	@Override
	protected float calcSensorValue(){
		//Reset contact points.
		contactPoints[0] = new Contact();
		contactPoints[1] = new Contact();
		
		//Get all robot hulls.
		BodyList bdList = this.world.getBodiesByName(Robot.ROBOT_HULL); 
		
		int numBodies = bdList.size();
		for (int i = 0; i < numBodies; i++) {
			Body bd = bdList.get(i);
			
			//Don't check the body of the sensor's robot.
			if (bd == this.robot.getHull())
				continue;
			
			int numCollisions = Collide.collide(contactPoints, this.sensorBody, bd, 1f);
			if (numCollisions == 0)
				continue;
			
			float dist = Float.MAX_VALUE;
			for (int n = 0; n < numCollisions; n++) {
				dist = Math.min(dist, contactPoints[n].getPosition().distance(this.sensorBody.getPosition()));
			}
			
			return Math.max(0f, 1 - (dist / this.vRange));
		}
		
		return 0f;
	}
}
