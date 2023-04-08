package ru.spbstu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DominantProbabilityTest {

    @Test
    public void exampleTest() throws IllegalArgumentException {
        Assertions.assertEquals(
                "0,78333",
                String.format("%.5f", DominantProbability.calculateDominantProbability("2 2 2"))
        );
    }

    @Test
    public void attemptTest() throws IllegalArgumentException {
        Assertions.assertEquals(
                "0,72180",
                String.format("%.5f", DominantProbability.calculateDominantProbability("17 30 21"))
        );
    }

}
