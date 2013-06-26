package mud;

import mud.geography.Room;
import mud.network.server.AreaManager;
import java.util.Scanner;

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

    public void testPlayerExploration() {
        Player player = new Player("Tester");
        AreaManager areaManager = new AreaManager();
        Room center = new Room(areaManager.getUniqueRoomID());
        Room north = new Room(areaManager.getUniqueRoomID());
        Room east = new Room(areaManager.getUniqueRoomID());
        Room south = new Room(areaManager.getUniqueRoomID());
        Room west = new Room(areaManager.getUniqueRoomID());
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
        //Add the player to a room
        center.addPlayer(player);
        player.setCurrentRoom(center);
        while (true) {
            Room thisRoom = player.getCurrentRoom();
            Scanner sc = new Scanner(System.in);
            System.out.println(thisRoom.getName());
            String nextDirection = sc.next();
            if (nextDirection.equalsIgnoreCase("n")) {
                if (player.currentRoom.getNorth() != null) {
                    player.currentRoom.removePlayer(player);
                    player.currentRoom.getNorth().addPlayer(player);
                    player.setCurrentRoom(player.currentRoom.getNorth());
                }
            } else if (nextDirection.equalsIgnoreCase("e")) {
                if (player.currentRoom.getEast() != null) {
                    player.currentRoom.removePlayer(player);
                    player.currentRoom.getEast().addPlayer(player);
                    player.setCurrentRoom(player.currentRoom.getEast());
                }
            } else if (nextDirection.equalsIgnoreCase("s")) {
                if (player.currentRoom.getSouth() != null) {
                    player.currentRoom.removePlayer(player);
                    player.currentRoom.getSouth().addPlayer(player);
                    player.setCurrentRoom(player.currentRoom.getSouth());
                }
            } else if (nextDirection.equalsIgnoreCase("w")) {
                if (player.currentRoom.getWest() != null) {
                    player.currentRoom.removePlayer(player);
                    player.currentRoom.getWest().addPlayer(player);
                    player.setCurrentRoom(player.currentRoom.getWest());
                }
            }
        }
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

    public static void main(String[] args) {
        Player bob = new Player("Bob");
        bob.testPlayerExploration();
    }
}
