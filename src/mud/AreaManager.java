package mud;

import file.FileManipulator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import mud.geography.Area;
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
    private int roomIDCount;
    private ArrayList<Integer> freeRoomIDs;
    private Room respawnRoom;
    private final String PATH = "/areas/";
    private final String EXTENSION = ".area";

    public AreaManager() {
        areaIDCount = 1;
        roomIDCount = 1;
        freeRoomIDs = new ArrayList<>();
        freeAreaIDs = new ArrayList<>();
        masterAreaList = new HashMap<>();
        System.out.println(ConsoleLog.log() + "Creating starter area.");
        createStarterArea();
    }

    /**
     * Saves the state of the passed area to the area folder (use this to store
     * the "default" state of an area).
     *
     * @param area the area to be saved
     */
    public void saveArea(Area area) {
        FileManipulator.writeObject(area, GameMaster.MAIN_DATA_PATH + PATH, area.getName() + EXTENSION);
    }

    /**
     * Creates a basic cross shaped area of 5 rooms to be used as a base for
     * area design and expansion.
     */
    public void createStarterArea() {
        //Create a new area with a new area ID
        Area testArea = new Area("Test Area", getUniqueAreaID());
        //Create 5 rooms
        Room center = new Room(getUniqueRoomID());
        Room north = new Room(getUniqueRoomID());
        Room east = new Room(getUniqueRoomID());
        Room south = new Room(getUniqueRoomID());
        Room west = new Room(getUniqueRoomID());
        //Set the name and exits for the center room
        center.setName("Center room.");
        center.setNorth(north);
        center.setEast(east);
        center.setSouth(south);
        center.setWest(west);
        //Set the respawn room
        setRespawnRoom(center);
        //Set the name and exits for the northern room
        north.setName("North room.");
        north.setSouth(center);
        //Set the name and exits for the eastern room
        east.setName("East room.");
        east.setWest(center);
        //Set the name and exits for the southern room
        south.setName("South room.");
        south.setNorth(center);
        //Set the name and exits for the western room
        west.setName("West room.");
        west.setEast(center);
        //All all of the rooms to the test area
        testArea.addRoom(center);
        testArea.addRoom(north);
        testArea.addRoom(east);
        testArea.addRoom(south);
        testArea.addRoom(west);
        //Save the area to a file
        saveArea(testArea);
        //Add the area to the master area list
        masterAreaList.put(getUniqueAreaID(), testArea);
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

    public static void main(String[] args) {
        
    }
}
