package robosim.robot.strategy;

import robosim.robot.SumoRobot;

public class Transition {

	private String name;
	protected int priority;
	private Event event;
	private Action action;
	protected State targetState;
	
	public Transition(String name, int priority, Event event, Action action, State targetState) {
		this.name = name;
		this.priority = priority;
		this.event = event;
		this.action = action;
		this.targetState = targetState;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public boolean isValid() {
		return event.evaluate();
	}
	
	public void performAction(SumoRobot robot) {
		action.perform(robot);
	}

}
