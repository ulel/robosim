package robosim.robot.strategy;

import robosim.robot.Robot;

public class NullStrategy extends Strategy {

	@Override
	public void step(Robot robot) {};
	
	@Override
	public void initStrategy(Robot robot) {	}

}
