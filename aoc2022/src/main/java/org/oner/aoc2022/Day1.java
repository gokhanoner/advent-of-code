package org.oner.aoc2022;

import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day1 {

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day1().solve(1));
        Timer.timeIt(() -> new Day1().solve(3));
    }

    private void solve(int topNSum) {
        List<String> input = Input.read();
        List<Integer> cals = new ArrayList<>();
        int cur = 0;
        for (String datum : input) {
            if (datum.isBlank()) {
                cals.add(cur);
                cur = 0;
            } else {
                cur += Integer.parseInt(datum);
            }
        }
        cals.sort(Comparator.reverseOrder());
        int sum = cals.stream().limit(topNSum).mapToInt(Integer::intValue).sum();
        System.out.println("Total ot top %s elements => %s".formatted(topNSum, sum));
    }
}
