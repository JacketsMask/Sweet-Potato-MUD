package mud;

import file.FileManipulator;
import java.util.ArrayList;
import mud.geography.Room;

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

    /**
     * Creates a new GameMaster, then attempts to load in an existing
     * AreaManger. If there is no saved AreaManager, it creates a new one (and a
     * sample area).
     */
    public GameMaster() {
        //Read in an existing area manager if possible, otherwise create a new one
        if (FileManipulator.fileExists(MAIN_DATA_PATH, "AreaManager.data")) {
            areaManager = (AreaManager) FileManipulator.readObject(MAIN_DATA_PATH, "AreaManager.data");
        } else {
            areaManager = new AreaManager();
        }
        players = new ArrayList<>();
    }

    /**
     * Adds the passed player to the player list.
     *
     * @param player the player to be added
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Removes the passed player from the player list.
     *
     * @param player the player to be removed
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * @return the area manager
     */
    public AreaManager getAreaManager() {
        return areaManager;
    }

    /**
     * Returns the player with the passed name if they exist in the system.
     * Otherwise returns null.
     *
     * @param name
     * @return the player, or null
     */
    public Player getPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Respawns the passed player and forces them to look at the room.
     *
     * @param player
     */
    public void respawnPlayer(Player player) {
        Room respawnRoom = player.getRespawnRoom();
        //See if the respawn point exists specifically for this player
        //If it doesn't exist, use the area respawn room
        if (respawnRoom == null) {
            player.setRespawnRoom(areaManager.getRespawnRoom());
            respawnRoom = player.getRespawnRoom();
        }
        if (respawnRoom == null) {
            System.out.println("crap, respawn room is null somehow");
        }
        player.setCurrentRoom(respawnRoom);
        player.look();
    }
}
