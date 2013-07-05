/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.HashMap;
import mud.Player;
import mud.geography.Direction;
import mud.geography.Room;
import mud.network.server.Connection;

/**
 *
 * @author Japhez
 */
public class NavigationCommandInterpreter implements Interpretable {

    HashMap<InetAddress, Connection> clientMap;

    public NavigationCommandInterpreter(HashMap<InetAddress, Connection> clientMap) {
        this.clientMap = clientMap;
    }

    @Override
    public boolean interpret(Connection sender, ParsedInput input) {
        Player player = sender.getPlayer();
        //Check to see if the player is trying to move in a direction
        return false;
    }

    /**
     * Attempts to move the player in the given direction, and then gives them
     * feedback about whether it was successful or not.
     *
     * @param client
     * @param direction
     */
    private void movePlayer(Connection client, Direction direction) {
        Player player = client.getPlayer();
        Room currentRoom = player.getCurrentRoom();
        Room roomInDirection = currentRoom.getRoomInDirection(direction);
        if (roomInDirection != null) {
            player.setCurrentRoom(roomInDirection);
            client.sendMessage("You move" + direction + ".");
        } else {
            client.sendMessage("You cannot move in that direction.");
        }
    }
}
