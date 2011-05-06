package robosim.robot.strategy.actions;

import robosim.arena.robosumomatch.robot.SumoRobot;
import robosim.robot.strategy.MotorControl;

public class Backward extends MotorControl {

	public Backward(int speed) {
		super(speed);
	}

	@Override
	public void perform(SumoRobot robot) {
		robot.backward();
	}
	
}
