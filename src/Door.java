/*
 * Author: Jonathan Haimann
 * Version: 1.1
 * Date:    07.07.2022
 */
import java.util.concurrent.ThreadLocalRandom;

public class Door {
    enum doorType{
        opferTuer,
        falltuer,
        tuer,
        abgeschlossen,
        falle,
        geheim,
        wand
    }
    private boolean offen;
    private int schaden;
    private boolean sichtbar;
    private doorType type;
    private int kosten;
	/**
	 * Create a Door object. Doortype must be supplied
     * Usage - Door(Door.doorType.tuer);
     * @param type - Door.doorType
     * @throws Exception
     */
    public Door(doorType type){
        this.type = type;
        switch(type){
            case opferTuer:
                offen = false;
                schaden = 0;
                sichtbar = true;
                kosten = 5000;
                break;
            case falltuer:
                offen = true;
                schaden = 0;
                sichtbar = true;
                kosten = 0;
                break;
            case tuer:
                offen = true;
                schaden = 0;
                sichtbar = true;
                kosten = 0;
                break;
            case abgeschlossen:
                offen = false;
                schaden = 0;
                sichtbar = true;
                kosten = 0;
                break;
            case falle:
                offen = true;
                schaden = ThreadLocalRandom.current().nextInt(0, 30 + 1);;
                sichtbar = true;
                kosten = 0;
                break;
            case geheim:
                offen = true;
                schaden = 0;
                sichtbar = false;
                kosten = 0;
                break;
            case wand:
                offen = true;
                schaden = 0;
                sichtbar = false;
                kosten = 0;
                break;
        }
    }
	/**
	 * open the door so player can pass through
     * Usage - openDoor();
     * @throws Exception
     */
    public void openDoor(){
        offen = true;
    }
    /**
	 * returns the amount that needs to be payed to go through the door
     * Usage - getKosten();
     * @return - int
     * @throws Exception
     */
    public int getKosten(){
        return kosten;
    }
	/**
	 * Give money to the door. It opens if enough was payed. returns the change
     * Usage - openDoor(500);
     * @param payment - int
     * @return - int
     * @throws Exception
     */
    public int payDoor(int payment){
        if (payment >= kosten){
            openDoor();
            int change = payment - kosten;
            kosten = 0;
            return change;
        }
        else {
            kosten = kosten - payment;
            return 0;
        }
    }
    /**
	 * closes the door so player can not pass through
     * Usage - closeDoor();
     * @throws Exception
     */
    public void closeDoor() {
        offen = false;
    }

    /**
	 * Returns the resulting action when player springs the trap
     * Usage - springTrap(player);
     * @param player - Player
     * @return - String
     * @throws Exception
     */
    public String springTrap(Player player){
        String returnString = "";
        int evasion = ThreadLocalRandom.current().nextInt(0, player.getDefense() + 1);
        if (evasion < schaden) {
            int damage =schaden - evasion; // check if player gets damage
            player.takeDamage(damage);
            returnString ="you Take " + damage + " damage";
            if (player.getHealth() <= 0) {
                returnString = returnString
                        + "\n\nYou are impaled and can not get free! Slowly you bleed out";
                return returnString;
            }
        } else {
            returnString ="You are lucky and take no damage";
        }
        return returnString;
    }
    
    /**
	 * returns the type of the door
     * Usage - gettype();
     * @return - Door.doorType
     * @throws Exception
     */
    public doorType gettype(){
        return type;
    }

    /**
	 * returns true if door is visible. 
     * returns false if door is invisile.
     * Usage - isSichtbar();
     * @return - boolean
     * @throws Exception
     */
    public boolean isSichtbar() {
        return sichtbar;
    }
    /**
	 * returns true if door is open
     * returns flase if door is closed
     * Usage - getoffen();
     * @return - boolean
     * @throws Exception
     */
    public boolean getoffen(){
        return offen;
    }
}
