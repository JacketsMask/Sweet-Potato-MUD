package mud;

import mud.geography.Room;
import mud.network.server.Connection;

/**
 * A MUD player.
 *
 * @author Japhez
 */
public class Player {

    private Connection connection;
    private String name;
    private Room currentRoom;
    private Room respawnRoom;

    public Player(String name) {
        this.name = name;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * This is the primary way you should move a player from one room to
     * another. Removes the player from an existing old room, sets their current
     * room to the new room, and adds them to that room's occupant list.
     *
     * @param currentRoom
     */
    public void setCurrentRoom(Room currentRoom) {
        //If the player is currently in a room
        if (currentRoom != null) {
            currentRoom.removePlayer(this); //Remove player from old room
        }
        this.currentRoom = currentRoom; //Set current room
        currentRoom.addPlayer(this); //Add player to current room
    }

    /**
     * @return this player's respawn room
     */
    public Room getRespawnRoom() {
        return respawnRoom;
    }

    public void setRespawnRoom(Room respawnRoom) {
        this.respawnRoom = respawnRoom;
    }

    /**
     * Sends a message to this via the player's established connection.
     *
     * @param message the message to send
     */
    public void sendMessage(String message) {
        connection.sendMessage(message);
    }

    /**
     * Gets and displays the description of the room to the player.
     */
    public void look() {
        sendMessage("\n" + currentRoom.getName() + "\n" + currentRoom.getDescription() + "\n" + currentRoom.getExits());
    }
}
