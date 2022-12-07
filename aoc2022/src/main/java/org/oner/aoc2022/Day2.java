package org.oner.aoc2022;

import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.List;
import java.util.Map;

public class Day2 {

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day2().solve(POINTS_P1));
        Timer.timeIt(() -> new Day2().solve(POINTS_P2));
    }

    private static final Map<String, Integer> POINTS_P1 = Map.of(
        "A X", 4, "B Y", 5, "C Z", 6,
        "B X", 1, "C X", 7,
        "A Y", 8, "C Y", 2,
        "A Z", 3, "B Z", 9);

    private static final Map<String, Integer> POINTS_P2 = Map.of(
        "A X", 3, "B X", 1, "C X", 2,
        "A Y", 4, "B Y", 5, "C Y", 6,
        "A Z", 8, "B Z", 9, "C Z", 7);

    private void solve(Map<String, Integer> pointMap) {
        List<String> input = Input.read();
        int totalPoints = input.stream().mapToInt(pointMap::get).sum();
        System.out.println(totalPoints);
    }
}
