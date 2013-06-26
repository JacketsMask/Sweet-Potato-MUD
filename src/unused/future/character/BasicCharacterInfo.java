package unused.future.character;

import unused.future.items.Armor;
import unused.future.items.Weapon;
import java.util.HashMap;
import unused.future.simulator.Roller;

/**
 *
 * @author Japhez
 */
public abstract class BasicCharacterInfo {

    //The current hitpoints of this character
    protected int currentHPs;
    //The maximum hitpoints of this character
    protected int maxHPs;
    //The current mana of this character
    protected int currentMana;
    //The maximum mana of this character
    protected int maxMana;
    //The name of this character
    protected String name;
    //The attack speed of this character, used to calculate initiative and chance for multiple attacks
    protected int attackSpeed;
    //Warriors can equip just one weapon
    protected HashMap<Weapon.WeaponSlots, Weapon> weapons;
    //A hashmap to contain up to a piece of armor for each armor slot
    protected HashMap<Armor.ArmorSlots, Armor> armor;
    //A roller for determining random values
    protected Roller roller;
    //A flag for quickly determining if this character is dead
    protected boolean dead;

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public boolean isDead() {
        return dead;
    }

    /**
     * @return determined attack damage for this character
     */
    abstract public int getAttackDamage();

    public int getCurrentHPs() {
        return currentHPs;
    }

    public void setCurrentHPs(int currentHPs) {
        this.currentHPs = currentHPs;
    }

    public int getMaxHPs() {
        return maxHPs;
    }

    public void setMaxHPs(int maxHPs) {
        this.maxHPs = maxHPs;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Weapon.WeaponSlots, Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(HashMap<Weapon.WeaponSlots, Weapon> weapons) {
        this.weapons = weapons;
    }

    public HashMap<Armor.ArmorSlots, Armor> getArmor() {
        return armor;
    }

    public void setArmor(HashMap<Armor.ArmorSlots, Armor> armor) {
        this.armor = armor;
    }
}
