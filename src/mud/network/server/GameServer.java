package mud.network.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import mud.GameMaster;
import mud.Player;
import mud.network.server.input.interpreter.LoginInterpreter;
import mud.network.server.log.ConsoleLog;

/**
 * This is the top-level server implementation. Lower level features such as
 * chat should be abstracted to allow for easier debugging and testing. Only top
 * level server interactions should be directly done here. (connecting,
 * disconnecting, etc.)
 *
 * @author Japhez
 */
public class GameServer implements Runnable {

    public static final int DEFAULT_PORT = 1337;
    private ServerSocket serverSocket;
    private HashMap<InetAddress, Connection> clientMap; //The master client list that holds connection
    private GameMaster gameMaster;
    private boolean localOnly;

    /**
     * Creates a new chat server operating at the passed port.
     *
     * @param port the port to operate the server on
     * @param localOnly whether or not this server will only accept one local
     * connection
     * @throws IOException
     */
    public GameServer(int port, boolean localOnly) throws IOException {
        this.localOnly = localOnly;
        System.out.println(ConsoleLog.log() + "Server starting on port " + port);
        serverSocket = new ServerSocket(port);
        clientMap = new HashMap<>();
        gameMaster = new GameMaster();
    }

    /**
     * Continually checks for new connections. When a client connects, requests
     * their player information.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Socket newClient = serverSocket.accept();
                connect(newClient);
                System.out.println(ConsoleLog.log() + "Player connected from "
                        + newClient.getInetAddress());
                //If the server is local only, only accept one connection
                if (localOnly) {
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Connection connect(Socket socket) {
        Connection connection = null;
        try {
            //Create a temporary (unlisted) character until info is validated
            Player player = new Player("NoOne");
            connection = new Connection(socket, socket.getInetAddress(), player, new LoginInterpreter(clientMap, gameMaster));
            //The player stores a reference to their current connection for convenience
            player.setConnection(connection);
            connection.sendMessage("Please enter your character name.");
        } catch (IOException ex) {
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            return connection;
        }
        return connection;
    }
}
