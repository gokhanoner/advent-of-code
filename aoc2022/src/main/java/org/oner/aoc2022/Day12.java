package org.oner.aoc2022;

import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Day12 {
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day12().solve());
    }

    private void solve() {
        List<String> input = Input.read();
        char[][] heightMap = asHeightMap(input);
        int height = heightMap.length;
        int width = heightMap[0].length;
        Set<Point> starts = new HashSet<>();
        Point start = null;
        Point end = null;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (heightMap[i][j] == 'S') {
                    start = new Point(i, j);
                    starts.add(start);
                    heightMap[i][j] = 'a';
                } else if (heightMap[i][j] == 'E') {
                    end = new Point(i, j);
                    heightMap[i][j] = 'z';
                } else if (heightMap[i][j] == 'a') {
                    starts.add(new Point(i, j));
                }
            }
        }

        System.out.println(find(start, end, heightMap));
        Point finalEnd = end;
        starts.stream().mapToInt(s -> find(s, finalEnd, heightMap)).min().ifPresent(System.out::println);
    }

    private int find(Point start, Point end, char[][] heightMap) {
        Map<Point, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        int height = heightMap.length;
        int width = heightMap[0].length;

        Queue<Point> next = new ArrayDeque<>();
        next.add(start);
        while (!next.isEmpty()) {
            Point current = next.poll();
            for (int[] delta : DIRECTIONS) {
                Point neighbour = current.move(delta[0], delta[1]);
                if (isValidPoint(neighbour, height, width) && hasValidHeight(current, neighbour, heightMap)) {
                    int steps = distances.getOrDefault(current, 0) + 1;
                    if (neighbour.equals(end)) {
                        return steps;
                    }
                    if (steps < distances.getOrDefault(neighbour, Integer.MAX_VALUE)) {
                        distances.put(neighbour, steps);
                        next.add(neighbour);
                    }
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private char[][] asHeightMap(List<String> data) {
        char[][] heightMap = new char[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            heightMap[i] = data.get(i).toCharArray();
        }
        return heightMap;
    }

    private boolean isValidPoint(Point p, int height, int width) {
        return p.y() >= 0 && p.y() < height && p.x() >= 0 && p.x() < width;
    }

    private boolean hasValidHeight(Point cur, Point next, char[][] heightMap) {
        int ch = heightMap[cur.y()][cur.x()];
        int th = heightMap[next.y()][next.x()];
        return ch + 1 >= th;
    }

    record Point(int y, int x) {
        public Point move(int deltaY, int deltaX) {
            return new Point(this.y() + deltaY, this.x() + deltaX);
        }
    }
}
