package unused.future.simulator;

import unused.future.character.NPC;
import unused.future.classes.Warrior;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * At its most simple implementation, the combat engine performs the logic
 * necessary to resolve rounds of combat.
 * //TODO: Make this more object oriented
 *
 * @author Japhez
 */
public class CombatEngine {

    private ArrayList<NPC> team1;
    private ArrayList<NPC> team2;

    public CombatEngine() {
        team1 = new ArrayList<>();
        Warrior pickles = new Warrior();
        pickles.setName("T1: Pickles");
        team1.add(pickles);
        Warrior cucumber = new Warrior();
        cucumber.setName("T1: Cucumber");
        team1.add(cucumber);

        team2 = new ArrayList<>();
        Warrior dillPickles = new Warrior();
        dillPickles.setName("T2: Dill");
        team2.add(dillPickles);
    }

    /**
     * Resolves a fight between two opposing teams. Every combatant acts in
     * turn, alternating until one team is completely dead.
     */
    public int fight() {
        //Sort the teams based off their attack speed from lowest to highest
        Comparator<NPC> attackSpeedCompare = new Comparator<NPC>() {

            @Override
            public int compare(NPC t, NPC t1) {
                if (t.getAttackSpeed() < t1.getAttackSpeed()) {
                    return -1;
                }
                if (t.getAttackSpeed() == t1.getAttackSpeed()) {
                    return 0;
                }
                return 1;
            }
        };
        Collections.sort(team1, attackSpeedCompare);
        Collections.sort(team2, attackSpeedCompare);
        //Loop combat rounds until one team is completely dead (handled below)
        while (true) {
            //Track the number of turns left for each team
            int teamOneIndex = team1.size() - 1;
            int teamTwoIndex = team2.size() - 1;
            //Let each fighter have a turn
            while (teamOneIndex >= 0 || teamTwoIndex >= 0) {
                NPC target; //Whoever the next combat's target will be
                //Check to see if no team one fighters are left
                if (teamOneIndex < 0) {
                    //There has to be team two fighters remaining
                    NPC teamTwoFighter = team2.get(teamTwoIndex);
                    target = teamTwoFighter.findTargetAndAct(team2, team1);
                    teamTwoIndex--;
                } else if (teamTwoIndex < 0) {
                    //There has to be team one fighters remaining
                    NPC teamOneFighter = team1.get(teamOneIndex);
                    target = teamOneFighter.findTargetAndAct(team1, team2);
                    teamOneIndex--;
                } else {
                    NPC teamOneFighter = team1.get(teamOneIndex);
                    NPC teamTwoFighter = team2.get(teamTwoIndex);
                    //Check to see if team one's next fighter is faster than team two's
                    if (teamOneFighter.getAttackSpeed() > teamTwoFighter.getAttackSpeed()) {
                        //Find a target for the first team's fighter to hit
                        target = teamOneFighter.findTargetAndAct(team1, team2);
                        teamOneIndex--;
                    } else {
                        //Find a target for the second team's fighter to hit
                        target = teamTwoFighter.findTargetAndAct(team2, team1);
                        teamTwoIndex--;
                    }
                }
                //Announce and remove the target if dead
                if (target.isDead()) {
//                    System.out.println(target.getName() + " was slain!");
                    if (team1.contains(target)) {
                        team1.remove(target);
                        teamOneIndex--; //TODO: Test necessity of this
                    }
                    if (team2.contains(target)) {
                        team2.remove(target);
                        teamTwoIndex--;
                    }
                    if (team1.isEmpty()) {
                        return 2;
                    } else if (team2.isEmpty()) {
                        return 1;
                    }
                }
//                try {
//                    Thread.sleep(4000);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(CombatEngine.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        }
    }

    public static void main(String[] args) {
        CombatEngine ce = new CombatEngine();
        int dillWinCount = 0;
        int dillLosses = 0;
        while (dillWinCount < 1) {
            while (ce.fight() == 1) {
                dillLosses++;
                ce = new CombatEngine();
            }
            dillWinCount++;
            ce = new CombatEngine();
        }
        int averageRoundsBeforeWin = dillLosses / dillWinCount;
        System.out.println("Dill won after an average of " + averageRoundsBeforeWin + " losses.");
    }
}
