package robosim.arena.robosumomatch;

import java.awt.Color;
import java.awt.Graphics2D;

import net.phys2d.raw.Body;
import net.phys2d.raw.Collide;
import net.phys2d.raw.Contact;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Circle;
import robosim.RoboArena;
import robosim.arena.robosumomatch.robot.ManuallyControlledSumoRobot;
import robosim.arena.robosumomatch.robot.SumoRobot;
import robosim.robot.Robot;
import robosim.robot.components.RobotComponent;
import robosim.robot.components.sensors.Sensor;
import robosim.robot.strategy.NullStrategy;
import robosim.robot.strategy.Strategy;

public class RoboSumoMatch extends RoboArena {
	public static final String DOHYO_ARENA = "DohyoArena";
	public static final long DOHYO_ARENA_BITMASK = 2;
	public static final String DOHYO_BORDER = "DohyoBorder";
	
	Robot r1;
	Robot r2;
	
	Class<Robot> r1Class;
	Class<Robot> r2Class;
	
	Class<Strategy> r1StrategyClass;
	Class<Strategy> r2StrategyClass;

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
				
//		r1 = new ManuallyControlledSumoRobot(world, 250, 200, (float)(Math.random() * Math.PI * 2), 10, new NullStrategy());
//		r2 = new SumoRobot(world, 250, 360, (float)(Math.random() * Math.PI * 2), 10, new NullStrategy());
		try {
			Strategy r1Strategy = r1StrategyClass.getConstructor().newInstance();
			Strategy r2Strategy = r2StrategyClass.getConstructor().newInstance();
			r1 = r1Class.getConstructor(World.class, float.class, float.class, float.class, float.class, Strategy.class).newInstance(world, 250, 200, (float)(Math.random() * Math.PI * 2), 10, r1Strategy);
			r2 = r2Class.getConstructor(World.class, float.class, float.class, float.class, float.class, Strategy.class).newInstance(world, 250, 360, (float)(Math.random() * Math.PI * 2), 10, r2Strategy);
		} catch (Exception e) { 
			// TODO: Should maybe catch the individual exceptions but there is
			// no good way to continue if it happens
			System.err.println("Error when loading robot.");
			e.printStackTrace();
			System.exit(1);
		}
		
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
		int i = 0;
		for (RobotComponent c : r1.getComponents()) {
			if (c instanceof Sensor) {
				i++;
				g.drawString(i + ". " + c.toString(), 350, 15 * (i + 2));
				this.drawSensor(g, (Sensor)c);
			}
		}
		for (RobotComponent c : r2.getComponents()) {
			if (c instanceof Sensor) {
				i++;
				g.drawString(i + ". " + c.toString(), 350, 15 * (i + 2));
				this.drawSensor(g, (Sensor)c);
			}
		}
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

	@Override
	public void setRobots(Class<Robot>[] robots) {
		r1Class = robots[0];
		r2Class = robots[1];
	}
	
	@Override
	public void setStrategies(Class<Strategy>[] strategies) {
		r1StrategyClass = strategies[0];
		r2StrategyClass = strategies[1];
	}

	@Override
	public int numberOfRobots() {
		return 2;
	}

}
