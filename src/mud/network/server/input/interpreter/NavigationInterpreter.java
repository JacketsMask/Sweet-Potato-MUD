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
 * An interpreter that checks to see if a player is attempting to move around.
 *
 * @author Japhez
 */
public class NavigationInterpreter implements Interpretable {

    private HashMap<InetAddress, Connection> clientMap;

    public NavigationInterpreter(HashMap<InetAddress, Connection> clientMap) {
        this.clientMap = clientMap;
    }

    @Override
    public boolean interpret(Connection sender, ParsedInput input) {
        //Check to see if the player is trying to move in a direction
        if (input.getWordCount() == 1) {
            String firstWord = input.getFirstWord();
            if (firstWord.equalsIgnoreCase("n") || firstWord.equalsIgnoreCase("north")) {
                movePlayer(sender, Direction.NORTH);
                return true;
            }
            if (firstWord.equalsIgnoreCase("e") || firstWord.equalsIgnoreCase("east")) {
                movePlayer(sender, Direction.EAST);
                return true;
            }
            if (firstWord.equalsIgnoreCase("s") || firstWord.equalsIgnoreCase("south")) {
                movePlayer(sender, Direction.SOUTH);
                return true;
            }
            if (firstWord.equalsIgnoreCase("w") || firstWord.equalsIgnoreCase("west")) {
                movePlayer(sender, Direction.WEST);
                return true;
            }
            if (firstWord.equalsIgnoreCase("u") || firstWord.equalsIgnoreCase("up")) {
                movePlayer(sender, Direction.UP);
                return true;
            }
            if (firstWord.equalsIgnoreCase("d") || firstWord.equalsIgnoreCase("down")) {
                movePlayer(sender, Direction.DOWN);
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to move the player in the given direction, and then gives them
     * feedback about whether it was successful or not.
     *
     * @param sender
     * @param direction
     */
    private void movePlayer(Connection sender, Direction direction) {
        Player player = sender.getPlayer();
        Room currentRoom = player.getCurrentRoom();
        Room roomInDirection = currentRoom.getRoomInDirection(direction);
        if (roomInDirection != null) {
            sender.sendMessage("You move" + direction + ".");
            currentRoom.removePlayer(player); //Remove the player from the old room
            player.setCurrentRoom(roomInDirection); //Move the player reference
            player.getCurrentRoom().addPlayer(player); //Add the player to the list in the new room
        } else {
            sender.sendMessage("You cannot move in that direction.");
        }
    }
}
