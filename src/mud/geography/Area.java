package mud.geography;

import java.io.Serializable;
import java.util.HashMap;

/**
 * An area is a collection of rooms that makes up a general geographical
 * location.
 *
 * @author Japhez
 */
public class Area implements Serializable {

    private int areaID;
    private String name;
    private String description;
    private int minLevel;
    private int maxLevel;
    /**
     * A HashMap of Rooms where the keys are the unique IDs of the rooms.
     */
    private HashMap<Integer, Room> roomList;

    public Area(String name, int areaID) {
        this.areaID = areaID;
        this.name = name;
        description = "";
        roomList = new HashMap<>();
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getAreaID() {
        return areaID;
    }

    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<Integer, Room> getRoomList() {
        return roomList;
    }

    /**
     * Adds a room to this area.
     *
     * @param room
     */
    public void addRoom(Room room) {
        roomList.put(room.getRoomID(), room);
    }
}
