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
    // implement taking key from player
    public void openDoor(){
        offen = true;
    }
    public int getSchaden() {
        return schaden;
    }
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
