package robosim.robot.strategy;

import robosim.arena.robosumomatch.robot.SumoRobot;
import robosim.robot.Robot;

public class Transition {

	private String name;
	private Event event;
	private Action action;
	protected State targetState;
	
	public Transition(String name, Event event, Action action, State targetState) {
		this.name = name;
		this.event = event;
		this.action = action;
		this.targetState = targetState;
	}
	
	public boolean isValid() {
		return event.evaluate();
	}
	
	public void performAction(Robot robot) {
		action.perform(robot);
	}

}
