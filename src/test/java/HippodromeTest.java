import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HippodromeTest {

    private final List<Horse> HORSE_LIST = new ArrayList<>();
    private final List<Horse> HORSE_MOCK_LIST = new ArrayList<>();

    @Test
    void nullArgumentConstructor() {
        IllegalArgumentException nullListHorse = assertThrows(IllegalArgumentException.class, () -> {
            new Hippodrome(null);
        });
        assertEquals("Horses cannot be null.", nullListHorse.getMessage());
    }

    @Test
    void emptyListArgumentConstructor() {
        List<Horse> testList = new ArrayList<>();
        IllegalArgumentException emptyListHorse = assertThrows(IllegalArgumentException.class, () -> {
            new Hippodrome(testList);
        });
        assertEquals("Horses cannot be empty.", emptyListHorse.getMessage());
    }

    @Test
    void getHorses() {
        HORSE_LIST.clear();
        for (int i = 0; i < 30; i++) {
            HORSE_LIST.add(new Horse("H" + i,  i * 0.1));
        }

        Hippodrome hippodrome = new Hippodrome(HORSE_LIST);
        List<Horse> fullList = hippodrome.getHorses();

        for (int i = 0; i < fullList.size(); i++) {
            assertEquals(HORSE_LIST.get(i), fullList.get(i));
        }
    }

    @Test
    void move() {
        for (int i = 0; i < 50; i++) {
            Horse mockHorse = mock(Horse.class, withSettings().name("Horse" + i));
            HORSE_MOCK_LIST.add(mockHorse);
        }

        Hippodrome hippodrome = new Hippodrome(HORSE_MOCK_LIST);
        hippodrome.move();

        for (Horse horse : HORSE_MOCK_LIST) {
            verify(horse, times(1)).move();
        }
    }

    @Test
    void getWinner() {
        HORSE_LIST.clear();
        for (int i = 0; i < 30; i++) {
            HORSE_LIST.add(new Horse("Horse" + i,  i * 0.1));
        }

        Horse horseMaxSpeed = HORSE_LIST.get(HORSE_LIST.size() - 1);
        Hippodrome hippodrome = new Hippodrome(HORSE_LIST);

        try(MockedStatic<Horse> horse = mockStatic(Horse.class, CALLS_REAL_METHODS)) {
            horse.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(0.5);

            for (int i = 0; i < 100; i++) {
                hippodrome.move();
            }

            Horse horseWinner = hippodrome.getWinner();

            assertEquals(horseMaxSpeed, horseWinner);
        }
    }
}