package robosim.robot.strategy;

import robosim.robot.Robot;

public class TimerEvent extends Event {
	
	Strategy strategy;
	long milliseconds;

	public TimerEvent(String name, Strategy strategy, long milliseconds) {
		super(name);
		this.strategy = strategy;
		this.milliseconds = milliseconds;
	}

	@Override
	public boolean evaluate() {
		return strategy.getTimeSinceLastStateSwitch() > milliseconds;
	}

}
