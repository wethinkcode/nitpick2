package za.co.wethinkcode;

import org.geepawhill.jltk.script.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class GuessingGameTest {

    GuessingGame game = new GuessingGame(15, 10, 100);

    @Test
    public void guessIsBelowRange() {
        String actual = game.guess(15, 1, 100, -1);
        assertEquals(actual, GuessingGame.GUESS_OUT_OF_BOUNDS);
    }

    @Test
    public void guessIsAboveRange() {
        String actual = game.guess(15, 1, 100, 101);
        assertEquals(actual, GuessingGame.GUESS_OUT_OF_BOUNDS);
    }

    @Test
    public void guessIsTooLow() {
        String actual = game.guess(15, 1, 100, 10);
        assertEquals(actual, GuessingGame.TOO_HIGH);
    }

    @Test
    public void guessIsTooHigh() {
        String actual = game.guess(15, 1, 100, 30);
        assertEquals(actual, GuessingGame.TOO_HIGH);
    }

    @Test
    public void guessIsJustRight() {
        String actual = game.guess(15, 1, 100, 15);
        assertEquals(actual, GuessingGame.justRight(15));
    }

    @Test
    public void scriptTest() {
        new ConsoleTester()
                .computerPrompts("Guess a number between 1 and 100: ")
                .humanSays("34")
                .computerSays("Congratulations! You guessed the number 34!")
                .computerSays("abc")
                .run(new GuessingGame(34, 1,100)::play);
    }

    @DisabledOnOs(OS.WINDOWS)
    @Test
    public void between1and100NumberIs34() {
        InputStream stdin = System.in;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        PrintStream stdout = System.out;

        System.setIn(new ByteArrayInputStream("1\nc\n25\n30\n35\n34\n".getBytes()));
        System.setOut(printStream);

        GuessingGame game = new GuessingGame(34, 1,100);
        game.play();

        System.setIn(stdin);
        System.setOut(stdout);
        String outputText = byteArrayOutputStream.toString();

        String expected = "Guess a number between 1 and 100: Too low!\n" +
                "Guess a number between 1 and 100: Guess must be Integer!\n" +
                "Guess a number between 1 and 100: Too low!\n" +
                "Guess a number between 1 and 100: Too low!\n" +
                "Guess a number between 1 and 100: Too high!\n" +
                "Guess a number between 1 and 100: Congratulations! You guessed the number 34!\n";

        assertEquals(expected, outputText);
    }
}
