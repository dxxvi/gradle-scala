package home;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by ly on 8/9/16.
 * Run this test after running phantomjs tile.js
 */
public class TileTest {
    @Test public void test() throws IOException {
        class Utils {
            // filename is like 1234px5678px.png
            private Point filenameToPoint(String filename) {
                try {
                    String[] xy = filename.substring(0, filename.length() - "px.png".length()).split("px");
                    return new Point(Integer.valueOf(xy[0]), Integer.valueOf(xy[1]));
                }
                catch (Throwable t) {
                    return null;
                }
            }

            private String pointToFilename(Point point) {
                return (int)point.getX() + "px" + (int)point.getY() + "px.png";
            }
        }
        Utils utils = new Utils();

        // given a directory with 256px x 256px tiles inside having names like leftpxtoppx.png
        final String directory = "/dev/shm/images";
        final TreeMap<Integer, Set<Point>> map = new TreeMap<>();
        for (String s : new File(directory).list((dir, name) -> utils.filenameToPoint(name) != null)) {
            Point p = utils.filenameToPoint(s);
            map
                    .computeIfAbsent((int) p.getY(), k -> new TreeSet<>((o1, o2) -> {
                        if (o1.getX() < o2.getX()) {
                            return -1;
                        }
                        if (o1.getX() > o2.getX()) {
                            return 1;
                        }
                        return (int) o1.getY() - (int) o2.getY();
                    }))
                    .add(p);
        }

        Map.Entry<Integer, Set<Point>> entry = map.entrySet().iterator().next();
        int width  = entry.getValue().size() * 256;
        int height = map.keySet().size() * 256;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        int row = 0;
        for (Map.Entry<Integer, Set<Point>> e : map.entrySet()) {
            int column = 0;
            for (Point p : e.getValue()) {
                BufferedImage bi = ImageIO.read(new File(directory + File.separator + utils.pointToFilename(p)));
                g2d.drawImage(bi, column * 256, row * 256, null);
                column++;
            }
            row++;
        }
        ImageIO.write(bufferedImage, "png", new File("/dev/shm/map.png"));
    }
}
