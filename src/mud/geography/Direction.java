package mud.geography;

/**
 * Directions, there ya go.
 *
 * @author Japhez
 */
public enum Direction {

    NORTH, EAST, SOUTH, WEST, UP, DOWN;

    @Override
    public String toString() {
        if (this.equals(NORTH)) {
            return "north";
        }
        if (this.equals(EAST)) {
            return "east";
        }
        if (this.equals(SOUTH)) {
            return "south";
        }
        if (this.equals(WEST)) {
            return "west";
        }
        if (this.equals(UP)) {
            return "up";
        }
        if (this.equals(DOWN)) {
            return "down";
        }
        return "?";
    }

    /**
     * Returns the enumeration for the direction representation of the passed
     * string if it exists, otherwise null.
     *
     * @param direction a string, i.e. "north"
     * @return the Direction enum representation of the passed string, or null
     */
    public static Direction getDirectionFromString(String direction) {
        if (direction.equalsIgnoreCase("north")) {
            return NORTH;
        }
        if (direction.equalsIgnoreCase("east")) {
            return EAST;
        }
        if (direction.equalsIgnoreCase("south")) {
            return SOUTH;
        }
        if (direction.equalsIgnoreCase("west")) {
            return WEST;
        }
        if (direction.equalsIgnoreCase("up")) {
            return UP;
        }
        if (direction.equalsIgnoreCase("down")) {
            return DOWN;
        }
        return null;
    }

    /**
     * @return an array of all Directions
     */
    public static synchronized Direction[] getDirections() {
        return new Direction[]{NORTH, EAST, SOUTH, WEST, UP, DOWN};
    }

    /**
     * Returns the opposite Direction of the passed Direction.
     *
     * @param direction the direction to find the opposite of
     * @return the opposite Direction
     */
    public static Direction getOppositeDirection(Direction direction) {
        if (direction.equals(NORTH)) {
            return SOUTH;
        }
        if (direction.equals(WEST)) {
            return EAST;
        }
        if (direction.equals(SOUTH)) {
            return NORTH;
        }
        if (direction.equals(EAST)) {
            return WEST;
        }
        if (direction.equals(UP)) {
            return DOWN;
        }
        if (direction.equals(DOWN)) {
            return UP;
        }
        return null; //Unreachable
    }
}
