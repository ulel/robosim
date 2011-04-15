package robosim;

import java.awt.Color;
import java.awt.Graphics2D;

import net.phys2d.raw.Body;
import net.phys2d.raw.Collide;
import net.phys2d.raw.Contact;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Circle;
import robosim.robot.*;
import robosim.robot.components.RobotComponent;
import robosim.robot.components.sensors.Sensor;

public class RoboSumoMatch extends Scene {
	public static final String DOHYO_ARENA = "DohyoArena";
	public static final long DOHYO_ARENA_BITMASK = 2;
	public static final String DOHYO_BORDER = "DohyoBorder";
	
	Robot r1;
	Robot r2;
	StaticBody border;
	StaticBody arena;
	
	public RoboSumoMatch(String title) {
		super(title);
	}

	@Override
	protected void init(World world) {
		world.setGravity(0,0);
		
		border = new StaticBody(RoboSumoMatch.DOHYO_BORDER, new Circle(240));
		border.setPosition(250, 270);
		arena = new StaticBody(RoboSumoMatch.DOHYO_ARENA, new Circle(220));
		arena.setPosition(250, 270);
				
		r1 = new ManuallyControlledSumoRobot(world, 250, 200, (float)(Math.random() * Math.PI * 2), 10);
		r2 = new SumoRobot(world, 250, 360, (float)(Math.random() * Math.PI * 2), 10);
		
		arena.setBitmask(DOHYO_ARENA_BITMASK);
		world.add(arena);
	}

	@Override
	protected void update() {
		super.update();
		
		if (Collide.collide(new Contact[] { new Contact(), new Contact() }, r1.getHull().getComponentBody(), arena, 0f) == 0)
			needsReset = true;

		if (Collide.collide(new Contact[] { new Contact(), new Contact() }, r2.getHull().getComponentBody(), arena, 0f) == 0) {
			needsReset = true;
		}
	}

	@Override
	protected void step() {
		r1.step(keys);
		r2.step(keys);
	}
	
	@Override
	protected void draw(Graphics2D g) {
		super.draw(g);
		
		for (RobotComponent c : r1.getComponents()) {
			if (c instanceof Sensor) {
				this.drawSensor(g, (Sensor)c);
			}
		}
		for (RobotComponent c : r2.getComponents()) {
			if (c instanceof Sensor) {
				this.drawSensor(g, (Sensor)c);
			}
		}
		
//		g.setColor(Color.black);
//		g.drawString("R1 sensorA= " + Math.round(r1.sensorA.getSensorValue() * 100) / 100f, 380, 40);
//		g.drawString("R1 sensorB= " + r1.sensorB.getSensorValue(), 380, 60);
//		g.drawString("R1 sensorC= " + r1.sensorC.getSensorValue(), 380, 80);
//		g.drawString("R2 sensorA= " + Math.round(r2.sensorA.getSensorValue() * 100) / 100f, 380, 100);
//		g.drawString("R2 sensorB= " + r2.sensorB.getSensorValue(), 380, 120);
//		g.drawString("R2 sensorC= " + r2.sensorC.getSensorValue(), 380, 140);
	}
	
	protected void drawSensor(Graphics2D g, Sensor s) {
		this.drawContact(g, s.getContactPoints()[0]);
		this.drawContact(g, s.getContactPoints()[1]);
	}
	
	@Override
	protected void drawBackground(Graphics2D g) {
		super.drawBackground(g);
		g.setColor(Color.DARK_GRAY);
		drawCircle(g, border);
		g.setColor(Color.WHITE);
		drawCircle(g, arena);
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(215, 225, 70, 7);
		g.fillRect(215, 325, 70, 7);
	}
	
	private void drawCircle(Graphics2D g, Body circle) {
		float x = circle.getPosition().getX();
		float y = circle.getPosition().getY();
		float r = ((Circle) circle.getShape()).getRadius();
		
		g.fillOval((int) (x-r),(int) (y-r),(int) (r*2),(int) (r*2));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new RoboSumoMatch("SumoRobot").start();
	}

}
