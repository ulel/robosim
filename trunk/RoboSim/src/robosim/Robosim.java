package robosim;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import robosim.robot.Robot;

public class Robosim {
	public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		Robosim sim = new Robosim();
		sim.start();
	}
	
	public void start() throws MalformedURLException, ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// String root = "C:\\Users\\Ulf\\dev\\eclipse_workspaces\\univ\\mde_workspace\\RoboSim\\bin";
		String root = new File(".").getAbsolutePath() + "\\bin";
		
		System.out.println("Root directory: " + root);
		URLClassLoader cl = getClassLoader(root);
		
		String choosenGame = chooseGame(root);

		RoboArena game = instansiateGame(cl, choosenGame);
		String[] choosenRobot = chooseRobots(root, choosenGame, game.numberOfRobots());
		
		Class<Robot>[] robotClasses = getRobotClasses(cl, choosenRobot);
		
		game.setRobots((Class<Robot>[]) robotClasses);
		game.start();
	}

	@SuppressWarnings("unchecked")
	private Class<Robot>[] getRobotClasses(URLClassLoader cl,
			String[] choosenRobots) throws ClassNotFoundException {
		
		Class<Robot>[] robotClasses = (Class<Robot>[]) Array.newInstance(Class.class, choosenRobots.length);
		
		int i = 0;
		for (String robotName : choosenRobots) {
			robotClasses[i] = (Class<Robot>) cl.loadClass(robotName);
			i++;
		}
		
		return robotClasses;
	}

	private RoboArena instansiateGame(URLClassLoader cl, String choosenGame)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		@SuppressWarnings("unchecked")
		Class<RoboArena> gameClass = (Class<RoboArena>) cl.loadClass(choosenGame);
		RoboArena game = gameClass.getConstructor(String.class).newInstance("Test");
		return game;
	}

	private String[] chooseRobots(String root, String choosenGame, int numberOfRobots) {
		String[] choosenRobots = (String[]) Array.newInstance(String.class, numberOfRobots);
	
		String[] availableRobotsForGame = getRobotsForGame(root, choosenGame);
		System.out.println("Available robots for " + choosenGame + ":");
		int i = 0;
		for (String robotName : availableRobotsForGame) {
			i++;
			System.out.println("  [" + i + "] " + robotName);
		}
		
		System.out.println("Should choose " + numberOfRobots + " robots.");
		
		for(i = 0; i < numberOfRobots; i++) {
			System.out.println("Choose robot " + (i+1));
			choosenRobots[i] = chooseRobot(availableRobotsForGame);
		}
		
		return choosenRobots;
	}

	private String chooseRobot(String[] availableRobots) {

		String choosenRobot = availableRobots[getChoiceFromUser()-1];
		System.out.println("Choosen robot: " + choosenRobot);
		return choosenRobot;
	}

	private String chooseGame(String root) {
		String[] availableGames = getGames(root);
		
		System.out.println("Available games: ");
		int i = 0;
		for (String gameName : availableGames) {
			i++;
			System.out.println("  [" + i + "] " + gameName);
		}

		String choosenGame = availableGames[getChoiceFromUser()-1];
		System.out.println("Choosen game: " + choosenGame);
		return choosenGame;
	}

	private int getChoiceFromUser() {
		Scanner in = new Scanner(System.in);
		return in.nextInt();
	}

	private URLClassLoader getClassLoader(String root) throws MalformedURLException {
		URL url = new File(root).toURI().toURL();
		URL[] urls = {url};
		
		URLClassLoader cl = new URLClassLoader(urls);
		return cl;
	}
	
	public String[] getGames(String root) {
		String gameDir = "robosim\\arena";
		String gamePackage = gameDir.replace("\\", ".") + ".";
		
		File[] gameDirs = new File(root + "\\" + gameDir).listFiles();
		
		ArrayList<String> gameClassNames = new ArrayList<String>();
		
		for (File file : gameDirs) {
			String gameClassName = gamePackage + file.getName() + "." + getGameClassName(file.getName(), file);
			gameClassNames.add(gameClassName);
		}
		return arrayListToStringArray(gameClassNames);
	}

	private String[] arrayListToStringArray(ArrayList<String> arrayList) {
		return arrayList.toArray((String[]) Array.newInstance(String.class, arrayList.size()));
	}
	
	public String[] getRobotsForGame(String root, String gameName) {
		String robotPackage = gameName.substring(0, gameName.lastIndexOf('.')) + ".robot";
		
		String gameDir = new File(root + "\\" + gameName.replace(".", "\\") + ".class").getParent();
		File robotDir = new File(gameDir + "\\robot");
		
		ArrayList<String> robots = new ArrayList<String>();
		
		File[] availableRobots = robotDir.listFiles(new ClassFilenameFilter());
		for (File robot : availableRobots) {
			String robotName = getClassName(robot);
			robots.add(robotPackage + "." + robotName);
		}
		
		return arrayListToStringArray(robots);
	}
	
	private class ClassFilenameFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".class") && !name.contains("$");
		}
	}
	
	private class GameFilenameFilter implements FilenameFilter {
		Pattern regexp;
		
		public GameFilenameFilter(String nameToMatch) {
			regexp = Pattern.compile(nameToMatch + ".class", Pattern.CASE_INSENSITIVE);
		}
		
		@Override
		public boolean accept(File dir, String name) {
			Matcher matcher = regexp.matcher(name);
			return matcher.matches() && !name.contains("$");
		}
	};
	
	public String getGameClassName(String fileName, File rootDir) {
		FilenameFilter filter = new GameFilenameFilter(fileName);
		
		File[] files = rootDir.listFiles(filter);
		if(files.length == 1) {
			return getClassName(files[0]);
		}else{
			return null;
		}
	}

	private String getClassName(File file) {
		String name = file.getName();
		int dotIndex = name.lastIndexOf('.');
		return name.substring(0, dotIndex);
	}
}
