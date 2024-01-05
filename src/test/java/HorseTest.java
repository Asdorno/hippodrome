import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class HorseTest {

    private final String name = "name";
    private final double speed = 10.0;
    private final double distance = 10.0;

    @Spy
    Horse mockHorse = new Horse(name, speed, distance);

    @Test
    void getName_ReturnsCorrectdName() {
        assertEquals(name, mockHorse.getName());
    }

    @Test
    void getSpeed_ReturnsCorrectSpeed() {
        assertEquals(speed, mockHorse.getSpeed());
    }

    @Test
    void getDistance_ReturnsCorrectDistance() {
        assertEquals(distance, mockHorse.getDistance());
    }

    @Test
    void getDistance_UsedTwoParamsConstructor_ReturnsZero() {
        double expected = 0;

        Horse horse = new Horse(name, speed);
        assertEquals(expected, horse.getDistance());
    }

    @Test
    void move_InvokeGetRandomDoubleMethod() {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            mockHorse.move();
            mockedStatic.verify(() -> {
                Horse.getRandomDouble(0.2, 0.9);
            });
        }
    }

    @ParameterizedTest
    @CsvSource({"0.2, 12",
            "0.3, 13",
            "0.4, 14",
            "0.5, 15",
            "0.6, 16",
            "0.7, 17",
            "0.8, 18",
            "0.9, 19"})
    void move_ChecksAlgorithmOfReceivingDistance(double randomReturn, double expectedResult) {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            mockedStatic.when(() -> Horse.getRandomDouble(anyDouble(), anyDouble())).thenReturn(randomReturn);
            mockHorse.move();
            assertEquals(expectedResult, mockHorse.getDistance());
        }
    }

    @Test
    void constructor_NameIsNull_ThrowsIllegalArgumentException() {
        String expectedMessage = "Name cannot be null.";

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Horse(null, speed);
                });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactory")
    void constructor_NameIsEmpty_ThrowsIllegalArgumentException(String arg) {
        String expectedMessage = "Name cannot be blank.";

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Horse(arg, speed);
                });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void constructor_SpeedIsNegative_ThrowsIllegalArgumentException() {
        String expectedMessage = "Speed cannot be negative.";

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Horse(name, -speed);
                });
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void constructor_DistanceIsNegative_ThrowsIllegalArgumentException() {
        String expectedMessage = "Distance cannot be negative.";

        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    new Horse(name, speed, -distance);
                });
        assertEquals(expectedMessage, exception.getMessage());
    }

    static Stream<String> argsProviderFactory() {
        return Stream.of("", " ", "   ", "\t", "\n");
    }
}