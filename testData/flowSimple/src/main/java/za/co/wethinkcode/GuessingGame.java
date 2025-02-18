package za.co.wethinkcode;

import org.geepawhill.jltk.prompt.*;

import java.util.*;

public class GuessingGame {

    public static final String GUESS_MUST_BE_INTEGER = "Guess must be Integer!";
    public static final String GUESS_OUT_OF_BOUNDS = "Guess out of bounds!";
    public static final String TOO_LOW = "Too low!";
    public static final String TOO_HIGH = "Too high!";

    private int number;
    private final int min;
    private final int max;
    private boolean gameOver = false;
    private Scanner scanner = new Scanner(System.in);

    public GuessingGame(int number, int min, int max){
        this.number = number;
        this.min = min;
        this.max = max;
    }

    public void play() {

        do {
            int guess = Prompt.anyInteger("Guess a number between " + min + " and " + max + ": ");
            String response = guess(number, min, max, guess);
            System.out.println(response);
        } while (!gameOver);
    }

    public String guess(int value, int min, int max, int guess) {
        if (guess < min || guess > max) return GUESS_OUT_OF_BOUNDS;
        if (guess < value) return TOO_LOW;
        if (guess > value) return TOO_HIGH;
        gameOver = true;
        return justRight(value);
    }

    public static String justRight(int value) {
        return "Congratulations! You guessed the number " + value + "!";
    }
}
