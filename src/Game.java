import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author: Jonathan Haimann
 * Version: 1.1
 * Date: 07.07.2022
 *
 * 
 * To play this game, create an instance of this class and call the "play"
 * routine.
 * 
 * This main class creates and initialises all the others: it creates all
 * rooms, creates the parser and starts the game. It also evaluates the
 * commands that the parser returns.
 */

public class Game {

	private boolean finished, retreat;
	private LevelMap map;
	private ASCIIArtGenerator artgen;
	private Parser parser;
	private Player player;
	private Room currentRoom;
	private Enemy currentEnemy;
	private int alcoholSips, attackCount;
	private Room lastRoom;
	private Door doorToPass;

	/**
	 * Create the game and initialise its internal map. initialises all the doors
	 * and the starting area
	 * Usage - Game();
	 * 
	 * @throws Exception
	 */
	public Game() {
		parser = new Parser();
		printWelcome();
		System.out.print("\033[H\033[2J");
		System.out.flush();	
		System.out.println("Chose your class:");
		System.out.println("1. Warrior");
		System.out.println("2. Assassin");
		System.out.println("3. Tank");
		Scanner input = new Scanner(System.in);
		System.out.print(">");
		int classChosen = input.nextInt();	
		switch (classChosen) {
		case 1:
			player = new Player(Player.PlayerClass.Warrior);
			break;
		case 2:
			player = new Player(Player.PlayerClass.Assassin);
			break;
		case 3:
			player = new Player(Player.PlayerClass.Tank);
			break;
		default:
			player = new Player(Player.PlayerClass.Warrior);
			break;
		}
		System.out.println("You chose the " + player.getPlayerClass() + " class!");
		System.out.println();
		System.out.println("press enter to continue");
		Scanner input1 = new Scanner(System.in);
		input1.nextLine();

		currentRoom = new Room(player);	// set starting room
		if (isPlayerStuck()){
			Iterator<Map.Entry<String, Door>> iterator = currentRoom.getDoors().entrySet().iterator();
			while (iterator.hasNext()){
				Map.Entry<String, Door> entry = iterator.next();
				if (entry.getValue().gettype() == Door.doorType.wand){
					Door door = new Door(Door.doorType.tuer);
					currentRoom.setOneDoor(door, entry.getKey());
					break;
				}
			}
		}
		map = new LevelMap(currentRoom);
		attackCount = 0;
		finished = false;
		retreat= false;
		alcoholSips = 0;
		currentEnemy = null;
	}

	/**
	 * Main play routine. Loops until end of play, when finished is set to true
	 * Usage - play();
	 * 
	 * @throws Exception
	 */
	public void play() {
		while (!finished) {
			if (player.getHealth() > 0) {
				System.out.print("\033[H\033[2J");
				System.out.flush();
				System.out.println(player.getStats());
				System.out.println();
				System.out.println();
				if (player.isAttackMode()) {
					System.out.println(getAttacked());					
				} else {
					System.out.println(currentRoom.description());
				}
				System.out.println();
				System.out.println("What do you do?");
				Command command = parser.getCommand();
				System.out.println();
				System.out.println(processCommand(command));
				System.out.println();
				System.out.println("press enter to continue");
				Scanner input = new Scanner(System.in);
				input.nextLine();
			} else {
				System.out.println("You are dead and will soon turn into a zombie yourself.\n");
				finished = true;
			}
		}
		artgen.printTextArt("thx for playing", 16, ASCIIArtGenerator.ASCIIArtFont.ART_FONT_SERIF, "M");
		System.out.println("your Stats are as follows:");
		System.out.println(player.getStatistics());
		System.out.println("Your Score is: " + player.getMoney());
		System.out.println("press enter to continue");
		Scanner input = new Scanner(System.in);
		input.nextLine();
	}

	/**
	 * Print out the opening message for the player.
	 * Usage - printWelcome();
	 * 
	 * @throws Exception
	 */
	private void printWelcome() {
		artgen = new ASCIIArtGenerator();
		System.out.println();
		artgen.printTextArt("Mad Zork", 16, ASCIIArtGenerator.ASCIIArtFont.ART_FONT_SERIF, "M");
		System.out.println();
		System.out.println(
				"You are a lonely knight in search of the treasure of Darleshaar. After years of searching, you stumbled upon an old map.");
		System.out
				.println("A map that leads to the old and haunted manor of Darleshaar. On the inscription your read:");
		System.out.println(
				"In showers of gold and gemstones shall be the one who defeats the great monster guarding the entrance to the cellar!!!");
		System.out.println(
				"Only with a lot of struggle and suffering, some help and legendary heroic deeds you reach the manor of Darleshaar.");
		System.out.println();
		System.out.println("Mad Zork is a simple adventure game.");
		System.out.println("Type 'help' if you need help.");
		System.out.println();
		System.out.println("press enter to continue");
		Scanner input = new Scanner(System.in);
		input.nextLine();
	}

	/**
	 * Given a command, process (that is: execute) the command and returns a string
	 * with the result
	 * returned.
	 * Usage - processCommand(command);
	 * 
	 * @param command - Command
	 * @return - String
	 * @throws Exception
	 */
	private String processCommand(Command command) {
		if (command.isUnknown()) {
			return "I don't know what you mean...";
		}
		String commandWord = command.getCommandWord();
		if (commandWord.equals("help") || commandWord.equals("h")) {
			return printHelp();
		} else if (commandWord.equals("drink")) {
			return drink(command);
		} else if (commandWord.equals("go")) {
			return goRoom(command);
		} else if (commandWord.equals("explore") || commandWord.equals("e")) {
			return exploreRoom();
		} else if (commandWord.equals("attack") || commandWord.equals("a")) {
			return attackEnemy();
		} else if (commandWord.equals("retreat") || commandWord.equals("r")) {
			return retreat();
		} else if (commandWord.equals("map") || commandWord.equals("m")) {
			return map.showMap(currentRoom);
		} else if (commandWord.equals("drop")) {
			return dropItem(command);
		} else if (commandWord.equals("quit")) {
			if (command.hasSecondWord()) {
				return "Quit what?";
			} else {
				finished = true;
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * Consumes a potion from the player. Returns the resulting action as a string.
	 * Implements easterEgg
	 * Usage - drink(command);
	 * 
	 * @param command - Command
	 * @return - String
	 * @throws Exception
	 */
	private String drink(Command command) {
		if (command.hasSecondWord()) {
			if (command.getSecondWord().equals("potion")) {
				if (player.drinkPotion()) {
					return "You drink a potion\nimediately you feel how strenght comes back to your weary body";
				} else {
					return "You have no more potions left.";
				}
			} else if (command.getSecondWord().equals("alcohol")) {
				alcoholSips++;
				if (alcoholSips < 4) {
					return "You grab your secret stash of vodka and take a sip. It feels sooo great!";
				} else if (alcoholSips < 7) {
					player.takeDamage(2);
					return "You grab your secret stash of vodka and take a sip. It feels s0o0o00o great!";
				} else if (alcoholSips >= 7) {
					player.takeDamage(5);
					String retuString = "Y0u grub your pants stash of vodka und take a sliper. It feels s0o00000o00o great!";
					if (player.getPotions() > 0) {
						player.dropItem(Room.Item.potion);
						retuString += "\n Wh00psy, a flusk oef potion has sluuped 0eut uf your haund und broek in se flo00r";
					} else {
						retuString += "\n you run to the corner and vomit all over the place";
					}

					return retuString;
				} else {
					return "";
				}
			} else {
				return "You don't have any " + command.getSecondWord() + " to drink";
			}
		} else {
			return "Drink what?";
		}
	}

	/**
	 * Takes Item from Player and puts it in the room
	 * Usage - dropItem(command);
	 * 
	 * @param command - Command
	 * @return - String
	 * @throws Exception
	 */
	private String dropItem(Command command) {
		String returnString = "";
		if (command.hasSecondWord()) {
			if (command.getSecondWord().equals("potion")) {
				if (player.dropItem(Room.Item.potion) == null) {
					returnString = "you have no potions to drop";
				} else {
					Random random = new Random();
					if (random.nextInt(3) > 0) {
						currentRoom.giveAnItem(Room.Item.potion);
						returnString = "you have dropped a potion";
					} else {
						returnString = "as you take the potion out of your belt, it slips your fingers and drops on the floor. The flask is distroied and the health potion a puddle on the ground";
					}
				}
			} else if (command.getSecondWord().equals("key")) {
				if (player.dropItem(Room.Item.key) == null) {
					returnString = "you have no keys to drop";
				} else {
					currentRoom.giveAnItem(Room.Item.key);
					returnString = "you have dropped a key";
				}
			} 
			else if (command.getSecondWord().equals("money")){
				returnString = "are you crazy??????? how could you even suggest to throw away money?";
			}
			else {
				returnString = "You can't drop that";
			}
		} else {
			returnString = "drop what?";
		}
		return returnString;
	}

	/**
	 * Player selects a random direction and tries to go there.
	 * Returns the resulting action as a string.
	 * Usage - retreat();
	 * 
	 * @return - String
	 * @throws Exception
	 */
	private String retreat() {
		String returnString = "You close your eyes and turn in circles. After a while you start running forward.";
		String direction = "";
		int intDirection = ThreadLocalRandom.current().nextInt(1, 4 + 1);
		switch (intDirection) {
			case 1:
				direction = "north";
				break;
			case 2:
				direction = "east";
				break;
			case 3:
				direction = "south";
				break;
			case 4:
				direction = "west";
				break;
		}
		doorToPass = currentRoom.doorToPass(direction);
		if (doorToPass.gettype() == Door.doorType.wand || !doorToPass.getoffen()){
			if (player.isAttackMode()){
				returnString = "You try to run to the " + direction + ", but do not manage to escape";
			}
			else{
				returnString += " You run into a wall. Bravo! I bet there is a better way to go to the next room";
			}
		}
		else {
			Command command = new Command("go", direction);
			if (player.isAttackMode()){
				returnString = "You run to the " + direction + " and escape the ugly monster";
			}
			else{
				returnString += " To your amazament there is no wall to run into.";
			}
			retreat = true;
			returnString += goRoom(command);
			player.setAttackMode(false);
		}
		return returnString;
	}

	/**
	 * Explores the room. If there are enemies, they will attack you
	 * Returns the resulting action as a string.
	 * Usage - exploreRoom();
	 * 
	 * @return - String
	 * @throws Exception
	 */
	private String exploreRoom() {
		String returnString = "";
		currentEnemy = currentRoom.getAnEnemy();
		if (player.isAttackMode()) {
			return "You cant explore now! You are in the middle of a fight!!!!!";
		} else {
			if (currentEnemy == null) { // no enemies are in the room
				if (currentRoom.isExplored()){
					returnString = "You have already explored this room";
				}
				else{
					returnString = "You explore the room and find nothing of interest";
					currentRoom.setExplored(true);
					player.addRoomsExplored(1);
				}
				if (!currentRoom.containsItem()) {
					returnString = "There is nothing more to find here";
				} else {
					returnString = player.giveItem(currentRoom.getItems(), currentRoom);
				}
			} else {
				player.setAttackMode(true);
				returnString = "You woke up a monster from its sleep.\nslowly the Level " + currentEnemy.getLevel() + " " + currentEnemy.getType().toString()
						+ " moves towards you";
			}
		}
		return returnString;
	}

	private String getAttacked(){
		String returnString = "";
		returnString = currentEnemy.getAttackMove();
		int damage = player.defend(currentEnemy);
		if (damage > 0) {
			returnString += "\nYou take " + damage + " damage";
			player.addDamageTaken(damage);
		} else {
			returnString += "\nYou block the attack";
		}
		return returnString;
	}

	/**
	 * Attack an enemy
	 * Returns the resulting action as a string.
	 * Usage - attackEnemy();
	 * 
	 * @return - String
	 * @throws Exception
	 */
	private String attackEnemy() {
		if (player.isAttackMode()) {
			attackCount++;
			String returnString = "you swing your sword at the enemy";
			int damage = currentEnemy.defend(player);
			player.addDamageDealt(damage);
			if (currentEnemy.getHealth() > 0) {
				returnString = returnString + "\nyou do " + damage + " damage";
				returnString = returnString + "\nit has " + currentEnemy.getHealth() + " health left.";
			} else {
				currentRoom.killEnemy(currentEnemy);
				player.setAttackMode(false);
				returnString = returnString + "\nYour sword finnaly cuts off the " + currentEnemy.getType().toString()
						+ "'s head";
				returnString = returnString + player.giveXp(currentEnemy.getXp(attackCount));
				player.addNrOfAttacks(attackCount);
				player.addMonstersKilled(1);
				attackCount = 0;
				if (player.getHealth() <= 0) {
					player.setHealth(1);
					returnString = returnString + "\nThrough sheer willpower you manage to barely stay alive";
				}
			}
			return returnString;
		} else {
			return "there is nothing to attack";
		}
	}

	/**
	 * Returns some helpful information to the player.
	 * Usage - printHelp();
	 * 
	 * @return - String
	 * @throws Exception
	 */
	private String printHelp() {
		return "You will never find the way to the treasure!!\nMuhahahaha!\n\nYour command words are:\n"
				+ parser.showCommands();
	}

	/**
	 * Try to go to one direction.
	 * Returns the resulting action as a string.
	 * Usage - goRoom(command);
	 * 
	 * @param command - Command
	 * @return - String
	 * @throws Exception
	 */
	private String goRoom(Command command) {
		String returnstring = "";
		// if there is no second word, we don't know where to go...
		if (player.isAttackMode() && !retreat) {
			return "If you want to run like a chicken, you should use the command 'retreat'";
		} else {
			retreat = false;
			if (!command.hasSecondWord()) {
				return "Go where?\nYour options are:\n" + currentRoom.exitString();
			} else {
				String direction = command.getSecondWord();
				if (direction.equals("up")){
					direction = "north";
				}
				if (direction.equals("right")){
					direction = "east";
				}
				if (direction.equals("down")){
					direction = "south";
				}
				if (direction.equals("left")){
					direction = "west";
				}
				
				if (direction.equals("back")) { // go to the last visited room
					for (int i = 0; i < currentRoom.getAdjacentRooms().length; i++) {
						if (currentRoom.getAdjacentRooms()[i] == null || lastRoom == null) {
							return "there is no turning back";
						} else {
							if (currentRoom.getAdjacentRooms()[i].equals(lastRoom)) {
								switch (i) {
									case 0: // north
										direction = "north";
										break;
									case 1: // east
										direction = "east";
										break;
									case 2: // south
										direction = "south";
										break;
									case 3: // west
										direction = "west";
										break;
									default:
										direction = "eisengard";
								}
							}
						}
					}
				}
				doorToPass = currentRoom.doorToPass(direction);
				// invalid doors
				if (doorToPass == null){
					return "That is not a valid direction. Directions are 'north', 'south', 'east', 'west' and 'back'";
				}
				if (doorToPass.gettype() == Door.doorType.wand) {
					player.addWalkedInWalls(1);
					return "Like a sims you try to walk into the wall several times until you finally give up and turn around.";
				} else if (doorToPass == null) {
					return "That is not a valid direction. Directions are:\nnorth, east, south, west and back";
				}
				// valid doors
				else {
					if (!doorToPass.getoffen()) { // Door is closed
						if (doorToPass.gettype() == Door.doorType.opferTuer){
							System.out.println("The door has no keyhole or doorknob. Instead it has a slit only big enough for a coin"+
							"\nyou knock and in blood the number " + doorToPass.getKosten() + " appears."+
							"\nHow much money do you want to insert into the door?");
							Scanner input = new Scanner(System.in);
							System.out.print(">");
							String answer = input.next();
							try{
								if (answer.equals("none") || answer.equals("0")){
									return "You don't pay and turn around.";								
								}
								else{
									int money = Integer.parseInt(answer);
									if (money <0){
										throw new NumberFormatException();
									}
									if (player.getMoney() < money){
										return "You don't have that amount of money\nYou turn around";
									}
									else{
										int change = doorToPass.payDoor(money);
										player.setMoney(player.getMoney()-money+change);
										if (doorToPass.getoffen()){
											if (change > 0 ){
												returnstring = "The Door spits out a coin worth " + change + ". You pick it up.\n";
											}
											returnstring += "The door swings wide open. You pass through it";
										}
										else{
											return "That was not quiet enough";
										}
									}	
								}
							}
							catch(NumberFormatException ex){
								return "What does that even mean? Confused you turn around.";
							}
						}
						else{
							System.out.println("This door is locked.");
							System.out.println("Do you want to open it?");
							Scanner input = new Scanner(System.in);
							System.out.print(">");
							String answer = input.next();
							if (answer.equals("yes")) {
								if (doorToPass.gettype() == Door.doorType.falltuer) {
									return "You try to open the door, but it does not work! \nBut the door was open before, when you passed through it! \nWhat is happening here? Confused you turn around ";
								} 
								else {
									if (player.getKeys() < 1) {
										return "You can't open the door without a key. you turn around.";
									} else {
										doorToPass.openDoor();
										player.takeKey();										
										returnstring = "You open the door";
									}
								}
							} else if (answer.equals("no")) {
								return "you dont open the door and turn around";
							} else {
								return "confused on what to do you turn around";
							}
						}
					}
					if (doorToPass.gettype() == Door.doorType.falle) { // Door has a trap
						returnstring = returnstring + "\nThere's a trap!!!!";
						returnstring += "\n"+doorToPass.springTrap(player);
					}
					if (doorToPass.gettype() == Door.doorType.geheim) { // Door is secret
						returnstring = returnstring + "\nYou walk right through the wall!";
					}
					if (doorToPass.gettype() == Door.doorType.falltuer) { // Door closes behind you
						doorToPass.closeDoor();
						returnstring = returnstring + "\nYou hear a loud BANG after you pass throug the door";
					}
					currentRoom.setVisited(true);
					lastRoom = currentRoom;
					if (lastRoom.getRoom(direction)==null){
						currentRoom = new Room(player);
						lastRoom.setRoom(direction, currentRoom);
						currentRoom.setOneDoor(lastRoom.doorToPass(direction), getOppositeDirection(direction));
						currentRoom.setRoom(getOppositeDirection(direction), lastRoom);
					}
					else{
						currentRoom = lastRoom.getRoom(direction);
					}					
					//check if player has exit inn this room or the next one
					if (isPlayerStuck()){
						Iterator<Map.Entry<String, Door>> iterator = currentRoom.getDoors().entrySet().iterator();
						while (iterator.hasNext()){
							Map.Entry<String, Door> entry = iterator.next();
							if (entry.getValue().gettype() == Door.doorType.wand){
								Door door = new Door(Door.doorType.tuer);
								currentRoom.setOneDoor(door, entry.getKey());
								break;
							}
						}
					}
					
					if (returnstring == ""){
						returnstring = "\nYou walk through the door into the next room";
					}
					map.addRoom(currentRoom);
					return returnstring;
				}
			}
		}
	}
	private String getOppositeDirection(String direction){
		String oppositeDirection = "";
		switch (direction){
			case "north":
				oppositeDirection = "south";
				break;
			case "south":
				oppositeDirection = "north";
				break;
			case "east":
				oppositeDirection = "west";
				break;
			case "west":
				oppositeDirection = "east";
				break;
		}
		return oppositeDirection;
	}

	private boolean isPlayerStuck(){
		boolean roomDoorStuck = true;
		Iterator<Map.Entry<String, Door>> iterator = currentRoom.getDoors().entrySet().iterator();
		while (iterator.hasNext()){
			Map.Entry<String, Door> entry = iterator.next();
			if (entry.getValue().gettype() == Door.doorType.wand){
			}
			else if (entry.getValue().gettype() == Door.doorType.abgeschlossen && !entry.getValue().getoffen() && player.getKeys()==0){
			}
			else if (entry.getValue().gettype() == Door.doorType.falltuer && !entry.getValue().getoffen()){
			}
			else if (entry.getValue().gettype() == Door.doorType.opferTuer && !entry.getValue().getoffen() && player.getMoney()<entry.getValue().getKosten()){
			}
			else{ 
				if (currentRoom.getRoom(entry.getKey()) == null){roomDoorStuck = false;}
				else{									
					Room room2 = currentRoom.getRoom(entry.getKey());
					Iterator<Map.Entry<String, Door>> iterator2 = room2.getDoors().entrySet().iterator();
					while (iterator2.hasNext()){
						Map.Entry<String, Door> entry2 = iterator2.next();
						if (entry2.getValue().gettype() == Door.doorType.wand){
						}
						else if (entry2.getValue().gettype() == Door.doorType.abgeschlossen && !entry2.getValue().getoffen() && player.getKeys()==0){
						}
						else if (entry2.getValue().gettype() == Door.doorType.falltuer && !entry2.getValue().getoffen()){
						}
						else if (entry2.getValue().gettype() == Door.doorType.opferTuer && !entry2.getValue().getoffen() && player.getMoney()<entry2.getValue().getKosten()){
						}
						else{ 
							if (room2.getRoom(entry2.getKey()) == null){roomDoorStuck = false;}
							else if (entry2.getKey() == getOppositeDirection(entry.getKey())){}
							else{										
								Room room3 = room2.getRoom(entry2.getKey());
								Iterator<Map.Entry<String, Door>> iterator3 = room3.getDoors().entrySet().iterator();
								while (iterator3.hasNext()){
									Map.Entry<String, Door> entry3 = iterator3.next();
									if (entry3.getValue().gettype() == Door.doorType.wand){
									}
									else if (entry3.getValue().gettype() == Door.doorType.abgeschlossen && !entry3.getValue().getoffen() && player.getKeys()==0){
									}
									else if (entry3.getValue().gettype() == Door.doorType.falltuer && !entry3.getValue().getoffen()){
									}
									else if (entry3.getValue().gettype() == Door.doorType.opferTuer && !entry3.getValue().getoffen() && player.getMoney()<entry3.getValue().getKosten()){
									}
									else{
										roomDoorStuck = false;
									}												
								}
							}
						}
					}
				}		
			}
		}
		return roomDoorStuck;
	}
}