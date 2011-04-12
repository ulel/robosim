package robosim.robot.strategy;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class State {
	
	private String name;
	SortedSet<Transition> transitions = new TreeSet<Transition>(new Comparator<Transition>() {
		@Override
		public int compare(Transition t1, Transition t2) {
			return t1.priority - t2.priority;
		}
	});
	
	public State(String name) {
		this.name = name;
	}
	
	public void addTransition(Transition transition) {
		transitions.add(transition);
	}
	
}