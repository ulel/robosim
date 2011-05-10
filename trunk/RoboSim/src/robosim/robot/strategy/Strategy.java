package robosim.robot.strategy;

import robosim.arena.robosumomatch.robot.SumoRobot;
import robosim.robot.Robot;

public abstract class Strategy {
	
	protected State currentState;
	
	public Strategy() { }
	
	public void step(Robot robot) {
		for (Transition transition : currentState.transitions) {
			if (transition.isValid()) {
				transition.performAction(robot);
				currentState = transition.targetState;
				break;
			}
		}
	}
	
	public abstract void initStrategy(Robot robot);
	
}