/***
 * FindAndCharge.java - Generated Robot
 */ 
package robosim.robot;

import robosim.RoboSumoMatch;
import robosim.robot.components.actuators.WheelMotor;
import robosim.robot.components.sensors.*;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;

public class FindAndCharge extends Robot {

	private enum State {
		Idle, Find, Charge
	}

	private State currentState = State.Idle;
	private WheelMotor wheel_left;
	private WheelMotor wheel_right;
	
	private float left_wheel_power;
	private float right_wheel_power;
	
	/**
	 * Sensors
	 */
	private ProximitySensor frontSensor;
	private GroundSensor groundSensor;
	private ContactSensor contactSensor;
	
	public FindAndCharge(World w, float posX, float posY, float rotation, float mass) {
		super(w, posX, posY, rotation, mass);
		
		this.wheel_left = new WheelMotor(1f, MAX_FWD_FORCE);
		this.wheel_right = new WheelMotor(1f, MAX_FWD_FORCE);
		this.addComponent(this.wheel_left, -10, 0, 0);
		this.addComponent(this.wheel_right, 10, 0, 0);
		
		this.frontSensor = new ProximitySensor();
		this.frontSensor.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.frontSensor.setHRange(20.0f);
		this.frontSensor.setVRange(200.0f);
		this.addComponent(this.frontSensor, 0, 10, 0);
		this.groundSensor = new GroundSensor(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.addComponent(this.groundSensor, 0, 0, 0);
		this.contactSensor = new ContactSensor();
		this.contactSensor.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.contactSensor.setComponentShape(new Box(15.0f, 2.5f));
		this.addComponent(this.contactSensor, 0, -16, 0);
	}

	@Override
	public void performBehavior() {
		switch(currentState){
		
		case Idle:
			if(true
			 ==
			  true
			  
			) {
				currentState = State.Find;
				turnCounterClockwise(0.3f);
			}
			break;	
		
		case Find:
			if(this.frontSensor.getSensorValue()
			 >
			  0.0
			  
			) {
				currentState = State.Charge;
				forward(1.0f);
			}
			break;	
		
		case Charge:
			if(this.frontSensor.getSensorValue()
			 ==
			  0.0
			  
			) {
				currentState = State.Find;
				turnClockwise(0.3f);
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

