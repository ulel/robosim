package robosim.robot.strategy.actions;

import robosim.robot.SumoRobot;
import robosim.robot.strategy.MotorControl;

public class TurnClockwise extends MotorControl {

	public TurnClockwise(int speed) {
		super(speed);
	}

	@Override
	public void perform(SumoRobot robot) {
		robot.turnCounterClockwise();
	}
	
}
