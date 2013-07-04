package mud.network.server;

import java.io.Serializable;

/**
 * Packets are used to communicate between a client and the server.
 *
 * @author Jacob Dorman
 */
public class Packet implements Serializable {

    private ProtocolCommand command;
    private Object arguments;

    /**
     * Create a new packet with the passed ProtocolCommand and arguments.
     *
     * @param command
     * @param arguments
     */
    public Packet(ProtocolCommand command, String arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public Object getArguments() {
        return arguments;
    }

    public ProtocolCommand getCommand() {
        return command;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public void setCommand(ProtocolCommand command) {
        this.command = command;
    }
}
