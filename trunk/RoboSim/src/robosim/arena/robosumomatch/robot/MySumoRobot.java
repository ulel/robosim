package robosim.arena.robosumomatch.robot;

import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import robosim.arena.robosumomatch.RoboSumoMatch;
import robosim.robot.Robot;
import robosim.robot.components.actuators.WheelMotor;
import robosim.robot.components.sensors.ContactSensor;
import robosim.robot.components.sensors.GroundSensor;
import robosim.robot.components.sensors.ProximitySensor;
import robosim.robot.strategy.Strategy;

public class MySumoRobot extends SumoRobot {

	public ProximitySensor sensorA;
	public ContactSensor sensorB;
	public GroundSensor sensorC;
	
	public MySumoRobot(World w, float posX, float posY, float rotation, float mass, Strategy strategy) {
		super(w, posX, posY, rotation, mass, strategy);
		
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
	}

}
