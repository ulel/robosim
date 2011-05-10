package robosim.arena.robosumomatch.robot;

import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import robosim.arena.robosumomatch.RoboSumoMatch;
import robosim.robot.Robot;
import robosim.robot.components.actuators.WheelMotor;
import robosim.robot.components.sensors.ContactSensor;
import robosim.robot.components.sensors.GroundSensor;
import robosim.robot.components.sensors.ProximitySensor;
import robosim.robot.strategy.Strategy;

public class SumoRobot extends Robot {

	private WheelMotor wheel_left;
	private WheelMotor wheel_right;
	
	public ProximitySensor sensorA;
	public ContactSensor sensorB;
	public GroundSensor sensorC;
	
	
	public SumoRobot(World w, float posX, float posY, float rotation, float mass, Strategy strategy) {
		super(w, posX, posY, rotation, mass, strategy);
		
		this.wheel_left = new WheelMotor(1f, MAX_FORCE);
		this.wheel_right = new WheelMotor(1f, MAX_FORCE);
		this.addComponent(this.wheel_left, -10, 0, 0);
		this.addComponent(this.wheel_right, 10, 0, 0);
		
		this.sensorA = new ProximitySensor();
		this.sensorA.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.sensorA.setHRange(20);
		this.sensorA.setVRange(160);
		this.addComponent(this.sensorA, 0, 10, 0);
		
		this.sensorB = new ContactSensor();
		this.sensorB.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.sensorB.setComponentShape(new Box(15, 2.5f));
		this.addComponent(this.sensorB, 0, -16, -(float)Math.PI);
		
		this.sensorC = new GroundSensor(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.addComponent(this.sensorC, 0, 0, 0);
	}


	private final float MAX_FORCE = 100;
	
	public void forward() {
		this.wheel_left.setPower(MAX_FORCE);
		this.wheel_right.setPower(MAX_FORCE);
	}
	
	public void backward() {
		this.wheel_left.setPower(-MAX_FORCE);
		this.wheel_right.setPower(-MAX_FORCE);
	}
	
	public void turnClockwise() {
		this.wheel_left.setPower(-MAX_FORCE * 0.5f);
		this.wheel_right.setPower(MAX_FORCE * 0.5f);
	}
	
	public void turnCounterClockwise() {
		this.wheel_left.setPower(MAX_FORCE * 0.5f);
		this.wheel_right.setPower(-MAX_FORCE * 0.5f);
	}

	@Override
	public void performBehavior(boolean[] keys) {
		strategy.step(this);
	}
}
