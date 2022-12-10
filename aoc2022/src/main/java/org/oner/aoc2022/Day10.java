package org.oner.aoc2022;

import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day10 {
    private static final int SCREEN_WIDTH = 40;
    private static final int SCREEN_HEIGHT = 6;
    private static final char FILL_CHAR = '.';
    private static final char DRAW_CHAR = '#';

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day10().solve());
    }

    private void solve() {
        List<String> input = Input.read();
        int register = 1;
        int signalStrengths = 0;
        Set<Integer> cycles2Check = new HashSet<>(Set.of(20, 60, 100, 140, 180, 220));
        int currCycle = 1;
        //For CRT
        char[][] screen = new char[SCREEN_HEIGHT][SCREEN_WIDTH];
        for (char[] screenRow : screen) {
            Arrays.fill(screenRow, FILL_CHAR);
        }
        Range spritePos = new Range(0, 2);
        for (String command : input) {
            String[] commandParts = command.split(" ");
            int commandCycle = 1;
            int increment = 0;
            if ("addx".equals(commandParts[0])) {
                commandCycle = 2;
                increment = Integer.parseInt(commandParts[1]);
            }
            for (int i = 0; i < commandCycle; i++) {
                if (cycles2Check.remove(currCycle)) {
                    signalStrengths += register * currCycle;
                }
                //CRT Drawing
                int crtRowPos = (currCycle - 1) / SCREEN_WIDTH;
                int crtColPos = (currCycle - 1) % SCREEN_WIDTH;
                if (spritePos.contains(crtColPos)) {
                    screen[crtRowPos][crtColPos] = DRAW_CHAR;
                }
                currCycle++;
            }
            register += increment;
            spritePos = spritePos.shift(increment);
        }
        System.out.println(signalStrengths);
        for (char[] screenRow : screen) {
            System.out.println(screenRow);
        }
    }

    record Range(int min, int max) {
        boolean contains(int target) {
            return target >= this.min() && target <= this.max();
        }

        Range shift(int increment) {
            return new Range(this.min() + increment, this.max() + increment);
        }
    }
}
