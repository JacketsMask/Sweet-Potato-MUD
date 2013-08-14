package mud;

import java.io.Serializable;
import java.util.ArrayList;
import mud.geography.Direction;
import mud.geography.Room;
import mud.network.server.Connection;

/**
 * A MUD player.
 *
 * @author Japhez
 */
public class Player extends NPC implements Serializable {

    private Connection connection;
    private char[] password;
    boolean needsSaving;

    public Player(String name) {
        super(name);
        needsSaving = false;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public boolean needsSaving() {
        return needsSaving;
    }

    public void hasBeenSaved() {
        this.needsSaving = false;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Removes the player from an existing old room, sets their current room to
     * the new room, and adds them to that room's occupant list. Similar to the
     * move method, but more explicit
     *
     * @param currentRoom
     */
    @Override
    public void setCurrentRoom(Room currentRoom) {
        //If the player is currently in a room
        if (this.currentRoom != null) {
            this.currentRoom.removePlayer(this); //Remove player from old room
        }
        this.currentRoom = currentRoom; //Set current room
        currentRoom.addPlayer(this); //Add player to current room
        needsSaving = true;
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
     * Gets and sends the description of the room and contents to the player.
     */
    public void look() {
        if (currentRoom == null) {
            sendMessage("You seem to be in limbo.  How odd.");
            return;
        }
        String description = "\n";
        //Get room title;
        description += currentRoom.getName();
        //Get room description;
        description += "\n" + currentRoom.getDescription();
        //Get room exits
        description += "\n" + currentRoom.getFormattedExits();
        //Get NPCs in room
        ArrayList<NPC> NPClist = currentRoom.getNPCs();
        if (!NPClist.isEmpty()) {
            for (NPC n : NPClist) {
                description += "\n" + n.getName() + " is here.";
            }
        }
        //Get players in room
        ArrayList<Player> playersList = currentRoom.getPlayers();
        if (!playersList.isEmpty()) {
            for (Player p : playersList) {
                //List the player if this player isn't that player...what?
                if (!p.equals(this)) {
                    description += "\n" + p.getName() + " is here.";
                }
            }
        }
        sendMessage(description);
    }

    /**
     * Attempts to move this player in the given direction, and then gives them
     * feedback about whether it was successful or not.
     *
     * @param direction the direction the player is trying to move
     */
    @Override
    public void move(Direction direction) {
        Room roomInDirection = currentRoom.getRoomInDirection(direction);
        if (roomInDirection != null) {
            //Remove the player from the player list in the current room
            currentRoom.removePlayer(this);
            //Send departure message to the room
            currentRoom.sendMessageToRoom(name + " leaves " + direction + ".");
            //Send moving message to the player
            sendMessage("You move " + direction + ".");
            //Send arrival message to the room
            roomInDirection.sendMessageToRoom(name + " arrives from the " + Direction.getOppositeDirection(direction) + ".");
            //Move the player in the given direction
            setCurrentRoom(roomInDirection);
            //Force the player to look
            look();
        } else {
            sendMessage("You cannot move in that direction.");
        }
    }
}
