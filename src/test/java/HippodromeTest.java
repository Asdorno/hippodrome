import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HippodromeTest {
    private static int counter = 0;

    @Test
    void getHorses() {
        List<Horse> horses = Stream.generate(HippodromeTest::generateHorse)
                .limit(30)
                .distinct()
                .collect(Collectors.toList());

        Hippodrome hippodrome = new Hippodrome(horses);

        assertEquals(horses, hippodrome.getHorses());
    }

    @Test
    void move() {
        List<Horse> horses = Stream.generate(HippodromeTest::spyHorse)
                .limit(50)
                .distinct()
                .collect(Collectors.toList());
        Hippodrome hippodrome = new Hippodrome(horses);
        hippodrome.move();
        for (Horse horse : hippodrome.getHorses()) {
            Mockito.verify(horse).move();
        }
    }

    @Test
    void getWinner() {
        Horse winner = new Horse("Winner", 50);
        List<Horse> horses = Stream.generate(HippodromeTest::spyHorse)
                .limit(4)
                .distinct()
                .collect(Collectors.toList());
        horses.add(winner);

        Hippodrome hippodrome = new Hippodrome(horses);
        hippodrome.move();

        assertEquals(winner, hippodrome.getWinner());
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfArgIsNull() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Hippodrome hippodrome = new Hippodrome(null);
                });
        assertEquals("Horses cannot be null.", exception.getMessage());
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionIfListIsEmpty() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Hippodrome hippodrome = new Hippodrome(new ArrayList<>());
                });
        assertEquals("Horses cannot be empty.", exception.getMessage());
    }

    private static Horse generateHorse() {
        return new Horse("Horse" + counter++, 10);
    }

    private static Horse spyHorse() {
        return Mockito.spy(generateHorse());
    }
}