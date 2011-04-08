package sumosim.robot;

import sumosim.robot.sensors.*;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.FixedJoint;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;


public class Robot {
	public static final String ROBOT_HULL = "RobotHull";
	public static final long ROBOT_HULL_BITMASK = 5; //bit 1 and 3.
	public static final String ROBOT_WHEEL = "RobotWheel";
	public static final long ROBOT_WHEEL_BITMASK = 8; //bit 4.
	
	private World world;
	private Body hull;
	private Body wheel1;
	private Body wheel2;
	
	public ProximitySensor sensorA;
	public ContactSensor sensorB;
	public GroundSensor sensorC;
	
	private float force = 200;
	
	//TODO fix rotation parameter: only works for 0f, partially for Pi, not at all for anything else.
	public Robot(World world, float posX, float posY, float rotation, float mass) {
		this.world = world;
		
		hull = new Body(Robot.ROBOT_HULL, new Box(40, 40), mass);
		hull.setBitmask(ROBOT_HULL_BITMASK);
		hull.setFriction(0.2f);
		hull.setPosition(posX, posY);
		//hull.setRotation(rotation);
		
		wheel1 = new Body(Robot.ROBOT_WHEEL, new Box(10, 20), 1);
		wheel1.setBitmask(ROBOT_WHEEL_BITMASK);
		wheel1.setPosition(posX - 13f, posY);
		wheel1.setDamping(0.1f);

		wheel2 = new Body(Robot.ROBOT_WHEEL, new Box(10, 20), 1);
		wheel2.setBitmask(ROBOT_WHEEL_BITMASK);
		wheel2.setPosition(posX + 13f, posY);
		wheel2.setDamping(0.1f);
		
//		world.add(new FixedJoint(wheel1, wheel2));
		world.add(new FixedJoint(hull, wheel1));//new BasicJoint(hull, wheel1, new Vector2f(posX - 11.5f, posY))
		world.add(new FixedJoint(hull, wheel2));//new BasicJoint(hull, wheel2, new Vector2f(posX + 11.5f, posY))

		world.add(hull);
		world.add(wheel1);
		world.add(wheel2);
		
		this.sensorA = new ProximitySensor(world, this);
		this.sensorA.setHRange(20);
		this.sensorA.setVRange(60);
		this.sensorA.setPosition(0, 18);
		
		this.sensorB = new ContactSensor(world, this);
		this.sensorB.setRotation((float)Math.PI);
		this.sensorB.setPosition(0, -20);
		this.sensorB.setSensorShape(new Box(30, 5));
		
		this.sensorC = new GroundSensor(world, this);
	}
	
	public float getPosX() {
		return this.hull.getPosition().getX();
	}
	public float getPosY() {
		return this.hull.getPosition().getY();
	}
	public float getRotation() {
		return this.hull.getRotation();
	}
	
	public Body getHull() {
		return this.hull;
	}
	
	private Vector2f getForce() {
		float r = hull.getRotation();
		float x = (float) (Math.sin(r) * force);
		float y = (float) (Math.cos(r) * force);
		
		return new Vector2f(x,y);
	}
	
	public void forward() {
		Vector2f force = getForce();
		wheel1.addForce(new Vector2f(-force.x,force.y));
		wheel2.addForce(new Vector2f(-force.x,force.y));
	}
	
	public void backward() {
		Vector2f force = getForce();
		wheel1.addForce(new Vector2f(force.x,-force.y));
		wheel2.addForce(new Vector2f(force.x,-force.y));
	}
	
	public void turnClockwise() {
		Vector2f force = getForce();
		wheel1.addForce(new Vector2f(force.x,-force.y));
		wheel2.addForce(new Vector2f(-force.x,force.y));
	}
	
	public void turnCounterClockwise() {
		Vector2f force = getForce();
		wheel1.addForce(new Vector2f(-force.x,force.y));
		wheel2.addForce(new Vector2f(force.x,-force.y));
	}
	
	
	
	
	private int missileFired = 61;
	public void fireMissile() {
		if (this.missileFired < 60)
		{
			this.missileFired++;
			return;
		}
		
		Body missileBody = new Body("missile", new Box(5, 20), 350);
		missileBody.setPosition(this.getPosX(), this.getPosY());
		missileBody.setRotation(this.getRotation());
		
		hull.addExcludedBody(missileBody);
		wheel1.addExcludedBody(missileBody);
		wheel2.addExcludedBody(missileBody);
		world.add(missileBody);
		
		missileBody.addForce(new Vector2f(0, (int)Math.pow(10, 6.5)));
		
		this.missileFired = 0;
	}
}