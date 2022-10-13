/*
 * Author: Jonathan Haimann
 * Version: 1.1
 * Date:    07.07.2022
 */

import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


public class Player {
    private boolean itemWasGiven;
    private int level;
    private int health;
    private int maxhealth;
    private int attack;
    private int defense;
    private int carryCapacity;
    private int keys;
    private int potions;
    private int money;
    private int xp;
    private boolean isFighting;
    private int monstersKilled;
    private int keysUsed;
    private int potionsUsed;
    private int walkedInWalls;
    private int roomsExplored;
    private int itemsPickedUp;
    private int itemsDropped;
    private int damageDealt;
    private int damageTaken;
    private int nrOfAttacks;
    public  enum PlayerClass {
        Warrior,
        Assassin,
        Tank
    }
    private PlayerClass playerClass;
    /**
	 * Create a player object. It starts with level 1 and no items
     * Usage - Player();
     * @throws Exception
     */
    public Player(PlayerClass playerClass) {
        this.playerClass = playerClass;
        switch (playerClass){
            case Warrior:
            maxhealth = 20;
            attack = 2;
            defense = 2;
            carryCapacity = 2;
            break;
            case Assassin:
            maxhealth = 15;
            attack = 3;
            defense = 1;
            carryCapacity = 1;
            break;
            case Tank:
            maxhealth = 25;
            attack = 1;
            defense = 3;
            carryCapacity = 3;
            break;
        }   
        level = 1;
        health = maxhealth;
        keys = 0;
        potions = 0;
        money = 0;
        monstersKilled = 0;
        keysUsed = 0;
        potionsUsed = 0;
        walkedInWalls = 0;
        roomsExplored = 0;
        itemsPickedUp = 0;
        itemsDropped = 0;
        damageDealt = 0;
        damageTaken = 0;        
        itemWasGiven = false;
    }
	/**
	 * gives the player a level-up and adjusts all the stats accordingly. it returns a String with the level up text
     * Usage - giveLevel();
	 * @return - String
     * @throws Exception
     */
    private void levelUp() {
        boolean invalidInput = true;
        level++;
        System.out.println("\n\nYou leveled up!");
        System.out.println("You are now level " + level);
        while (invalidInput){
            System.out.println("Where do you want to put your stat points?");
            System.out.println("1. Health");
            System.out.println("2. Attack");
            System.out.println("3. Defense");
            System.out.println("4. Carry Capacity");
            Scanner scanner = new Scanner(System.in);
            System.out.print(">");
            String choice = scanner.next();
            switch (choice) {
                case "1" , "Health":
                    health = (int) (((double) health / (double) maxhealth) * (double) maxhealth+10);
                    maxhealth += 10;
                    invalidInput = false;
                    break;
                case "2" , "Attack":
                    attack += 1;
                    invalidInput = false;
                    break;
                case "3" , "Defense":
                    defense += 1;
                    invalidInput = false;
                    break;
                case "4" , "Carry Capacity":
                    carryCapacity += 1;
                    invalidInput = false;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
    }
	/**
	 * returns a random number depending on how many sides the dice has
     * Usage - getDiceRoll(20);
     * Usage - getDiceRoll();
     * @param nrOfSides -int
	 * @return - int
     * @throws Exception
     */
    private int getDiceRoll(int nrOfSides) {
        int randomNumber = ThreadLocalRandom.current().nextInt(1, nrOfSides + 1);
        return randomNumber;
    }
    private int getDiceRoll() {
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 20 + 1);
        return randomNumber;
    }

    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
	/**
	 * returns if the player is in attackmode or not
     * Usage - isAttackMode();
	 * @return - boolean
     * @throws Exception
     */
    public boolean isAttackMode() {
        return isFighting;
    }
    /**
	 * set the attack mode of the player
     * Usage - setAttackMode(true);
	 * @param attackMode - boolean
     * @throws Exception
     */
    public void setAttackMode(boolean attackMode) {
        this.isFighting = attackMode;
    }
	/**
	 * returns the level of the player
     * Usage - getLevel();
	 * @return - int
     * @throws Exception
     */
    public int getLevel() {
        return level;
    }
	/**
	 * returns the health of the player
     * Usage - getHealth();
	 * @return - int
     * @throws Exception
     */
    public int getHealth() {
        return health;
    }
	/**
	 * sets the health of the player
     * Usage - setHealth();
	 * @param health - int
     * @throws Exception
     */
    public void setHealth(int health) {
        this.health = health;
    }
	/**
	 * damage the player
     * Usage - takeDamage(99);
	 * @param damage - int
     * @throws Exception
     */
    public void takeDamage(Integer damage) {
        health = health - damage;
    }
	/**
	 * returns the attack stat of the player
     * Usage - getAttack();
	 * @return - int
     * @throws Exception
     */
    public int getAttack() {
        return attack;
    }
	/**
	 * returns the defense stat of the player
     * Usage - getDefense();
	 * @return - int
     * @throws Exception
     */
    public int getDefense() {
        return defense;
    }
	/**
	 * returns the amount of keys a player posesses
     * Usage - getKeys();
	 * @return - int
     * @throws Exception
     */
    public int getKeys() {
        return keys;
    }
    /**
	 * returns the max health a player can have
     * Usage - getMaxhealth();
	 * @return - int
     * @throws Exception
     */
    public int getMaxhealth() {
        return maxhealth;
    }
	/**
	 * removes 1 key from the player
     * Usage - takeKey();
     * @throws Exception
     */
    public void takeKey() {
        keys--;
        addKeysUsed(1);
    }
	/**
	 * returns the amount of potions a player posesses
     * Usage - getPotions();
	 * @return - int
     * @throws Exception
     */
    public int getPotions() {
        return potions;
    }
	/**
	 * takes an item from the player and returns it. If the player does not have the item, it returns null
     * Usage - dropItem(Room.Item.keys);
     * @param item - Room.Item
	 * @return - Room.Item
     * @throws Exception
     */
    public Room.Item dropItem(Room.Item item){
        if (item == Room.Item.key){
            if (keys > 0){
                keys--;
                addItemsDropped(1);
                return Room.Item.key;
            }
            else{
                return null;
            }
        }
        else if(item == Room.Item.potion){
            if (potions > 0){
                potions--;
                addItemsDropped(1);
                return Room.Item.potion;
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }
    /**
	 * Gets an Item and checks if it can be given to the player. Sets the global boolean Value itemWasGiven to true if it was given to the player.
     * Returns the resulting action as a String
     * Usage - recieveItem(Room.Item.keys);
     * @param item - Room.Item
	 * @return - String
     * @throws Exception
     */
    private String recieveItem (Room.Item item){
        String returnString = "";
        itemWasGiven = false;
        if (item == Room.Item.gold || item == Room.Item.silver || item == Room.Item.bronze) {
            returnString = item.toString() + " coin";
        } else {
            if (getRemainingCarryCapacity() > 0) {
                returnString = item.toString();
            } else {
                return "";
            }
        }
        itemWasGiven = true;
            addItemsPickedUp(1);
            switch (item) {
                case key:
                keys++;
                break;
                case potion:
                potions++;
                break;
                case bronze:
                money = money + 100;
                break;
                case silver:
                money = money + 200;
                break;
                case gold:
                money = money + 500;
                break;
            }             
        return returnString;
    }
    /**
	 * Asks the user if he wants to pick up an item.
     * Gives an item to the player if item is known and player has enough carrycapacity left.
     * Removes the Item from the room it was given from.
     * Returns the resulting action as a String
     * Usage - giveItem(Room.Item.keys, currentRoom);
     * @param item - Room.Item
     * @param room - Room
     * @return - String
     * @throws Exception
     */
    public String giveItem(List<Room.Item> items, Room room) {
        String returnString = "You have found a Chest containing following items:";
        for (Room.Item item : items) {
            if (item == Room.Item.gold || item == Room.Item.silver || item == Room.Item.bronze) {
                returnString += "\na " + item.toString() + " coin";
            } else {
                returnString += "\na " + item.toString();
            }          
        }
        returnString += "\n\nWhat do you want to pick up?";
        System.out.println(returnString);
        Scanner input = new Scanner(System.in);
        System.out.print(">");
        String answer = input.next();

            if(answer.equals("everything")||answer.equals("all")){
                ListIterator<Room.Item> iterator = items.listIterator();
                int oldSize = items.size();
                returnString = "You have picked up the following items: ";
                while(iterator.hasNext()){
                    Room.Item item = iterator.next();
                    String itemString = recieveItem(item);
                    if (itemString != ""){
                        returnString += itemString +", ";
                        if (itemWasGiven){
                            iterator.remove();
                        }
                    }
                }
                if (items.size() == 0){
                    returnString = "You have picked up all items";
                }
                else if (items.size() == oldSize){
                    returnString = "You have not picked up any items. Please make room in your inventory using the drop command.";
                }
            }                           
            else if(answer.equals("nothing") || answer.length()<=0 || answer.equals("no")){
                return "You leave it all behind and turn around";
            }
            else{
                Boolean wrongItem = true;
                returnString = "You pick up a ";
                ListIterator<Room.Item> iterator = items.listIterator();
                int oldSize = items.size();
                    while(iterator.hasNext()){
                        Room.Item item = iterator.next();
                        if (item.toString().equals(answer)){
                            returnString += recieveItem(item);
                            if (itemWasGiven){
                                iterator.remove();
                            }
                            wrongItem = false;
                            break;
                        }
                    }
                if (wrongItem){
                    returnString = "You can not pick up a " + answer + ". It seems to you that no matter how long you look in the box you will not find a "+answer;
                }
                else if (items.size() == oldSize){
                    returnString = "You have not picked up any items. Please make room in your inventory using the drop command.";
                }
            }    
        return returnString;
    }
    /**
	 * the player consumes a potion and gets max hp. returns false if no potions are avalable
     * Usage - drinkPotion();
     * @return - boolean
     * @throws Exception
     */
    public boolean drinkPotion() {
        if (potions > 0) {
            health = maxhealth;
            potions = potions - 1; 
            addPotionsUsed(1);
            return true;
        } else {
            return false;
        }
    }
    /**
	 * gives the player xp. When enough xp is accumulated a level-up is done
     * Usage - giveXp(29);
     * @param amount - int
     * @return -String
     * @throws Exception
     */
    public String giveXp(int amount) {
        String returnString = "\nYou get " + amount + "xp";
        xp = xp + amount;
        if (xp >= level * 100) {
            xp = xp - level * 100;
            levelUp();
        }
        return returnString;
    }
    /**
	 * returns all the player stats as a string
     * Usage - getStats();
     * @return -String
     * @throws Exception
     */
    public String getStats() {
        int xpToLevelUp = (level * 100) - xp;
        int carrySlots = keys + potions;
        String text = "";
        text = text + "health: " + health + "/" + maxhealth;
        text = text + "\t\tkeys: " + keys;
        text = text + "\t\tpotions: " + potions;
        text = text + "\tcarry capacity: " + carrySlots +"/"+carryCapacity;
        text = text + "\tmoney: " + money;
        text = text + "\tlevel: " + level;
        text = text + "\txp until next level: " + xpToLevelUp;
        if (isAttackMode()){
            text += "\n\nUsefull commands: attack, drink potion, retreat";
        }
        else{
            text += "\n\nUsefull commands: go, explore, map, quit";
        }
        return text;
    }
    /**
	 * returns the damage taken after defending
     * Usage - defend(20);
     * @param attackDamage - int
     * @return -int
     * @throws Exception
     */
    public int defend(Enemy enemy) {
        int attackChance = enemy.attack();
        int diceRoll = getDiceRoll();
        int defense = diceRoll + this.defense;
        if (diceRoll == 20){
            defense = defense * 2;
        }
        else if (diceRoll == 1){
            defense = 0;
        }
        //TODO remove syso
        System.out.println("Your defence chance is: " + defense + " and the " + enemy.getType() + "'s attack chance is: " + attackChance);
        if (defense < attackChance) {
            health = health - enemy.getAttack();
            return enemy.getAttack();
        }
        else{
            return 0;
        }
    }
    /**
	 * returns the damage made
     * Usage - attack();
     * @return -int
     * @throws Exception
     */
    public int attack() {
        int diceRoll = getDiceRoll();
        int attack = diceRoll + this.attack;
        if (diceRoll == 20) {
            return 2 * attack;
        } 
        else if(diceRoll == 1){
            return 0;
        }
        else {
            return attack;
        }
    }
    /**
	 * returns the remaining carry capacity
     * Usage - getRemainingCarryCapacity();
     * @return -int
     * @throws Exception
     */
    public int getRemainingCarryCapacity() {
        return carryCapacity - keys - potions;
    }    
    public void addMonstersKilled(int monstersKilled) {
        this.monstersKilled += monstersKilled;
    }
    public void addKeysUsed(int keysUsed) {
        this.keysUsed += keysUsed;
    }
    public void addPotionsUsed(int potionsUsed) {
        this.potionsUsed += potionsUsed;
    }
    public void addWalkedInWalls(int walkedInWalls) {
        this.walkedInWalls += walkedInWalls;
    }
    public void addRoomsExplored(int roomsExplored) {
        this.roomsExplored += roomsExplored;
    }
    public void addItemsPickedUp(int itemsPickedUp) {
        this.itemsPickedUp += itemsPickedUp;
    }
    public void addItemsDropped(int itemsDropped) {
        this.itemsDropped += itemsDropped;
    }
    public void addDamageDealt(int damageDealt) {
        this.damageDealt += damageDealt;
    }
    public void addDamageTaken(int damageTaken) {
        this.damageTaken += damageTaken;
    }
    public void addNrOfAttacks(int nrOfAttacks) {
        this.nrOfAttacks += nrOfAttacks;
    }
    public String getStatistics(){
        String text = "";
        text = text + "Monsters killed: " + monstersKilled;
        text = text + "\t\tDamage dealt: " + damageDealt;
        text = text + "\t\tDamage taken: " + damageTaken;
        text = text + "\t\tNumber of attacks: " + nrOfAttacks;
        text = text + "\t\tPotions used: " + potionsUsed;
        text = text + "\t\tKeys used: " + keysUsed;
        text = text + "\t\tItems picked up: " + itemsPickedUp;
        text = text + "\t\tItems dropped: " + itemsDropped;
        text = text + "\t\tWalked in walls: " + walkedInWalls;
        text = text + "\t\tRooms explored: " + roomsExplored;
        return text;
    }
    public PlayerClass getPlayerClass() {
        return playerClass;
    }
}