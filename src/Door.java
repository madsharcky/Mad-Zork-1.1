/*
 * Author: Jonathan Haimann
 * Version: 1.1
 * Date:    07.07.2022
 */
import java.util.concurrent.ThreadLocalRandom;

public class Door {
    enum doorType{
        durchgang,
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
	/**
	 * Create a Door object. Doortype must be supplied
     * Usage - Door(Door.doorType.tuer);
     * @param type - Door.doorType
     * @throws Exception
     */
    public Door(doorType type){
        this.type = type;
        switch(type){
            case durchgang:
                offen = true;
                schaden = 0;
                sichtbar = true;
                break;
            case falltuer:
                offen = true;
                schaden = 0;
                sichtbar = true;
                break;
            case tuer:
                offen = true;
                schaden = 0;
                sichtbar = true;
                break;
            case abgeschlossen:
                offen = false;
                schaden = 0;
                sichtbar = true;
                break;
            case falle:
                offen = true;
                schaden = ThreadLocalRandom.current().nextInt(0, 30 + 1);;
                sichtbar = true;
                break;
            case geheim:
                offen = true;
                schaden = 0;
                sichtbar = false;
                break;
            case wand:
                offen = true;
                schaden = 0;
                sichtbar = false;
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
	 * returns damage if door is a trap. 
     * returns 0 if door is not a trap
     * Usage - openDoor();
     * @return - int
     * @throws Exception
     */
    public int getSchaden() {
        return schaden;
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
    public doorType gettype(){
        return type;
    }
    public boolean getoffen(){
        return offen;
    }
    public void setOffen(boolean offen) {
        this.offen = offen;
    }
}
