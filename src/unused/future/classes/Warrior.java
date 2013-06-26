package unused.future.classes;

import unused.future.character.NPC;
import java.util.ArrayList;
import java.util.HashMap;
import unused.future.simulator.Roller;

/**
 * A warrior excels with strength, constitution, melee weapons, and shields.
 *
 * Warriors are capable of wielding two-handed weapons at the expense of a
 * shield, but cannot dual-wield weapons.
 *
 * @author Japhez
 */
public class Warrior extends NPC {

    public Warrior() {
        roller = new Roller();
        maxHPs = 20;
        currentHPs = 20;
        currentMana = 10;
        name = "Warrior " + roller.rollDie(100); //TODO: Change naming process
        attackSpeed = 5;
        weapons = new HashMap<>();
        armor = new HashMap<>();
    }

    @Override
    public int getAttackDamage() {
        return roller.rollDie(6);
    }

    @Override
    public NPC findTargetAndAct(ArrayList<NPC> friendlyTeam, ArrayList<NPC> enemyTeam) {
        //Attack enemy
        NPC target = enemyTeam.get(0);
        //Attack the target with random damage
        target.receiveDamage(this, getAttackDamage());
        //TODO: Apply tactics in target decision making process
        return target; //Right now just returns the firest enemy in the list
    }
}
