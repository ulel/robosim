package robosim;

import java.awt.Graphics2D;

import robosim.robot.*;
import robosim.robot.components.RobotComponent;
import robosim.robot.components.sensors.Sensor;
import net.phys2d.raw.Collide;
import net.phys2d.raw.Contact;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;

public class Maze extends Scene {

	private Robot r;
	private StaticBody goal;
	
	private long start;
	private long best_time = Long.MAX_VALUE;
	private long current_time = 0;
	
	public Maze(String title) {
		super(title);
	}

	@Override
	protected void init(World world) {
		world.setGravity(0,0);
		
		this.createBorder();
		this.createMaze();
		
		this.r = new MazeRobot2(world, 75, 75, (float)(Math.random() * Math.PI * 2), 10);
		
		start = System.currentTimeMillis() / 1000;
	}

	private void createBorder() {
		StaticBody w1 = new StaticBody(new Box(500, 5));
		w1.setPosition(250, 30);
		world.add(w1);
		StaticBody w2 = new StaticBody(new Box(500, 5));
		w2.setPosition(490, 270);
		w2.setRotation((float)Math.PI / 2);
		world.add(w2);
		StaticBody w3 = new StaticBody(new Box(500, 5));
		w3.setPosition(250, 510);
		world.add(w3);
		StaticBody w4 = new StaticBody(new Box(500, 5));
		w4.setPosition(10, 270);
		w4.setRotation((float)Math.PI / 2);
		world.add(w4);
	}
	private void createMaze() {
		StaticBody w = new StaticBody(new Box(150, 5));
		w.setPosition(320, 170);
		world.add(w);
		
		w = new StaticBody(new Box(150, 5));
		w.setPosition(70, 200);
		world.add(w);
		
		w = new StaticBody(new Box(150, 5));
		w.setPosition(145, 275);
		w.setRotation((float)Math.PI / 2);
		world.add(w);
		
		w = new StaticBody(new Box(150, 5));
		w.setPosition(70, 350);
		world.add(w);
		
		w = new StaticBody(new Box(150, 5));
		w.setPosition(245, 105);
		w.setRotation((float)Math.PI / 2);
		world.add(w);
		
		w = new StaticBody(new Box(150, 5));
		w.setPosition(245, 245);
		w.setRotation((float)Math.PI / 2);
		world.add(w);
		
		w = new StaticBody(new Box(150, 5));
		w.setPosition(170, 430);
		world.add(w);
		
		w = new StaticBody(new Box(150, 5));
		w.setPosition(330, 430);
		world.add(w);
		
		w = new StaticBody(new Box(150, 5));
		w.setPosition(360, 345);
		w.setRotation((float)Math.PI / 2);
		world.add(w);
		

		this.goal = new StaticBody("Goal", new Circle(15));
		this.goal.setBitmask(0);
		this.goal.setPosition(300, 100);
		world.add(this.goal);
	}
	
	@Override
	protected void update() {
		super.update();
		
		current_time = (System.currentTimeMillis() / 1000) - start;
		
		if (Collide.collide(new Contact[] { new Contact(), new Contact() }, r.getHull().getComponentBody(), this.goal, 0f) > 0)
		{
			needsReset = true;
			if (current_time < best_time)
				best_time = current_time;
		}
	}
	
	@Override
	protected void step() {
		super.step();
		this.r.step(this.keys);
	}
	
	@Override
	protected void draw(Graphics2D g) {
		super.draw(g);
		
		int i = 0;
		for (RobotComponent c : r.getComponents()) {
			if (c instanceof Sensor) {
				i++;
				this.drawContact(g, ((Sensor)c).getContactPoints()[0]);
				this.drawContact(g, ((Sensor)c).getContactPoints()[1]);
				
				g.drawString(i + ". " + c.toString(), 350, 15 * (i + 2));
			}
		}
		
	    if (best_time != Long.MAX_VALUE)
	    	g.drawString("Best: " + this.best_time + "s", 410, 485);
	    
	    g.drawString("Current: " + current_time + "s", 410, 500);

	}
	
	
	public static void main(String[] args) {
		new Maze("Maze").start();
	}
}
