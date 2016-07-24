package home;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ly on 6/26/16.
 */
public class FileRenamingTest {
    @Test public void test() throws IOException {
        final String s = " - ";
        Files.list(Paths.get("/dev/shm/Queen - The Best Songs")).forEach(path -> {
            String t = path.getFileName().toString();
            try {
                Files.move(path, path.getParent().resolve(t.replaceAll(" .", ".")));
            }
            catch (IOException ioex) {
                throw new RuntimeException(ioex);
            }
        });

        Tiger tiger = new Tiger().setAge(3).setName("my tiger").setColor("yellow");
    }
}

class AutoPart {
    private int number;
    private String name;
    private boolean outOfStock;

    public AutoPart(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public AutoPart setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public Optional<AutoPart> outOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
        return Optional.of(this);
    }

    public Optional<AutoPart> replicateAutoPartMonad() {
        System.out.printf("Part replicated: %s\n", this);
        return Optional.of(this);
    }

    @Override
    public String toString() {
        return "AutoPart {" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", outOfStock=" + outOfStock +
                '}';
    }
}

class Animal<Derived extends Animal<Derived>> {
    private int age;
    private String name;

    public int getAge() {
        return age;
    }

    public Derived setAge(int age) {
        this.age = age;
        return (Derived) this;
    }

    public String getName() {
        return name;
    }

    public Derived setName(String name) {
        this.name = name;
        return (Derived) this;
    }
}

class Tiger extends Animal<Tiger> {
    private String color;

    public String getColor() {
        return color;
    }

    public Tiger setColor(String color) {
        this.color = color;
        return this;
    }
}
