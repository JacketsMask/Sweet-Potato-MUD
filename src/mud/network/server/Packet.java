package mud.network.server;

/**
 * Packets are used to communicate between a client and the server.
 *
 * @author Jacob Dorman
 */
public class Packet {

    private ProtocolCommand command;
    private String arguments;

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

    public String getArguments() {
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
