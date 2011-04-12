package robosim.robot.strategy;

public abstract class MotorControl implements Action {

	protected float speed;

	public MotorControl(float speed) {
		this.speed = speed;
	}

}
