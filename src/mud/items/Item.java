package mud.items;

import java.io.Serializable;

/**
 * A generic Item that has attributes that all inheriting items should share.
 *
 * @author Japhez
 */
public class Item implements Serializable {

    private int weight;
    private int value;
    private String shortName;
    private String longName;

    public Item(int weight, int value, String shortName, String longName) {
        this.weight = weight;
        this.value = value;
        this.shortName = shortName;
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
