package mud.network.server.input.interpreter;

import java.util.ArrayList;
import mud.AreaManager;
import mud.characters.NPC;
import mud.characters.Player;
import mud.geography.Area;
import mud.geography.Direction;
import mud.geography.Room;
import mud.network.server.Connection;

/**
 * World shaper commands allow a user to directly modify the structure of an
 * area, create new rooms, and modify the content and exits of those rooms.
 *
 * A world shaper can change data about the area or room that they are in. If
 * they create a new room, it must be adjacent to the room they are in somehow.
 *
 * Only adjacent rooms can be deleted.
 *
 * @author Japhez
 */
public class WorldShaperInterpreter extends Interpreter {

    private AreaManager areaManger;

    public WorldShaperInterpreter(AreaManager areaManager) {
        this.areaManger = areaManager;
    }

    @Override
    public ArrayList<CommandHelpFile> getCommandsAndUsages() {
        String category = "World Shaper";
        ArrayList<CommandHelpFile> commands = new ArrayList<>();
        commands.add(new CommandHelpFile(category, "area info", "Displays information about the area you are in."));
        commands.add(new CommandHelpFile(category, "room info", "Displays information about the room you were in."));
        commands.add(new CommandHelpFile(category, "area name {name}", "Change the name of the area you are in."));
        commands.add(new CommandHelpFile(category, "room name {name}", "Change the name of the room you are in."));
        commands.add(new CommandHelpFile(category, "room description {description}", "Change the description of the room you are in."));
        commands.add(new CommandHelpFile(category, "link {direction} {roomID}", "Links an exit of this room to the specified room."));
        commands.add(new CommandHelpFile(category, "unlink {direction}", "Removes the exit from this room in the given direction."));
        commands.add(new CommandHelpFile(category, "create {direction}", "Creates a new room in the specified direction."));
        commands.add(new CommandHelpFile(category, "delete {direction}", "Unlinks all connection to the room in the passed direction, then deletes the room forever."));
        commands.add(new CommandHelpFile(category, "npc create {name}", "Creates a new NPC in this room with the passed name."));
        commands.add(new CommandHelpFile(category, "npc delete {name}", "Deletes the NPC with the passed name if they are currently in the room."));
        return commands;
    }

    private boolean commandAreaNameChange(Connection sender, ParsedInput input) {
        //Change current area's name - "area" "name" "new name"
        if (input.getWordCount() > 2 && input.getWordsUpToIndex(1).equalsIgnoreCase("area name")) {
            String areaName = input.getWordsStartingAtIndex(2);
            sender.getPlayer().getCurrentRoom().getArea().setName(areaName);
            sender.sendMessage("This area is now know as " + areaName + ".");
            return true;
        }
        return false;
    }

    private boolean commandRoomNameChange(Connection sender, ParsedInput input) {
        //Change current room's name - "room" "name" "new name"
        ArrayList<String> words = input.getWords();
        if (input.getWordCount() > 2 && input.getWordsUpToIndex(1).equalsIgnoreCase("room name")) {
            String newName = "";
            for (int i = 2; i < input.getWordCount(); i++) {
                newName += words.get(i) + " ";
            }
            newName = newName.trim();
            sender.getPlayer().getCurrentRoom().setName(newName);
            sender.sendMessage("Room title updated.");
            return true;
        }
        return false;
    }

    private boolean commandRoomDescriptionChange(Connection sender, ParsedInput input) {
        if (input.getWordCount() > 2 && input.getWordsUpToIndex(1).equalsIgnoreCase("room description")) {
            String newDescription = input.getWordsStartingAtIndex(2);
            newDescription = newDescription.trim();
            sender.getPlayer().getCurrentRoom().setDescription(newDescription);
            sender.sendMessage("Room description updated.");
            return true;
        }
        return false;
    }

    private boolean commandRoomLink(Connection sender, ParsedInput input) {
        //Link this room to an existing room - "link" "direction" "roomID"
        ArrayList<String> words = input.getWords();
        if (input.getWordCount() > 2 && input.getFirstWord().equalsIgnoreCase("link") && (Direction.getDirectionFromString(words.get(1)) != null)) {
            Direction direction = Direction.getDirectionFromString(words.get(1));
            int roomID;
            try {
                roomID = Integer.parseInt(words.get(2));
            } catch (NumberFormatException ex) {
                sender.sendMessage("That's not a number.");
                return true;
            }
            Room targetRoom = areaManger.getRoom(roomID);
            if (sender.getPlayer().getCurrentRoom().linkToRoom(direction, targetRoom)) {
                if (direction.equals(Direction.UP)) {
                    sender.sendMessage(targetRoom.getName() + " is now above you.");
                } else if (direction.equals((Direction.DOWN))) {
                    sender.sendMessage(targetRoom.getName() + " now lies below you.");
                } else {
                    sender.sendMessage(targetRoom.getName() + " now lies to the " + direction + " of here.");
                }
                return true;
            } else {
                sender.sendMessage("Unable to link rooms.  Are both room exists available?");
                return true;
            }
        }
        return false;
    }

    private boolean commandCreateRoom(Connection sender, ParsedInput input) {
        //Create a new room and link to this - "create" "direction"
        Direction direction = Direction.getDirectionFromString(input.getWordAtIndex(1));
        Room currentRoom = sender.getPlayer().getCurrentRoom();
        if (input.getWordCount() == 2 && direction != null) {
            //Create a new room and link to this - "create" "direction"
            if (input.getFirstWord().equalsIgnoreCase("create")) {
                //First check to make sure direction is clear
                if (currentRoom.getRoomInDirection(direction) != null) {
                    sender.sendMessage("There's already a room in that direction.");
                    return true;
                }
                //Create the new room
                Room newRoom = new Room(sender.getPlayer().getCurrentRoom().getArea(), areaManger);
                //Link the room to this one
                currentRoom.linkToRoom(direction, newRoom);
                if (direction.equals(Direction.UP)) {
                    sender.sendMessage("Something appears above you.");
                } else if (direction.equals(Direction.DOWN)) {
                    sender.sendMessage("Something appears below you.");
                } else {
                    sender.sendMessage("Something interesting just appeared to the " + direction + "." + " (#" + newRoom.getRoomID() + ")");
                }
                return true;
            }
        }
        return false;
    }

    private boolean commandUnlinkRoom(Connection sender, ParsedInput input) {
        //Unlink the passed direction - "unlink" "direction"
        Direction direction = Direction.getDirectionFromString(input.getWordAtIndex(1));
        if (input.getWordCount() == 2 && direction != null && input.getFirstWord().equalsIgnoreCase("unlink")) {
            sender.getPlayer().getCurrentRoom().unlinkExits(new Direction[]{direction});
            if (direction.equals(Direction.UP)) {
                sender.sendMessage("There's nothing overhead anymore.");
            } else if (direction.equals(Direction.DOWN)) {
                sender.sendMessage("There's nothing below you anymore.");
            } else {
                sender.sendMessage("Nothing to the " + direction + " of here seems interesting anymore.");
            }
            return true;
        }
        return false;
    }

    private boolean commandDeleteRoom(Connection sender, ParsedInput input) {
        //Delete the room in the passed direction - "delete" "direction"
        Direction direction = Direction.getDirectionFromString(input.getWordAtIndex(1));
        if (input.getWordCount() == 2 && direction != null && input.getFirstWord().equalsIgnoreCase("delete")) {
            Room doomedRoom = sender.getPlayer().getCurrentRoom().getRoomInDirection(direction);
            //Check to make sure that the room exists
            if (doomedRoom != null) {
                areaManger.deleteRoom(doomedRoom);
                sender.sendMessage(doomedRoom.getName() + " vanishes into nothingness.");
                return true;
            } else {
                sender.sendMessage("There's already nothing over there.  Mission accomplished I guess?");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean interpret(Connection sender, ParsedInput input) {
        if (commandAreaNameChange(sender, input)) {
            return true;
        }
        if (commandRoomNameChange(sender, input)) {
            return true;
        }
        if (commandRoomDescriptionChange(sender, input)) {
            return true;
        }
        if (commandRoomLink(sender, input)) {
            return true;
        }
        if (commandCreateRoom(sender, input)) {
            return true;
        }
        if (commandUnlinkRoom(sender, input)) {
            return true;
        }
        if (commandDeleteRoom(sender, input)) {
            return true;
        }
        if (commandRoomInfo(sender, input)) {
            return true;
        }
        if (commandAreaInfo(sender, input)) {
            return true;
        }
        if (commandNPCCreate(sender, input)) {
            return true;
        }
        if (commandNPCDelete(sender, input)) {
            return true;
        }
        if (commandNPCModifyName(sender, input)) {
            return true;
        }
        System.out.println(input.getOriginalInput() + input.getWordCount());
        return false;
    }

    private boolean commandRoomInfo(Connection sender, ParsedInput input) {
        //Get info about the room - "room" "info"
        if (input.getWordCount() == 2 && input.getWordsUpToIndex(1).equalsIgnoreCase("room info")) {
            sender.sendMessage("Room ID: #" + sender.getPlayer().getCurrentRoom().getRoomID());
            return true;
        }
        return false;
    }

    /**
     * Creates a new room connected to the room the player is in. The direction
     * is the direction to the new room.
     *
     * @param player
     * @param direction the exit to the new room
     */
    public void createRoom(Player player, Direction direction) {
        //Verify that the exit of the current room doesn't already exist
        Room roomInDirection = player.getCurrentRoom().getRoomInDirection(direction);
        if (roomInDirection != null) {
            player.sendMessage("There's already a room to the " + direction);
        } else {
            Room newRoom = new Room(player.getCurrentRoom().getArea(), areaManger);
            player.getCurrentRoom().setRoomInDirection(direction, newRoom);
        }
    }

    private boolean commandAreaInfo(Connection sender, ParsedInput input) {
        //Get info about the area - "area" "info"
        if (input.getWordCount() == 2 && input.getWordsUpToIndex(1).equalsIgnoreCase("area info")) {
            Area area = sender.getPlayer().getCurrentRoom().getArea();
            sender.sendMessage("Area" + "#" + area.getAreaID() + ": " + area.getName());
            return true;
        }
        return false;
    }

    private boolean commandNPCCreate(Connection sender, ParsedInput input) {
        //Create NPC
        if (input.getWordCount() == 3 && input.getWordsUpToIndex(1).equalsIgnoreCase("npc create")) {
            String NPCName = input.getWordAtIndex(2);
            //Validate name length
            if (NPCName.length() < 2) {
                sender.sendMessage("That's not a very meaningful name.");
                return true;
            }
            //Create the new NPC
            NPC npc = new NPC(NPCName);
            sender.getPlayer().getCurrentRoom().addNPC(npc);
            sender.sendMessage(npc.getName() + " appears out of thin air!");
            return true;
        }
        return false;
    }

    private boolean commandNPCDelete(Connection sender, ParsedInput input) {
        //Delete NPC
        if (input.getWordCount() == 3 && input.getWordsUpToIndex(1).equalsIgnoreCase("npc delete")) {
            ArrayList<String> words = input.getWords();
            Room currentRoom = sender.getPlayer().getCurrentRoom();
            String NPCName = words.get(2);
            ArrayList<NPC> NPCs = currentRoom.getNPCs();
            for (NPC npc : NPCs) {
                //Check for a matching name, and remove if it exists
                if (npc.getName().equalsIgnoreCase(NPCName)) {
                    currentRoom.removeNPC(npc);
                    sender.sendMessage(npc.getName() + " is no more!");
                    return true;
                }
            }
            sender.sendMessage("What's a \"" + NPCName + "\"?");
            return true;
        }
        return false;
    }

    private boolean commandNPCModifyName(Connection sender, ParsedInput input) {
        //Change NPC name
        if (input.getWordCount() == 5 && input.getWordsUpToIndex(2).equalsIgnoreCase("npc modify name")) {
            ArrayList<String> words = input.getWords();
            String NPCName = words.get(3);
            String NPCNewName = words.get(4);
            ArrayList<NPC> NPCs = sender.getPlayer().getCurrentRoom().getNPCs();
            for (NPC npc : NPCs) {
                //Check for a matching name, and change the name if it exists
                if (npc.getName().equalsIgnoreCase(NPCName)) {
                    sender.sendMessage(npc.getName() + " is now " + NPCNewName + ".");
                    npc.setName(NPCNewName);
                    return true;
                }
            }
            sender.sendMessage("I can't find " + NPCName + ".");
            return true;
        }
        return false;
    }
}
