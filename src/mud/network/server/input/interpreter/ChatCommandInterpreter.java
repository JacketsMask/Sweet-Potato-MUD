/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Set;
import mud.network.server.Client;
import mud.network.server.Packet;
import mud.network.server.ProtocolCommand;

/**
 * This ServerChatHelper contains methods used to facilitate communication
 * between players within the game. It will require information provided from
 * the GameServer class, and in turn will provide abstraction to make that class
 * more manageable.
 *
 * @author Jacob Dorman
 */
public class ChatCommandInterpreter implements Interpretable {

    private HashMap<InetAddress, Client> clientMap;

    public ChatCommandInterpreter(HashMap<InetAddress, Client> clientMap) {
        this.clientMap = clientMap;
    }
    /**
     * Interprets the packet passed in, and takes action if this packet relates
     * to communication, then returns true. Otherwise this method returns false.
     *
     * @param player the player that sent the command
     * @param packet the Packet sent by the Client
     * @param args the arguments passed
     * @return true if the command is communication related, false otherwise
     */
    @Override
    public boolean interpret(InetAddress sender, Packet packet) {
        ProtocolCommand command = packet.getCommand();
        Object arguments = packet.getArguments();
        //Check to see if the message is a tell
        if (command.equals(ProtocolCommand.TELL)) {
            tellPlayer(clientMap.get(sender), arguments);
        }
        if (command.equals(ProtocolCommand.SAY)) {
            sendMesageToRoom(clientMap.get(sender), arguments);
        }
        return false;
    }

    private void tellPlayer(Client sender, Object arguments) {
    }

    private void sendMesageToRoom(Client sender, Object message) {
    }

    /**
     * Sends the given string to all connected clients except the given client.
     *
     * @param message the message to send
     */
    private void sendToAllConnectedUsers(String message, mud.network.server.Client exception) {
        Set<InetAddress> keySet = clientMap.keySet();
        for (InetAddress inetAddress : keySet) {
            mud.network.server.Client nextClient = clientMap.get(inetAddress);
            if (nextClient != exception && nextClient.isOnline()) {
                nextClient.sendMessage(message);
            }
        }
    }
}
