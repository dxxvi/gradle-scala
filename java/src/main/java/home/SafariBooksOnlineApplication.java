package home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * This class creates a REST service to receive html code from the safaribooksonline greasemonkey script.
 */
@SpringBootApplication
@RestController
public class SafariBooksOnlineApplication extends WebMvcConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(SafariBooksOnlineApplication.class);
    private final DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final List<Data> list = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(SafariBooksOnlineApplication.class, args);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    @RequestMapping(path = "/write", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> writeToFile() throws IOException {
        list.stream().sorted(Comparator.comparing(Data::getTextarea));

        Files.write(Paths.get("/dev/shm/textarea.txt"),
                list.stream().map(Data::getTextarea).collect(Collectors.toList()),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(Paths.get("/dev/shm/h.txt"),
                list.stream().map(Data::getWgetimg).collect(Collectors.toList()),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        list.clear();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("now", LocalDateTime.now().format(dtf));
        return result;
    }

    @RequestMapping(path = "/safaribooksonline", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> safaribooksonline(@RequestBody Data data) {
        logger.debug("textarea: {}", data.getTextarea());
        logger.debug("wgetimg: {}", data.getWgetimg());

        list.add(data);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("now", LocalDateTime.now().format(dtf));
        return result;
    }
}

class Data {
    private String textarea;
    private String wgetimg;

    String getTextarea() {
        return textarea;
    }

    public void setTextarea(String textarea) {
        this.textarea = textarea;
    }

    String getWgetimg() {
        return wgetimg;
    }

    public void setWgetimg(String wgetimg) {
        this.wgetimg = wgetimg;
    }
}