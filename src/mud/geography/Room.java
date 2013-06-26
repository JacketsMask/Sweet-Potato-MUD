package mud.geography;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import mud.Player;

/**
 * Represents a room for an area of the MUD.
 *
 * @author Japhez
 */
public class Room implements Serializable {

    private int roomID;
    private String name;
    private String description;
    private Room north;
    private Room east;
    private Room south;
    private Room west;
    private Room up;
    private Room down;
    //private HashMap<String, Room> hiddenExits; //TODO: Milestone #2
    //private ArrayList<Item> items; //TODO: Milestone #2
    //private ArrayList<NPC> npcs; //TODO: Milestone #2
    private ArrayList<Player> players;

    public Room(int roomID) {
        this.roomID = roomID;
        name = "Unnamed Room";
        description = "You're in a nondescript room.";
        players = new ArrayList<>();
    }
    
    public void addPlayer(Player player) {
        players.add(player);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
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

    public Room getNorth() {
        return north;
    }

    public void setNorth(Room north) {
        this.north = north;
    }

    public Room getEast() {
        return east;
    }

    public void setEast(Room east) {
        this.east = east;
    }

    public Room getSouth() {
        return south;
    }

    public void setSouth(Room south) {
        this.south = south;
    }

    public Room getWest() {
        return west;
    }

    public void setWest(Room west) {
        this.west = west;
    }

    public Room getUp() {
        return up;
    }

    public void setUp(Room up) {
        this.up = up;
    }

    public Room getDown() {
        return down;
    }

    public void setDown(Room down) {
        this.down = down;
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }
}
