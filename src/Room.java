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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

public class Room {
	private int randomAmount;
	private boolean firstTimeHere;
	private Random random;
	private String description;
	private int roomNr;
	private boolean visited;
	private boolean explored;
	private HashMap<String, Entry<Room, Door>> exits; // stores exits of this room.
	private Enemy enemies[];
	// private Item items[];
	private List <Item> items;
	private int playerKeys;

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
	 * Usage - Room(1, 12, "This is a kitchen", 3, 20, 1);
     * @param playerKeys - int
	 * @param roomNr - int
     * @param description - String
	 * @param maxItems - int
	 * @param enemyMaxLevel - int
	 * @param maxEnemyamount - int
     * @throws Exception
     */
	public Room(int playerKeys, Integer roomNr, String description, int maxItems, int enemyMaxLevel,
			int maxEnemyamount) {
		firstTimeHere = true;
		visited = false;
		this.playerKeys = playerKeys;
		this.roomNr = roomNr;
		this.description = description;
		this.exits = new HashMap<String, Entry<Room, Door>>();
		if (roomNr == 12){ //initial room (Room12) has always 3 items (at least 1 key)
			randomAmount = 3;
		}
		else{
			randomAmount = ThreadLocalRandom.current().nextInt(0, maxItems + 1);
		}
		items = new ArrayList<Room.Item>();
		setItems(randomAmount);

		int randomAmount = ThreadLocalRandom.current().nextInt(0, maxEnemyamount + 1);
		enemies = new Enemy[randomAmount];
		setEnemies(randomAmount, enemyMaxLevel);
	}

	/**
	 * Put random enemies in the Room
     * Usage - setEnemies(4, 5);
	 * @param amount - int
	 * @param maxLevel - int
     * @throws Exception
     */
	private void setEnemies(int amount, int maxLevel) {
		for (int i = 0; i < amount; i++) {
			Enemy.Enemytype enemyType = selectRandomEnemyType();
			int randomLevel = ThreadLocalRandom.current().nextInt(1, maxLevel + 1);
			Enemy enemy = new Enemy(randomLevel, enemyType);
			enemies[i] = enemy;
		}
	}
	/**
	 * returns 1 random enemy type
     * Usage - selectRandomEnemyType();
	 * @return - Enemy.Enemytype
     * @throws Exception
     */
	private Enemy.Enemytype selectRandomEnemyType() {
		random = new Random();
		Enemy.Enemytype enemyList[] = Enemy.Enemytype.values();
		return enemyList[random.nextInt(enemyList.length)];
	}

	/**
	 * Put random items in the Room
	  * Usage - setItems(10);
	 * @param amount - int
     * @throws Exception
     */
	private void setItems(int amount) {
		if (amount > 0) {
			if (playerKeys < 1) {
				items.add(Item.key);
				for (int i = 1; i < amount; i++) {
					Item item = selectRandomItem();
					items.add(item);
				}
			} else {
				for (int i = 0; i < amount; i++) {
					Item item = selectRandomItem();
					items.add(item);
				}
			}
		}
	}
	/**
	 * returns 1 random item
     * Usage - selectRandomItem();
	 * @return - Item
     * @throws Exception
     */
	private Item selectRandomItem() {
		random = new Random();
		Item itemList[] = Item.values();
		return itemList[random.nextInt(itemList.length)];
	}

	/**
	 * Define the exits of this room. Every direction either leads to
	 * another room or is null (no exit there).
	 * Usage - setExits(roomWand, doorWand, room16, door16t1, roomWand, doorWand, room4, door1t4);
     * @param roomNorth - Room
	 * @param doorNorth - Door
	 * @param roomEast - Room
	 * @param doorEast - Door
	 * @param roomSouth - Room
	 * @param doorSouth - Door
	 * @param roomWest - Room
	 * @param doorWest - Door
     * @throws Exception
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
	 * Return a description of this room. If the room was visited before, the String starts of with "This looks familiar"
	 * Usage - description();
	 * @return - String
     * @throws Exception
     */
	public String description() {
		if (visited) {
			return "This looks familiar\n\n" + description + "\n" + exitString();
		} 
		else if (firstTimeHere){
			firstTimeHere = false;
			return "Entering, you find yourself " + description + "\n" + exitString();
		}
		else {
			return "You are " + description + "\n" + exitString();
		}
	}

	/**
	 * Return a string describing the room's exits, if the room is not explored the return String is 
	 * "You dont know this room. You need to explore it first"
	 * Usage - exitString();
	 * @return - String
     * @throws Exception
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
	/**
	 * Return an array of all the adjacent rooms
	 * Usage - getAdjacentRooms();
	 * @return - Room[]
     * @throws Exception
     */
	public Room[] getAdjacentRooms() {
		Room adjacentRooms[] = { nextRoom("north"), nextRoom("east"), nextRoom("south"), nextRoom("west") };
		return adjacentRooms;
	}

	/**
	 * Return the room that is reached if we go from this room in direction
	 * "direction". If there is no room in that direction, return null.
	 * Usage - nextRoom();
	 * @return - Room
     */
	public Room nextRoom(String direction) {
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
		try {
			return exits.get(direction).getKey();
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * Return the door wich lies in a given direction
	 * If there is no door in that direction, return null.
	 * Usage - doorToPass(door2t7);
	 * @param direction - String
	 * @return - Door
     */
	public Door doorToPass(String direction) {
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
		try {
			return exits.get(direction).getValue();
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * Returns 1 random enemy from the room. If there are no enemies left, return null
	 * Usage - getAnEnemy();
	 * @return - Enemy
     * @throws Exception
     */
	public Enemy getAnEnemy() {
		if (enemies.length > 0) {
			random = new Random();
			return enemies[random.nextInt(enemies.length)];
		} else {
			return null;
		}
	}
	/**
	 * remove Enemy from room
	 * Usage - killEnemy(ghoul);
	 * @param enemyToKill - Enemy
     * @throws Exception
     */
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
	/**
	 * returns true if room contains at least 1 item
	 * Usage - containsItem();
	 * @return - boolean
     * @throws Exception
     */
	public boolean containsItem(){
		if (items.size() > 0){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * removes a specific item if in room
	 * Usage - getAnItem(item);
	 * @return - boolean
     * @throws Exception
     */
	public boolean getAnItem(Item itemToDelete) {
		if (items.contains(itemToDelete)){
			items.remove(itemToDelete);
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * appends an item in the room array
	 * Usage - giveAnItem(Room.Item.potion);
	 * @param itemToGive - Room.Item
     * @throws Exception
     */
	public void giveAnItem(Item itemToGive){
		items.add(itemToGive);
	}
	/**
	 * returns the number of the room
	 * Usage - getRoomNr();
	 * @return - int
     * @throws Exception
     */
	public int getRoomNr() {
		return roomNr;
	}
	/**
	 * set if the room is visited
	 * Usage - setVisited(true);
	 * @param visited - boolean
     * @throws Exception
     */
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	/**
	 * set if the room is explored
	 * Usage - setExplored(true);
	 * @param explored - boolean
     * @throws Exception
     */
	public void setExplored(boolean explored) {
		this.explored = explored;
	}
	/**
	 * returns true if the room is explored
	 * Usage - isExplored();
	 * @return - boolean
     * @throws Exception
     */
	public boolean isExplored() {
		return explored;
	}
	/**
	 * returns a list of all the items in the room
	 * Usage - getItems();
	 * @return - Item[]
     * @throws Exception
     */
	public List<Item> getItems() {
		return items;
	}
}
