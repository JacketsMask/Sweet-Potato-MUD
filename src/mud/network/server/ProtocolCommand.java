package mud.network.server;

/**
 * An enumeration of protocols used to communicate between client and server. To
 * communicate, an object array of size 2 should be used to communicate. The
 * first element should contain one of the commands below, and the second should
 * be any String arguments for that command.
 *
 * @author Jacob Dorman
 */
public enum ProtocolCommand {

    //Commands from client to server
    //Communication
    WHO, HELP, LOOK, TELL, SAY,
    //Navigation
    NORTH, EAST, WEST, SOUTH
}
