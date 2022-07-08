/*
 * Author: Jonathan Haimann
 * Version: 1.1
 * Date:    07.07.2022
 */

import java.util.ArrayList;

public class Map {
    private ArrayList<Room> roomList;
    private ArrayList<Integer> xKoordinaten;
    private ArrayList<Integer> yKoordinaten;
    private Room currentRoom, room;
    private int xKoordinate, yKoordinate;
	/**
	 * Create a Map object. it needs the room the player is in, as a starting point for the map.
     * It creates 3 lists with equal indexes. A list to sotre the room, one for the x-coordinates and one for the y-coordinates
     * Usage - Map(Room14);
     * @param startingRoom - Room
     * @throws Exception
     */
    public Map(Room startingRoom) {
        roomList = new ArrayList<Room>();
        xKoordinaten = new ArrayList<Integer>();
        yKoordinaten = new ArrayList<Integer>();
        currentRoom = startingRoom;
        xKoordinate = 0;
        yKoordinate = 0;
        roomList.add(startingRoom);
        xKoordinaten.add(xKoordinate);
        yKoordinaten.add(yKoordinate);
    }
    /**
	 * Saves the coordinates of the room in the variables yKoordinate and xKoordinate
     * Usage - koordinatenBerechnen(Room14);
     * @param roomToAdd - Room
     * @throws Exception
     */
    private void koordinatenBerechnen(Room roomToAdd) {
        // current room koordinaten
        for (int i = 0; i < roomList.size(); i++) {
            room = roomList.get(i);
            if (room.equals(currentRoom)) {
                xKoordinate = xKoordinaten.get(i);
                yKoordinate = yKoordinaten.get(i);
            }
        }
        for (int i = 0; i < roomToAdd.getAdjacentRooms().length; i++) {
            if (roomToAdd.getAdjacentRooms()[i] == null) {

            } else {
                if (roomToAdd.getAdjacentRooms()[i].equals(currentRoom)) {
                    switch (i) {
                        case 0: // north
                            yKoordinate = yKoordinate + 3;
                            break;
                        case 1: // east
                            xKoordinate = xKoordinate - 3;
                            break;
                        case 2: // south
                            yKoordinate = yKoordinate - 3;
                            break;
                        case 3: // west
                            xKoordinate = xKoordinate + 3;
                            break;
                    }
                }
            }
        }
    }
	/**
	 * Return a string containing the map and the legend at the bottom
     * Usage - showMap(Room8);
     * @param roomPlayerIsIn - Room
	 * @return - String
     * @throws Exception
     */
    public String showMap(Room roomPlayerIsIn) {
        String returnString = "";
        boolean roomFound = true;
        int xMin = 0, xMax = 3, yMin = 0, yMax = 3;
        for (int i = 0; i < roomList.size(); i++) {
            room = roomList.get(i);

            if (xKoordinaten.get(i) < xMin) {
                xMin = xKoordinaten.get(i);
            }
            if (xKoordinaten.get(i) > xMax) {
                xMax = xKoordinaten.get(i);
            }
            if (yKoordinaten.get(i) < yMin) {
                yMin = yKoordinaten.get(i);
            }
            if (yKoordinaten.get(i) > yMax) {
                yMax = yKoordinaten.get(i);
            }
        }
        for (int y = yMin; y <= yMax + 3; y++) {
            for (int x = xMin; x <= xMax + 3; x++) {
                roomFound = false;
                for (int i = 0; i < roomList.size(); i++) {
                    room = roomList.get(i);
                    if (x >= 0 && y >= 0) {
                        if (x == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            if (room.doorToPass("north").gettype() != Door.doorType.wand
                                    && room.doorToPass("north").gettype() != Door.doorType.geheim
                                    && room.isExplored())
                                     {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "-";
                            }
                            roomFound = true;
                        } else if (x - 2 == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.doorToPass("west").gettype() != Door.doorType.wand
                                    && room.doorToPass("west").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "|";
                            }
                            roomFound = true;
                        } else if (x - 2 == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.doorToPass("east").gettype() != Door.doorType.wand
                                    && room.doorToPass("east").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "|";
                            }
                            roomFound = true;
                        } else if (x == ((xKoordinaten.get(i))) && y - 2 == ((yKoordinaten.get(i)))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == (xKoordinaten.get(i)) && y - 2 == (yKoordinaten.get(i))) {
                            if (room.doorToPass("south").gettype() != Door.doorType.wand
                                    && room.doorToPass("south").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "-";
                            }
                            roomFound = true;
                        } else if (x - 2 == (xKoordinaten.get(i)) && y - 2 == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.equals(roomPlayerIsIn)) {
                                returnString = returnString + "X";
                            } 
                            else if(room.containsItem()){
                                returnString = returnString + "$";
                            }else {
                                returnString = returnString + " ";
                            }
                            roomFound = true;
                        }
                    }
                    if (x < 0 && y >= 0) {
                        if (x == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            if (room.doorToPass("north").gettype() != Door.doorType.wand
                                    && room.doorToPass("north").gettype() != Door.doorType.geheim
                                    && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "-";
                            }
                            roomFound = true;
                        } else if (x - 2 == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.doorToPass("west").gettype() != Door.doorType.wand
                                    && room.doorToPass("west").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "|";
                            }
                            roomFound = true;
                        } else if (x - 2 == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.doorToPass("east").gettype() != Door.doorType.wand
                                    && room.doorToPass("east").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "|";
                            }
                            roomFound = true;
                        } else if (x == ((xKoordinaten.get(i))) && y - 2 == ((yKoordinaten.get(i)))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == (xKoordinaten.get(i)) && y - 2 == (yKoordinaten.get(i))) {
                            if (room.doorToPass("south").gettype() != Door.doorType.wand
                                    && room.doorToPass("south").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "-";
                            }
                            roomFound = true;
                        } else if (x - 2 == (xKoordinaten.get(i)) && y - 2 == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.equals(roomPlayerIsIn)) {
                                returnString = returnString + "X";
                            } 
                            else if(room.containsItem()){
                                returnString = returnString + "$";
                            }else {
                                returnString = returnString + " ";
                            }
                            roomFound = true;
                        }
                    }
                    if (x >= 0 && y < 0) {
                        if (x == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            if (room.doorToPass("north").gettype() != Door.doorType.wand
                                    && room.doorToPass("north").gettype() != Door.doorType.geheim
                                    && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "-";
                            }
                            roomFound = true;
                        } else if (x - 2 == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.doorToPass("west").gettype() != Door.doorType.wand
                                    && room.doorToPass("west").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "|";
                            }
                            roomFound = true;
                        } else if (x - 2 == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.doorToPass("east").gettype() != Door.doorType.wand
                                    && room.doorToPass("east").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "|";
                            }
                            roomFound = true;
                        } else if (x == ((xKoordinaten.get(i))) && y - 2 == ((yKoordinaten.get(i)))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == (xKoordinaten.get(i)) && y - 2 == (yKoordinaten.get(i))) {
                            if (room.doorToPass("south").gettype() != Door.doorType.wand
                                    && room.doorToPass("south").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "-";
                            }
                            roomFound = true;
                        } else if (x - 2 == (xKoordinaten.get(i)) && y - 2 == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.equals(roomPlayerIsIn)) {
                                returnString = returnString + "X";
                            } 
                            else if(room.containsItem()){
                                returnString = returnString + "$";
                            }else {
                                returnString = returnString + " ";
                            }
                            roomFound = true;
                        }
                    }
                    if (x < 0 && y < 0) {
                        if (x == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            if (room.doorToPass("north").gettype() != Door.doorType.wand
                                    && room.doorToPass("north").gettype() != Door.doorType.geheim
                                    && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "-";
                            }
                            roomFound = true;
                        } else if (x - 2 == (xKoordinaten.get(i)) && y == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.doorToPass("west").gettype() != Door.doorType.wand
                                    && room.doorToPass("west").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "|";
                            }
                            roomFound = true;
                        } else if (x - 2 == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.doorToPass("east").gettype() != Door.doorType.wand
                                    && room.doorToPass("east").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "|";
                            }
                            roomFound = true;
                        } else if (x == ((xKoordinaten.get(i))) && y - 2 == ((yKoordinaten.get(i)))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == (xKoordinaten.get(i)) && y - 2 == (yKoordinaten.get(i))) {
                            if (room.doorToPass("south").gettype() != Door.doorType.wand
                                    && room.doorToPass("south").gettype() != Door.doorType.geheim && room.isExplored()) {
                                returnString = returnString + "/";
                            } else {
                                returnString = returnString + "-";
                            }
                            roomFound = true;
                        } else if (x - 2 == (xKoordinaten.get(i)) && y - 2 == (yKoordinaten.get(i))) {
                            returnString = returnString + "+";
                            roomFound = true;
                        } else if (x - 1 == ((xKoordinaten.get(i))) && y - 1 == ((yKoordinaten.get(i)))) {
                            if (room.equals(roomPlayerIsIn)) {
                                returnString = returnString + "X";
                            } 
                            else if(room.containsItem()){
                                returnString = returnString + "$";
                            }                            
                            else {
                                returnString = returnString + " ";
                            }
                            roomFound = true;
                        }
                    }
                }
                if (roomFound == false) {
                    returnString = returnString + " ";
                }
            }
            returnString = returnString + "\n";
        }
        returnString += "X - you are here\t$ - there is at least 1 item in the room\t/ - Door";
        return returnString;
    }
	/**
	 * add a Room to the map. The room needs to be adjacent to a room which is already on the map
     * Usage - addRoom(room6);
	 * @param roomToAdd - Room
     * @throws Exception
     */
    public void addRoom(Room roomToAdd) {
        if (roomList.isEmpty()) {
            roomList.add(roomToAdd);
            xKoordinaten.add(xKoordinate);
            yKoordinaten.add(yKoordinate);
        } else {
            if (roomList.contains(roomToAdd)) {
            } else {
                koordinatenBerechnen(roomToAdd);
                roomList.add(roomToAdd);
                xKoordinaten.add(xKoordinate);
                yKoordinaten.add(yKoordinate);
            }
            currentRoom = roomToAdd;
        }
    }
}
