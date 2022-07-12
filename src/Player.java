/*
 * Author: Jonathan Haimann
 * Version: 1.1
 * Date:    07.07.2022
 */

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Player {
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

	/**
	 * Create a player object. It starts with level 1, no items and 4 carry slots
     * Usage - Player();
     * @throws Exception
     */
    public Player() {
        level = 1;
        maxhealth = 100 * level;
        health = maxhealth;
        attack = 20 * level;
        defense = 10 * level;
        keys = 0;
        potions = 0;
        money = 0;
        carryCapacity = 3 + level;
    }
	/**
	 * gives the player a level-up and adjusts all the stats accordingly. it returns a String with the level up text
     * Usage - giveLevel();
	 * @return - String
     * @throws Exception
     */
    private String giveLevel() {
        level++;
        attack = 20 * level;
        defense = 20 * level;
        health = (((health*100)/maxhealth)*(100*level))/100;
        maxhealth = 100 * level;
        carryCapacity = 3 + level;
        return "\nLevel up!\nYou are now level " + level;
    }
	/**
	 * returns a random number between a lower and an upper bound
     * Usage - getRandomNumber(0, 20);
     * @param lowerBound -int
     * @param upperBound -int
	 * @return - int
     * @throws Exception
     */
    private int getRandomNumber(int lowerBound, int upperBound) {
        int randomNumber = ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
        return randomNumber;
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
	 * removes 1 key from the player
     * Usage - takeKey();
     * @throws Exception
     */
    public void takeKey() {
        keys--;
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
                return Room.Item.key;
            }
            else{
                return null;
            }
        }
        else if(item == Room.Item.potion){
            if (potions > 0){
                potions--;
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
	 * Asks the user if he wants to pick up an item.
     * Gives an item to the player if item is known and player has enough carrycapacity left.
     * Returns the resulting action as a String
     * Usage - giveItem(Room.Item.keys, currentRoom);
     * @param item - Room.Item
     * @param room - Room
     * @return - String
     * @throws Exception
     */
    public String giveItem(Room.Item item, Room room) {
        String returnString = "";
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
            } else {
                if (getRemainingCarryCapacity() > 0) {
                    returnString = "you take the " + item.toString();
                    returnString += " and put it in your belt";
                } else {
                    room.giveAnItem(item);
                    return "You carry too much on your belt. drop a key or potion to free up space";
                }
            }
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
        } else {
            if (item == Room.Item.gold || item == Room.Item.silver || item == Room.Item.bronze) {
                returnString = "you leave the " + item.toString() + " coin";
            } else {
                returnString = "you leave the " + item.toString();
            }
            returnString += " where it lies";
            room.giveAnItem(item);
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
        if (xp > level * 100) {
            xp = xp - level * 100;
            returnString = returnString + giveLevel();
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
    public int defend(int attackDamage) {
        int damage = 0;
        int defense = getRandomNumber(level, this.defense);
        if (defense < attackDamage) {
            damage = attackDamage - defense;
            health = health - damage;
        }
        return damage;
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
}
