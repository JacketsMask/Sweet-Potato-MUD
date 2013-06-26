/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mud.network.server.chat;

import java.net.InetAddress;
import java.util.HashMap;
import mud.network.server.GameServer.Client;
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
public class ServerChatHelper {

    private HashMap<InetAddress, Client> clientMap;

    public ServerChatHelper(HashMap<InetAddress, Client> clientMap) {
        this.clientMap = clientMap;
    }

    /**
     * Interprets the packet passed in, and takes action if this packet relates
     * to communication, then returns true. Otherwise this method returns false.
     *
     * @param packet the Packet sent by the Client
     * @param sender the Client sending the packet
     * @return true if the command is communication related, false otherwise
     */
    public boolean interpretInput(Packet packet, Client sender) {
        ProtocolCommand command = packet.getCommand();
        String arguments = packet.getArguments();
        //Check to see if the message is a tell
        if (command.equals(ProtocolCommand.TELL)) {
        }
        if (command.equals(ProtocolCommand.SAY)) {
        }
        return false;
    }
}
