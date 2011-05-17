package robosim.arena.robosumomatch.robot.strategies;

import robosim.arena.robosumomatch.robot.SumoRobot;
import robosim.arena.robosumomatch.robot.actions.*;
import robosim.robot.Robot;
import robosim.robot.strategy.*;

public class FindAndChargeAlternating extends Strategy {
	
	@Override
	public void initStrategy(Robot robot) {
		final SumoRobot sumoRobot = (SumoRobot)robot;
		
		final Event wait5Seconds = new TimerEvent("Wait5Seconds", this, 5000);

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
		
		State idleState = new State("idle");
		State findCWState = new State("findCW");
		State findCCWState = new State("findCCW");
		State attackFromCWState = new State("attackFromCW");
		State attackFromCCWState = new State("attackFromCCW");
		
		idleState.addTransition(new Transition("start", wait5Seconds, new TurnClockwise(0.5f), findCWState));			
		findCWState.addTransition(new Transition("findCWToAttackFromCW", foundEnemyEvent, new Forward(1.0f), attackFromCWState));			
		findCCWState.addTransition(new Transition("findCCWToAttackFromCCW", foundEnemyEvent, new Forward(1.0f), attackFromCCWState));			
		attackFromCWState.addTransition(new Transition("attackFromCWToFindCCW", lostEnemyEvent, new TurnCounterClockwise(0.5f), findCCWState));			
		attackFromCCWState.addTransition(new Transition("attackFromCCWToFindCW", lostEnemyEvent, new TurnClockwise(0.5f), findCWState));			

		setState(idleState);
	}	
}
