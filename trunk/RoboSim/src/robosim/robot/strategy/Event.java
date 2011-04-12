package robosim.robot.strategy;

import robosim.robot.Robot;

public class Event {
	
	private String name;
	private Expression expression;
	
	public Event(String name, Expression expression) {
		this.name = name;
		this.expression = expression;
	}
	
	public boolean evaluate() {
		return expression.evaluate();
	}
	
}