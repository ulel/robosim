package robosim.robot.strategy.actions;

import robosim.robot.SumoRobot;
import robosim.robot.strategy.MotorControl;

public class Forward extends MotorControl {

	public Forward(int speed) {
		super(speed);
	}

	@Override
	public void perform(SumoRobot robot) {
		robot.forward();
	}
	
}
