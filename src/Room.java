/*
 * Class Room - a room in an adventure game.
 *
 *
 * Author: Jonathan Haimann
 * Version: 1.1
 * Date:    07.07.2022
 * 
 * 
 * This class is part of Zork. Zork is a simple, text based adventure game.
 *
 * "Room" represents one location in the scenery of the game.  It is 
 * connected to at most four other rooms via exits.  The exits are labelled
 * north, east, south, west.  For each direction, the room stores a reference
 * to the neighbouring room, or null if there is no exit in that direction.
 */

import java.util.HashMap;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

public class Room {

	private Random random;
	private String description;
	private int roomNr;
	private boolean visited;
	private boolean explored;
	private HashMap<String, Entry<Room, Door>> exits; // stores exits of this room.
	private Enemy enemies[];
	private Item items[];
	private int keyAmount;

	enum Item {
		key,
		potion,
		bronze,
		silver,
		gold
	}

	/**
	 * Create a room described "description". Initially, it has no exits.
	 * "description" is something like "a kitchen" or "an open court yard".
	 */
	public Room(int keyAmount, Integer roomNr, String description, int maxItems, int enemyMaxLevel,
			int maxEnemyamount) {
		int randomItemAmount = 0;
		visited = false;
		this.keyAmount = keyAmount;
		this.roomNr = roomNr;
		this.description = description;
		this.exits = new HashMap<String, Entry<Room, Door>>();
		//initial room has always 3 items (at least 1 key)
		if (roomNr == 12){
			randomItemAmount = 3;
		}
		else{
			randomItemAmount = ThreadLocalRandom.current().nextInt(0, maxItems + 1);
		}
		items = new Item[randomItemAmount];
		setItems(randomItemAmount);

		int randomEnemyAmount = ThreadLocalRandom.current().nextInt(0, maxEnemyamount + 1);
		enemies = new Enemy[randomEnemyAmount];
		setEnemies(randomEnemyAmount, enemyMaxLevel);
	}

	/**
	 * Put random enemies in the Room
	 */
	private void setEnemies(int randomAmount, int maxLevel) {
		for (int i = 0; i < randomAmount; i++) {
			Enemy.Enemytype enemyType = selectRandomEnemyType();
			int randomLevel = ThreadLocalRandom.current().nextInt(1, maxLevel + 1);
			Enemy enemy = new Enemy(randomLevel, enemyType);
			enemies[i] = enemy;
		}
	}

	private Enemy.Enemytype selectRandomEnemyType() {
		random = new Random();
		Enemy.Enemytype enemyList[] = Enemy.Enemytype.values();
		return enemyList[random.nextInt(enemyList.length)];
	}

	/**
	 * Put random items in the Room
	 */
	private void setItems(int randomAmount) {
		if (randomAmount > 0) {
			if (keyAmount < 1) {
				items[0] = Item.key;
				for (int i = 1; i < randomAmount; i++) {
					Item item = selectRandomItem();
					items[i] = item;
				}
			} else {
				for (int i = 0; i < randomAmount; i++) {
					Item item = selectRandomItem();
					items[i] = item;
				}
			}
		}
	}

	private Item selectRandomItem() {
		random = new Random();
		Item itemList[] = Item.values();
		return itemList[random.nextInt(itemList.length)];
	}

	/**
	 * Define the exits of this room. Every direction either leads to
	 * another room or is null (no exit there).
	 */
	public void setExits(Room roomNorth, Door doorNorth, Room roomEast, Door doorEast, Room roomSouth, Door doorSouth,
			Room roomWest, Door doorWest) {
		if (roomNorth != null) {
			exits.put("north", new SimpleEntry(roomNorth, doorNorth));
		}
		if (roomEast != null) {
			exits.put("east", new SimpleEntry(roomEast, doorEast));
		}
		if (roomSouth != null) {
			exits.put("south", new SimpleEntry(roomSouth, doorSouth));
		}
		if (roomWest != null) {
			exits.put("west", new SimpleEntry(roomWest, doorWest));
		}
	}

	/**
	 * Return the description of the room (the one that was defined in the
	 * constructor).
	 */
	public String shortDescription() {
		return description;
	}

	/**
	 * Return a long description of this room, on the form:
	 * You are in the kitchen.
	 * Exits: north west
	 */
	public String longDescription() {
		if (visited) {
			return "This looks familiar \n" + description + "\n" + exitString();
		} else {
			return description + "\n" + exitString();
		}
	}

	/**
	 * Return a string describing the room's exits, for example
	 * "Exits: north west ".
	 */
	public String exitString() {
		String returnString = "";
		if (explored) {
			returnString = "You See no Doors";
			Integer nrOfExits = 0;
			for (String key : exits.keySet()) {
				if (exits.get(key).getValue().gettype() != Door.doorType.geheim
						&& exits.get(key).getValue().gettype() != Door.doorType.wand) {
					nrOfExits++;
				}
			}
			if (nrOfExits > 0) {
				returnString = "The doors you see are:";
				for (String key : exits.keySet()) {
					if (exits.get(key).getValue().gettype() != Door.doorType.geheim
							&& exits.get(key).getValue().gettype() != Door.doorType.wand) {
						returnString += " " + key;
					}
				}
			}

		} else {
			returnString = "You dont know this room. You need to explore it first";
		}
		return returnString;
	}

	public Room[] getAdjacentRooms() {
		Room adjacentRooms[] = { nextRoom("north"), nextRoom("east"), nextRoom("south"), nextRoom("west") };
		return adjacentRooms;
	}

	/**
	 * Return the room that is reached if we go from this room in direction
	 * "direction". If there is no room in that direction, return null.
	 */
	public Room nextRoom(String direction) {
		try {
			return exits.get(direction).getKey();
		} catch (Exception e) {
			return null;
		}
	}

	public Door doorToPass(String direction) {
		try {
			return exits.get(direction).getValue();
		} catch (Exception e) {
			return null;
		}
	}

	public Enemy getAnEnemy() {
		if (enemies.length > 0) {
			random = new Random();
			return enemies[random.nextInt(enemies.length)];
		} else {
			return null;
		}
	}

	public void killEnemy(Enemy enemyToKill) {
		Enemy tempArray[] = new Enemy[enemies.length - 1];

		for (int i = 0, k = 0; i < enemies.length; i++) {
			if (enemies[i].equals(enemyToKill)) {
				continue;
			}
			tempArray[k++] = enemies[i];
		}
		enemies = tempArray;
	}

	public boolean containsItem(){
		if (items.length > 0){
			return true;
		}
		else{
			return false;
		}
	}
	public Item getAnItem() {
		Item itemToReturn = null;
		if (items.length > 0) {
			Item tempArray[] = new Item[items.length - 1];

			for (int i = 0, k = 0; i < items.length; i++) {
				if (i == 0) {
					itemToReturn = items[i];
					continue;
				}
				tempArray[k++] = items[i];
			}
			items = tempArray;
		}
		return itemToReturn;
	}
	public void giveAnItem(Item itemToGive){
		Item tempArray[] = new Item[items.length + 1];
		int i = 0;
		for (Item item : items) {
			tempArray[i] = item;
			i++;
		}
		tempArray[i] = itemToGive;
		items = tempArray;
	}

	public int getRoomNr() {
		return roomNr;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public void setExplored(boolean explored) {
		this.explored = explored;
	}

	public boolean isExplored() {
		return explored;
	}
}
