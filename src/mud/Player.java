package mud;

import mud.geography.Room;

/**
 * A MUD player.
 *
 * @author Japhez
 */
public class Player {

    private String name;
    private Room currentRoom;

    public Player(String name) {
        this.name = name;
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

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
}
