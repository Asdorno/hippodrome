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
    void getHorses_ReturnsListOfHorses() {
        List<Horse> horses = Stream.generate(HippodromeTest::generateHorse)
                .limit(10)
                .distinct()
                .collect(Collectors.toList());

        Hippodrome hippodrome = new Hippodrome(horses);

        assertEquals(horses, hippodrome.getHorses());
    }

    @Test
    void getWinner_ReturnsWinner() {
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
    void move_InvokesMoveMethodForEachHorse() {
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
    void constructor_ListIsNull_ThrowsIllegalArgumentException() {
        String expectedMessage = "Horses cannot be null.";
        List<Horse> horses = null;

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Hippodrome(horses);
                });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void constructor_ListIsEmpty_ThrowsIllegalArgumentException() {
        String expectedMessage = "Horses cannot be empty.";
        List<Horse> horses = new ArrayList<>();

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Hippodrome(horses);
                });
        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Horse generateHorse() {
        return new Horse("Horse" + counter++, 10);
    }

    private static Horse spyHorse() {
        return Mockito.spy(generateHorse());
    }
}