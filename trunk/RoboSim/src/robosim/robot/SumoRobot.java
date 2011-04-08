package robosim.robot;

import robosim.RoboSumoMatch;
import robosim.robot.components.actuators.WheelMotor;
import robosim.robot.components.sensors.*;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;

public class SumoRobot extends Robot {

	private WheelMotor wheel_left;
	private WheelMotor wheel_right;
	
	public ProximitySensor sensorA;
	public ContactSensor sensorB;
	public GroundSensor sensorC;
	
	public SumoRobot(World w, float posX, float posY, float rotation, float mass) {
		super(w, posX, posY, rotation, mass);
		
		this.wheel_left = new WheelMotor(w, this, -10, 0, 0, 1f);
		this.wheel_right = new WheelMotor(w, this, 10, 0, 0, 1f);
		
		this.sensorA = new ProximitySensor(w, this, 0, 10, 0);
		this.sensorA.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.sensorA.addExcludedBody(this.getHull().getComponentBody());
		this.sensorA.setHRange(20);
		this.sensorA.setVRange(60);
		this.addSensor(this.sensorA);
		
		this.sensorB = new ContactSensor(w, this, 0, -16, (float)Math.PI);
		this.sensorB.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.sensorB.addExcludedBody(this.getHull().getComponentBody());
		this.sensorB.setComponentShape(new Box(15, 2.5f));
		this.addSensor(this.sensorB);
		
		this.sensorC = new GroundSensor(w, this, 0, 0, 0, RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.addSensor(this.sensorC);
	}


	
	private float fwd_force = 150;
	private float turn_force = 100;
	
	public void forward() {
		wheel_left.setPower(fwd_force);
		wheel_right.setPower(fwd_force);
	}
	
	public void backward() {
		wheel_left.setPower(-fwd_force);
		wheel_right.setPower(-fwd_force);
	}
	
	public void turnClockwise() {
		wheel_left.setPower(-turn_force);
		wheel_right.setPower(turn_force);
	}
	
	public void turnCounterClockwise() {
		wheel_left.setPower(turn_force);
		wheel_right.setPower(-turn_force);
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
		
		missileBody.addExcludedBody(this.getHull().getComponentBody());
		missileBody.addExcludedBody(this.wheel_left.getComponentBody());
		missileBody.addExcludedBody(this.wheel_right.getComponentBody());
		world.add(missileBody);
		
		missileBody.addForce(new Vector2f(0, (int)Math.pow(10, 6.5)));
		
		this.missileFired = 0;
	}
}
