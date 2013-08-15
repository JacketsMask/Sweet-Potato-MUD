package mud.characters;

import java.io.Serializable;
import mud.geography.Direction;
import mud.geography.Room;

/**
 * An NPC (non-player character) is a character with which players may interact
 * in some ways. Players are really just controlled NPCs with some more
 * features.
 *
 * @author Japhez
 */
public class NPC implements Serializable {

    //The name of this character
    protected String name;
    //The current hitpoints of this character
    protected int currentHPs;
    //The maximum hitpoints of this character
    protected int maxHPs;
    //The current mana of this character
    protected int currentMana;
    //The maximum mana of this character
    protected int maxMana;
    //The equipment this character has equipped
    protected Equipment equipment;
    //The room the character is currently in
    protected Room currentRoom;
    //The room this character respawns in
    protected Room respawnRoom;

    //Create a new NPC with the passed name
    public NPC(String name) {
        this.name = name;
        equipment = new Equipment();
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentHPs() {
        return currentHPs;
    }

    public void setCurrentHPs(int currentHPs) {
        this.currentHPs = currentHPs;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public int getMaxHPs() {
        return maxHPs;
    }

    public void setMaxHPs(int maxHPs) {
        this.maxHPs = maxHPs;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public Room getRespawnRoom() {
        return respawnRoom;
    }

    public void setRespawnRoom(Room respawnRoom) {
        this.respawnRoom = respawnRoom;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Removes the NPC from an existing old room, sets their current room to the
     * new room, and adds them to that room's occupant list. Similar to the move
     * method, but allows for direction-less transport.
     *
     * @param newRoom
     */
    public void setCurrentRoom(Room newRoom) {
        //If the NPC is currently in a room
        if (this.currentRoom != null) {
            this.currentRoom.removeNPC(this); //Remove NPC from old room
        }
        this.currentRoom = newRoom; //Set current room
        newRoom.addNPC(this); //Add NPC to current room
    }

    /**
     * Attempts to move this NPC in the given direction, and displays a message
     * to the room if the direction was invalid.
     *
     * @param direction
     */
    public void move(Direction direction) {
        Room roomInDirection = currentRoom.getRoomInDirection(direction);
        if (roomInDirection != null) {
            //Remove the NPC from the NPC list in the current room
            currentRoom.removeNPC(this);
            //Send departure message to the room
            currentRoom.sendMessageToRoom(name + " leaves " + direction + ".");
            //Send arrival message to the room
            roomInDirection.sendMessageToRoom(name + " arrives from the " + Direction.getOppositeDirection(direction) + ".");
            //Move the NPC in the given direction
            setCurrentRoom(roomInDirection);
        } //If the direction is null, send a message to the room showing that the NPC is confused
        else {
            currentRoom.sendMessageToRoom(name + " appears confused.");
        }
    }
}
