package robosim.robot;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import robosim.RoboSumoMatch;
import robosim.robot.components.actuators.WheelMotor;
import robosim.robot.components.sensors.ContactSensor;
import robosim.robot.components.sensors.GroundSensor;
import robosim.robot.components.sensors.ProximitySensor;
import robosim.robot.strategy.Event;
import robosim.robot.strategy.Expression;
import robosim.robot.strategy.State;
import robosim.robot.strategy.Strategy;
import robosim.robot.strategy.Transition;
import robosim.robot.strategy.actions.Forward;
import robosim.robot.strategy.actions.TurnClockwise;
import robosim.robot.strategy.actions.TurnCounterClockwise;

public class SumoRobot extends Robot {

	private WheelMotor wheel_left;
	private WheelMotor wheel_right;
	
	public ProximitySensor sensorA;
	public ContactSensor sensorB;
	public GroundSensor sensorC;
	
	private Strategy strategy;
	
	public SumoRobot(World w, float posX, float posY, float rotation, float mass) {
		super(w, posX, posY, rotation, mass);
		
		this.wheel_left = new WheelMotor(1f, MAX_FORCE);
		this.wheel_right = new WheelMotor(1f, MAX_FORCE);
		this.addComponent(this.wheel_left, -10, 0, 0);
		this.addComponent(this.wheel_right, 10, 0, 0);
		
		this.sensorA = new ProximitySensor();
		this.sensorA.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.sensorA.setHRange(20);
		this.sensorA.setVRange(160);
		this.addComponent(this.sensorA, 0, 10, 0);
		
		this.sensorB = new ContactSensor();
		this.sensorB.removeBit(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.sensorB.setComponentShape(new Box(15, 2.5f));
		this.addComponent(this.sensorB, 0, -16, -(float)Math.PI);
		
		this.sensorC = new GroundSensor(RoboSumoMatch.DOHYO_ARENA_BITMASK);
		this.addComponent(this.sensorC, 0, 0, 0);
		
		setupStrategy();
	}

	private void setupStrategy() {
		final Event foundEnemyEvent = new Event("foundEnemy", new Expression() {
			@Override
			public boolean evaluate() {
				return sensorA.getSensorValue() > 0;
			}
		});
		
		final Event lostEnemyEvent = new Event("lostEnemy", new Expression() {
			@Override
			public boolean evaluate() {
				return sensorA.getSensorValue() == 0;
			}
		});
		
		final Event alwaysTrueEvent = new Event("true", new Expression() {
			@Override
			public boolean evaluate() {
				return true;
			}
		});
		
		strategy = new Strategy() {
			@Override
			public void initStrategy() {
				State idleState = new State("idle");
				State findState = new State("find");
				State attackState = new State("attack");

				idleState.addTransition(new Transition("start", 0, alwaysTrueEvent, new TurnClockwise(1), findState));
				findState.addTransition(new Transition("findToAttack", 0, foundEnemyEvent, new Forward(1), attackState));
				attackState.addTransition(new Transition("attackToFind", 0, lostEnemyEvent, new TurnCounterClockwise(1), findState));				

				currentState = idleState;
			}
		};
	}



	private final float MAX_FORCE = 100;
	
	public void forward() {
		this.wheel_left.setPower(MAX_FORCE);
		this.wheel_right.setPower(MAX_FORCE);
	}
	
	public void backward() {
		this.wheel_left.setPower(-MAX_FORCE);
		this.wheel_right.setPower(-MAX_FORCE);
	}
	
	public void turnClockwise() {
		this.wheel_left.setPower(-MAX_FORCE * 0.5f);
		this.wheel_right.setPower(MAX_FORCE * 0.5f);
	}
	
	public void turnCounterClockwise() {
		this.wheel_left.setPower(MAX_FORCE * 0.5f);
		this.wheel_right.setPower(-MAX_FORCE * 0.5f);
	}
	
	private int missileFired = 61;
	public void fireMissile() {
		if (this.missileFired < 60)
		{
			this.missileFired++;
			return;
		}
		
		Body missileBody = new Body("missile", new Box(5, 20), 350);
		missileBody.setPosition(this.getPosX(), this.getPosY());
		missileBody.setRotation(this.getRotation());
		missileBody.addExcludedBody(this.getHull().getComponentBody());
		missileBody.addExcludedBody(this.wheel_left.getComponentBody());
		missileBody.addExcludedBody(this.wheel_right.getComponentBody());
		world.add(missileBody);
		
		float r = this.getRotation();
		float x = (float) (-Math.sin(r) * 2000000);
		float y = (float) (Math.cos(r) * 2000000);
		
		missileBody.addForce(new Vector2f(x, y));
		
		this.missileFired = 0;
	}

	@Override
	public void performBehavior() {
		strategy.step(this);
	}
}
