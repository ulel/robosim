package robosim.arena.robosumomatch.robot;

import net.phys2d.raw.World;
import robosim.robot.Robot;
import robosim.robot.components.actuators.WheelMotor;
import robosim.robot.strategy.Strategy;

public abstract class SumoRobot extends Robot {

	private WheelMotor wheel_left;
	private WheelMotor wheel_right;
	
	public SumoRobot(World w, float posX, float posY, float rotation, float mass, Strategy strategy) {
		super(w, posX, posY, rotation, mass, strategy);
		
		this.wheel_left = new WheelMotor(1f, MAX_FORCE);
		this.wheel_right = new WheelMotor(1f, MAX_FORCE);
		this.addComponent(this.wheel_left, -10, 0, 0);
		this.addComponent(this.wheel_right, 10, 0, 0);
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
