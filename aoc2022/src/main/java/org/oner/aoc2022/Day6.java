package org.oner.aoc2022;

import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6 {

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day6().solve(4));
        Timer.timeIt(() -> new Day6().solve(14));
    }

    private void solve(int nOfDistinctChars) {
        List<String> data = Input.read();
        String line = data.get(0);
        int start = 0;
        int cnt = 0;
        Map<Character, Integer> charPosMap = new HashMap<>();
        for (int i = 0; i < line.length(); i++) {
            char signal = line.charAt(i);
            int posSeen = charPosMap.getOrDefault(signal, -1);
            if (posSeen >= start) {
                //character seen before, update the index & increase the start
                start = posSeen + 1;
                cnt = i - start;
            }
            cnt++;
            charPosMap.put(signal, i);
            if (cnt == nOfDistinctChars) {
                System.out.println(start + nOfDistinctChars);
                return;
            }
        }
    }
}
