package za.co.wethinkcode;

import org.geepawhill.jltk.flow.*;

public class Main {
    public static void main(String[] args) {
        RandomIntFactory rand = new RandomIntFactory(1, 100);
        GuessingGame game = new GuessingGame(rand.getNumber(), rand.getMin(), rand.getMax());
        game.play();
    }

    static {
        new Recorder().logRun();
    }
}