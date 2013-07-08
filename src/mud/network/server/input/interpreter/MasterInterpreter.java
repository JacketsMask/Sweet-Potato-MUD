/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.HashMap;
import mud.GameMaster;
import mud.network.server.Connection;

/**
 * An interpreter that should be used when a client is under normal conditions
 * (connected, logged in, playing the game). Uses several sub-interpreters to
 * attempt to properly act on input.
 *
 * @author Japhez
 */
public class MasterInterpreter implements Interpretable {

    private GameMaster master;
    private HashMap<InetAddress, Connection> clientMap;
    private NavigationInterpreter navigationInterpreter;
    private ChatInterpreter chatInterpreter;
    private OtherCommandInterpreter otherInterpreter;
    private static final String PROTOCOL_COMMANDS = "Commands:"
            + "\n/who (display all connected players)"
            + "\n/tell player_name message (send a personal message)"
            + "\n/clear (clears the chat window)"
            + "\n/connect address:port"
            + "\n/disconnect (disconnect from the server)";

    public MasterInterpreter(HashMap<InetAddress, Connection> clientMap, GameMaster master) {
        this.clientMap = clientMap;
        this.master = master;
        navigationInterpreter = new NavigationInterpreter(clientMap);
        chatInterpreter = new ChatInterpreter(clientMap, this.master);
        otherInterpreter = new OtherCommandInterpreter(clientMap);
    }

    @Override
    public boolean interpret(Connection sender, ParsedInput input) {
        //Check movement commands
        if (navigationInterpreter.interpret(sender, input)) {
            return true;
        }
        //Check chat commands
        if (chatInterpreter.interpret(sender, input)) {
            return true;
        }
        //Check other commands
        if (otherInterpreter.interpret(sender, input)) {
            return true;
        }
        return false;
    }
}
