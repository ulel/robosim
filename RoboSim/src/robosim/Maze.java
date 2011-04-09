package robosim;

import java.awt.Color;
import java.awt.Graphics2D;

import robosim.robot.MazeRobot;
import robosim.robot.components.sensors.Sensor;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;

public class Maze extends Scene {

	private MazeRobot r;
	
	public Maze(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init(World world) {
		world.setGravity(0,0);
		
		this.createBorder();
		this.createMaze();
		
		this.r = new MazeRobot(world, 50, 50, 0, 10);
		
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
	}
	
	@Override
	protected void step() {
		super.step();
		this.r.step();
	}
	
	@Override
	protected void draw(Graphics2D g) {
		super.draw(g);
		
		for (Sensor s : r.getSensors()) {
			this.drawContact(g, s.getContactPoints()[0]);
			this.drawContact(g, s.getContactPoints()[1]);
		}
		
		g.setColor(Color.black);
		g.drawString("S Front1= " + r.sensorFront1.getSensorValue(), 380, 40);
		g.drawString("S Front2= " + r.sensorFront2.getSensorValue(), 380, 60);
	}
	
	
	public static void main(String[] args) {
		new Maze("Maze").start();
	}
}
