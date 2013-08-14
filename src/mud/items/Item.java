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
    private String name;

    public Item(int weight, int value, String name) {
        this.weight = weight;
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
