/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.HashMap;
import mud.Player;
import mud.network.server.Client;
import mud.network.server.Packet;

/**
 *
 * @author Japhez
 */
public class MasterInterpreter implements Interpretable {

    private HashMap<InetAddress, Client> clientMap;
    private ChatCommandInterpreter chatHelper;
    private OtherCommandInterpreter otherHelper;
    private static final String PROTOCOL_COMMANDS = "Commands:"
            + "\n/who (display all connected players)"
            + "\n/tell player_name message (send a personal message)"
            + "\n/clear (clears the chat window)"
            + "\n/connect address:port"
            + "\n/disconnect (disconnect from the server)";

    public MasterInterpreter(HashMap<InetAddress, Client> clientMap) {
        this.clientMap = clientMap;
        chatHelper = new ChatCommandInterpreter(clientMap);
        otherHelper = new OtherCommandInterpreter(clientMap);
    }

    @Override
    public boolean interpret(InetAddress sender, Packet packet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
