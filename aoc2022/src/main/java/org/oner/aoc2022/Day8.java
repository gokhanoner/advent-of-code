package org.oner.aoc2022;

import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Day8 {

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day8().solveP1());
        Timer.timeIt(() -> new Day8().solveP2());
    }

    private void solveP1() {
        List<String> inputs = Input.read();
        char[][] charMatrix = asCharMatrix(inputs);

        int height = charMatrix.length;
        int width = charMatrix[0].length;
        Set<String> visibleFromLeft = visibleFromLeft(width, height, charMatrix);
        Set<String> visibleFromRight = visibleFromRight(width, height, charMatrix);
        Set<String> visibleFromUp = visibleFromUp(width, height, charMatrix);
        Set<String> visibleFromDown = visibleFromDown(width, height, charMatrix);
        Set<String> visible = new TreeSet<>(visibleFromLeft);
        visible.addAll(visibleFromRight);
        visible.addAll(visibleFromUp);
        visible.addAll(visibleFromDown);
        System.out.println(visible.size() + (height - 1) * 2 + (width - 1) * 2);
    }

    private void solveP2() {
        List<String> inputs = Input.read();
        char[][] charMatrix = asCharMatrix(inputs);

        int height = charMatrix.length;
        int width = charMatrix[0].length;
        int maxScenicScore = 0;

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                int left = nOfVisibleTreeLeft(j, i, charMatrix);
                int right = nOfVisibleTreeRight(j, i, charMatrix);
                int up = nOfVisibleTreeUp(j, i, charMatrix);
                int down = nOfVisibleTreeDown(j, i, charMatrix);
                int score = left * right * up * down;
                maxScenicScore = Math.max(maxScenicScore, score);
            }
        }
        System.out.println(maxScenicScore);
    }

    private int nOfVisibleTreeLeft(int x, int y, char[][] matrix) {
        int cnt = 0;
        for (int i = x - 1; i >= 0; i--) {
            cnt++;
            if (matrix[y][i] >= matrix[y][x]) {
                break;
            }
        }
        return cnt;
    }

    private int nOfVisibleTreeRight(int x, int y, char[][] matrix) {
        int cnt = 0;
        for (int i = x + 1; i < matrix[y].length; i++) {
            cnt++;
            if (matrix[y][i] >= matrix[y][x]) {
                break;
            }
        }
        return cnt;
    }

    private int nOfVisibleTreeUp(int x, int y, char[][] matrix) {
        int cnt = 0;
        for (int i = y - 1; i >= 0; i--) {
            cnt++;
            if (matrix[i][x] >= matrix[y][x]) {
                break;
            }
        }
        return cnt;
    }

    private int nOfVisibleTreeDown(int x, int y, char[][] matrix) {
        int cnt = 0;
        for (int i = y + 1; i < matrix.length; i++) {
            cnt++;
            if (matrix[i][x] >= matrix[y][x]) {
                break;
            }
        }
        return cnt;
    }

    private char[][] asCharMatrix(List<String> data) {
        char[][] charMatrix = new char[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            charMatrix[i] = data.get(i).toCharArray();
        }
        return charMatrix;
    }

    private Set<String> visibleFromLeft(int height, int width, char[][] matrix) {
        Set<String> visible = new HashSet<>();
        for (int i = 1; i < height - 1; i++) {
            int max = matrix[i][0];
            for (int j = 1; j < width - 1; j++) {
                if (matrix[i][j] > max) {
                    visible.add(i + "-" + j);
                    max = matrix[i][j];
                }
            }
        }
        return visible;
    }

    private Set<String> visibleFromRight(int height, int width, char[][] matrix) {
        Set<String> visible = new HashSet<>();
        for (int i = 1; i < height - 1; i++) {
            int max = matrix[i][width - 1];
            for (int j = width - 2; j > 0; j--) {
                if (matrix[i][j] > max) {
                    visible.add(i + "-" + j);
                    max = matrix[i][j];
                }
            }
        }
        return visible;
    }

    private Set<String> visibleFromUp(int height, int width, char[][] matrix) {
        Set<String> visible = new HashSet<>();
        for (int i = 1; i < width - 1; i++) {
            int max = matrix[0][i];
            for (int j = 1; j < height - 1; j++) {
                if (matrix[j][i] > max) {
                    visible.add(j + "-" + i);
                    max = matrix[j][i];
                }
            }
        }
        return visible;
    }

    private Set<String> visibleFromDown(int height, int width, char[][] matrix) {
        Set<String> visible = new HashSet<>();
        for (int i = 1; i < width - 1; i++) {
            int max = matrix[height - 1][i];
            for (int j = height - 2; j > 0; j--) {
                if (matrix[j][i] > max) {
                    visible.add(j + "-" + i);
                    max = matrix[j][i];
                }
            }
        }
        return visible;
    }
}
