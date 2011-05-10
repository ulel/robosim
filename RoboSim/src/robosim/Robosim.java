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
import robosim.robot.strategy.Strategy;

public class Robosim {
	public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		Robosim sim = new Robosim();
		sim.start();
	}
	
	public void start() throws MalformedURLException, ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String root = new File(".").getAbsolutePath() + "/bin";
		
		System.out.println("Root directory: " + root);
		URLClassLoader cl = getClassLoader(root);
		
		String choosenGame = chooseGame(root);

		RoboArena game = instantiateGame(cl, choosenGame);
		String[][] choosenRobots = chooseRobots(root, choosenGame, game.numberOfRobots());
		
		Class<Robot>[] robotClasses = getRobotClasses(cl, choosenRobots);
		Class<Strategy>[] strategyClasses = getStrategyClasses(cl, choosenRobots);
		
		game.setRobots((Class<Robot>[]) robotClasses);
		game.setStrategies((Class<Strategy>[]) strategyClasses);
		game.start();
	}

	@SuppressWarnings("unchecked")
	private Class<Robot>[] getRobotClasses(URLClassLoader cl, String[][] choosenRobots) throws ClassNotFoundException {
		
		Class<Robot>[] robotClasses = (Class<Robot>[]) Array.newInstance(Class.class, choosenRobots.length);
		
		int i = 0;
		for (String[] robot : choosenRobots) {
			robotClasses[i] = (Class<Robot>) cl.loadClass(robot[0]);
			i++;
		}
		
		return robotClasses;
	}
	
	@SuppressWarnings("unchecked")
	private Class<Strategy>[] getStrategyClasses(URLClassLoader cl, String[][] choosenRobots) throws ClassNotFoundException {
		
		Class<Strategy>[] strategyClasses = (Class<Strategy>[]) Array.newInstance(Class.class, choosenRobots.length);
		
		int i = 0;
		for (String[] robot : choosenRobots) {
			strategyClasses[i] = (Class<Strategy>) cl.loadClass(robot[1]);
			i++;
		}
		
		return strategyClasses;
	}

	private RoboArena instantiateGame(URLClassLoader cl, String choosenGame)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		@SuppressWarnings("unchecked")
		Class<RoboArena> gameClass = (Class<RoboArena>) cl.loadClass(choosenGame);
		RoboArena game = gameClass.getConstructor(String.class).newInstance("Test");
		return game;
	}
	
	private String[][] chooseRobots(String root, String choosenGame, int numberOfRobots) {
		String[][] choosenRobots = new String[numberOfRobots][2];
	
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
			String choosenRobot = chooseRobot(availableRobotsForGame);
			String choosenStrategy = chooseStrategy(root, choosenRobot);
			choosenRobots[i] = new String[] { choosenRobot, choosenStrategy };
		}
		
		return choosenRobots;
	}

	private String chooseStrategy(String root, String choosenRobot) {
		String[] availableStrategiesForRobot = getStrategiesForRobot(root, choosenRobot);
		System.out.println("Available strategies for " + choosenRobot + ":");
		int i = 0;
		for (String strategyName : availableStrategiesForRobot) {
			i++;
			System.out.println("  [" + i + "] " + strategyName);
		}
		
		System.out.println("Choose strategy");
		String choosenStrategy = availableStrategiesForRobot[getChoiceFromUser()-1];
		System.out.println("Chosen staragey: " + choosenStrategy);
		
		return choosenStrategy;
	}

	private String chooseRobot(String[] availableRobots) {

		String choosenRobot = availableRobots[getChoiceFromUser()-1];
		System.out.println("Chosen robot: " + choosenRobot);
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
		System.out.println("Chosen game: " + choosenGame);
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
		String gameDir = "robosim/arena";
		String gamePackage = gameDir.replace("/", ".") + ".";
		
		File[] gameDirs = new File(root + "/" + gameDir).listFiles();
		
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
		
		String gameDir = new File(root + "/" + gameName.replace(".", "/") + ".class").getParent();
		File robotDir = new File(gameDir + "/robot");
		
		ArrayList<String> robots = new ArrayList<String>();
		
		File[] availableRobots = robotDir.listFiles(new ClassFilenameFilter());
		for (File robot : availableRobots) {
			String robotName = getClassName(robot);
			robots.add(robotPackage + "." + robotName);
		}
		
		return arrayListToStringArray(robots);
	}
	
	public String[] getStrategiesForRobot(String root, String robotName) {
		String strategiesPackage = robotName.substring(0, robotName.lastIndexOf('.')) + ".strategies";
		
		String robotDir = new File(root + "/" + robotName.replace(".", "/") + ".class").getParent();
		File strategiesDir = new File(robotDir + "/strategies");
		
		ArrayList<String> strategies = new ArrayList<String>();
		strategies.add("robosim.robot.strategy.NullStrategy");
		
		File[] availableStrategies = strategiesDir.listFiles(new ClassFilenameFilter());
		for (File strategy : availableStrategies) {
			String strategyName = getClassName(strategy);
			strategies.add(strategiesPackage + "." + strategyName);
		}
		
		return arrayListToStringArray(strategies);
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
