package robosim.robot.strategy;

import robosim.robot.SumoRobot;

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
	
	public void performAction(SumoRobot robot) {
		action.perform(robot);
	}

}
