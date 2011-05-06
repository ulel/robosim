package robosim.robot.strategy;

import robosim.arena.robosumomatch.robot.SumoRobot;

public abstract class Strategy {
	
	protected State currentState;
	
	public Strategy() {
		initStrategy();
	}
	
	public void step(SumoRobot robot) {
		for (Transition transition : currentState.transitions) {
			if (transition.isValid()) {
				transition.performAction(robot);
				currentState = transition.targetState;
				break;
			}
		}
	}
	
	public abstract void initStrategy();
	
}