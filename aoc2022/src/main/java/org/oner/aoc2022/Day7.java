package org.oner.aoc2022;

import org.oner.utils.Input;
import org.oner.utils.Timer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

public class Day7 {
    private static final String ROOT = "/";
    private static final Directory ROOT_DIR = new Directory(null, ROOT, new HashMap<>());
    private static final String COMMAND_PREFIX = "$";
    private static final int MAX_FOLDER_SIZE = 100000;
    private static final int TOTAL_DISC_SPACE = 70000000;
    private static final int MIN_UNUSED_SPACE = 30000000;

    public static void main(String[] args) {
        Timer.timeIt(() -> new Day7().solve());
    }

    private void solve() {
        List<String> data = Input.read();
        int lineIndex = 0;
        int limit = data.size();
        Directory currentDir = null;
        while (lineIndex < limit) {
            String line = data.get(lineIndex);
            if (!isCommandLine(line)) {
                throw new IllegalStateException("Parsing error: Expecting a command");
            }
            String[] commandParts = line.split(" ");
            String command = commandParts[1];
            switch (command) {
                case "cd" -> {
                    String folder = commandParts[2];
                    switch (folder) {
                        case "/" -> currentDir = ROOT_DIR;
                        case ".." -> currentDir = currentDir.parent();
                        default -> currentDir = (Directory) currentDir.childs().get(folder);
                    }
                    lineIndex++;
                }
                case "ls" -> {
                    while (++lineIndex < limit && !isCommandLine(data.get(lineIndex))) {
                        String listLine = data.get(lineIndex);
                        String[] listParts = listLine.split(" ");
                        String typeOrSize = listParts[0];
                        String name = listParts[1];
                        switch (typeOrSize) {
                            case "dir" -> currentDir.addDir(name);
                            default -> currentDir.addFile(name, Integer.parseInt(typeOrSize));
                        }
                    }
                }
            }
        }

        List<Integer> folderSizes = new ArrayList<>();
        Stack<Directory> dirs = new Stack<>();
        dirs.push(ROOT_DIR);

        while (!dirs.isEmpty()) {
            Directory dir = dirs.pop();
            int dirSize = dir.totalSize();
            folderSizes.add(dirSize);
            dir.childs().forEach((k, v) -> {
                if (v instanceof Directory d) {
                    dirs.push(d);
                }
            });
        }

        folderSizes.sort(Comparator.naturalOrder());
        int minSum = folderSizes.stream().takeWhile(v -> v <= MAX_FOLDER_SIZE).mapToInt(Integer::intValue).sum();
        System.out.println("Total size of folders under %s size is %s".formatted(MAX_FOLDER_SIZE, minSum));

        int rootSize = ROOT_DIR.totalSize();
        int minDirSize2Del = MIN_UNUSED_SPACE - (TOTAL_DISC_SPACE - rootSize);

        Optional<Integer> min2Del = folderSizes.stream().dropWhile(v -> v < minDirSize2Del).findFirst();
        System.out.println("Min folder size to delete to have %s empty space => %s".formatted(MIN_UNUSED_SPACE, min2Del));

    }

    private boolean isCommandLine(String line) {
        return line.startsWith(COMMAND_PREFIX);
    }

    interface DirObject {
        int totalSize();
    }

    record Directory(Directory parent, String name, Map<String, DirObject> childs) implements DirObject {
        @Override
        public int totalSize() {
            return childs.values().stream().mapToInt(DirObject::totalSize).sum();
        }

        public void addDir(String name) {
            childs.put(name, new Directory(this, name, new HashMap<>()));
        }

        public void addFile(String name, int size) {
            childs.put(name, new File(this, name, size));
        }
    }

    record File(Directory parent, String name, int size) implements DirObject {
        @Override
        public int totalSize() {
            return size;
        }
    }
}
