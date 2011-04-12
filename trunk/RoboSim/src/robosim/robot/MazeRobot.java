package robosim.robot;

import robosim.robot.components.actuators.WheelMotor;
import robosim.robot.components.sensors.*;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;

public class MazeRobot extends Robot {

	private WheelMotor wheel_left;
	private WheelMotor wheel_right;
	public ContactSensor sensorFront1;
	public ContactSensor sensorFront2;
	
	public MazeRobot(World w, float posX, float posY, float rotation, float mass) {
		super(w, posX, posY, rotation, mass);
		
		this.wheel_left = new WheelMotor(w, this, -10, 0, 0, 1f, fwd_power);
		this.wheel_right = new WheelMotor(w, this, 10, 0, 0, 1f, fwd_power);
		
		this.sensorFront1 = new ContactSensor(w, this, 8, 13, (float)Math.PI / 3);
		this.sensorFront1.setComponentShape(new Box(5, 10));
		this.addComponent(this.sensorFront1);
		this.sensorFront2 = new ContactSensor(w, this, -8, 13, (float)-Math.PI / 3);
		this.sensorFront2.setComponentShape(new Box(5, 10));
		this.addComponent(this.sensorFront2);
		
		this.getHull().setComponentShape(new Circle(15));
	}

	private float fwd_power = 120f;
	private float turn_power = 75f;
	private int reverseTime = 150;
	private int reversing = 0;
	private int turnTime = 100;
	private int turning = 0;
	private int state = STATE_FWD;
	private static final int STATE_FWD = 1;
	private static final int STATE_BWD = 2;
	private static final int STATE_BEGIN_TURN = 3;
	private static final int STATE_TURN = 4;
	@Override
	public void performBehavior() {
		if (state == STATE_FWD) {
			this.wheel_left.setPower(fwd_power);
			this.wheel_right.setPower(fwd_power);
			
			float s1 = this.sensorFront1.getSensorValue();
			float s2 = this.sensorFront2.getSensorValue();
			if (s1 == 1 || s2 == 1)
				state = STATE_BWD;
			if (s1 == 1 && s2 == 0)
				turn_power = -Math.abs(turn_power);
			else if (s1 == 0 && s2 == 1)
				turn_power = Math.abs(turn_power);
		} else if (state == STATE_BWD) {
			this.wheel_left.setPower(-fwd_power);
			this.wheel_right.setPower(-fwd_power);
			reversing++;
			
			if (reversing >= reverseTime) {
				reversing = 0;
				state = STATE_BEGIN_TURN;
			}
		} else if (state == STATE_BEGIN_TURN) {
			turnTime = 20 + (int)(Math.random() * 50);
			state = STATE_TURN;
		} else if (state == STATE_TURN) {
			this.wheel_left.setPower(turn_power);
			this.wheel_right.setPower(-turn_power);
			turning++;
			
			if (turning >= turnTime) {
				turning = 0;
				state = STATE_FWD;
			}
		}
		
	}
}
