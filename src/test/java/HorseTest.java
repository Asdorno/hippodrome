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

    String expectedName = "expected";
    double expectedSpeed = 10.0;
    double expectedDistance = 10.0;

    @Spy
    Horse mockHorse = new Horse(expectedName, expectedSpeed, expectedDistance);

    @Test
    void getName() {
        assertEquals(expectedName, mockHorse.getName());
    }

    @Test
    void getSpeed() {
        assertEquals(expectedSpeed, mockHorse.getSpeed());
    }

    @Test
    void getDistance() {
        assertEquals(expectedDistance, mockHorse.getDistance());
    }

    @Test
    void getDistanceShouldReturnZeroWhenConstructorUsesTwoParams() {
        double expected = 0;
        Horse horse = new Horse("name", 10.0);
        assertEquals(expected, horse.getDistance());
    }

    @Test
    void move() {
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
    void moveShouldVerifyAlgorithmOfReceivingDistance(double randomReturn, double expectedResult) {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            mockedStatic.when(() -> Horse.getRandomDouble(anyDouble(), anyDouble())).thenReturn(randomReturn);
            mockHorse.move();
            assertEquals(expectedResult, mockHorse.getDistance());
        }
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionWhenFirstArgIsNull() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Horse horse = new Horse(null, 10.0);
                });
        assertEquals("Name cannot be null.", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactory")
    void constructorShouldThrowIllegalArgumentExceptionWhenFirstArgIsEmpty(String arg) {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Horse horse = new Horse(arg, 10);
                });
        assertEquals("Name cannot be blank.", exception.getMessage());
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionWhenSecondArgIsNegative() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Horse horse = new Horse("name", -10);
                });
        assertEquals("Speed cannot be negative.", exception.getMessage());
    }

    @Test
    void constructorShouldThrowIllegalArgumentExceptionWhenThirdArgIsNegative() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Horse horse = new Horse("name", 10, -10);
                });
        assertEquals("Distance cannot be negative.", exception.getMessage());
    }

    static Stream<String> argsProviderFactory() {
        return Stream.of("", " ", "   ", "\t", "\n");
    }
}