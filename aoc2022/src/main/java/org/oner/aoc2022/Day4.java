package org.oner.aoc2022;

import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.List;
import java.util.function.BiPredicate;

public class Day4 {
    private static final BiPredicate<Range, Range> COVER_PREDICATE = (left, right) -> left.cover(right) || right.cover(left);
    private static final BiPredicate<Range, Range> OVERLAP_PREDICATE = (left, right) -> left.overlap(right);

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day4().solve(COVER_PREDICATE));
        Timer.timeIt(() -> new Day4().solve(OVERLAP_PREDICATE));
    }

    private void solve(BiPredicate<Range, Range> predicate) {
        List<String> input = Input.read();
        int cnt = 0;
        for (String line : input) {
            String[] parts = line.split(",");
            String[] leftData = parts[0].split("-");
            String[] rightData = parts[1].split("-");
            Range left = new Range(Integer.parseInt(leftData[0]), Integer.parseInt(leftData[1]));
            Range right = new Range(Integer.parseInt(rightData[0]), Integer.parseInt(rightData[1]));
            if (predicate.test(left, right)) {
                cnt++;
            }
        }
        System.out.println(cnt);
    }

    record Range(int min, int max) {
        boolean cover(Range target) {
            return this.min() <= target.min() && this.max() >= target.max();
        }

        boolean overlap(Range target) {
            return isInRange(target.min()) || target.isInRange(this.min());
        }

        boolean isInRange(int number) {
            return number >= this.min() && number <= this.max();
        }
    }
}
