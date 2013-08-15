package mud.network.server.input.interpreter;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import mud.GameMaster;
import mud.characters.Player;
import mud.PlayerManager;
import mud.network.server.Connection;

/**
 * An interpreter that handles commands and input related to logging in.
 *
 * @author Japhez
 */
public class LoginInterpreter extends Interpreter {

    private LoginStage currentStage;
    private String suggestedName;
    private PlayerManager playerManager;
    private static final int NAME_MIN_LENGTH = 4;
    private static final int NAME_MAX_LENGTH = 8;

    public LoginInterpreter(HashMap<InetAddress, Connection> clientMap, GameMaster master) {
        this.clientMap = clientMap;
        this.master = master;
        currentStage = LoginStage.NAME_SELECTION;
        playerManager = master.getPlayerManager();
    }

    /**
     * An enumeration to store the current state of the login stage.
     */
    public enum LoginStage {

        CONNECTION, NAME_SELECTION, NAME_CREATION, PASSWORD_SELECTION, PASSWORD_INPUT, PASSWORD_FINISHED
    }

    @Override
    public boolean interpret(Connection sender, ParsedInput input) {
        //Unused at the moment
        if (currentStage.equals(LoginStage.CONNECTION)) {
            return true;
        }
        //Name selection stage
        if (currentStage.equals(LoginStage.NAME_SELECTION)) {
            //Attempt to reconcile player name
            //Multiple words
            if (input.getWordCount() != 1) {
                sender.sendMessage("Names can only be one word, please try again.");
                return true;
                //One word
            } else {
                suggestedName = input.getFirstWord();
                //Verify name length
                if (suggestedName.length() < NAME_MIN_LENGTH) {
                    sender.sendMessage("That's an awfully short name, don't you think? Try something with at least 4 characters.");
                    return true;
                }
                if (suggestedName.length() > NAME_MAX_LENGTH) {
                    sender.sendMessage("That's quite the mouthful.  Try something with less than 9 characters?");
                    return true;
                }
                //Make first letter in name uppercase, rest lower case
                suggestedName = Character.toUpperCase(suggestedName.charAt(0)) + suggestedName.substring(1).toLowerCase();
                //See if player data is in memory
                if (playerManager.getPlayer(suggestedName) != null) {
                    //Use player data from memory
                    sender.sendMessage("Please input your password.");
                    sender.setPlayer(playerManager.getPlayer(suggestedName));
                    currentStage = LoginStage.PASSWORD_INPUT;
                    return true;
                } else {
                    //Attempt to load in the player
                    Player loadedPlayer = playerManager.loadPlayer(suggestedName);
                    //Player name isn't yet taken
                    if (loadedPlayer == null) {
                        sender.sendMessage("I don't know that name, would you like to be " + suggestedName + "? (\"yes\"/\"no\")");
                        this.setCurrentStage(LoginStage.NAME_CREATION);
                        return true;
                        //Player name is taken
                    } else {
                        //Add them to the list in game if they haven't been yet
                        if (playerManager.getPlayer(loadedPlayer.getName()) == null) {
                            playerManager.addPlayer(loadedPlayer);
                        }
                        sender.sendMessage("Please input your password.");
                        currentStage = LoginStage.PASSWORD_INPUT;
                        sender.setPlayer(playerManager.getPlayer(suggestedName));
                        return true;
                    }
                }
            }
        }
        //Name creation stage
        if (currentStage.equals(LoginStage.NAME_CREATION)) {
            String firstWord = input.getFirstWord();
            if (firstWord.equalsIgnoreCase("yes")) {
                sender.sendMessage("Excellent! You will be known as " + suggestedName + ".");
                sender.sendMessage("What would you like your password to be, " + suggestedName + "?");
                currentStage = LoginStage.PASSWORD_SELECTION;
                return true;
            } else if (firstWord.equalsIgnoreCase("no")) {
                sender.sendMessage("Okay, who are you, then?");
                setCurrentStage(LoginStage.NAME_SELECTION);
                return true;
            } else {
                sender.sendMessage("A simple \"yes\" or \"no\" will suffice, thank you.");
                return true;
            }
        }
        //Password selection stage
        if (currentStage.equals(LoginStage.PASSWORD_SELECTION)) {
            validatePasswordChoice(sender, input);
            if (currentStage.equals(LoginStage.PASSWORD_FINISHED)) {
                createNewPlayer(sender);
                sender.sendMessage("Welcome, traveler...");
                finishLogin(sender);
            }
            return true;
        }
        //Password input stage
        if (currentStage.equals(LoginStage.PASSWORD_INPUT)) {
            if (checkPassword(sender.getPlayer(), input)) {
                sender.sendMessage("Welcome, " + sender.getPlayer().getName() + ".");
                finishLogin(sender);
            } else {
                sender.sendMessage("Invalid password, please try again.");
                System.out.println("Invalid password from " + sender.getClientAddress() + ". \"" + String.valueOf(sender.getPlayer().getPassword()) + "\" expected.");
            }
            return true;
        }
        return false;
    }

    /**
     * Sets the current stage of the login process.
     *
     * @param stage
     */
    public void setCurrentStage(LoginStage stage) {
        this.currentStage = stage;
    }

    /**
     * Returns true if the passed password matches the passed player.
     *
     * @param player
     * @param password
     * @return true if the password works, false otherwise
     */
    private boolean checkPassword(Player player, ParsedInput input) {
        char[] password = input.getFirstWord().toCharArray();
        return (Arrays.equals(password, player.getPassword()));
    }

    /**
     * Validates and sets the user's desired password.
     *
     * @param sender
     * @param input
     */
    private void validatePasswordChoice(Connection sender, ParsedInput input) {
        if (input.getWordCount() != 1) {
            sender.sendMessage("Your password must only be one word.");
            return;
        }
        if (input.getFirstWord().length() < 4 || input.getFirstWord().length() > 10) {
            sender.sendMessage("Your password should be between 4 and 10 characters.");
            return;
        }
        System.out.println("your password: " + input.getFirstWord());
        sender.getPlayer().setPassword(input.getFirstWord().toCharArray());
        currentStage = LoginStage.PASSWORD_FINISHED;
        sender.sendMessage("Remember your password, there's no other way to access your player currently!");
    }

    private void createNewPlayer(Connection connection) {
        Player player = connection.getPlayer();
        //Set the player's name to whatever they wanted
        player.setName(suggestedName);
        //Add the player to the master list
        playerManager.addPlayer(player);
        //Save player file
        playerManager.savePlayer(player);
    }

    /**
     * Change the interpreter and end the login process.
     *
     * @param connection
     */
    private void finishLogin(Connection connection) {
        Player player = connection.getPlayer();
        //Add the player to the visible client map
        clientMap.put(connection.getClientAddress(), connection);
        //Update the player's interpreter
        connection.setInterpreter(new MasterInterpreter(clientMap, master));
        //TODO: In the future don't grant every player world shaper status
//                if (master.playerIsWorldShaper(player)) {
        MasterInterpreter mint = connection.getMasterInterpreter();
        mint.addInterpreter(new WorldShaperInterpreter(master.getAreaManager()));
        //                }
        if (player.getCurrentRoom() == null) {
            master.respawnPlayer(connection.getPlayer());
        } else {
            //Return the player to the world
            player.setCurrentRoom(player.getCurrentRoom());
        }
        player.look();
    }
}
