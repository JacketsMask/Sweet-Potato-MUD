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
}
