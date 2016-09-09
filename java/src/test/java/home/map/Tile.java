package home.map;

import java.io.File;
import java.io.Serializable;
import java.util.Optional;

/**
 * Created by ly on 8/28/16.
 */
public class Tile implements Serializable {
    private Integer id;
    private Integer upId;
    private Integer downId;
    private Integer leftId;
    private Integer rightId;
    private String md5;
    private String folder;
    private String name;
    private int size;
    private int zoom;
    private Integer asId1;
    private Integer asId2;
    private Integer asId3;

    public Tile(String folder, String name) {
        this.folder = folder;
        this.name = name;
    }

    public Tile(String folder, String name, int size, int zoom) {
        this.folder = folder;
        this.name = name;
        this.size = size;
        this.zoom = zoom;
    }

    public Integer getId() {
        return id;
    }

    public Tile setId(Integer id) {
        this.id = id;
        return this;
    }

    public int getSize() {
        return size;
    }

    public int getZoom() {
        return zoom;
    }

    public Integer getUpId() {
        return upId;
    }

    public Tile setUpId(Integer upId) {
        this.upId = upId;
        return this;
    }

    public Integer getDownId() {
        return downId;
    }

    public Tile setDownId(Integer downId) {
        this.downId = downId;
        return this;
    }

    public Integer getLeftId() {
        return leftId;
    }

    public Tile setLeftId(Integer leftId) {
        this.leftId = leftId;
        return this;
    }

    public Integer getRightId() {
        return rightId;
    }

    public Tile setRightId(Integer rightId) {
        this.rightId = rightId;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public Tile setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public String getFolder() {
        return folder;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Tile {" +
                "id=" + id +
                ", upId=" + upId +
                ", downId=" + downId +
                ", leftId=" + leftId +
                ", rightId=" + rightId +
                ", md5='" + md5 + '\'' +
                ", location='" + folder + File.separator + name + '\'' +
                ", size=" + size +
                (asId1 != null ? ", asId1=" + asId1 : "") +
                (asId2 != null ? ", asId2=" + asId2 : "") +
                (asId3 != null ? ", asId3=" + asId3 : "") +
                '}';
    }

    public Optional<Tile> upTile() {
        String[] s = name.replace("px.png", "").split("px");
        return Optional.of(new Tile(folder, s[0] + "px" + (Integer.parseInt(s[1]) - 256) + "px.png"));
    }

    public Optional<Tile> downTile() {
        String[] s = name.replace("px.png", "").split("px");
        return Optional.of(new Tile(folder, s[0] + "px" + (Integer.parseInt(s[1]) + 256) + "px.png"));
    }

    public Optional<Tile> leftTile() {
        String[] s = name.replace("px.png", "").split("px");
        return Optional.of(new Tile(folder, (Integer.parseInt(s[0]) - 256) + "px" + s[1] + "px.png"));
    }

    public Optional<Tile> rightTile() {
        String[] s = name.replace("px.png", "").split("px");
        return Optional.of(new Tile(folder, (Integer.parseInt(s[0]) + 256) + "px" + s[1] + "px.png"));
    }

    public Integer getAsId1() {
        return asId1;
    }

    public Tile setAsId1(Integer asId1) {
        this.asId1 = asId1;
        return this;
    }

    public Integer getAsId2() {
        return asId2;
    }

    public Tile setAsId2(Integer asId2) {
        this.asId2 = asId2;
        return this;
    }

    public Integer getAsId3() {
        return asId3;
    }

    public Tile setAsId3(Integer asId3) {
        this.asId3 = asId3;
        return this;
    }

    public void setAsId(int _asId) {
        Integer asId = Integer.valueOf(_asId);
        if (!asId.equals(asId1) && !asId.equals(asId2) && !asId.equals(asId3)) {
            if (asId1 == null) {
                asId1 = asId;
            }
            else if (asId2 == null) {
                asId2 = asId;
            }
            else if (asId3 == null) {
                asId3 = asId;
            }
            else {
                throw new RuntimeException("Tile " + id + " has no room for asId " + _asId);
            }
        }
    }
}
