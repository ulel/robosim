package robosim.robot;

import java.awt.event.KeyEvent;

import robosim.RoboSumoMatch;
import robosim.robot.components.actuators.WheelMotor;
import robosim.robot.components.sensors.ContactSensor;
import robosim.robot.components.sensors.GroundSensor;
import robosim.robot.components.sensors.ProximitySensor;
import robosim.robot.components.weapons.MissileLauncher;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;

public class ManuallyControlledSumoRobot extends Robot {

	private WheelMotor wheel_left;
	private WheelMotor wheel_right;
	
	public ProximitySensor sensorA;
	public ContactSensor sensorB;
	public GroundSensor sensorC;
	
	public MissileLauncher missileLauncher_left;
	public MissileLauncher missileLauncher_right;
	
	public ManuallyControlledSumoRobot(World w, float posX, float posY, float rotation, float mass) {
		super(w, posX, posY, rotation, mass);
		
		this.wheel_left = new WheelMotor(1f, fwd_force);
		this.wheel_right = new WheelMotor(1f, fwd_force);
		this.addComponent(this.wheel_left, -10, 0, 0);
		this.addComponent(this.wheel_right, 10, 0, 0);
		
		this.sensorA = new ProximitySensor();
		this.sensorA.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.sensorA.setHRange(20);
		this.sensorA.setVRange(60);
		this.addComponent(this.sensorA, 0, 10, 0);
		
		this.sensorB = new ContactSensor();
		this.sensorB.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.sensorB.setComponentShape(new Box(15, 2.5f));
		this.addComponent(this.sensorB, 0, -16, (float)Math.PI);
		
		this.sensorC = new GroundSensor(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.addComponent(this.sensorC, 0, 0, 0);
		
		this.missileLauncher_left = new MissileLauncher(0.0001f, MissileLauncher.MISSILE_MASS, MissileLauncher.MISSILE_FORCE);
		this.addComponent(this.missileLauncher_left, 21, 0, 0);
		this.missileLauncher_right = new MissileLauncher(0.0001f, MissileLauncher.MISSILE_MASS, MissileLauncher.MISSILE_FORCE);
		this.addComponent(this.missileLauncher_right, -21, 0, 0);
	}


	
	private float fwd_force = 150;
	private float turn_force = 100; //Higher value means smaller turn-radius, but lower fwd speed when turning.
	
	public void stop() {
		wheel_left.setPower(0);
		wheel_right.setPower(0);
	}
	public void forward() {
		wheel_left.setPower(fwd_force);
		wheel_right.setPower(fwd_force);
	}
	
	public void backward() {
		wheel_left.setPower(-fwd_force);
		wheel_right.setPower(-fwd_force);
	}
	
	public void turnClockwise() {
		this.wheel_left.setPower(this.wheel_left.getPower() - turn_force);
		this.wheel_right.setPower(this.wheel_right.getPower() + turn_force);
	}
	
	public void turnCounterClockwise() {
		this.wheel_left.setPower(this.wheel_left.getPower() + turn_force);
		this.wheel_right.setPower(this.wheel_right.getPower() - turn_force);
	}
	
	private int missileFired = 61;
	public void fireMissile() {
		if (this.missileFired < 60)
		{
			this.missileFired++;
			return;
		}
		
		this.missileLauncher_left.fire();
		this.missileLauncher_right.fire();
		
		this.missileFired = 0;
	}

	
	
	@Override
	public void performBehavior(boolean[] keys) {
		//If no directional keys are pressed, stop the robot.
		if (!keys[KeyEvent.VK_UP] && !keys[KeyEvent.VK_DOWN] && !keys[KeyEvent.VK_LEFT] && !keys[KeyEvent.VK_RIGHT])
			this.stop();
		
		//Drive!
		if (keys[KeyEvent.VK_UP]) this.forward();
		if (keys[KeyEvent.VK_DOWN]) this.backward();
		if (keys[KeyEvent.VK_LEFT]) this.turnCounterClockwise();
		if (keys[KeyEvent.VK_RIGHT]) this.turnClockwise();
		
		//Explosions etc.
		if (keys[KeyEvent.VK_SPACE]) this.fireMissile();
	}
}
