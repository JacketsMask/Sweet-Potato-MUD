package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import mud.GameMaster;
import mud.network.server.Connection;

/**
 * An interface to be implemented by all interpreters.
 *
 * @author Japhez
 */
public abstract class Interpreter {

    protected HashMap<InetAddress, Connection> clientMap;
    protected GameMaster master;

    public abstract boolean interpret(Connection sender, ParsedInput input);

    /**
     * Removes the client from the client map.
     *
     * @param client the client to be removed
     */
    public final void removeFromClientMap(Connection client) {
        clientMap.remove(client.getClientAddress());
    }

    /**
     * Removes the client from the client map.
     *
     * @param address the address (key) of the client to be removed
     */
    public final void removeFromClientMap(InetAddress address) {
        clientMap.remove(address);
    }

    public ArrayList<CommandHelpFile> getCommandsAndUsages() {
        return null;
    }
}
