package home.Optional;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by ly on 7/31/16.
 */
public class OptionalExperiment {
    /**
     * Do this in both java and scala: given a text file, each line is a number or a non-number; return a list of all
     * the numbers there. If file cannot be opened, return an empty list.
     * Check the Scala version of the implementation as well.
     */
    @Test public void f() {
        Function<Path, Stream<String>> getLines = p -> {
            try {
                return Files.lines(p);
            }
            catch (IOException ioex) {
                return Stream.empty();
            }
        };
        Function<String, Optional<Integer>> convertToInteger = s -> {
            try {
                return Optional.of(Integer.valueOf(s));
            }
            catch (Exception ex) {
                return Optional.empty();
            }
        };
        Stream<Integer> stream = getLines.apply(Paths.get("/dev/shm/test.txt"))
                .map(convertToInteger)
                .filter(Optional::isPresent)
                .map(Optional::get);

        System.out.println(Arrays.toString(stream.mapToInt(i -> i).toArray()));

        // or ... this idea is from Scala
        Function<String, Stream<Integer>> convertToStream = s -> {
            try {
                return Stream.of(Integer.valueOf(s));
            }
            catch (Exception ex) {
                return Stream.empty();
            }
        };

        getLines.apply(Paths.get("/dev/shm/test.txt")).flatMap(convertToStream).mapToInt(i -> i).toArray();
    }
}
