package org.oner.aoc2022;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Day13 {

    private static final Set<String> CONT_LIST = Set.of("[[", "]]");
    private static final Set<String> PASS_LIST = Set.of("][", "]N");
    private static final Set<String> FAIL_LIST = Set.of("[]", "N]");

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day13().solve());
    }

    private void solve() {
        List<String> input = Input.read();
        List<List<String>> partitions = Lists.partition(input, 3);
        int sum = IntStream.range(0, partitions.size())
            .filter(i -> isInRightOrder(normalize(partitions.get(i).get(0)), normalize(partitions.get(i).get(1))))
            .map(i -> i + 1)
            .sum();
        System.out.println(sum);
    }

    private String normalize(String data) {
        return StringUtils.replace(data, ",", "");
    }

    private boolean isInRightOrder(String left, String right) {
        for (int leftIndex = 0, rightIndex = 0; leftIndex < left.length() && rightIndex < right.length(); leftIndex++, rightIndex++) {
            char leftChar = left.charAt(leftIndex);
            char rightChar = right.charAt(rightIndex);
            boolean isLeftDigit = Character.isDigit(leftChar);
            boolean isRightDigit = Character.isDigit(rightChar);
            if (isLeftDigit && isRightDigit) {
                if (leftChar == rightChar) {
                    continue;
                }
                return leftChar < rightChar;
            } else {
                leftChar = isLeftDigit ? 'N' : leftChar;
                rightChar = isRightDigit ? 'N' : rightChar;
                String lr = leftChar + "" + rightChar;
                if (CONT_LIST.contains(lr)) {
                    continue;
                }
                if (PASS_LIST.contains(lr)) {
                    return true;
                }
                if (FAIL_LIST.contains(lr)) {
                    System.out.println("%s\n%s\n".formatted(left, right));
                    return false;
                }
                switch (lr) {
                    case "[N" -> {
                        right = addSquareBrackets(right, rightIndex);
                    }
                    case "N[" -> {
                        left = addSquareBrackets(left, leftIndex);
                    }
                    default -> throw new RuntimeException(lr + " => " + left + " <> " + right);
                }
            }
        }
        return false;
    }

    private String addSquareBrackets(String string, int index) {
        return new StringBuilder(string)
            .insert(index + 1, ']')
            .insert(index, '[')
            .toString();
    }
}
