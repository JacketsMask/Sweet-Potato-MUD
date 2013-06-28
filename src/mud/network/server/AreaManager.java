package mud.network.server;


// Making a small change.

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import mud.geography.Area;
import mud.geography.Room;
import file.FileManipulator;

/**
 * A room manager that holds a list of all the areas and rooms in the MUD.
 * Manages room ID numbers by providing unique room numbers when they are
 * requested.
 *
 * @author Japhez
 */
public class AreaManager implements Serializable {

    private ArrayList<Integer> availableAreaIDs;
    //A master list of all areas in the MUD
    private HashMap<Integer, Area> masterAreaList;
    private ArrayList<Integer> availableRoomIDs;
    private Room respawnRoom;

    public AreaManager() {
        //Add 1000 unique room ids to be made available
        availableRoomIDs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            availableRoomIDs.add(i + 1);
        }
        availableAreaIDs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            availableAreaIDs.add(i + 1);
        }
        masterAreaList = new HashMap<>();
    }

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
        FileManipulator.writeObject(testArea, "/areas/", testArea.getName() + ".area");
        //Add the area to the master area list
        masterAreaList.put(getUniqueAreaID(), testArea);
    }

    public void setRespawnRoom(Room respawnRoom) {
        this.respawnRoom = respawnRoom;
    }

    public Room getRespawnRoom() {
        return respawnRoom;
    }

    public int getUniqueRoomID() {
        return availableRoomIDs.remove(0);
    }

    public int getUniqueAreaID() {
        return availableAreaIDs.remove(0);
    }

    public static void main(String[] args) {
        new AreaManager().createStarterArea();
    }
}
