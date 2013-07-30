package mud;

import file.FileManipulator;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import mud.geography.Area;
import mud.geography.Direction;
import mud.geography.Room;
import mud.network.server.log.ConsoleLog;

/**
 * A room manager that holds a list of all the areas and rooms in the MUD.
 * Manages room ID numbers by providing unique room numbers when they are
 * requested.
 *
 * @author Japhez
 */
public final class AreaManager implements Serializable {

    private int areaIDCount;
    private ArrayList<Integer> freeAreaIDs;
    //A master list of all areas in the MUD
    private HashMap<Integer, Area> masterAreaList;
    //A master list of all rooms in the MUD
    private HashMap<Integer, Room> masterRoomList;
    private int roomIDCount;
    private ArrayList<Integer> freeRoomIDs;
    private Room respawnRoom;
    private final String PATH = "areas//";
    private final String EXTENSION = ".area";

    public AreaManager() {
        areaIDCount = 1;
        roomIDCount = 1;
        freeRoomIDs = new ArrayList<>();
        freeAreaIDs = new ArrayList<>();
        masterAreaList = new HashMap<>();
        masterRoomList = new HashMap<>();
        System.out.println(ConsoleLog.log() + "Creating starter area.");
        createStarterArea();
    }

    public void removeOrphanRooms() {
        //TODO: Scan all areas for rooms without exits, delete any disconnected rooms
    }

    /**
     * Returns the room with the given ID, if the room exists. If it doesn't
     * exist, returns null.
     *
     * @param roomID
     * @return the room if it exists, otherwise null
     */
    public Room getRoom(int roomID) {
        return masterRoomList.get(roomID);
    }

    /**
     * Saves the state of the passed area to the area folder (use this to store
     * the "default" state of an area).
     *
     * @param area the area to be saved
     */
    public void saveArea(Area area) {
        FileManipulator.writeObject(area, (GameMaster.MAIN_DATA_PATH + PATH), area.getName() + EXTENSION);
        System.out.println(ConsoleLog.log() + area.getName() + " saved locally.");
    }

    /**
     * Creates a basic cross shaped area of 5 rooms to be used as a base for
     * area design and expansion.
     */
    public void createStarterArea() {
        //Create a new area with a new area ID
        Area testArea = new Area("Test Area", getUniqueAreaID());
        //Create 5 rooms
        Room center = new Room(testArea, this);
        Room north = new Room(testArea, this);
        Room east = new Room(testArea, this);
        Room south = new Room(testArea, this);
        Room west = new Room(testArea, this);
        //Set room names and exists
        center.setName("Center room.");
        north.setName("North room.");
        east.setName("East room.");
        south.setName("South room.");
        west.setName("West room.");
        center.linkToRoom(Direction.NORTH, north);
        center.linkToRoom(Direction.EAST, east);
        center.linkToRoom(Direction.SOUTH, south);
        center.linkToRoom(Direction.WEST, west);
        //Set the respawn room
        setRespawnRoom(center);
        //Save the area to a file
        saveArea(testArea);
        //Add the area to the master area list
        masterAreaList.put(getUniqueAreaID(), testArea);
    }

    /**
     * Deletes the passed room after unlinking all its exits.
     *
     * @param room
     */
    public void deleteRoom(Room room) {
        room.unlinkExits(Direction.getDirections());
        masterRoomList.remove(room.getRoomID());
    }

    /**
     * Sets the spawn room, where new players should start, and dead players
     * should reappear at.
     *
     * @param respawnRoom the room to respawn in
     */
    public void setRespawnRoom(Room respawnRoom) {
        this.respawnRoom = respawnRoom;
    }

    /**
     * @return the respawn room
     */
    public Room getRespawnRoom() {
        return respawnRoom;
    }

    /**
     * @return an available unique room ID
     */
    public int getUniqueRoomID() {
        if (freeRoomIDs.isEmpty()) {
            return roomIDCount++;
        } else {
            return freeRoomIDs.remove(0);
        }
    }

    /**
     * @return an available unique area ID
     */
    public int getUniqueAreaID() {
        if (freeAreaIDs.isEmpty()) {
            return areaIDCount++;
        } else {
            return freeAreaIDs.remove(0);
        }
    }

    /**
     * You don't need to call this manually. It adds the passed room to the
     * master list, but this method is called when a room is created initially
     * anyways. So...yeah. Don't call this, you'll look silly.
     *
     * @param room
     */
    public void addRoom(Room room) {
        masterRoomList.put(room.getRoomID(), room);
    }

    /**
     * Loads an area from the passed file and adds it to the list.
     *
     * @param file
     */
    private void loadArea(File file) {
        //Load in the area from the file
        Area area = (Area) FileManipulator.readObject(file.getPath(), file.getName());
        //Add the area wuth its existing area id to the master area list
        //TODO: Check for duplicate area IDs before adding to list
        masterAreaList.put(area.getAreaID(), area);
        //TODO: Update available area and room IDs
    }
}
