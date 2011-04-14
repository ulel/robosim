package robosim.robot.strategy;

import java.util.LinkedList;
import java.util.List;

public class State {
	
	private String name;
	protected List<Transition> transitions = new LinkedList<Transition>();
	
	public State(String name) {
		this.name = name;
	}
	
	public void addTransition(Transition transition) {
		transitions.add(transition);
	}
	
}