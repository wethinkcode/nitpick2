package za.co.wethinkcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomIntFactoryTest {

    @Test
    public void randomNumberShouldBeWithinGivenWideRange() {
        RandomIntFactory rand = new RandomIntFactory(1, 100);
        int num = rand.getNumber();
        assertTrue(num >= 1 && num <= 100);
    }

    @Test
    public void randomNumberShouldBeWithinGivenWideRange_wrongConstructorArgs() {
        RandomIntFactory rand = new RandomIntFactory(0, 100);
        int num = rand.getNumber();
        assertTrue(num >= 1 && num <= 100);
    }

    @Test
    public void randomNumberShouldBeWithinGivenSmallRange() {
        RandomIntFactory rand = new RandomIntFactory(1, 10);
        int num = rand.getNumber();
        assertTrue(num >= 1 && num <= 10);
    }

    @Test
    public void randomNumberShouldBeWithinGivenSmallerRange() {
        RandomIntFactory rand = new RandomIntFactory(1, 2);
        int num = rand.getNumber();
        assertTrue(num >= 1 && num <= 2);
    }

    @Test
    public void randomNumberShouldBeWithinGivenNoRange() {
        RandomIntFactory rand = new RandomIntFactory(1, 1);
        int num = rand.getNumber();
        assertTrue(num >= 1 && num <= 2);
    }
}
