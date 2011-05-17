package robosim.arena.robosumomatch.robot.strategies;

import robosim.arena.robosumomatch.robot.MySumoRobot;
import robosim.arena.robosumomatch.robot.actions.Forward;
import robosim.arena.robosumomatch.robot.actions.TurnClockwise;
import robosim.arena.robosumomatch.robot.actions.TurnCounterClockwise;
import robosim.robot.Robot;
import robosim.robot.strategy.Event;
import robosim.robot.strategy.Expression;
import robosim.robot.strategy.ExpressionEvent;
import robosim.robot.strategy.State;
import robosim.robot.strategy.Strategy;
import robosim.robot.strategy.TimerEvent;
import robosim.robot.strategy.Transition;

public class FindAndCharge extends Strategy {
	
	@Override
	public void initStrategy(Robot robot) {
		State idleState = new State("idle");
		State findState = new State("find");
		State attackState = new State("attack");
		
		final MySumoRobot sumoRobot = (MySumoRobot)robot;
		
		final Event foundEnemyEvent = new ExpressionEvent("foundEnemy", new Expression() {
			@Override
			public boolean evaluate() {
				return sumoRobot.sensorA.getSensorValue() > 0;
			}
		});
		
		final Event lostEnemyEvent = new ExpressionEvent("lostEnemy", new Expression() {
			@Override
			public boolean evaluate() {
				return sumoRobot.sensorA.getSensorValue() == 0;
			}
		});
		
		final Event wait5Seconds = new TimerEvent("Wait5Seconds", this, 5000);
		
		idleState.addTransition(new Transition("start", wait5Seconds, new TurnClockwise(1), findState));
		findState.addTransition(new Transition("findToAttack", foundEnemyEvent, new Forward(1), attackState));
		attackState.addTransition(new Transition("attackToFind", lostEnemyEvent, new TurnCounterClockwise(1), findState));				

		setState(idleState);
	}	
}
