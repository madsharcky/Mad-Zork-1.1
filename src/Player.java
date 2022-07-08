/*
 * Author: Jonathan Haimann
 * Version: 1.1
 * Date:    07.07.2022
 */

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

    public Player() {
        level = 1;
        maxhealth = 100 * level;
        health = maxhealth;
        attack = 20 * level;
        defense = 10 * level;
        keys = 0;
        potions = 1;
        money = 0;
        carryCapacity = 3 + level;
    }

    private String giveLevel() {
        level++;
        attack = 20 * level;
        defense = 20 * level;
        health = health + ((100 * level) - maxhealth);
        maxhealth = 100 * level;
        carryCapacity = 3 + level;
        return "\nLevel up!\nYou are now level " + level;
    }

    private int getRandomNumber(int lowerBound, int upperBound) {
        int randomNumber = ThreadLocalRandom.current().nextInt(lowerBound, upperBound + 1);
        return randomNumber;
    }

    public boolean isAttackMode() {
        return isFighting;
    }

    public void setAttackMode(boolean attackMode) {
        this.isFighting = attackMode;
    }

    public int getLevel() {
        return level;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void makeMaxHealth() {
        health = maxhealth;
    }

    public void takeDamage(Integer hp) {
        health = health - hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getMaxhealth() {
        return maxhealth;
    }

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }

    public int getPotions() {
        return potions;
    }
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

    public void givePotion() {
        potions = potions + 1;
    }

    public boolean drinkPotion() {
        if (potions > 0) {
            health = maxhealth;
            potions = potions - 1;
            return true;
        } else {
            return false;
        }
    }

    public int getMoney() {
        return money;
    }

    public void giveMoney(int amount) {
        money = money + amount;
    }

    public int getXp() {
        return xp;
    }

    public String giveXp(int amount) {
        String returnString = "\nYou get " + amount + "xp";
        xp = xp + amount;
        if (xp > level * 100) {
            xp = xp - level * 100;
            returnString = returnString + giveLevel();
        }
        return returnString;
    }

    public String getStats() {
        int xpToLevelUp = (level * 100) - xp;
        int carrySlots = keys + potions;
        String text = "";
        text = text + "health: " + health + "/" + maxhealth;
        text = text + "\t\tkeys: " + keys;
        text = text + "\t\tpotions: " + potions;
        text = text + "\tcarry capacity: " + carrySlots +"/"+carryCapacity;
        text = text + "\t\tmoney: " + money;
        text = text + "\t\tlevel: " + level;
        text = text + "\txp until next level: " + xpToLevelUp;
        if (isAttackMode()){
            text += "\nCommands: attack, drink potion, retreat";
        }
        else{
            text += "\nCommands: go, explore, map, quit";
        }
        return text;
    }

    public void giveItem(Room.Item item) {
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

    }

    public int defend(int attackToParry) {
        int damage = 0;
        int parrychance = getRandomNumber(level, defense);
        if (parrychance < attackToParry) {
            damage = attackToParry - parrychance;
            health = health - damage;
        }
        return damage;
    }
    
    public int getRemainingCarryCapacity() {
        return carryCapacity - keys - potions;
    }
}
