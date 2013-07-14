/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.ArrayList;
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
public class MasterInterpreter extends Interpreter {

    private ArrayList<Interpreter> interpreters;

    public MasterInterpreter(HashMap<InetAddress, Connection> clientMap, GameMaster master) {
        this.clientMap = clientMap;
        this.master = master;
        //Initialize basic interpreters for gameplay
        interpreters = new ArrayList<>();
        interpreters.add(new NavigationInterpreter());
        interpreters.add(new ChatInterpreter(this.clientMap, this.master));
        interpreters.add(new OtherCommandInterpreter(this.clientMap));
    }

    @Override
    public boolean interpret(Connection sender, ParsedInput input) {
        //Check interpreters
        for (Interpreter i : interpreters) {
            if (i.interpret(sender, input)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the passed interpreter to the list of interpreters for this client.
     *
     * @param interpreter
     */
    public void addInterpreter(Interpreter interpreter) {
        interpreters.add(interpreter);
    }

    /**
     * Removes the passed interpreter from the list of interpreters for this
     * client.
     *
     * @param interpreter
     */
    public void removeInterpreter(Interpreter interpreter) {
        interpreters.remove(interpreter);
    }
}
