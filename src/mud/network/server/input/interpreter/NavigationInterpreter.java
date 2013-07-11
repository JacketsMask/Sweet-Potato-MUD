package mud.network.server.input.interpreter;

import mud.Player;
import mud.geography.Direction;
import mud.network.server.Connection;

/**
 * An interpreter that checks to see if a player is attempting to move around.
 *
 * @author Japhez
 */
public class NavigationInterpreter implements Interpretable {

    public NavigationInterpreter() {
    }

    @Override
    public boolean interpret(Connection sender, ParsedInput input) {
        Player player = sender.getPlayer();
        //Check to see if the player is trying to move in a direction
        if (input.getWordCount() == 1) {
            String firstWord = input.getFirstWord();
            if (firstWord.equalsIgnoreCase("n") || firstWord.equalsIgnoreCase("north")) {
                player.move(Direction.NORTH);
                return true;
            }
            if (firstWord.equalsIgnoreCase("e") || firstWord.equalsIgnoreCase("east")) {
                player.move(Direction.EAST);
                return true;
            }
            if (firstWord.equalsIgnoreCase("s") || firstWord.equalsIgnoreCase("south")) {
                player.move(Direction.SOUTH);
                return true;
            }
            if (firstWord.equalsIgnoreCase("w") || firstWord.equalsIgnoreCase("west")) {
                player.move(Direction.WEST);
                return true;
            }
            if (firstWord.equalsIgnoreCase("u") || firstWord.equalsIgnoreCase("up")) {
                player.move(Direction.UP);
                return true;
            }
            if (firstWord.equalsIgnoreCase("d") || firstWord.equalsIgnoreCase("down")) {
                player.move(Direction.DOWN);
                return true;
            }
        }
        return false;
    }
}
