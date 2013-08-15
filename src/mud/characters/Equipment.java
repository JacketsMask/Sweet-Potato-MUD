package mud.characters;

import java.util.HashMap;
import mud.items.Armor;
import mud.items.Armor.ArmorSlot;
import mud.items.Item;
import mud.items.Weapon;
import mud.items.Weapon.WeaponType;

/**
 * Contains information about equipment that a character is wearing/holding.
 *
 * @author Japhez
 */
public class Equipment {

    HashMap<WeaponType, Weapon> weapons;
    HashMap<ArmorSlot, Armor> armor;
    Item leftHand;
    Item rightHand;

    public Equipment() {
        weapons = new HashMap<>();
        armor = new HashMap<>();
    }

    /**
     * @return the total armor value of all equipped armor
     */
    public int getTotalArmorValue() {
        int totalValue = 0;
        for (Armor a : armor.values()) {
            totalValue += a.getArmorValue();
        }
        return totalValue;
    }

    /**
     * Attempts to equip the passed armor, and returns true if the slot was open
     * and the armor was equipped, or false if the slot was taken and the item
     * wasn't equipped.
     *
     * @param armor the armor to equip
     * @return true if equipped, false otherwise
     */
    public boolean equipArmor(Armor armor) {
        //Check to see if something is already equipped in that armor slot
        if (this.armor.containsKey(armor.getArmorSlot())) {
            return false;
        } else {
            this.armor.put(armor.getArmorSlot(), armor);
            return true;
        }
    }

    /**
     * Attempts to equip the passed weapon, and returns true if successful or
     * false if not.
     *
     * @param weapon
     * @return true if equipped, false otherwise
     */
    public boolean equipWeapon(Weapon weapon) {
        //Two-handed
        if (weapon.getWeaponType().equals(Weapon.WeaponType.TWO_HANDED)) {
            //Check to see if we're equipping a two-handed weapon when we don't have both hands free
            if (leftHand != null || rightHand != null) {
                return false;
            } else {
                //Equip weapon in both hands
                leftHand = weapon;
                rightHand = weapon;
            }
        }
        //One-handed
        if (weapon.getWeaponType().equals(Weapon.WeaponType.ONE_HANDED)) {
            if (leftHand == null) {
                leftHand = weapon;
                return true;
            } else if (rightHand == null) {
                rightHand = weapon;
                return true;
            } else {
                //No hands free
                return false;
            }
        }
        //Unhandled weapon type?
        return false;
    }
}
