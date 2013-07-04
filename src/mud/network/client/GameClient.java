package mud.network.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;
import mud.network.server.Packet;
import mud.network.server.ProtocolCommand;

/**
 * The client connection to the server. This is used to communicate information
 * with the server. A player should be able to interact with the game
 * exclusively using the client, as long as a connection to a server is
 * established (this would be the case if a friend connects to an existing
 * server).
 *
 * NOTE: Don't use System.out.print() unless just debugging. All output should
 * go to the JTextArea. The reason for this is that the server will be printing
 * to the console, and we don't need that kind of complication.
 *
 * @author Jacob Dorman
 */
public class GameClient {

    private boolean connected;
    private JTextArea output;
    private JTextField commandBar;
    private Socket server;
    private ClientReader reader;
    private Thread readerThread;
    private ObjectOutputStream out;

    /**
     * Creates a new ChatClient with the passed name. Attempts to connect to the
     * passed address and port.
     *
     * @param address the address of the server
     * @param port the port of the server
     * @param output the output JTextArea
     * @param commandBar the input JTextField
     * @throws UnknownHostException
     * @throws IOException
     */
    public GameClient(String address, int port, JTextArea output, JTextField commandBar) throws UnknownHostException, IOException {
        this.output = output;
        this.commandBar = commandBar;
        addWritingActionListener();
        this.server = new Socket(address, port);
        reader = new ClientReader();
        out = new ObjectOutputStream(server.getOutputStream());
        output.append("Connection established on " + address + ":" + port);
        connected = true;
    }

    /**
     * Creates a new ChatClient with only the passed name, and allows the user
     * to connect manually later.
     *
     * @param output
     * @param commandBar
     */
    public GameClient(JTextArea output, JTextField commandBar) throws IOException {
        this.output = output;
        this.commandBar = commandBar;
        addWritingActionListener();
        connected = false;
        commandBar.setText("/connect address:port");
    }

    /**
     * Attempts to connect to the passed address and port.
     *
     * @param address
     * @param port
     */
    public void connect(String address, int port) throws UnknownHostException, IOException {
        this.server = new Socket(address, port);
        reader = new ClientReader();
        out = new ObjectOutputStream(server.getOutputStream());
        connected = true;
        commandBar.setText("/help");
    }

    /**
     * Cleanly closes the connection to the server by interrupting the reading
     * and listening threads, closing the connections to the reader and writer,
     * and finally closing the server connection.
     *
     * @throws IOException
     */
    private void cleanUpConnection() throws IOException {
        readerThread.interrupt();
        reader.fromServer.close();
        server.close();
        output.append("\nYou no longer feel connected by an unseen force...");
        connected = false;
    }

    /**
     * @return true if this client is currently connected to a server
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * When enter is pressed, the command is interpreted and sent to the server.
     */
    private void addWritingActionListener() {
        commandBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = commandBar.getText();
                commandBar.selectAll();
                //Otherwise attempt to send command to the server if connected
                if (!connected) {
                    //Check to see if the user is connecting
                    if (text.contains("/connect ")) {
                        try {
                            //Connect to the address and port
                            text = text.substring(9);
                            text = text.trim();
                            String[] split = text.split(":");
                            String address = split[0];
                            int port = Integer.parseInt(split[1]);
                            output.append("\nAttempting to connect to " + address + ":" + port);
                            connect(address, port);
                        } catch (NumberFormatException | IOException ex) {
                            output.append("\nYou've failed to reconnect with reality.");
                        }
                    } else {
                        output.append("\nYou seem out of touch with reality... (use /connect address:port)");
                    }
                    //Check to see if the user is trying to talk to the server
                } else if (text.length() > 8 && text.substring(0, 7).equalsIgnoreCase("/serve ")) {
                    Packet packet = new Packet(ProtocolCommand.TALK_TO_SERVER, text.substring(7, text.length()));
                    try {
                        out.writeObject(packet);
                    } catch (IOException ex) {
                        Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } //Check to see if the user is disconnecting
                else if (text.equalsIgnoreCase(
                        "/disconnect")) {
                    try {
                        output.append("\nCome back soon...");
                        Packet packet = new Packet(ProtocolCommand.DISCONNECT, null);
                        out.writeObject(packet);
                        cleanUpConnection();
                    } catch (IOException ex) {
                        Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } //Check to see if the user is clearing the screen
                else if (text.equalsIgnoreCase(
                        "/clear")) {
                    output.setText("");
                    DefaultCaret caret = (DefaultCaret) output.getCaret();
                    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                } else {
                    //Malformed input, no need to send it to the server
                    output.append("\n\"" + text + "\" not recognized.");
                }
            }
        });
    }

    /**
     * A reader for a client program that receives messages from the server and
     * prints them to console.
     */
    public class ClientReader implements Runnable {

        private BufferedReader fromServer;

        /**
         * Create a new client reader.
         *
         * @throws IOException
         */
        public ClientReader() throws IOException {
            fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
            readerThread = new Thread(this);
            readerThread.start();
        }

        /**
         * Print any incoming messages from the server to the chat window.
         */
        @Override
        public void run() {
            String s;
            while (!Thread.interrupted()) {
                try {
                    if ((s = fromServer.readLine()) != null) {
                        //Display the server's message
                        output.append("\n" + s);
                    }
                } catch (IOException ex) {
                    break;
                }
            }
            //Clean up connection in case of error
            try {
                cleanUpConnection();
            } catch (IOException ex) {
                Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
