package home.map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by ly on 8/28/16.
 */
public class DataAccessService {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Tile> rowMapper =
            (rs, rowNum) ->
                    new Tile(rs.getString("FOLDER"), rs.getString("NAME"), rs.getInt("SIZE"), rs.getInt("ZOOM"))
                    .setId((Integer) rs.getObject("ID"))
                    .setUpId((Integer) rs.getObject("UPID"))
                    .setDownId((Integer) rs.getObject("DOWNID"))
                    .setLeftId((Integer) rs.getObject("LEFTID"))
                    .setRightId((Integer) rs.getObject("RIGHTID"))
                    .setMd5(rs.getString("MD5"))
                    .setAsId1((Integer) rs.getObject("ASID1"))
                    .setAsId2((Integer) rs.getObject("ASID2"))
                    .setAsId3((Integer) rs.getObject("ASID3"))
            ;
    
    private final RowMapper<Pair> pairRowMapper = (rs, rowNum) -> {
        Tile tile1 = new Tile(rs.getString("FOLDER"), rs.getString("NAME"), rs.getInt("SIZE"), rs.getInt("ZOOM"))
                .setId((Integer) rs.getObject("ID"))
                .setUpId((Integer) rs.getObject("UPID"))
                .setDownId((Integer) rs.getObject("DOWNID"))
                .setLeftId((Integer) rs.getObject("LEFTID"))
                .setRightId((Integer) rs.getObject("RIGHTID"))
                .setMd5(rs.getString("MD5"))
                .setAsId1((Integer) rs.getObject("ASID1"))
                .setAsId2((Integer) rs.getObject("ASID2"))
                .setAsId3((Integer) rs.getObject("ASID3"));
        Tile tile2 = new Tile(rs.getString("_FOLDER"), rs.getString("_NAME"), rs.getInt("_SIZE"), rs.getInt("_ZOOM"))
                .setId((Integer) rs.getObject("_ID"))
                .setUpId((Integer) rs.getObject("_UPID"))
                .setDownId((Integer) rs.getObject("_DOWNID"))
                .setLeftId((Integer) rs.getObject("_LEFTID"))
                .setRightId((Integer) rs.getObject("_RIGHTID"))
                .setMd5(rs.getString("_MD5"))
                .setAsId1((Integer) rs.getObject("_ASID1"))
                .setAsId2((Integer) rs.getObject("_ASID2"))
                .setAsId3((Integer) rs.getObject("_ASID3"));
        return new Pair(tile1, tile2);
    };

    public DataAccessService(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Tile> getAllTiles() {
        return jdbcTemplate.query("select * from TILE", rowMapper);
    }

    @Transactional public int insert(Tile tile) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("upid", tile.getUpId());
        parameters.put("downid", tile.getDownId());
        parameters.put("leftid", tile.getLeftId());
        parameters.put("rightid", tile.getRightId());
        parameters.put("md5", tile.getMd5());
        parameters.put("folder", tile.getFolder());
        parameters.put("name", tile.getName());
        parameters.put("size", tile.getSize());
        parameters.put("zoom", tile.getZoom());
        parameters.put("asid1", tile.getAsId1());
        parameters.put("asid2", tile.getAsId2());
        parameters.put("asid3", tile.getAsId3());

        SqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update("insert into TILE (UPID, DOWNID, LEFTID, RIGHTID, MD5, FOLDER, NAME, SIZE, ZOOM, ASID1, ASID2, ASID3) " +
                " values (:upid, :downid, :leftid, :rightid, :md5, :folder, :name, :size, :zoom, :asid1, :asid2, :asid3)",
                parameterSource, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // there's already a unique constraint on (folder, name) so
    public Optional<Tile> getTileByFolderAndName(String folder, String name) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("folder", folder);
        parameterSource.addValue("name", name);

        List<Tile> result = jdbcTemplate.query("select * from TILE where FOLDER = :folder and NAME = :name",
                parameterSource, rowMapper);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public Optional<Tile> getTileById(Integer id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        List<Tile> result = jdbcTemplate.query("select * from TILE where ID = :id", parameterSource, rowMapper);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public List<Tile> getTilesToUpdateNeighbors() {
        return jdbcTemplate.query("select * from TILE where" +
                " (UPID is not null or DOWNID is not null or LEFTID is not null or RIGHTID is not null)", rowMapper);
    }

    @Transactional(propagation = Propagation.MANDATORY) public void update(Tile t) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("upid", t.getUpId());
        parameters.put("downid", t.getDownId());
        parameters.put("leftid", t.getLeftId());
        parameters.put("rightid", t.getRightId());
        parameters.put("md5", t.getMd5());
        parameters.put("folder", t.getFolder());
        parameters.put("name", t.getName());
        parameters.put("size", t.getSize());
        parameters.put("zoom", t.getZoom());
        parameters.put("asid1", t.getAsId1());
        parameters.put("asid2", t.getAsId2());
        parameters.put("asid3", t.getAsId3());
        parameters.put("id", t.getId());

        jdbcTemplate.update("update TILE set UPID = :upid, DOWNID = :downid, LEFTID = :leftid, RIGHTID = :rightid," +
                " ASID1 = :asid1, ASID2 = :asid2, ASID3 = :asid3, MD5 = :md5, FOLDER = :folder, NAME = :name," +
                " SIZE = :size, ZOOM = :zoom where ID = :id", parameters);
    }

    // update the id's in these center and up tiles
    @Transactional public void updateCenterUp(Tile center, Tile up) {
        center.setUpId(up.getId());
        up.setDownId(center.getId());
        update(center);
        update(up);
    }

    @Transactional public void updateCenterDown(Tile center, Tile down) {
        center.setDownId(down.getId());
        down.setUpId(center.getId());
        update(center);
        update(down);
    }

    @Transactional public void updateCenterLeft(Tile center, Tile left) {
        center.setLeftId(left.getId());
        left.setRightId(center.getId());
        update(center);
        update(left);
    }

    @Transactional public void updateCenterRight(Tile center, Tile right) {
        center.setRightId(right.getId());
        right.setLeftId(center.getId());
        update(center);
        update(right);
    }

    @Transactional public void complete() {
        class Temp {
            private final Integer id1;
            private final Integer id2;

            private Temp(Integer id1, Integer id2) {
                this.id1 = id1;
                this.id2 = id2;
            }
        }
        RowMapper<Temp> rm = (rs, rowNum) -> new Temp((Integer)rs.getObject("ID1"), (Integer)rs.getObject("ID2"));
        jdbcTemplate.query("select t1.ID as ID1, t2.ID as ID2 from TILE t1, TILE t2 " +
                "where t1.UPID is null and t2.DOWNID = t1.ID", rm)
                .forEach(t -> {
                    getTileById(t.id1).flatMap(tile -> {
                        tile.setUpId(t.id2);
                        update(tile);
                        return Optional.empty();
                    });
                });
        jdbcTemplate.query("select t1.ID as ID1, t2.ID as ID2 from TILE t1, TILE t2 " +
                "where t1.DOWNID is null and t2.UPID = t1.ID", rm)
                .forEach(t -> {
                    getTileById(t.id1).flatMap(tile -> {
                        tile.setDownId(t.id2);
                        update(tile);
                        return Optional.empty();
                    });
                });
        jdbcTemplate.query("select t1.ID as ID1, t2.ID as ID2 from TILE t1, TILE t2 " +
                "where t1.LEFTID is null and t2.RIGHTID = t1.ID", rm)
                .forEach(t -> {
                    getTileById(t.id1).flatMap(tile -> {
                        tile.setLeftId(t.id2);
                        update(tile);
                        return Optional.empty();
                    });
                });
        jdbcTemplate.query("select t1.ID as ID1, t2.ID as ID2 from TILE t1, TILE t2 " +
                "where t1.RIGHTID is null and t2.LEFTID = t1.ID", rm)
                .forEach(t -> {
                    getTileById(t.id1).flatMap(tile -> {
                        tile.setRightId(t.id2);
                        update(tile);
                        return Optional.empty();
                    });
                });
    }

    // a kernel tile has all 4 UPID, DOWNID, LEFTID, RIGHTID of null and size > 1000 bytes
    public List<Tile> findKernelTiles() {
        List<Tile> result = new LinkedList<>();
/*
        result.addAll(jdbcTemplate.query("select * from TILE where UPID is null and DOWNID is null", rowMapper));
        result.addAll(jdbcTemplate.query("select * from TILE where UPID is null and LEFTID is null", rowMapper));
        result.addAll(jdbcTemplate.query("select * from TILE where UPID is null and RIGHTID is null", rowMapper));
        result.addAll(jdbcTemplate.query("select * from TILE where DOWNID is null and LEFTID is null", rowMapper));
        result.addAll(jdbcTemplate.query("select * from TILE where DOWNID is null and RIGHTID is null", rowMapper));
        result.addAll(jdbcTemplate.query("select * from TILE where LEFTID is null and RIGHTID is null", rowMapper));
*/

        result.addAll(jdbcTemplate.query("select * from TILE" +
                " where UPID is null or DOWNID is null or LEFTID is null or RIGHTID is null", rowMapper));
        return result;
    }

    public List<Pair> findDuplicates() {
        return jdbcTemplate.query("select t1.*, t2.ID as _ID, t2.UPID as _UPID, t2.DOWNID as _DOWNID," +
                " t2.LEFTID as _LEFTID, t2.RIGHTID as _RIGHTID, t2.MD5 as _MD5, t2.FOLDER as _FOLDER," +
                " t2.NAME as _NAME, t2.SIZE as _SIZE, t2.ZOOM as _ZOOM, t2.ASID1 as _ASID1," +
                " t2.ASID2 as _ASID2, t2.ASID3 as _ASID3" +
                " from TILE t1, TILE t2 where t1.MD5 = t2.MD5 and t1.ID <> t2.ID and t1.SIZE > 419", pairRowMapper);
    }

    @Transactional public void update(Pair pair) {
        update(pair.tile1);
        update(pair.tile2);
    }
}
