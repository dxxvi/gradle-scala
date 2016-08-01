package home;

import org.junit.Test;

/**
 * Created by ly on 7/31/16. Please read the README.md file.
 */
public class ImageSplittingTest {
    @Test public void test() {
        int widthUnit = 1084, numberOfWidthUnit = 4, heightUnit = 1355, numberOfHeightUnit = 4;
        String fileName = "nj-40.83--74.12-1084-4-1355-4.png";
        for (int x = 0; x < numberOfWidthUnit; x++) {
            for (int y = 0; y < numberOfHeightUnit; y++) {
                System.out.printf("convert -crop %dx%d+%d+%d %s %s\n", widthUnit, heightUnit, x*widthUnit, y*heightUnit,
                        fileName, String.format("p-%d-%d.png", x, y));
            }
        }
    }
}
