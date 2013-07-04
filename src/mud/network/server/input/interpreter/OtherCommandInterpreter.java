package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Set;
import mud.network.server.ClientConnection;
import mud.network.server.Packet;
import mud.network.server.ProtocolCommand;
import mud.network.server.log.ConsoleLog;

/**
 *
 * @author Japhez
 */
public class OtherCommandInterpreter implements Interpretable {

    private HashMap<InetAddress, ClientConnection> clientMap;

    public OtherCommandInterpreter(HashMap<InetAddress, ClientConnection> clientMap) {
        this.clientMap = clientMap;
    }

    @Override
    public boolean interpret(InetAddress sender, Packet packet) {
        ClientConnection client = clientMap.get(sender);
        ProtocolCommand command = packet.getCommand();
        Object arguments = packet.getArguments();
        //Check to see if the client is disconnecting
        if (command.equals(ProtocolCommand.DISCONNECT)) {
            System.out.println(ConsoleLog.log() + client.getPlayer().getName() + " has disconnected.");
            return true;
            //Check to see if the player is communicating with the server
        } else if (command.equals(ProtocolCommand.TALK_TO_SERVER)) {
            String message = (String) arguments;
            System.out.println(ConsoleLog.log() + client.getPlayer().getName() + " talks to the server: " + "\"" + message + "\"");
            client.sendMessage("I hear you.");
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
            ClientConnection client = clientMap.get(s);
            serverClients += client.getPlayer().getName();
            //If the client is online, mark them as online
            if (clientMap.get(s).isOnline()) {
                serverClients += " [Online]";
            } else {
                serverClients += " [Offline]";
            }
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
