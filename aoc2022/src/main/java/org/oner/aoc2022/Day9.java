package org.oner.aoc2022;

import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day9 {

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day9().solve(2));
        Timer.timeIt(() -> new Day9().solve(10));
    }

    private void solve(int nOfKnots) {
        List<String> input = Input.read();
        int tailIndx = nOfKnots - 1;
        Set<Point> tailRoute = new HashSet<>();

        Point start = new Point(0, 0);
        tailRoute.add(start);

        Point[] knots = new Point[nOfKnots];
        Arrays.fill(knots, start);

        for (String line : input) {
            String[] commandParts = line.split(" ");
            String direction = commandParts[0];
            int nOfMovements = Integer.parseInt(commandParts[1]);
            for (int i = 0; i < nOfMovements; i++) {
                switch (direction) {
                    case "L" -> knots[0] = knots[0].moveH(-1);
                    case "R" -> knots[0] = knots[0].moveH(1);
                    case "U" -> knots[0] = knots[0].moveV(1);
                    case "D" -> knots[0] = knots[0].moveV(-1);
                }
                for (int j = 1; j < nOfKnots; j++) {
                    if (knots[j].isTouching(knots[j - 1])) {
                        break;
                    }
                    knots[j] = knots[j].moveD(knots[j - 1]);
                    if (j == tailIndx) {
                        tailRoute.add(knots[j]);
                    }
                }
            }
        }
        System.out.println(tailRoute.size());
    }

    record Point(int y, int x) {
        Point moveH(int move) {
            return new Point(y, x + move);
        }

        Point moveV(int move) {
            return new Point(y + move, x);
        }

        /**
         * Assumes points are not touching. Since this is always a diagonal move,
         * it'll move to the same x or y coordinate with target if distance is 1, move next to if distance is 2
         *
         * @param target target to move next to
         * @return new Point to move
         */
        Point moveD(Point target) {
            int xDiff = target.x() - this.x();
            int yDiff = target.y() - this.y();
            int xP = Math.abs(xDiff) == 1 ? this.x() + xDiff : this.x() + xDiff / 2;
            int yP = Math.abs(yDiff) == 1 ? this.y() + yDiff : this.y() + yDiff / 2;
            return new Point(yP, xP);
        }

        boolean isTouching(Point target) {
            return Math.abs(this.x() - target.x()) <= 1 && Math.abs(this.y() - target.y()) <= 1;
        }
    }
}
