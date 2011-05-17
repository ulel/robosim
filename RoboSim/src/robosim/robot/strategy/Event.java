package robosim.robot.strategy;

import robosim.robot.Robot;

public abstract class Event {
	
	private String name;
	
	
	public Event(String name) {
		this.name = name;
	}
	
	abstract public boolean evaluate();
	
}