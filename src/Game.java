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

	private boolean finished = false;
	private Map map;
	private ASCIIArtGenerator artgen;
	private Parser parser;
	private Player player;
	private Room currentRoom;
	private Enemy currentEnemy;
	private int roomNr, alcoholSips = 0;
	private Room nextRoom, lastRoom;
	private Door doorToPass;
	private Room roomTreppe, roomDraussen, roomWand;
	private Room room1, room2, room3, room4, room5, room6, room7, room8, room9, room10, room11, room12, room13, room14,
			room15, room16, room17, room18, room19, room20, room21, room22, room23, room24, room25;
	private Door doorGeheim, doorNormal, doorWand, door12t18, door18t14, door14t11, door8t9, door9t3, door2t16,
			door6t20, door13t17, door10t15, door16t1, door21t24, door17t19, door19tTreppe;

	/**
	 * Create the game and initialise its internal map. initialises all the doors
	 * and the starting area
	 * Usage - Game();
	 * 
	 * @throws Exception
	 */
	public Game() {

		parser = new Parser();
		player = new Player();

		// initialise doors
		doorNormal = new Door(Door.doorType.tuer);
		doorGeheim = new Door(Door.doorType.geheim);
		doorWand = new Door(Door.doorType.wand);
		door12t18 = new Door(Door.doorType.falle);
		door6t20 = new Door(Door.doorType.falle);
		door16t1 = new Door(Door.doorType.falle);
		door21t24 = new Door(Door.doorType.falle);
		door2t16 = new Door(Door.doorType.falltuer);
		door13t17 = new Door(Door.doorType.falltuer);
		door18t14 = new Door(Door.doorType.abgeschlossen);
		door14t11 = new Door(Door.doorType.abgeschlossen);
		door8t9 = new Door(Door.doorType.abgeschlossen);
		door9t3 = new Door(Door.doorType.abgeschlossen);
		door10t15 = new Door(Door.doorType.abgeschlossen);
		door17t19 = new Door(Door.doorType.abgeschlossen);
		door19tTreppe = new Door(Door.doorType.abgeschlossen);

		// initialize all the starting rooms
		roomDraussen = new Room(player.getKeys(), 0,
				"You stand in front of the entrance of the huge mansion, or rather where the entrance should be. Unfortunately,",
				0, 1, 0);
		roomWand = new Room(player.getKeys(), -1, "", 0, 0, 0);
		room12 = new Room(player.getKeys(), 12,
				"Entering, you find yourself in a Laboratory, used to develop alchemical material transmutations. The walls are decorated with dark smears and stains. The floor is made of fitted shale bricks.",
				3, player.getLevel(), 0);
		// initialize all the starting doors and adacent rooms
		roomDraussen.setExits(roomDraussen, doorWand, roomDraussen, doorWand, roomDraussen, doorWand, room12,
				doorGeheim);
		roomDraussen.setExplored(true);
		roomDraussen.setVisited(true);
		// start game outside
		currentRoom = roomDraussen;
		map = new Map(currentRoom);
		currentEnemy = null;
	}

	/**
	 * Main play routine. Loops until end of play, when finished is set to true
	 * Usage - play();
	 * 
	 * @throws Exception
	 */
	public void play() {
		printWelcome();
		while (!finished) {
			if (player.getHealth() > 0) {
				System.out.print("\033[H\033[2J");
				System.out.flush();
				System.out.println(player.getStats());
				System.out.println();
				System.out.println();
				// Attack mode
				if (player.isAttackMode()) {
					System.out.println(currentEnemy.getAttackMove());
					int damage = player.defend(currentEnemy.getAttack());
					if (damage > 0) {
						System.out.println("You take " + damage + " damage");
					} else {
						System.out.println("You manage to dodge the attack");
					}
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
		if (commandWord.equals("help")) {
			return printHelp();
		} else if (commandWord.equals("drink")) {
			return drink(command);
		} else if (commandWord.equals("go")) {
			return goRoom(command);
		} else if (commandWord.equals("explore")) {
			return exploreRoom();
		} else if (commandWord.equals("attack")) {
			return attackEnemy();
		} else if (commandWord.equals("retreat")) {
			return retreat();
		} else if (commandWord.equals("map")) {
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
		int intDirection = getRandomNumber(1, 4);
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
		nextRoom = currentRoom.nextRoom(direction);
		doorToPass = currentRoom.doorToPass(direction);
		if (nextRoom == roomWand || nextRoom == null || nextRoom == roomDraussen || nextRoom == roomTreppe
				|| !doorToPass.getoffen()) {
					if (player.isAttackMode()){
						returnString = "you try to run to the " + direction + ", but do not manage to escape";
					}
					else{
						returnString += " You run into a wall. Bravo! I bet there is a better way to go to the next room";
					}
		} else {
			Command command = new Command("go", direction);
			if (player.isAttackMode()){
				returnString = returnString + "you run to the " + direction + " to escape the ugly monster";
			}
			else{
				returnString += " To your amazament there is no wall to run into.";
			}
			returnString += goRoom(command);
			player.setAttackMode(false);
		}
		return returnString;
	}

	/**
	 * returns a random number between a lower and an upper bound
	 * Usage - getRandomNumber(12, 20);
	 * 
	 * @param lowerBound - int
	 * @param upperBound - int
	 * @return - int
	 * @throws Exception
	 */
	private int getRandomNumber(int lowerBound, int upperBound) {
		int randomNumber = ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
		return randomNumber;
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
			String returnString = "you swing your sword at the enemy";
			int damage = currentEnemy.defend(player.getAttack());
			if (currentEnemy.getHealth() > 0) {
				returnString = returnString + "\nyou do " + damage + " damage";
				returnString = returnString + "\nit has " + currentEnemy.getHealth() + " health left.";
			} else {
				currentRoom.killEnemy(currentEnemy);
				player.setAttackMode(false);
				returnString = returnString + "\nYour sword finnaly cuts off the " + currentEnemy.getType().toString()
						+ "'s' head";
				returnString = returnString + player.giveXp(currentEnemy.getLevel() * 30);
				if (player.getHealth() <= 0) {
					player.setHealth(1);
					returnString = returnString + "\nthrough sheer willpower you manage to barely stay alive";
				}
			}
			return returnString;
		} else {
			return "there is nothing to attack";
		}
	}

	/**
	 * explores the room. If there are enemies, they will attack you
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
				currentRoom.setExplored(true);
				Room.Item item = currentRoom.getAnItem();
				if (item == null) {
					returnString = "there is nothing more to find here";
				} else {
					if (item == Room.Item.gold || item == Room.Item.silver || item == Room.Item.bronze) {
						returnString = "you have found a " + item.toString() + " coin";
					} else {
						returnString = "you have found a " + item.toString();
					}
					returnString += "\nDo you want to pick it up?";
					System.out.println(returnString);
					Scanner input = new Scanner(System.in);
					System.out.print(">");
					String answer = input.next();
					if (answer.equals("yes")) {
						if (item == Room.Item.gold || item == Room.Item.silver || item == Room.Item.bronze) {
							returnString = "you take the " + item.toString() + " coin and put it in your huge backpack.";
							player.giveItem(item);
						} else {
							if (player.getRemainingCarryCapacity() > 0) {
								returnString = "you take the " + item.toString();
								returnString += " and put it in your belt";
								player.giveItem(item);
							} else {
								returnString = "You carry too much on your belt. drop a key or potion to free up space";
								currentRoom.giveAnItem(item);
							}
						}
					} else {
						if (item == Room.Item.gold || item == Room.Item.silver || item == Room.Item.bronze) {
							returnString = "you leave the " + item.toString() + " coin";
						} else {
							returnString = "you leave the " + item.toString();
						}
						returnString += " where it lies";
						currentRoom.giveAnItem(item);
					}
				}
			} else {
				player.setAttackMode(true);
				returnString = "you woke up a monster from its sleep.\nslowly the " + currentEnemy.getType().toString()
						+ " moves towards you";
			}
		}
		return returnString;
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
		if (player.isAttackMode()) {
			return "If you want to run like a chicken, you should use the command 'retreat'";
		} else {
			if (!command.hasSecondWord()) {
				return "Go where?\nYour options are:\n" + currentRoom.exitString();
			} else {
				String direction = command.getSecondWord();
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
				nextRoom = currentRoom.nextRoom(direction);
				doorToPass = currentRoom.doorToPass(direction);
				// invalid rooms
				if (nextRoom == roomWand) {
					return "Like a sims you try to walk into the wall several times until you finally give up and turn around.";
				} else if (nextRoom == roomDraussen) {
					return "You really don't want to go there. I'mean its where you come from. Where is the adventure in that?";
				} else if (nextRoom == null) {
					return "That is not a valid direction. Directions are:\nnorth, east, south, west and back";
				}
				// valid rooms
				else {
					if (!doorToPass.getoffen()) { // Door is closed
						System.out.println("This door is closed.");
						System.out.println("Do you want to open it?");
						Scanner input = new Scanner(System.in);
						System.out.print(">");
						String answer = input.next();
						if (answer.equals("yes")) {
							if (doorToPass.gettype() == Door.doorType.falltuer) {
								return "You try to open the door, but it does not work! \nBut the door was open before, when you passed through it! \nWhat is happening here? Confused you turn around ";
							} else {
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
					if (doorToPass.gettype() == Door.doorType.falle) { // Door has a trap
						returnstring = returnstring + "\nThere's a trap!!!!";
						int evasion = ThreadLocalRandom.current().nextInt(0, player.getDefense() + 1);
						if (evasion < doorToPass.getSchaden()) {
							int damage = doorToPass.getSchaden() - evasion; // check if player gets damage
							player.takeDamage(damage);
							returnstring = returnstring + "\nyou Take " + damage + " damage";
							if (player.getHealth() <= 0) {
								returnstring = returnstring
										+ "\nYou are impaled and can not get free! Slowly you bleed out";
								return returnstring;
							}
						} else {
							returnstring = returnstring + "\nYou are lucky and take no damage";
						}
					}
					if (doorToPass.gettype() == Door.doorType.geheim) { // Door is secret
						returnstring = returnstring + "\nYou walk right through the wall!";
					}
					if (doorToPass.gettype() == Door.doorType.falltuer) { // Door closes behind you
						doorToPass.closeDoor();
						returnstring = returnstring + "\nYou hear a loud BANG after you pass throug the door";
					}
					if (nextRoom == roomTreppe) { // Next Room is Victory
						currentRoom = nextRoom;
						finished = true;
						return "Congratulation! you found the way to the cellar.\nAs you walk down, you smell a weird stench rising up. You open The door at the buttom of the stairs and see a huge pile of gold."
								+
								"\nfinally I get what I deserve! you shout out and make a small jump of joy. You run towards the money. As soon as you reach it you hear a puff and it all vanishes into thick smoke."
								+
								"\n It is dark and you can barely breathe. You try to find the exit but have lost all orientation. You slowly get tired and finally you fall to the ground and into a deep sleep.\n\n"
								+
								"Whatever happened to the young knight is unknown until this day. Tales of the knights heroism and greed are still told to this day";
					} else {
						currentRoom.setVisited(true);
						lastRoom = currentRoom;
						currentRoom = nextRoom;
						roomNr = currentRoom.getRoomNr();
						switch (roomNr) {// initialise adjacent rooms and doors
							case 1:
								if (room16 == null) {
									room16 = new Room(player.getKeys(), 16,
											"Entering, you find yourself in a Workshop, intended for the crafting of electronics. The walls are decorated with illustrations. The main scene is of a krayt dragon and a wyvern embracing an adiaphorism. The action is set in a corrupted dining hall. The piece is formed entirely out of tiny, colored beads.  The floor is made of unsanded rock.",
											3, player.getLevel(), 3);
								}
								if (room4 == null) {
									room4 = new Room(player.getKeys(), 4,
											"Entering, you find yourself in a Chapel, dedicated to Sheela Peryoryl, the Green Sister, with an altarpiece focused on the afterlife. The walls are decorated with dark smears and stains. The floor is made of smooth-sanded granite.",
											3, player.getLevel(), 3);
								}
								room1.setExits(roomWand, doorWand, room16, door16t1, roomWand, doorWand, room4,
										doorNormal);
								break;
							case 2:
								if (room16 == null) {
									room16 = new Room(player.getKeys(), 16,
											"Entering, you find yourself in a Workshop, intended for the crafting of electronics. The walls are decorated with illustrations. The main scene is of a krayt dragon and a wyvern embracing an adiaphorism. The action is set in a corrupted dining hall. The piece is formed entirely out of tiny, colored beads.  The floor is made of unsanded rock.",
											3, player.getLevel(), 3);
								}
								if (room3 == null) {
									room3 = new Room(player.getKeys(), 3,
											"Entering, you find yourself in a Barn, designed to house and feed camels. The walls are decorated with cracked triangular windows. The floor is made of warm, throbbing flesh.",
											3, player.getLevel(), 3);
								}
								room2.setExits(roomWand, doorWand, room3, doorNormal, roomWand, doorWand, room16,
										door2t16);
								break;
							case 3:
								if (room2 == null) {
									room2 = new Room(player.getKeys(), 2,
											"Entering, you find yourself in a Museum, dedicated to ancient tomes of Theology. The walls are decorated with faded and rotted tapestries. The floor is made of rough soil.",
											3, player.getLevel(), 3);
								}
								if (room9 == null) {
									room9 = new Room(player.getKeys(), 9,
											"Entering, you find yourself in a Storeroom, designed to keep and preserve candles and lantern oil. The walls are decorated with bookshelves with tomes of Angelology. The floor is made of fitted granite flagstones.",
											3, player.getLevel(), 3);
								}
								room3.setExits(roomWand, doorWand, roomWand, doorWand, room9, door9t3, room2,
										doorNormal);
								break;
							case 4:
								if (room1 == null) {
									room1 = new Room(player.getKeys(), 1,
											"Entering, you find yourself in a Training Room, focused on mage training. The walls are decorated with bookshelves with texts of Literature. The floor is made of unsanded rock.",
											3, player.getLevel(), 3);
								}
								if (room10 == null) {
									room10 = new Room(player.getKeys(), 10,
											"Entering, you find yourself in a Room of sinister and unclear purpose. Various furniture are all oriented towards a weird lifelike sculpture of a winged child in burnished brass. The walls are decorated with a plaque on the wall with the warning: 'The cantharidal hyena shall soon discase to the instance.'. The floor is made of fitted granite flagstones.",
											3, player.getLevel(), 3);
								}
								room4.setExits(roomWand, doorWand, room1, doorNormal, room10, doorGeheim, roomWand,
										doorWand);
								break;
							case 5:
								if (room16 == null) {
									room16 = new Room(player.getKeys(), 16,
											"Entering, you find yourself in a Workshop, intended for the crafting of electronics. The walls are decorated with illustrations. The main scene is of a krayt dragon and a wyvern embracing an adiaphorism. The action is set in a corrupted dining hall. The piece is formed entirely out of tiny, colored beads.  The floor is made of unsanded rock.",
											3, player.getLevel(), 3);
								}
								room5.setExits(room16, doorNormal, roomWand, doorWand, roomWand, doorWand, roomWand,
										doorWand);
								break;
							case 6:
								if (room15 == null) {
									room15 = new Room(player.getKeys(), 15,
											"Entering, you find yourself in a Office, designed for moneylenders. The walls are decorated with a plaque on the wall with the warning: 'The standard summa shall soon recapitulate to the oligopoly.'. The floor is made of seated tiles of iron.",
											3, player.getLevel(), 3);
								}
								if (room7 == null) {
									room7 = new Room(player.getKeys(), 7,
											"Entering, you find yourself in a Storeroom, designed to keep and preserve gold and gems - a treasury. The walls are decorated with bookshelves with texts of Caprine and Ovine Husbandry. The floor is made of warm, throbbing flesh.",
											6, player.getLevel(), 3);
								}
								if (room20 == null) {
									room20 = new Room(player.getKeys(), 20,
											"Entering, you find yourself in a Kitchen, which has clearly been used to cook with nuts and rice. The walls are decorated with bookshelves with tomes of Abjuration. The floor is made of seated tiles of iron.",
											3, player.getLevel(), 3);
								}
								room6.setExits(room15, doorNormal, roomWand, doorWand, room20, door6t20, room7,
										doorGeheim);
								break;
							case 7:
								if (room6 == null) {
									room6 = new Room(player.getKeys(), 6,
											"Entering, you find yourself in a Bathrooms, richly appointed. The walls are decorated with mosaics. The piece is of a xenomorph consumed by hope. The scene occurs in the basement of a smooth building.  The floor is made of smooth-sanded marble.",
											3, player.getLevel(), 3);
								}
								room7.setExits(roomWand, doorWand, room6, doorGeheim, roomWand, doorWand, roomWand,
										doorWand);
								break;
							case 8:
								if (room9 == null) {
									room9 = new Room(player.getKeys(), 9,
											"Entering, you find yourself in a Storeroom, designed to keep and preserve candles and lantern oil. The walls are decorated with bookshelves with tomes of Angelology. The floor is made of fitted granite flagstones.",
											3, player.getLevel(), 3);
								}
								if (room12 == null) {
									room12 = new Room(player.getKeys(), 12,
											"Entering, you find yourself in a Laboratory, used to develop alchemical material transmutations. The walls are decorated with dark smears and stains. The floor is made of fitted shale bricks.",
											3, player.getLevel(), 0);
								}
								room8.setExits(roomWand, doorWand, room9, door8t9, room12, doorNormal, roomWand,
										doorWand);
								break;
							case 9:
								if (room3 == null) {
									room3 = new Room(player.getKeys(), 3,
											"Entering, you find yourself in a Barn, designed to house and feed camels. The walls are decorated with cracked triangular windows. The floor is made of warm, throbbing flesh.",
											3, player.getLevel(), 3);
								}
								if (room8 == null) {
									room8 = new Room(player.getKeys(), 8,
											"Entering, you find yourself in a Barracks, built to house assassins. The walls are decorated with long, gouged-out claw-marks. The floor is made of fitted shale bricks.",
											3, player.getLevel(), 3);
								}
								room9.setExits(room3, door9t3, roomWand, doorWand, roomWand, doorWand, room8, door8t9);
								break;
							case 10:
								if (room4 == null) {
									room4 = new Room(player.getKeys(), 4,
											"Entering, you find yourself in a Chapel, dedicated to Sheela Peryoryl, the Green Sister, with an altarpiece focused on the afterlife. The walls are decorated with dark smears and stains. The floor is made of smooth-sanded granite.",
											3, player.getLevel(), 3);
								}
								if (room13 == null) {
									room13 = new Room(player.getKeys(), 13,
											"Entering, you find yourself in a Dining hall, built for use by the lowest servants. The walls are decorated with paintings. The image exhibits a squadron of hunter sharks constructing a building. The scene is set in a decrepit kitchen. The piece is formed entirely out of tiny seashells.  The floor is made of rough, solid granite.",
											3, player.getLevel(), 3);
								}
								if (room15 == null) {
									room15 = new Room(player.getKeys(), 15,
											"Entering, you find yourself in a Office, designed for moneylenders. The walls are decorated with a plaque on the wall with the warning: 'The standard summa shall soon recapitulate to the oligopoly.'. The floor is made of seated tiles of iron.",
											3, player.getLevel(), 3);
								}
								room10.setExits(room4, doorGeheim, room13, doorNormal, room15, door10t15, roomWand,
										doorWand);
								break;
							case 11:
								if (room14 == null) {
									room14 = new Room(player.getKeys(), 14,
											"Entering, you find yourself in a Training Room, focused on sparring. The walls are decorated with a plaque on the wall with the saying: 'Never turn-down the aviculture.'. The floor is made of fitted sandstone bricks.",
											3, 10, 3);
								}
								room11.setExits(roomWand, doorWand, room14, door14t11, roomWand, doorWand, roomWand,
										doorWand);
								break;
							case 12:
								if (room8 == null) {
									room8 = new Room(player.getKeys(), 8,
											"Entering, you find yourself in a Barracks, built to house assassins. The walls are decorated with long, gouged-out claw-marks. The floor is made of fitted shale bricks.",
											3, player.getLevel(), 3);
								}
								if (room18 == null) {
									room18 = new Room(player.getKeys(), 18,
											"Entering, you find yourself in a Barracks, built to house archers. The walls are decorated with paintings. The main image displays a filial rove. The action is set in a richly decorated operations center. It's signed W.L. The floor is made of smooth-sanded sandstone.",
											3, player.getLevel(), 3);
								}
								room12.setExits(room8, doorNormal, roomDraussen, doorGeheim, room18, door12t18,
										roomWand,
										doorWand);
								break;
							case 13:
								if (room10 == null) {
									room10 = new Room(player.getKeys(), 10,
											"Entering, you find yourself in a Room of sinister and unclear purpose. Various furniture are all oriented towards a weird lifelike sculpture of a winged child in burnished brass. The walls are decorated with a plaque on the wall with the warning: 'The cantharidal hyena shall soon discase to the instance.'. The floor is made of fitted granite flagstones.",
											3, player.getLevel(), 3);
								}
								if (room17 == null) {
									room17 = new Room(player.getKeys(), 17,
											"Entering, you find yourself in a Hospital, clearly designed for an epidemic of mental illness. The walls are decorated with bookshelves with texts of contrition of Corellon, Father of Elves. The floor is made of smooth-sanded granite.",
											3, 10, 3);
								}
								room13.setExits(roomWand, doorWand, roomWand, doorWand, room17, door13t17, room10,
										doorNormal);
								break;
							case 14:
								if (room11 == null) {
									room11 = new Room(player.getKeys(), 11,
											"Entering, you find yourself in a Classroom, which once taught Hydrology. There are many desks and chairs. The walls are decorated with a plaque on the wall with the saying: 'The line of the tike shall curve where the pessimal computation fords.'. The floor is made of rough, solid granite.",
											10, player.getLevel(), 3);
								}
								if (room18 == null) {
									room18 = new Room(player.getKeys(), 18,
											"Entering, you find yourself in a Barracks, built to house archers. The walls are decorated with paintings. The main image displays a filial rove. The action is set in a richly decorated operations center. It's signed W.L. The floor is made of smooth-sanded sandstone.",
											3, player.getLevel(), 3);
								}
								room14.setExits(roomWand, doorWand, room18, door18t14, roomWand, doorWand, room11,
										door14t11);
								break;
							case 15:
								if (room10 == null) {
									room10 = new Room(player.getKeys(), 10,
											"Entering, you find yourself in a Room of sinister and unclear purpose. Various furniture are all oriented towards a weird lifelike sculpture of a winged child in burnished brass. The walls are decorated with a plaque on the wall with the warning: 'The cantharidal hyena shall soon discase to the instance.'. The floor is made of fitted granite flagstones.",
											3, player.getLevel(), 3);
								}
								if (room6 == null) {
									room6 = new Room(player.getKeys(), 6,
											"Entering, you find yourself in a Bathrooms, richly appointed. The walls are decorated with mosaics. The piece is of a xenomorph consumed by hope. The scene occurs in the basement of a smooth building.  The floor is made of smooth-sanded marble.",
											3, player.getLevel(), 3);
								}
								room15.setExits(room10, door10t15, roomWand, doorWand, room6, doorNormal, roomWand,
										doorWand);
								break;
							case 16:
								if (room1 == null) {
									room1 = new Room(player.getKeys(), 1,
											"Entering, you find yourself in a Training Room, focused on mage training. The walls are decorated with bookshelves with texts of Literature. The floor is made of unsanded rock.",
											3, player.getLevel(), 3);
								}
								if (room2 == null) {
									room2 = new Room(player.getKeys(), 2,
											"Entering, you find yourself in a Museum, dedicated to ancient tomes of Theology. The walls are decorated with faded and rotted tapestries. The floor is made of rough soil.",
											3, player.getLevel(), 3);
								}
								if (room5 == null) {
									room5 = new Room(player.getKeys(), 5,
											"Entering, you find yourself in a Meeting Room, designed for gambling. The walls are decorated with illustrations. The image depicts an affiliation of greed demons assembled in a firing squad, executing a prisoner. The scene takes place in an enclouded operations center. The piece bears small white moons in opposite sides.  The floor is made of rough, solid granite.",
											3, player.getLevel(), 3);
								}
								room16.setExits(roomWand, doorWand, room2, door2t16, room5, doorNormal, room1,
										door16t1);
								break;
							case 17:
								if (room13 == null) {
									room13 = new Room(player.getKeys(), 13,
											"Entering, you find yourself in a Dining hall, built for use by the lowest servants. The walls are decorated with paintings. The image exhibits a squadron of hunter sharks constructing a building. The scene is set in a decrepit kitchen. The piece is formed entirely out of tiny seashells.  The floor is made of rough, solid granite.",
											3, player.getLevel(), 3);
								}
								if (room19 == null) {
									room19 = new Room(player.getKeys(), 19,
											"Entering, you find yourself in a Market, a large room for trading of bulk food. The walls are decorated with dark smears and stains. The floor is made of rough soil.",
											3, player.getLevel() + 3, 3);
								}
								room17.setExits(room13, door13t17, room19, door17t19, roomWand, doorWand, roomWand,
										doorWand);
								break;
							case 18:
								if (room12 == null) {
									room12 = new Room(player.getKeys(), 12,
											"Entering, you find yourself in a Laboratory, used to develop alchemical material transmutations. The walls are decorated with dark smears and stains. The floor is made of fitted shale bricks.",
											3, player.getLevel(), 0);
								}
								if (room14 == null) {
									room14 = new Room(player.getKeys(), 14,
											"Entering, you find yourself in a Training Room, focused on sparring. The walls are decorated with a plaque on the wall with the saying: 'Never turn-down the aviculture.'. The floor is made of fitted sandstone bricks.",
											3, 10, 3);
								}
								if (room21 == null) {
									room21 = new Room(player.getKeys(), 21,
											"Entering, you find yourself in a Forge, with molds and casts specifically for crafting weapons. The walls are decorated with etchings on Daggers. The main image reveals great psychic battles , all fictional. The piece is outlined in red.  The floor is made of fitted slate flagstones.",
											3, player.getLevel(), 3);
								}
								room18.setExits(room12, door12t18, roomWand, doorWand, room21, doorNormal, room14,
										door18t14);
								break;
							case 19:
								if (room17 == null) {
									room17 = new Room(player.getKeys(), 17,
											"Entering, you find yourself in a Hospital, clearly designed for an epidemic of mental illness. The walls are decorated with bookshelves with texts of contrition of Corellon, Father of Elves. The floor is made of smooth-sanded granite.",
											3, 10, 3);
								}
								if (roomTreppe == null) {
									roomTreppe = new Room(player.getKeys(), -1,
											"Entering, you find yourself in a Stairwell leading towards the basement. This is It! The Treasure must be down there, you think. ",
											0, 1, 0);
								}
								room19.setExits(roomWand, doorWand, roomTreppe, door19tTreppe, roomWand, doorWand,
										room17,
										door17t19);
								break;
							case 20:
								if (room6 == null) {
									room6 = new Room(player.getKeys(), 6,
											"Entering, you find yourself in a Bathrooms, richly appointed. The walls are decorated with mosaics. The piece is of a xenomorph consumed by hope. The scene occurs in the basement of a smooth building.  The floor is made of smooth-sanded marble.",
											3, player.getLevel(), 3);
								}
								if (room22 == null) {
									room22 = new Room(player.getKeys(), 22,
											"Entering, you find yourself in a Museum, dedicated to relics of the masters of Conjuration. The walls are decorated with decorated tapestries. The piece displays a weeping mezzoloth conversing with a undead tiger. The scene occurs in a windy jungle. A dizzying hypostyle is visible in the distance. The floor is made of fitted red bricks.",
											3, player.getLevel(), 3);
								}
								room20.setExits(room6, door6t20, room22, doorNormal, roomWand, doorWand, roomWand,
										doorWand);
								break;
							case 21:
								if (room18 == null) {
									room18 = new Room(player.getKeys(), 18,
											"Entering, you find yourself in a Barracks, built to house archers. The walls are decorated with paintings. The main image displays a filial rove. The action is set in a richly decorated operations center. It's signed W.L. The floor is made of smooth-sanded sandstone.",
											3, player.getLevel(), 3);
								}
								if (room24 == null) {
									room24 = new Room(player.getKeys(), 24,
											"Entering, you find yourself in a Bathrooms, filthy and crumbling. The walls are decorated with frosted triangular windows. The floor is made of seated tiles of iron.",
											3, player.getLevel(), 3);
								}
								room21.setExits(room18, doorNormal, roomWand, doorWand, roomWand, doorWand, room24,
										door21t24);
								break;
							case 22:
								if (room20 == null) {
									room20 = new Room(player.getKeys(), 20,
											"Entering, you find yourself in a Kitchen, which has clearly been used to cook with nuts and rice. The walls are decorated with bookshelves with tomes of Abjuration. The floor is made of seated tiles of iron.",
											3, player.getLevel(), 3);
								}
								if (room24 == null) {
									room24 = new Room(player.getKeys(), 24,
											"Entering, you find yourself in a Bathrooms, filthy and crumbling. The walls are decorated with frosted triangular windows. The floor is made of seated tiles of iron.",
											3, player.getLevel(), 3);
								}
								room22.setExits(roomWand, doorWand, room24, doorNormal, roomWand, doorWand, room20,
										doorNormal);
								break;
							case 23:
								if (room25 == null) {
									room25 = new Room(player.getKeys(), 25,
											"Entering, you find yourself in a Guard Post, with a focus on explosives. The walls are decorated with a plaque on the wall with the epigram: 'Alchemise until the paragenetic self-heal gangrenes.'. The floor is made of rough soil.",
											3, player.getLevel(), 3);
								}
								room23.setExits(roomWand, doorWand, roomWand, doorWand, roomWand, doorWand, room25,
										doorGeheim);
								break;
							case 24:
								if (room21 == null) {
									room21 = new Room(player.getKeys(), 21,
											"Entering, you find yourself in a Forge, with molds and casts specifically for crafting weapons. The walls are decorated with etchings on Daggers. The main image reveals great psychic battles , all fictional. The piece is outlined in red.  The floor is made of fitted slate flagstones.",
											3, player.getLevel(), 3);
								}
								if (room22 == null) {
									room22 = new Room(player.getKeys(), 22,
											"Entering, you find yourself in a Museum, dedicated to relics of the masters of Conjuration. The walls are decorated with decorated tapestries. The piece displays a weeping mezzoloth conversing with a undead tiger. The scene occurs in a windy jungle. A dizzying hypostyle is visible in the distance. The floor is made of fitted red bricks.",
											3, player.getLevel(), 3);
								}
								if (room25 == null) {
									room25 = new Room(player.getKeys(), 25,
											"Entering, you find yourself in a Guard Post, with a focus on explosives. The walls are decorated with a plaque on the wall with the epigram: 'Alchemise until the paragenetic self-heal gangrenes.'. The floor is made of rough soil.",
											3, player.getLevel(), 3);
								}
								room24.setExits(roomWand, doorWand, room21, door21t24, room25, doorGeheim, room22,
										doorNormal);
								break;
							case 25:
								if (room23 == null) {
									room23 = new Room(player.getKeys(), 23,
											"Entering, you find yourself in a Jail, which clearly emphasizes religious fanaticism. Two prisoners are here, begging for escape, though only 1 is innocent. The walls are decorated with bookshelves with texts of divinity of Torm, the Hand of Righteousness. The floor is made of wooden boards.",
											3, player.getLevel(), 3);
								}
								if (room24 == null) {
									room24 = new Room(player.getKeys(), 24,
											"Entering, you find yourself in a Bathrooms, filthy and crumbling. The walls are decorated with frosted triangular windows. The floor is made of seated tiles of iron.",
											3, player.getLevel(), 3);
								}
								room25.setExits(room24, doorGeheim, room23, doorGeheim, roomWand, doorWand, roomWand,
										doorWand);
								break;
						}
						map.addRoom(currentRoom);
						return returnstring;
					}
				}
			}
		}
	}
}