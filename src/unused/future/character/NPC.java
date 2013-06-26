package unused.future.character;

import java.util.ArrayList;

/**
 * Characters are the basic members of a combat party.
 *
 * @author Japhez
 */
public abstract class NPC extends BasicCharacterInfo {

    /**
     * Analyzes the friendly and enemy teams and takes action based off of that
     * knowledge.
     *
     * #TODO: Add parameter for combat analysis to speed up processing
     *
     * @param friendlyTeam
     * @param enemyTeam
     * @return the target NPC
     */
    abstract public NPC findTargetAndAct(ArrayList<NPC> friendlyTeam, ArrayList<NPC> enemyTeam);

    /**
     * Inflicts the passed damage on this character. If this character goes
     * below 0 hitpoints, set the isDead flag to true.
     *
     * @param attackDamage the damage this character will sustain
     */
    public void receiveDamage(NPC source, int attackDamage) {
        currentHPs -= attackDamage; //TODO: Reduce damage sustained by armor value
//        System.out.print(source.getName() + " attacks " + getName() + " ");
//        System.out.println("for " + attackDamage + " damage! [" + currentHPs + "/" + maxHPs + "]");
        if (currentHPs < 1) {
            dead = true;
        }
    }
}
