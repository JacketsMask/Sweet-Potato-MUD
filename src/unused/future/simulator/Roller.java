package unused.future.simulator;

import java.util.Random;

/**
 * This Roller provides additional functionality for "dice roll" calculations
 * using a shared Random object.
 * 
 * TODO: Replace with ApacheMath when possible for normal distribution.
 *
 * @author Japhez
 */
public class Roller {

    private static Random random; //Random is thread safe, so I need not worry about that

    public Roller() {
        random = new Random();
    }

    /**
     * Returns a value between the passed lower and upper bounds (inclusive)
     *
     * @param lower the lower value
     * @param upper the upper value
     * @return a random integer between the upper and lower values
     */
    public int rollBetween(int lower, int upper) {
        //Example lower 5, upper 10
        //10 - 5 = 5 (random number between 0 and 5)
        //We add the lower bound to the result, resulting in a value
        //Between 5 and 10, which is what we want
        return random.nextInt(upper - lower) + lower;
    }

    /**
     * Rolls a single die with the given number of sides and returns the result.
     *
     * @param numberOfSides the number of sides on the die
     * @return the resulting die face value
     */
    public int rollDie(int numberOfSides) {
        return rollDice(1, numberOfSides);
    }

    /**
     * Rolls the given number of dice with the given number of sides and returns
     * the result.
     *
     * @param numberOfDice the number of dice
     * @param numberOfSides the number of sides to each die
     * @return the value of the dice added up
     */
    public int rollDice(int numberOfDice, int numberOfSides) {
        int value = 0;
        for (int i = 0; i < numberOfDice; i++) {
            value += (random.nextInt(numberOfSides) + 1);
        }
        return value;
    }
}
