/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.HashMap;
import mud.network.server.Connection;

/**
 * An interpreter that should be used when a client is under normal conditions
 * (connected, logged in, playing the game). Uses several sub-interpreters to
 * attempt to properly act on input.
 *
 * @author Japhez
 */
public class MasterInterpreter implements Interpretable {

    private HashMap<InetAddress, Connection> clientMap;
    private ChatCommandInterpreter chatHelper;
    private OtherCommandInterpreter otherHelper;
    private static final String PROTOCOL_COMMANDS = "Commands:"
            + "\n/who (display all connected players)"
            + "\n/tell player_name message (send a personal message)"
            + "\n/clear (clears the chat window)"
            + "\n/connect address:port"
            + "\n/disconnect (disconnect from the server)";

    public MasterInterpreter(HashMap<InetAddress, Connection> clientMap) {
        this.clientMap = clientMap;
        chatHelper = new ChatCommandInterpreter(clientMap);
        otherHelper = new OtherCommandInterpreter(clientMap);
    }

    @Override
    public boolean interpret(Connection sender, ParsedInput input) {
        //Check other commands
        if (otherHelper.interpret(sender, input)) {
            return true;
        }
        return false;
    }
}
