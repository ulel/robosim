package robosim.robot.strategy;

import robosim.arena.robosumomatch.robot.SumoRobot;
import robosim.robot.Robot;

public abstract class Strategy {
	
	private State currentState;
	private long stateSwitchTime;
	
	public Strategy() { }
	
	public void step(Robot robot) {
		for (Transition transition : currentState.transitions) {
			if (transition.isValid()) {
				transition.performAction(robot);
				setState(transition.targetState);
				break;
			}
		}
	}
	
	public abstract void initStrategy(Robot robot);
	
	protected void setState(State state) {
		currentState = state;
		stateSwitchTime = System.currentTimeMillis();
	}
	
	protected long getTimeSinceLastStateSwitch() {
		return System.currentTimeMillis() - stateSwitchTime;
	}
	
}