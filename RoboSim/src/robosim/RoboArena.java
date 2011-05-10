package robosim;

import net.phys2d.raw.World;
import robosim.robot.Robot;
import robosim.robot.strategy.Strategy;

// TODO This class might not be needed
//      I first thought Scene was part of phys2d but then I realized it is part
//      of robosim so could maybe remove this and put in Scene.
public abstract class RoboArena extends Scene {

	public RoboArena(String title) {
		super(title);
	}
	
	public abstract void setRobots(Class<Robot>[] robots);
	public abstract void setStrategies(Class<Strategy>[] strategies);
	
	public abstract int numberOfRobots();

}
