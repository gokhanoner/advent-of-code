package org.oner.aoc2022;

import org.apache.commons.lang3.StringUtils;
import org.oner.support.Pair;
import org.oner.support.TriConsumer;
import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day5 {
    private static final TriConsumer<Stack<Character>, Stack<Character>, Command> SINGLE_MOVE_CRANE =
        (source, target, command) -> {
            for (int i = 0; i < command.cnt(); i++) {
                target.push(source.pop());
            }
        };
    private static final TriConsumer<Stack<Character>, Stack<Character>, Command> MULTI_MOVE_CRANE =
        (source, target, command) -> {
            Stack<Character> tmpStack = new Stack<>();
            for (int i = 0; i < command.cnt(); i++) {
                tmpStack.push(source.pop());
            }
            while (!tmpStack.isEmpty()) {
                target.push(tmpStack.pop());
            }
        };

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day5().solve(SINGLE_MOVE_CRANE));
        Timer.timeIt(() -> new Day5().solve(MULTI_MOVE_CRANE));
    }

    private void solve(TriConsumer<Stack<Character>, Stack<Character>, Command> craneOpr) {
        Pair<Map<String, Stack<Character>>, List<Command>> dataCommandPair = parseInput(Input.read());
        Map<String, Stack<Character>> data = dataCommandPair.left();
        List<Command> commands = dataCommandPair.right();
        for (Command command : commands) {
            Stack<Character> source = data.get(command.source());
            Stack<Character> target = data.get(command.target());
            craneOpr.accept(source, target, command);
        }
        String res = data.values().stream().map(s -> s.isEmpty() ? " " : s.peek()).map(Objects::toString).collect(Collectors.joining());
        System.out.println(res);
    }

    private Pair<Map<String, Stack<Character>>, List<Command>> parseInput(List<String> input) {
        boolean data = true;
        // LinkedHashMap so it'll appear in the same iteration order
        Map<String, Stack<Character>> dataMap = new LinkedHashMap<>();
        List<Command> commands = new ArrayList<>();
        // each new crate letter appear 4 pos after the prev one
        int increment = 4;
        Stack<String> tmpData = new Stack<>();
        for (String line : input) {
            if (line.isBlank()) {
                data = false;
                String[] stacks = StringUtils.split(tmpData.pop());
                int stackSize = stacks.length;
                while (!tmpData.empty()) {
                    String dataLine = tmpData.pop();
                    int position = 1;
                    for (int i = 0; i < stackSize; i++, position += increment) {
                        char crate = dataLine.charAt(position);
                        if (!Character.isWhitespace(crate)) {
                            dataMap.computeIfAbsent(stacks[i], k -> new Stack<>()).push(crate);
                        }
                    }
                }
                continue;
            }
            if (data) {
                tmpData.push(line);
            } else {
                // 0    1     2    3        4  5
                // move <cnt> from <source> to <target>
                String[] commandParts = StringUtils.split(line);
                commands.add(new Command(commandParts[3], commandParts[5], Integer.parseInt(commandParts[1])));
            }
        }
        return new Pair<>(dataMap, commands);
    }

    record Command(String source, String target, int cnt) {
    }
}
