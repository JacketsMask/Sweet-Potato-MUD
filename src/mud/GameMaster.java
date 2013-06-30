package mud;

import file.FileManipulator;
import java.util.ArrayList;

/**
 * The game manager that stores area, and player data. This is the top-level
 * class that provides functionality for the rest of the program.
 *
 * @author Japhez
 */
public class GameMaster {

    public final static String MAIN_DATA_PATH = "/data/";
    private AreaManager areaManager;
    private ArrayList<Player> players;

    public GameMaster() {
        //Read in an existing area manager if possible, otherwise create a new one
        if (FileManipulator.fileExists(MAIN_DATA_PATH, "AreaManager.data")) {
            areaManager = (AreaManager) FileManipulator.readObject(MAIN_DATA_PATH, "AreaManager.data");
        } else {
            areaManager = new AreaManager();
        }
        players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }
}
