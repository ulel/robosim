package robosim.arena.robosumomatch.robot.actions;

import robosim.arena.robosumomatch.robot.SumoRobot;
import robosim.robot.Robot;
import robosim.robot.strategy.MotorControl;

public class TurnCounterClockwise extends MotorControl {

	public TurnCounterClockwise(float speed) {
		super(speed);
	}

	@Override
	public void perform(Robot robot) {
		((SumoRobot)robot).turnClockwise(speed);
	}
	
}
