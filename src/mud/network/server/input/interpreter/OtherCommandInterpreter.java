package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Set;
import mud.network.server.Connection;
import mud.network.server.log.ConsoleLog;

/**
 *
 * @author Japhez
 */
public class OtherCommandInterpreter implements Interpretable {

    private HashMap<InetAddress, Connection> clientMap;

    public OtherCommandInterpreter(HashMap<InetAddress, Connection> clientMap) {
        this.clientMap = clientMap;
    }

    @Override
    public boolean interpret(Connection sender, ParsedInput input) {
        String firstWord = input.getFirstWord();
        //Check to see if the client is disconnecting
        if (firstWord.equalsIgnoreCase("disconnect")) {
            System.out.println(ConsoleLog.log() + sender.getPlayer().getName() + " has disconnected.");
            return true;
            //Check to see if the player is communicating with the server
        } else if (firstWord.equalsIgnoreCase("serve")) {
            String message = input.getWordsStartingAtIndex(1);
            if (message == null) {
                sender.sendMessage("Why tell the server nothing?");
                return true;
            } else {
                System.out.println(ConsoleLog.log() + sender.getPlayer().getName() + " talks to the server: " + "\"" + message + "\"");
                sender.sendMessage("I hear you.");
                return true;
            }
        }
        return false;
    }

    /**
     * @return a list of server clients that have connected to the server
     */
    private String getUserList() {
        String serverClients = "Server clients:\n";
        Set<InetAddress> keySet = clientMap.keySet();
        for (InetAddress s : keySet) {
            //Retrieve each client's name
            Connection client = clientMap.get(s);
            serverClients += client.getPlayer().getName();
            //If the client is online, mark them as online
            serverClients += " [Online]";
            serverClients += "\n";
        }
        //Remove the final newline character
        serverClients = serverClients.substring(0, serverClients.length() - 1);
        return serverClients;
    }

    /**
     * Returns true if the passed name is a client that has connected to the
     * server.
     *
     * @param name the name to check
     * @return true if the name has been taken by a client
     */
    private boolean isNameTaken(String name) {
        Set<InetAddress> keySet = clientMap.keySet();
        for (InetAddress i : keySet) {
            if (clientMap.get(i).getPlayer().getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
