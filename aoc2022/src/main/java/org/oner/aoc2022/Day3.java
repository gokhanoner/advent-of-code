package org.oner.aoc2022;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day3 {
    private static final int LOWER_CASE_THRESHOLD = 97;
    private static final int LOWER_CASE_FIX = 96;
    private static final int UPPER_CASE_FIX = 38;
    private static final Function<Collection<Integer>, Integer> SUM_OPERATION = collection -> {
        int sum = 0;
        for (Integer number : collection) {
            int weight = number > LOWER_CASE_THRESHOLD ? number - LOWER_CASE_FIX : number - UPPER_CASE_FIX;
            sum += weight;
        }
        return sum;
    };

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day3().solveP1());
        Timer.timeIt(() -> new Day3().solveP2());
    }

    private void solveP1() {
        List<String> input = Input.read();
        int sum = 0;
        for (String line : input) {
            int half = line.length() / 2;
            Set<Integer> left = line.substring(0, half).chars().boxed().collect(Collectors.toSet());
            Set<Integer> right = line.substring(half).chars().boxed().collect(Collectors.toSet());
            Set<Integer> intersection = Sets.intersection(left, right);
            sum += SUM_OPERATION.apply(intersection);
        }
        System.out.println(sum);
    }

    private void solveP2() {
        List<String> input = Input.read();
        List<List<String>> partitions = Lists.partition(input, 3);
        int sum = 0;
        for (List<String> partition : partitions) {
            Optional<Set<Integer>> reduce = partition.stream().map(String::chars)
                .map(s -> s.boxed().collect(Collectors.toSet()))
                .reduce(Sets::intersection);

            if (reduce.isPresent()) {
                sum += SUM_OPERATION.apply(reduce.get());
            }
        }
        System.out.println(sum);
    }
}
