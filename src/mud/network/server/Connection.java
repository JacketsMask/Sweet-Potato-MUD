package mud.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import mud.Player;
import mud.network.server.input.interpreter.Interpretable;
import mud.network.server.input.interpreter.MasterInterpreter;
import mud.network.server.input.interpreter.ParsedInput;
import mud.network.server.log.ConsoleLog;

/**
 * A Connection that that allows for reading and writing from the client socket.
 * This class and subclasses should be used for server/client interaction.
 */
public class Connection {

    private final int SLEEP_DELAY = 100;
    private Player player; //A connection always has a player, even if it's a temporary one
    private InetAddress address;
    private Socket client;
    private boolean connected;
    private ClientWriter writer;
    private ClientReader reader;
    private Thread writerThread;
    private Thread readerThread;
    private Interpretable interpreter;

    /**
     * Creates a new Connection which contains connection information for the
     * server.
     *
     * @param client
     * @throws IOException
     */
    public Connection(Socket client, InetAddress address, Player player, Interpretable interpreter) throws IOException {
        this.client = client;
        this.address = address;
        connected = true;
        this.player = player;
        this.interpreter = interpreter;
        this.writer = new ClientWriter();
        this.reader = new ClientReader(this);
        startThreads();
    }

    /**
     * Sets an input interpret for this client's input to be interpreted by.
     *
     * @param interpreter
     */
    public void setInterpreter(Interpretable interpreter) {
        this.interpreter = interpreter;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Sends the passed string to this client to be printed.
     *
     * @param message
     */
    public void sendMessage(String message) {
        writer.queue.offer(message);
    }

    /**
     * Cleanly closes the connection of the passed client.
     *
     * @throws IOException
     */
    public synchronized void cleanUpConnection() {
        if (connected) {
            try {
                disconnectClient();
                reader.fromClient.close();
                writer.toClient.close();
                System.out.println(ConsoleLog.log() + player.getName() + " has disconnected.");
                //Remove the player from their current room
                player.getCurrentRoom().removePlayer(player);
                connected = false;
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public String toString() {
        return address.toString() + "(" + player.getName() + ")";
    }

    /**
     * @return this client's Internet address
     */
    public InetAddress getClientAddress() {
        return address;
    }

    /**
     * Starts the reading and writing threads for this client.
     */
    private void startThreads() {
        readerThread = new Thread(reader);
        readerThread.start();
        writerThread = new Thread(writer);
        writerThread.start();
    }

    /**
     * Interrupts the client's threads, cutting off communication to and from
     * this client. Also marks this client as being offline.
     */
    public void disconnectClient() {
        readerThread.interrupt();
        writerThread.interrupt();
    }

    /**
     * @return true if this connection is still open
     */
    public boolean isOnline() {
        return connected;
    }

    /**
     * @return the master interpreter for this player's connection
     */
    public MasterInterpreter getMasterInterpreter() {
        return (MasterInterpreter) interpreter;
    }

    /**
     * Reads input from this client and acts upon it.
     */
    private class ClientReader extends Thread {

        private Connection connection;
        private BufferedReader fromClient;

        /**
         * Creates a new ChatReader to receive input from the client.
         *
         * @throws IOException
         */
        public ClientReader(Connection connection) throws IOException {
            this.connection = connection;
            fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }

        /**
         * Continually waits for input from the client, and interprets them when
         * they arrive.
         */
        @Override
        public synchronized void run() {
            while (!Thread.interrupted()) {
                String message;
                try {
                    while ((message = fromClient.readLine()) != null) {
                        ParsedInput parsedInput = new ParsedInput(message);
                        if (parsedInput.getWordCount() == 0) {
                            sendMessage("...");
                        } else {
                            boolean interpreted = interpreter.interpret(connection, parsedInput);
                            if (!interpreted) {
                                sendMessage("I don't get your meaning.");
                            }
                        }
                    }
                    Thread.sleep(SLEEP_DELAY);
                } catch (NoSuchElementException | IllegalStateException | IOException | InterruptedException e) {
                    if (connected) {
                        System.out.println(ConsoleLog.log() + " " + connection.getClientAddress() + " reader crashed.");
                    }
                    break;
                }
            }
            cleanUpConnection();
        }
    }

    /**
     * Writes messages to this client.
     */
    private class ClientWriter implements Runnable {

        private LinkedBlockingQueue queue;
        private PrintWriter toClient;

        /**
         * Creates a new ChatWriter that allows the server to write to a client.
         *
         * @throws IOException
         */
        public ClientWriter() throws IOException {
            queue = new LinkedBlockingQueue();
            toClient = new PrintWriter(client.getOutputStream(), true);
        }

        public LinkedBlockingQueue getQueue() {
            return queue;
        }

        /**
         * Continually waits for a queued message to send to the client, and
         * sends them in the order they are received.
         */
        @Override
        public synchronized void run() {
            while (!Thread.interrupted()) {
                //If there's something in the queue, retrieve it and send it
                if (!queue.isEmpty()) {
                    String poll = (String) queue.poll();
                    toClient.println(poll);
                }
                //Sleep to prevent rediculous CPU usage costs, haha
                try {
                    Thread.sleep(SLEEP_DELAY);
                } catch (InterruptedException ex) {
                    //In case the client disconnects
                    cleanUpConnection();
                    return;
                }
            }
            cleanUpConnection();
        }
    }
}