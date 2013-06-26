package mud;

import mud.geography.Area;
import mud.geography.Room;
import mud.network.server.AreaManager;
import file.FileManipulator;

/**
 * The game manager that stores area, NPC, and player data. This is the
 * top-level class that provides functionality for the rest of the program.
 *
 * @author Japhez
 */
public class GameMaster {

    private final String MAIN_DATA_PATH = "/data/";
    private AreaManager areaManger;

    public GameMaster() {
        //Read in an existing area manager if possible, otherwise create a new one
        if (FileManipulator.fileExists(MAIN_DATA_PATH, "AreaManager.data")) {
            areaManger = (AreaManager) FileManipulator.readObject(MAIN_DATA_PATH, "AreaManager.data");
        } else {
            areaManger = new AreaManager();
        }
        test();
    }

    private void test() {
        int uniqueAreaID = areaManger.getUniqueAreaID();
        Area testArea = new Area("Test Area", uniqueAreaID);
        Room center = new Room(areaManger.getUniqueRoomID());
        Room north = new Room(areaManger.getUniqueRoomID());
        Room east = new Room(areaManger.getUniqueRoomID());
        Room south = new Room(areaManger.getUniqueRoomID());
        Room west = new Room(areaManger.getUniqueRoomID());
        center.setName("Center room.");
        center.setNorth(north);
        center.setEast(east);
        center.setSouth(south);
        center.setWest(west);
        north.setName("North room.");
        north.setSouth(center);
        east.setName("East room.");
        east.setWest(center);
        south.setName("South room.");
        south.setNorth(center);
        west.setName("West room.");
        west.setEast(center);


        testArea.addRoom(center);
        testArea.addRoom(north);
        testArea.addRoom(east);
        testArea.addRoom(south);
        testArea.addRoom(west);

    }
}
