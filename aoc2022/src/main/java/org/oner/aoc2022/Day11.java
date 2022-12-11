package org.oner.aoc2022;

import com.google.common.collect.Lists;
import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;

public class Day11 {
    public static void main(String[] args) {
        Timer.timeIt(() -> new Day11().solve(20, true));
        Timer.timeIt(() -> new Day11().solve(10_000, false));
    }

    private void solve(int rounds, boolean worryLess) {
        System.out.println(KeepAwayGame.playNewGame(Input.read(), rounds, worryLess));
    }

    static class KeepAwayGame {
        private static final Function<Long, Long> RELIEF = a -> a / 3;
        private final List<Monkey> monkeys;
        private final int rounds;

        private KeepAwayGame(List<Monkey> monkeys, int rounds) {
            this.monkeys = monkeys;
            this.rounds = rounds;
        }

        long monkeyBusiness() {
            for (int i = 0; i < rounds; i++) {
                for (Monkey monkey : monkeys) {
                    for (long itemWorry : monkey.items()) {
                        long newWorry = monkey.worryCalc().apply(itemWorry);
                        int target = monkey.targetCalc().apply(newWorry);
                        monkeys.get(target).items().add(newWorry);
                    }
                    monkey.inspections().add(monkey.items().size());
                    monkey.items().clear();
                }
            }
            long result = monkeys.stream().map(m -> m.inspections().sum())
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .mapToLong(Long::longValue)
                .reduce(1, Math::multiplyExact);

            return result;
        }

        static long playNewGame(List<String> input, int rounds, boolean worryLess) {
            return new KeepAwayGame(parseInput(input, worryLess), rounds).monkeyBusiness();
        }

        private static List<Monkey> parseInput(List<String> input, boolean worryLess) {
            List<Monkey> monkeys = new ArrayList<>();
            long leastCommonMultiple = input.stream()
                .filter(s -> s.contains("divisible"))
                .mapToLong(s -> Long.parseLong(substringAfterLast(s.trim(), "divisible by ")))
                .distinct()
                .reduce(1, Math::multiplyExact);

            List<List<String>> partitions = Lists.partition(input, 7);
            for (List<String> partition : partitions) {
                List<Long> worries = Arrays.stream(split(substringAfterLast(partition.get(1).trim(), ":"), ", "))
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .collect(Collectors.toList());

                String[] oprParts = split(substringAfter(partition.get(2).trim(), "old"));
                Function<Long, Long> worryCalc = switch (oprParts[1]) {
                    case "old" -> square();
                    default -> switch (oprParts[0]) {
                        case "+" -> sumWith(Integer.parseInt(oprParts[1]));
                        case "*" -> multiplyWith(Integer.parseInt(oprParts[1]));
                        default -> throw new IllegalStateException();
                    };
                };
                if (worryLess) {
                    worryCalc = worryCalc.andThen(RELIEF);
                }

                int divideBy = Integer.parseInt(substringAfterLast(partition.get(3).trim(), "divisible by "));
                int trueTarget = Integer.parseInt(substringAfterLast(partition.get(4).trim(), "monkey "));
                int falseTarget = Integer.parseInt(substringAfterLast(partition.get(5).trim(), "monkey "));
                Function<Long, Integer> targetFunc = targetFunction(divideBy, trueTarget, falseTarget);
                worryCalc = worryCalc.andThen(a -> a % leastCommonMultiple);
                monkeys.add(new Monkey(worries, worryCalc, targetFunc, new LongAdder()));
            }
            return monkeys;
        }

        private static Function<Long, Long> sumWith(long x) {
            return a -> a + x;
        }

        private static Function<Long, Long> multiplyWith(long x) {
            return a -> a * x;
        }

        private static Function<Long, Long> square() {
            return a -> a * a;
        }

        private static Function<Long, Integer> targetFunction(long divideBy, int trueVal, int falseVal) {
            return a -> a % divideBy == 0 ? trueVal : falseVal;
        }
    }

    record Monkey(List<Long> items, Function<Long, Long> worryCalc, Function<Long, Integer> targetCalc,
                  LongAdder inspections) {
    }
}
