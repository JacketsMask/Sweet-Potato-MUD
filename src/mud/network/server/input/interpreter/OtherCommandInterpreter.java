package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import mud.network.server.Connection;

/**
 *
 * @author Japhez
 */
public class OtherCommandInterpreter extends Interpreter {

    public OtherCommandInterpreter(HashMap<InetAddress, Connection> clientMap) {
        this.clientMap = clientMap;
    }

    @Override
    public ArrayList<CommandHelpFile> getCommandsAndUsages() {
        String category = "Other";
        ArrayList<CommandHelpFile> commands = new ArrayList<>();
        commands.add(new CommandHelpFile(category, "disconnect", "Disconnects you from the server."));
        commands.add(new CommandHelpFile(category, "who", "Lists players connected to the server."));
        return commands;
    }

    @Override
    public boolean interpret(Connection sender, ParsedInput input) {
        String firstWord = input.getFirstWord();
        //Check to see if client is looking
        if (input.getWordCount() == 1 && (firstWord.equalsIgnoreCase("look") || firstWord.equalsIgnoreCase("l"))) {
            sender.getPlayer().look();
            return true;
        }
        //Check to see if the client is disconnecting
        if (firstWord.equalsIgnoreCase("disconnect")) {
            sender.cleanUpConnection();
            return true;
            //Check if the user wants to know who is currently connected.
        } else if (firstWord.equalsIgnoreCase("who")) {
            String who = "Players online:\n";
            for (Connection c : clientMap.values()) {
                who += "   " + c.getPlayer().getName() + "\n";
            }
            sender.sendMessage(who);
            return true;
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
}
