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
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

public class Room {
	private int randomAmount;
	private boolean firstTimeHere;
	private Random random;
	private String description;
	private boolean visited;
	private boolean explored;
	private HashMap<String, Door> doors; // stores doors of this room for every direction
	private HashMap<String, Room> rooms;
	private Enemy enemies[];
	private List <Item> items;
	private int playerKeys;
	enum Item {
		key,
		potion,
		bronze,
		silver,
		gold
	};
	String roomDescriptions[] = {
		"in a Laboratory, used to develop alchemical material transmutations. The walls are decorated with dark smears and stains. The floor is made of fitted shale bricks.",
		"in a Workshop, intended for the crafting of electronics. The walls are decorated with illustrations. The main scene is of a krayt dragon and a wyvern embracing an adiaphorism. The action is set in a corrupted dining hall. The piece is formed entirely out of tiny, colored beads.  The floor is made of unsanded rock.",
		"in a Chapel, dedicated to Sheela Peryoryl, the Green Sister, with an altarpiece focused on the afterlife. The walls are decorated with dark smears and stains. The floor is made of smooth-sanded granite.in a Chapel, dedicated to Sheela Peryoryl, the Green Sister, with an altarpiece focused on the afterlife. The walls are decorated with dark smears and stains. The floor is made of smooth-sanded granite.",
		"in a Barn, designed to house and feed camels. The walls are decorated with cracked triangular windows. The floor is made of warm, throbbing flesh.",
		"in a Storeroom, designed to keep and preserve candles and lantern oil. The walls are decorated with bookshelves with tomes of Angelology. The floor is made of fitted granite flagstones.",
		"in a Training Room, focused on mage training. The walls are decorated with bookshelves with texts of Literature. The floor is made of unsanded rock.",
		"in a Room of sinister and unclear purpose. Various furniture are all oriented towards a weird lifelike sculpture of a winged child in burnished brass. The walls are decorated with a plaque on the wall with the warning: 'The cantharidal hyena shall soon discase to the instance.'. The floor is made of fitted granite flagstones.",
		"in a Office, designed for moneylenders. The walls are decorated with a plaque on the wall with the warning: 'The standard summa shall soon recapitulate to the oligopoly.'. The floor is made of seated tiles of iron.",
		"in a Storeroom, designed to keep and preserve gold and gems - a treasury. The walls are decorated with bookshelves with texts of Caprine and Ovine Husbandry. The floor is made of warm, throbbing flesh.",
		"in a Kitchen, which has clearly been used to cook with nuts and rice. The walls are decorated with bookshelves with tomes of Abjuration. The floor is made of seated tiles of iron.",
		"in a Bathrooms, richly appointed. The walls are decorated with mosaics. The piece is of a xenomorph consumed by hope. The scene occurs in the basement of a smooth building.  The floor is made of smooth-sanded marble.",
		"in a Chapel, dedicated to Sheela Peryoryl, the Green Sister, with an altarpiece focused on the afterlife. The walls are decorated with dark smears and stains. The floor is made of smooth-sanded granite.",
		"in a Dining hall, built for use by the lowest servants. The walls are decorated with paintings. The image exhibits a squadron of hunter sharks constructing a building. The scene is set in a decrepit kitchen. The piece is formed entirely out of tiny seashells.  The floor is made of rough, solid granite.",
		"in a Training Room, focused on sparring. The walls are decorated with a plaque on the wall with the saying: 'Never turn-down the aviculture.'. The floor is made of fitted sandstone bricks.",
		"in a Barracks, built to house assassins. The walls are decorated with long, gouged-out claw-marks. The floor is made of fitted shale bricks.",
		"in a Barracks, built to house archers. The walls are decorated with paintings. The main image displays a filial rove. The action is set in a richly decorated operations center. It's signed W.L. The floor is made of smooth-sanded sandstone.",
		"in a Hospital, clearly designed for an epidemic of mental illness. The walls are decorated with bookshelves with texts of contrition of Corellon, Father of Elves. The floor is made of smooth-sanded granite.",
		"in a Classroom, which once taught Hydrology. There are many desks and chairs. The walls are decorated with a plaque on the wall with the saying: 'The line of the tike shall curve where the pessimal computation fords.'. The floor is made of rough, solid granite.",
		"in a Market, a large room for trading of bulk food. The walls are decorated with dark smears and stains. The floor is made of rough soil.",
		"in a Forge, with molds and casts specifically for crafting weapons. The walls are decorated with etchings on Daggers. The main image reveals great psychic battles , all fictional. The piece is outlined in red.  The floor is made of fitted slate flagstones.",
		"in a drab octogonal room; the pristine stone walls covered in dust. Rodent's scurry from your sight across the polishedfloor. A single lantern is lit in the center of the room.",
		"in a basic circular room, where the polished timber walls have missing portions that show through to the earth. The crumbling floor shows signs of a campfire of unknown age. The room is absent of light, but candles line the wall.",
		"in a narrow circular room, where moss covers the worn walls. Bat droppings cover the fractured floor. An unlit chandalier hangs overhead. It seems like this room is a privy.",
		"in a modest room with polished marble walls. Scattered bones line the old floor. This room is completely dark, lacking torches or lamps.",
		"in a mundane room with fractured walls that have deep cracks through to the earth. Dead insects cover the tile floor. A glow eminates from the opposite side of the room. Floating in the center is a brazier.",
		"in a clean room. Claw marks run up and down the decaying walls. Insects surry from your sight across the timber floor. An unlit chandalier hangs overhead. A shelf can be found along the wall containing a jar filled with some sort of mold, a marionette and a helmet.",
		"in an enormous hexagonal room, where the filthy stone walls show signs of battle. A single deteriorated body lies in the center of the fractured floor.",
		"in a complex room. Claw marks run up and down the dry obsidean walls. The warpedfloor is littered with stones and large rubble. This room is completely dark, lacking torches or lamps.",
		"in a harsh oval room. The fractured marble walls are angled 15 degrees outward. Insects surry from your sight across the floor."
	};
	
	/**
	 * Create a room with random parameters depending on the player stats. The description is randomised from a list.
	 * Usage - Room(1, 12, "This is a kitchen", 3, 20, 1);
	 * @param player - Player
     */
	public Room(){

	}
	public Room(Player player) {
		Random random = new Random();
		description = roomDescriptions[random.nextInt(roomDescriptions.length)];
		firstTimeHere = true;			
		visited = false;
		this.playerKeys = player.getKeys();
		doors = new HashMap<String, Door>();
		rooms = new HashMap<String, Room>();
		makeDoors();

		int maxAmount = 0;
		if (player.getHealth() < player.getMaxhealth()*0.25){
			maxAmount = 4;
		}
		else if (player.getHealth() < player.getMaxhealth()*0.5){
			maxAmount = 3;
		}
		else if (player.getHealth() < player.getMaxhealth()*0.75){
		maxAmount = 2;
		}
		else{
			maxAmount = 1;
		}
		int randomItemAmount = ThreadLocalRandom.current().nextInt(0, maxAmount + 1);
		items = new ArrayList<Room.Item>();
		setItems(randomItemAmount);
		
		if (player.getHealth() < player.getMaxhealth()*0.25){
			maxAmount = 1;
		}
		else if (player.getHealth() < player.getMaxhealth()*0.5){
			maxAmount = 2;
		}
		else{
			maxAmount = 3;
		}
		int randomEnemyAmount = ThreadLocalRandom.current().nextInt(0, maxAmount + 1);
		enemies = new Enemy[randomEnemyAmount];
		setEnemies(randomAmount, player.getLevel());		
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

	private Door.doorType selectRandomDoorType(){
		random = new Random();
		Door.doorType doorTypeList[] = Door.doorType.values();
		return doorTypeList[random.nextInt(doorTypeList.length)];
	}

	private void makeDoors(){
		int randomnumber = 0;
		Door.doorType doorTypeNorth = Door.doorType.wand;
		Door.doorType doorTypeEast = Door.doorType.wand;
		Door.doorType doorTypeSouth = Door.doorType.wand;
		Door.doorType doorTypeWest = Door.doorType.wand;
		for(int i = 0; i < 4; i++){
			randomnumber = ThreadLocalRandom.current().nextInt(0, 1 + 1);
			switch (i){
				case 1:
					if (randomnumber == 1){
						doorTypeNorth = selectRandomDoorType();
					}
					break;
				case 2:
					if (randomnumber == 1){
						doorTypeEast = selectRandomDoorType();
					}
					break;
				case 3:
					if (randomnumber == 1){
						doorTypeSouth = selectRandomDoorType();
					}
					break;
				case 4:
					if (randomnumber == 1){
						doorTypeWest = selectRandomDoorType();
					}
					break;
			}

		}
		setDoors(doorTypeNorth, doorTypeEast, doorTypeSouth, doorTypeWest);
	}

	public void setRoom(String direction, Room room){
		if (doors.get(direction).gettype().equals(Door.doorType.wand)){
		}
		else {
			rooms.put(direction, room);
		}
	}
	/**
	 * Define the doors of this room. Every direction needs to have a door. Insert a doorWand to have no exit.
	 * Usage - setExits(Door.doorType.wand, Door.doorType.tuer, Door.doorType.wand, Door.doorType.wand);
	 * @param doorTypeNorth - Door.doorType
	 * @param doorTypeEast - Door.doorType
	 * @param doorTypeSouth - Door.doorType
	 * @param doorTypeWest - Door.doorType
     * @throws Exception
     */
	private void setDoors (Door.doorType doorTypeNorth, Door.doorType doorTypeEast, Door.doorType doorTypeSouth, Door.doorType doorTypeWest){
		if (doorTypeNorth != null){
			Door door = new Door(doorTypeNorth);
			doors.put("north", door);
			if (doorTypeNorth == Door.doorType.wand){
				setRoom("north", null);
			}
		}
		if (doorTypeEast != null){
			Door door = new Door(doorTypeEast);
			doors.put("east", door);
			if (doorTypeNorth == Door.doorType.wand){
				setRoom("east", null);
			}
		}
		if (doorTypeSouth != null){
			Door door = new Door(doorTypeSouth);
			doors.put("south", door);
			if (doorTypeNorth == Door.doorType.wand){
				setRoom("south", null);
			}
		}
		if (doorTypeWest != null){
			Door door = new Door(doorTypeWest);
			doors.put("west", door);
			if (doorTypeNorth == Door.doorType.wand){
				setRoom("west", null);
			}
		}
	}
	public void setOneDoor(Door door, String direction){
		doors.put(direction, door);
	}


	/**
	 * Return a description of this room. If the room was visited before, the String starts of with "This looks familiar"
	 * Usage - description();
	 * @return - String
     * @throws Exception
     */
	public String description() {
		String returnString ="";
		if (visited) {
			returnString = "This looks familiar\n\n";
		} 
		if (firstTimeHere){
			firstTimeHere = false;
			return "Entering, you find yourself " + description + "\n" + exitString();
		}
		returnString += "You are " + description + "\n" + exitString();
		return returnString;

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
			for (String key : doors.keySet()) {
				if (doors.get(key).gettype() != Door.doorType.geheim
						&& doors.get(key).gettype() != Door.doorType.wand) {
					nrOfExits++;
				}
			}
			if (nrOfExits > 0) {
				returnString = "The doors you see are:";
				for (String key : doors.keySet()) {
					if (doors.get(key).gettype() != Door.doorType.geheim
							&& doors.get(key).gettype() != Door.doorType.wand) {
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
	//TODO change behaviour of recieving
	public Room[] getAdjacentRooms() {
		Room adjacentRooms[] = { rooms.get("north"), rooms.get("east"), rooms.get("south"), rooms.get("west") };
		return adjacentRooms;
		
	}

	/**
	 * Return the door that is reached if we go from this room in direction
	 * "direction". If there is no door in that direction, return null.
	 * Usage - nextDoor("north");
	 * @return - Door
     */
	public Door nextDoor(String direction) {
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
			return doors.get(direction);
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
			return doors.get(direction);
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
