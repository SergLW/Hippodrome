import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HorseTest {

    private final Horse HORSE_START = new Horse("Wivo V", 3.0);
    private final Horse HORSE_FINISH = new Horse("Wivo V", 2.5, 5);

    @Test
    void nameIsNullConstructor() {
        IllegalArgumentException testNullName = assertThrows(IllegalArgumentException.class, () -> {
            new Horse(null, 3.0);
        });
        assertEquals("Name cannot be null.", testNullName.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t"})
    void wrongNameConstructor(String names) {
        IllegalArgumentException testWrongName = assertThrows(IllegalArgumentException.class, () -> {
            new Horse(names, 3.0);
        });
        assertEquals("Name cannot be blank.", testWrongName.getMessage());
    }

    @Test
    void speedValueConstructor() {
        IllegalArgumentException negativeSpeed = assertThrows(IllegalArgumentException.class, () -> {
            new Horse("Wivo V", -3.0);
        });
        assertEquals("Speed cannot be negative.", negativeSpeed.getMessage());
    }

    @Test
    void distanceValueConstructor() {
        IllegalArgumentException negativeDistance = assertThrows(IllegalArgumentException.class, () -> {
            new Horse("Wivo V", 3.0, -5);
        });
        assertEquals("Distance cannot be negative.", negativeDistance.getMessage());
    }

    @Test
    void getName() {
        assertEquals("Wivo V", HORSE_START.getName());
    }

    @Test
    void getSpeed() {
        assertEquals(3.0, HORSE_START.getSpeed());
    }

    @Test
    void getDistance() {
        assertEquals(0.0, HORSE_START.getDistance());
        assertEquals(5.0, HORSE_FINISH.getDistance());
    }


    @ParameterizedTest
    @CsvSource({
            "0.3, 0.25, 0.85, 4.2",
            "0.21, 0.7, 0.9, 5.43"
    })
    void moveHorse(double x1, double x2, double x3, double expectedDistance) {
        try(MockedStatic<Horse> util = mockStatic(Horse.class, CALLS_REAL_METHODS)) {

            util.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(x1, x2, x3);

            HORSE_START.move();
            HORSE_START.move();
            HORSE_START.move();

            util.verify(() -> Horse.getRandomDouble(0.2, 0.9), times(3));

            assertEquals(expectedDistance, HORSE_START.getDistance(), 1e-9);
        }
    }
}