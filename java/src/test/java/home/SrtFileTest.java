package home;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ly on 7/6/16.
 */
public class SrtFileTest {
    // we need to add an offset to a line like this: 00:00:55,375 --> 00:00:58,197
    private final Duration offset = Duration.ofMillis(-800);
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
    private final Pattern pattern =
            Pattern.compile("\\d\\d:\\d\\d:\\d\\d,\\d\\d\\d --> \\d\\d:\\d\\d:\\d\\d,\\d\\d\\d");

    private String addOffset(String line) {
        final String separator = " --> ";
        return pattern.matcher(line).matches() ?
            Stream.of(line.split(separator))
                    .map(t -> LocalTime.parse(t, dtf).plus(offset))
                    .map(u -> u.format(dtf))
                    .collect(Collectors.joining(separator))
                :
                line;
    }

    @Test public void addOffsetTest() throws IOException {
        List<String> lines =
                Files.lines(Paths.get("/dev/shm/Everybody.Wants.Some.2016.1080p.BluRay.x264-[YTS.AG].srt"))
                .map(this::addOffset)
                .collect(Collectors.toList());
        Files.write(Paths.get("/dev/shm/Everybody.Wants.Some.2016.1080p.BluRay.x264-[YTS.AG.srt"), lines);
    }

    @Test public void test() {
        System.out.println(LocalTime.parse("00:00:37,336", dtf));
    }
}
