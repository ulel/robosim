/***
 * SumoBot.java - Generated Robot
 */ 
package robosim.robot;

import robosim.RoboSumoMatch;
import robosim.robot.components.actuators.WheelMotor;
import robosim.robot.components.sensors.*;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;

public class SumoBot extends Robot {

	private enum State {
		Idle, Find, Charge
	}

	private State currentState = State.Idle;
	private WheelMotor wheel_left;
	private WheelMotor wheel_right;
	
	private float left_wheel_power;
	private float right_wheel_power;
	
	public ProximitySensor sensorA;
	public ContactSensor sensorB;
	public GroundSensor sensorC;
	
	public SumoBot(World w, float posX, float posY, float rotation, float mass) {
		super(w, posX, posY, rotation, mass);
		
		this.wheel_left = new WheelMotor(w, this, -10, 0, 0, 1f);
		this.wheel_right = new WheelMotor(w, this, 10, 0, 0, 1f);
		
		this.sensorA = new ProximitySensor(w, this, 0, 10, 0);
		
		this.sensorA.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.sensorA.setHRange(20);
		this.sensorA.setVRange(200);
		this.addSensor(this.sensorA);
		
		this.sensorB = new ContactSensor(w, this, 0, -16, (float)Math.PI);
		this.sensorB.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.sensorB.setComponentShape(new Box(15, 2.5f));
		this.addSensor(this.sensorB);
		
		this.sensorC = new GroundSensor(w, this, 0, 0, 0, RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.addSensor(this.sensorC);
	}

	@Override
	public void performBehavior() {
		switch(currentState){
		
		case Idle:
			if(true) {
				currentState = State.Find;
				turnCounterClockwise(0.5f);
			}
			break;	
		
		case Find:
			if(this.sensorA.getSensorValue() > 0.0) {
				currentState = State.Charge;
				forward(1.0f);
			}
			break;	
		
		case Charge:
			if(this.sensorA.getSensorValue() < 0.01) {
				currentState = State.Find;
				turnCounterClockwise(0.5f);
			}
			break;	
		}
		wheel_left.setPower(left_wheel_power);
		wheel_right.setPower(right_wheel_power);
	}
	
	private final float MAX_FWD_FORCE = 150;
	private final float MAX_TURN_FORCE = 100;
	
	public void forward(float speed) {
		left_wheel_power = MAX_FWD_FORCE*speed;
		right_wheel_power = MAX_FWD_FORCE*speed;
	}
	
	public void backward(float speed) {
		left_wheel_power = -MAX_FWD_FORCE*speed;
		right_wheel_power = -MAX_FWD_FORCE*speed;
	}
	
	public void turnClockwise(float speed) {
		left_wheel_power = -MAX_TURN_FORCE*speed;
		right_wheel_power = MAX_TURN_FORCE*speed;
	}
	
	public void turnCounterClockwise(float speed) {
		left_wheel_power = MAX_TURN_FORCE*speed;
		right_wheel_power = -MAX_TURN_FORCE*speed;
	}
}

