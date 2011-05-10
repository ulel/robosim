package robosim.arena.maze.robot;

import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Circle;
import robosim.robot.Robot;
import robosim.robot.components.actuators.WheelMotor;
import robosim.robot.components.sensors.ProximitySensor;
import robosim.robot.strategy.Strategy;

public class MazeRobot2 extends Robot {

	private WheelMotor wheel_left;
	private WheelMotor wheel_right;
	
	public ProximitySensor sensorFront1;
	public ProximitySensor sensorFront2;

	public MazeRobot2(World w, float posX, float posY, float rotation, float mass, Strategy strategy) {
		super(w, posX, posY, rotation, mass, strategy);
		
		this.wheel_left = new WheelMotor(1f, fwd_power);
		this.wheel_right = new WheelMotor(1f, fwd_power);
		this.addComponent(this.wheel_left, -10, 0, 0);
		this.addComponent(this.wheel_right, 10, 0, 0);

		this.sensorFront1 = new ProximitySensor();
		this.sensorFront1.setHRange(10);
		this.sensorFront1.setVRange(30);
		this.addComponent(this.sensorFront1, 6, 14, (float)-Math.PI / 6);
		
		this.sensorFront2 = new ProximitySensor();
		this.sensorFront2.setHRange(10);
		this.sensorFront2.setVRange(30);
		this.addComponent(this.sensorFront2, -6, 14, (float)Math.PI / 6);
		
		this.getHull().setComponentShape(new Circle(15));
	}

	private float fwd_power = 120f;
	private float turn_power = 50f;
	private int state = STATE_FWD;
	private static final int STATE_FWD = 1;
	private static final int STATE_TURN = 2;
	@Override
	public void performBehavior(boolean[] keys) {
		float s1 = this.sensorFront1.getSensorValue();
		float s2 = this.sensorFront2.getSensorValue();
		
		if (state == STATE_FWD) {
			this.wheel_left.setPower(fwd_power);
			this.wheel_right.setPower(fwd_power);

			if (s1 > 0.1f || s2 > 0.1f)
				state = STATE_TURN;
			if (s1 > s2)
				turn_power = -Math.abs(turn_power);
			else
				turn_power = Math.abs(turn_power);
		} else if (state == STATE_TURN) {
			this.wheel_left.setPower(turn_power);
			this.wheel_right.setPower(-turn_power);
			
			if (s1 < 0.1f && s2 < 0.1f) {
				state = STATE_FWD;
			}
		}
		
	}
}