package za.co.wethinkcode;

import java.util.Random;

public class RandomIntFactory
{
    private final int min;
    private final int max;
    private final Random rand;
    private int number;

    public RandomIntFactory( int min, int max) {
        this.min = min;
        this.max = max;
        rand = new Random();
        nextInt();
    }

    public void nextInt() {
        number = rand.nextInt(max - min + 1) + min;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getNumber() {
        return number;
    }
}
