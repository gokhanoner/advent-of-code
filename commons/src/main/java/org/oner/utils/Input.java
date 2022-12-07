package org.oner.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Input {

    private static final String AOC_INPUT_FILE_TEMPLATE = "inputs/%s/%s.txt";
    private static final String PACKAGE_PREFIX = "aoc";
    private static final String SAMPLE_SUFFIX = "sample";
    //private static final String CLASS_PREFIX = "Day";

    private Input() {
        throw new IllegalStateException("Static Class - InputReader");
    }

    public static List<String> readSample() {
        return readInternal(true);
    }

    public static List<String> read() {
        return readInternal(false);
    }

    private static List<String> readInternal(boolean sample) {
        StackWalker walker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        var stackFrame = walker.walk(frames -> frames.skip(2).findFirst()).orElseThrow();
        var simplePackageName = StringUtils.substringAfterLast(stackFrame.getDeclaringClass().getPackageName(), '.');
        var year = StringUtils.substringAfterLast(simplePackageName, PACKAGE_PREFIX);
        var day = StringUtils.splitByCharacterTypeCamelCase(stackFrame.getDeclaringClass().getSimpleName())[1];
        return read(year, day.concat(sample ? SAMPLE_SUFFIX : ""));
    }

    public static List<String> read(Object year, Object day) {
        try {
            return Files.readAllLines(Path.of(Input.class.getClassLoader().getResource(AOC_INPUT_FILE_TEMPLATE.formatted(year, day)).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
