package robosim.arena.robosumomatch.robot.strategies;

import robosim.arena.robosumomatch.robot.SumoRobot;
import robosim.arena.robosumomatch.robot.actions.Forward;
import robosim.arena.robosumomatch.robot.actions.TurnClockwise;
import robosim.arena.robosumomatch.robot.actions.TurnCounterClockwise;
import robosim.robot.Robot;
import robosim.robot.strategy.Event;
import robosim.robot.strategy.Expression;
import robosim.robot.strategy.State;
import robosim.robot.strategy.Strategy;
import robosim.robot.strategy.Transition;

public class FindAndCharge extends Strategy {
	
	@Override
	public void initStrategy(Robot robot) {
		State idleState = new State("idle");
		State findState = new State("find");
		State attackState = new State("attack");
		
		final SumoRobot sumoRobot = (SumoRobot)robot;
		
		final Event foundEnemyEvent = new Event("foundEnemy", new Expression() {
			@Override
			public boolean evaluate() {
				return sumoRobot.sensorA.getSensorValue() > 0;
			}
		});
		
		final Event lostEnemyEvent = new Event("lostEnemy", new Expression() {
			@Override
			public boolean evaluate() {
				return sumoRobot.sensorA.getSensorValue() == 0;
			}
		});
		
		final Event alwaysTrueEvent = new Event("true", new Expression() {
			@Override
			public boolean evaluate() {
				return true;
			}
		});
		
		idleState.addTransition(new Transition("start", alwaysTrueEvent, new TurnClockwise(1), findState));
		findState.addTransition(new Transition("findToAttack", foundEnemyEvent, new Forward(1), attackState));
		attackState.addTransition(new Transition("attackToFind", lostEnemyEvent, new TurnCounterClockwise(1), findState));				

		currentState = idleState;
	}	
}
