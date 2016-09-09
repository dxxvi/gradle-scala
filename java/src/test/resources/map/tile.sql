create table TILE (
  ID integer primary key auto_increment,
  UPID integer,
  DOWNID integer,
  LEFTID integer,
  RIGHTID integer,
  MD5 varchar2(32),
  FOLDER varchar2(64),
  NAME varchar2(32),
  SIZE integer not null,
  ZOOM integer not null,
  ASID1 integer,
  ASID2 integer,
  ASID3 integer,
  foreign key (UPID) references (ID),
  foreign key (DOWNID) references (ID),
  foreign key (LEFTID) references (ID),
  foreign key (RIGHTID) references (ID),
  foreign key (ASID1) references (ID),
  foreign key (ASID2) references (ID),
  foreign key (ASID3) references (ID),
  unique (FOLDER, NAME)
);

create index ZOOM_IDX on TILE(ZOOM);

select t1.ID, t1.NAME, t1.UPID, t1.DOWNID, t1.LEFTID, t1.RIGHTID, t2.ID, t2.NAME, t2.UPID, t2.DOWNID, t2.LEFTID, t2.RIGHTID
  from TILE t1, TILE t2
  where t1.FOLDER = '/dev/shm/usa-maps/32.0and-108.0at8z' and t2.FOLDER = '/dev/shm/usa-maps/32.0and-107.0at8z'
    and t1.MD5 = t2.MD5
;

select t1.ID, t1.FOLDER || '/' || t1.NAME, t1.UPID, t1.DOWNID, t1.LEFTID, t1.RIGHTID, t2.ID, t2.FOLDER || '/' || t2.NAME, t2.UPID, t2.DOWNID, t2.LEFTID, t2.RIGHTID
  from TILE t1, TILE t2
  where 1 = 1
  --  and t1.FOLDER = '/dev/shm/usa-maps/32.0and-108.0at8z' and t2.FOLDER = '/dev/shm/usa-maps/32.0and-107.0at8z'
    and t1.FOLDER <> t2.FOLDER
    and t1.MD5 = t2.MD5
    and t1.ZOOM = t2.ZOOM
;
