package mud.items;

/**
 * Armor can be equipped by characters and will provide bonuses and effects.
 *
 * @author Japhez
 */
public class Armor extends Item {

    private int armorValue;
    private ArmorSlot armorSlot;

    public Armor(int weight, int value, String shortName, String longName, int armorValue, ArmorSlot armorType) {
        super(weight, value, shortName, longName);
        this.armorValue = armorValue;
    }

    public int getArmorValue() {
        return armorValue;
    }

    public void setArmorValue(int armorValue) {
        this.armorValue = armorValue;
    }

    public ArmorSlot getArmorSlot() {
        return armorSlot;
    }

    public void setArmorType(ArmorSlot armorType) {
        this.armorSlot = armorType;
    }

    public enum ArmorSlot {

        HEAD, NECK, TORSO, ARMS, HANDS, LEGS, BOOTS
    }
}
