package za.co.wethinkcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SayHelloTest {
    @Test
    void sayHello() {
        SayHello sayHello = new SayHello();
        assertEquals("hello", sayHello.speak());
    }
}