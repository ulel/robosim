package robosim.robot.components.weapons;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.DynamicShape;
import robosim.robot.components.RobotComponent;

//TODO: check when missiles go out of world bounds and remove them.
//TODO: check missile impact with other bodies -> remove missile body?
public class MissileLauncher extends RobotComponent {
	public static final float LAUNCHER_MASS = 10;
	public static final float MISSILE_MASS = 50;
	public static final float MISSILE_FORCE = 300000;
	
	private float missileMass;
	private float missileForce;
	
	public MissileLauncher(float mass, float missileMass, float missileForce) {
		super(mass);
		
		this.missileMass = missileMass;
		this.missileForce = missileForce;
	}
	
	@Override
	protected DynamicShape createDefaultComponentShape() {
		return new Box(5, 20);
	}
	
	
	/**
	 * BOMBS AWAY!
	 */
	public void fire() {
		Body missileBody = new Body("missile", new Box(5, 20), this.missileMass);
		missileBody.setPosition(this.componentBody.getPosition().getX(), this.componentBody.getPosition().getY());
		missileBody.setRotation(this.componentBody.getRotation());
		
		for(RobotComponent c : this.robot.getComponents()) {
			missileBody.addExcludedBody(c.getComponentBody());
		}

		world.add(missileBody);
		
		float r = this.getWorldRotation();
		float x = (float) (-Math.sin(r) * this.missileForce);
		float y = (float) (Math.cos(r) * this.missileForce);
		
		missileBody.addForce(new Vector2f(x, y));
	}

}
