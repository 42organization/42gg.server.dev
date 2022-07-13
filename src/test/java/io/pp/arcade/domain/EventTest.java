package io.pp.arcade.domain;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static java.lang.Thread.sleep;

public class EventTest {
    @Test
    public void event() throws InterruptedException {
        Random random = new Random();
        int a;
        for (int i = 0; i < 100; i++) {
            random.setSeed(System.currentTimeMillis());
            a = random.nextInt() % 100;
            sleep(10);
            if (a == 42) {
                System.out.println(a);
            }
        }
    }
}
