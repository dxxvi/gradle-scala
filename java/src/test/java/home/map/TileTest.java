package home.map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.awt.RenderingHints.*;

/**
 * Created by ly on 8/9/16.
 * Run this test after running phantomjs tile.js
 */
public class TileTest {
    // puts all tiles in a folder together to make a map
    @Test public void test() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        DataAccessService dataAccessService = context.getBean(DataAccessService.class);
        final int width = 20;
        final int height = 22;
        final int topLeftId = 114;
        Tile[][] tiles = new Tile[width][height];
        Optional<Tile> startTile = dataAccessService.getTileById(topLeftId);
        if (!startTile.isPresent()) {
            throw new RuntimeException("No tile with id " + topLeftId);
        }
        tiles[0][0] = startTile.get();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y] == null) {
                    if (y > 0 && tiles[x][y-1] != null) {
                        tiles[x][y] = dataAccessService.getTileById(tiles[x][y-1].getDownId()).orElse(null);
                    }
                    else if (x > 0 && tiles[x-1][y] != null) {
                        tiles[x][y] = dataAccessService.getTileById(tiles[x-1][y].getRightId()).orElse(null);
                    }
                }
            }
        }

        BufferedImage bufferedImage = new BufferedImage(width*256, height*256, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y] != null) {
                    BufferedImage bi = ImageIO.read(new File(tiles[x][y].getFolder() + File.separator + tiles[x][y].getName()));
                    g2d.drawImage(bi, x * 256, y * 256, null);
                }
            }
        }
        ImageIO.write(bufferedImage, "png", new File("/dev/shm/test.png"));
    }

    @Test public void test1() throws Exception {
        final int WIDTH_MARGIN = 12;
        final int HEIGHT_MARGIN = 9;
        String[][] array = {
                "4578 4665 4571 4874 4692 4642 4780 4606 4489 4631 4788 4753 4501 4688 4454 4484 4720 4700 4734 4774".split(" "),
                "4727 4764 4478 4704 4710 4523 4860 4765 4469 4511 4475 4492 4718 4471 4608 4534 4654 4724 4619 4706".split(" "),
                "4592 4556 4682 4624 4658 4838 4519 4726 4801 4615 4876 4679 4856 4537 4472 4687 4562 4739 4725 4625".split(" "),
                "4495 4661 4892 4828 4735 4573 4582 4645 4546 4775 4805 4792 4868 4494 4804 4626 4819 4539 4610 4836".split(" "),
                "4766 4583 4675 4464 4782 4830 4858 4837 4757 4887 4857 4575 4465 4822 4891 4453 4503 4616 4553 4560".split(" "),
                "4772 4842 4752 4789 4577 4480 4504 4776 4569 4690 4756 4798 4761 4693 4781 4839 4701 4879 4698 4568".split(" "),
                "4474 4699 4807 4482 4812 4601 4530 4759 4618 4888 4827 4521 4602 4603 4758 4627 4594 4680 4709 4507".split(" "),
                "4841 4669 4621 4852 4769 4885 4460 4820 4878 4890 4736 4770 4676 4722 4855 4806 4714 4737 4748 4598".split(" "),
                "4579 4717 4525 4633 4461 4810 4849 4499 4660 4863 4799 4815 4681 4793 4524 4634 4749 4541 4500 4589".split(" "),
                "4685 4641 4846 4854 4884 4529 4457 4867 4652 4873 4528 4834 4564 4590 4705 4696 4570 4755 4673 4824".split(" "),
                "4605 4455 4531 4488 4648 4833 4614 4584 4640 4508 4871 4462 4607 4566 4733 4650 4861 4473 4889 4493".split(" "),
                "4729 4497 4563 4818 4742 4644 4538 4588 4794 4485 4459 4762 4623 4552 4702 4458 4513 4691 4744 4708".split(" "),
                "4527 4731 4817 4540 4613 4831 4628 4638 4516 4466 4591 4741 4778 4763 4797 4721 4548 4773 4496 4777".split(" "),
                "4723 4580 4515 4865 4886 4694 4787 4851 4802 4745 4883 4479 4809 4576 4596 4862 4535 4784 4554 4629".split(" "),
                "4790 4512 4740 4779 4514 4586 4751 4593 4622 4536 4825 4829 4754 4609 4533 4611 4707 4656 4549 4643".split(" "),
                "4542 4662 4719 4487 4844 4786 4689 4716 4595 4832 4502 4520 4547 4657 4695 4647 4843 4545 4826 4558".split(" "),
                "4697 4728 4551 4813 4505 4612 4490 4559 4532 4840 4678 4674 4651 4872 4870 4670 4814 4599 4663 4715".split(" "),
                "4635 4672 4845 4866 4522 4882 4683 4486 4476 4767 4803 4785 4561 4659 4620 4847 4811 4703 4835 4850".split(" "),
                "4574 4483 4572 4477 4565 4467 4481 4760 4637 4864 4875 4468 4509 4649 4498 4666 4711 4686 4470 4881".split(" "),
                "4800 4639 4869 4653 4544 4456 4585 4795 4630 4668 4636 4491 4816 4587 4823 4712 4821 4510 4746 4597".split(" ")
/*
                "380 277 281 100 448 343 430 232 342 346 293 289 167 44 51 81 367 411 136 338 67 344 456 28/565 758 613 658 470 461 709 500 746 608 757 794 562 753 750 771 595 625 723 642/974 1308 924 951 1159 1188 1202 1305 1233 910 1039 1027 1020 1287 1174 1288 921 1239 856 843 1012 1192 990 1231 1066/1578 1654 1576 1633 1651 1380 1550 1534 1377 1510 1563 1559 1542 1591 1549 1449 1514 1561 1394".split(" "),
                "176 413 416 443 124 49 20 72 103 190 61 337 236 131 349 198 184 38 288 309 152 225 19 268/670 717 789 822 656 782 748 719 548 673 523 632 796 701 631 651 690 627 814 460/896 992 1204 1210 1026 1103 1036 899 1266 1203 1170 891 1195 1255 838 901 1007 890 946 997 958 882 1165 1090 1213/1609 1313 1427 1404 1476 1417 1545 1556 1332 1497 1454 1421 1538 1330 1340 1659 1656 1482 1352".split(" "),
                "168 324 86 340 235 107 89 245 274 79 334 208 195 444 54 37 311 284 69 113 389 130 143 409/621 697 503 507 654 473 687 787 530 637 713 552 533 483 649 832 694 572 479 497/858 940 1051 1295 1117 1172 1226 1080 918 1282 1154 1122 1216 1146 854 1143 915 1214 1043 1033 873 1193 860 1160 1014/1602 1665 1667 1397 1371 1629 1466 1521 1489 1626 1467 1398 1450 1425 1325 1606 1605 1451 1401".split(" "),
                "407 278 3 399 185 308 310 126 312 305 441 135 276 296 78 215 359 398 319 423 419 145 445 149/1 695 589 764 542 686 592 633 714 531 803 529 785 724 485 675 662 598 826 586/1093 1076 1097 1010 956 892 885 1074 1208 1040 837 986 1050 1113 1177 876 1296 929 1275 1259 1131 1184 1110 912 948/1448 1635 1435 1531 1648 1403 1653 1475 1604 1412 1376 1615 1614 1360 1351 1567 1399 1524 1551".split(" "),
                "177 193 410 298 437 52 427 428 387 157 42 271 330 45 183 65 384 163 420 297 388 80 279 395/818 820 583 827 667 515 605 474 739 465 825 493 582 491 534 650 600 778 482 527/1053 839 1049 877 1121 1098 1274 962 880 1265 1278 1263 1129 863 1101 935 1088 1063 1132 927 914 870 1082 1023 1271/1572 1646 1504 1564 1387 1343 1405 1658 1465 1359 1587 1627 1502 1518 1461 1423 1596 1443 1517".split(" "),
                "73 169 161 186 171 370 231 285 335 454 452 134 158 267 368 418 178 253 345 82 353 434 213 362/648 756 791 798 652 711 502 691 809 480 715 802 487 674 688 588 464 676 543 696/1078 1254 1087 1136 1300 1056 1058 1301 1291 1025 908 1201 1269 1236 1261 895 977 1294 1297 1013 1306 1157 1006 844 1140/1384 1424 1498 1459 1458 1589 1565 1337 1370 1390 1314 1620 1339 1503 1571 1433 1432 1478 1362".split(" "),
                "147 402 66 25 70 13 216 333 84 339 62 90 125 403 331 210 246 316 439 217 138 230 450 63/685 546 780 773 477 807 752 830 544 681 590 471 616 536 547 718 692 677 754 768/1055 905 1089 1284 1018 1169 861 1230 1218 1094 1186 864 973 859 1241 906 1120 1067 971 1293 1037 959 1091 1111 1178/1671 1566 1645 1323 1447 1533 1597 1420 1619 1511 1336 1426 1364 1557 1318 1668 1456 1372 1349".split(" "),
                "203 58 104 442 141 406 377 238 156 31 132 304 222 174 32 405 323 417 116 227 40 133 326 350/506 777 705 596 469 551 817 512 797 703 538 587 740 665 505 786 581 666 574 614/1124 1243 1139 1173 1181 1209 841 998 1126 1057 1290 1260 1107 1004 1150 916 1021 937 1042 855 996 928 1002 1125 1075/1636 1612 1649 1492 1335 1547 1463 1312 1652 1537 1474 1381 1575 1608 1319 1400 1558 1382 1546".split(" "),
                "270 148 365 373 11 179 243 272 6 421 383 385 314 144 77 76 301 173 247 175 280 160 105 56/517 577 570 539 738 488 644 468 466 640 458 540 698 481 494 761 779 528 522 457/952 939 1028 1268 1095 985 1065 949 978 1285 963 1307 868 1199 1059 1207 976 938 984 1152 1245 1077 960 1197 980/1354 1484 1569 1669 1663 1469 1516 1326 1428 1496 1477 1368 1583 1536 1355 1573 1501 1396 1324".split(" "),
                "374 159 259 172 188 256 320 206 202 360 151 252 121 363 2 95 12 435 237 209 146 392 248 446/776 806 726 513 620 593 799 733 812 545 463 635 604 603 550 760 801 813 736 731/1215 1205 1116 1005 1187 999 1168 1009 1279 1099 991 1048 1302 1148 975 1064 866 903 875 934 1032 1167 1176 931 982/1414 1430 1374 1462 1607 1408 1373 1660 1378 1418 1593 1544 1441 1623 1631 1634 1452 1338 1464".split(" "),
                "306 394 108 303 102 111 433 375 142 336 244 250 153 302 87 114 391 255 30 451 10 194 50 241/568 585 775 610 526 622 728 484 661 672 763 645 619 601 537 514 730 684 707 569/857 1234 1262 1183 1249 1105 1102 1194 1248 1258 950 913 932 1141 1250 1041 1198 1017 925 1280 1166 1179 1191 1164 1046/1494 1637 1592 1590 1445 1438 1650 1361 1664 1480 1429 1416 1613 1662 1640 1594 1625 1639 1588".split(" "),
                "98 449 106 170 1 325 426 378 129 119 24 273 299 122 109 381 431 287 123 352 251 64 128 39/816 737 584 535 573 575 744 668 578 509 815 833 810 647 495 643 793 511 567 501/1196 1127 941 1003 944 1083 1270 1256 1240 1223 926 1299 1228 904 983 1138 1221 936 970 1070 1130 1156 979 907 1019/1455 1508 1585 1487 1548 1493 1584 1528 1552 1579 1431 1509 1655 1630 1603 1346 1411 1347 1322".split(" "),
*/
        };
        ApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        DataAccessService dataAccessService = context.getBean(DataAccessService.class);
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 4; c++) {
                BufferedImage bufferedImage = new BufferedImage(5*256 + WIDTH_MARGIN*2, 4*256 + HEIGHT_MARGIN*2, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
                g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
                g2d.setColor(new Color(255, 100, 100));

                for (int i= 0; i < 4; i++) {
                    for (int j = 0; j < 5; j++) {
                        Optional<Tile> _t = dataAccessService.getTileById(Integer.valueOf(array[4*r + i][5*c + j].split("/")[0]));
                        if (!_t.isPresent()) {
                            throw new RuntimeException("No tile for " + array[4*r + i][5*c + j]);
                        }
                        Tile t = _t.get();
                        BufferedImage bi = ImageIO.read(new File(t.getFolder() + File.separator + t.getName()));
                        g2d.drawImage(bi, WIDTH_MARGIN + 256*j, HEIGHT_MARGIN + 256*i, null);
                    }
                }

                g2d.drawString(String.format("r-%d c-%d", r, c), 15, 22);
                g2d.dispose();
                ImageIO.write(bufferedImage, "jpg", new File(String.format("/dev/shm/r-%d-c-%d.jpg", r, c)));
            }
        }
    }

    @Test public void test2() throws IOException {
        StringBuilder sb = new StringBuilder();

        for (float _x = 29f; _x <= 42.29f; _x += 49f) {
            for (float _y = -90; _y <= -70f; _y += 100.9f) {
                String x = String.format("%.1f", _x);
                String y = String.format("%.1f", _y);
                sb.append(String.format("cp template.js %sand%sat7.5z.js\n", x, y));
                sb.append(String.format("echo page.open\\(\\'https://www.google.com/maps/@%s,%s,7.5z?force=lite\\', f1\\)\\; >> %sand%sat7.5z.js\n", x, y, x, y));
                sb.append(String.format("phantomjs %sand%sat7.5z.js > %sand%sat7.5z.wget\n", x, y, x, y));
                // sb.append(String.format("mv map.png %sand%sat7.5z.png\n", x, y));
                sb.append(String.format("mkdir %sand%sat7.5z\n", x, y));
                sb.append(String.format("cd %sand%sat7.5z\n", x, y));
                sb.append(String.format(". ../%sand%sat7.5z.wget\n", x, y));
                sb.append("cd ..\n");
                sb.append(String.format("java -cp . TileTest %sand%sat7.5z\n\n", x, y));
            }
        }
        Files.write(Paths.get("/dev/shm/run.sh"), sb.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Test public void createDatabaseRecords() {
        class Utils {
            private String calculateMd5(Path path) {
                StringBuilder sb = new StringBuilder();
                try {
                    MessageDigest md5Digest = MessageDigest.getInstance("MD5");
                    byte[] md5 = md5Digest.digest(Files.readAllBytes(path));
                    for (byte t : md5) {
                        sb.append(String.format("%02x", t));
                    }
                    return sb.toString();
                }
                catch (IOException | NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
            }

            private Stream<File> getAllTileFiles(Path parent) {
                return Stream.of(parent.toFile().listFiles())
                        .flatMap(f -> {
                            if (f.isFile()) {
                                return f.getName().endsWith("px.png") ? Stream.of(f) : Stream.empty();
                            }
                            else {
                                return Stream.of(f.listFiles()).filter(File::isFile);
                            }
                        })
                        .filter(f -> f.getName().endsWith("px.png"));
            }
        }
        Utils utils = new Utils();

        ApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        DataAccessService dataAccessService = context.getBean(DataAccessService.class);

        Pattern pattern = Pattern.compile(".+at(\\d+?)z");

        utils.getAllTileFiles(Paths.get("/dev/shm/usa-maps/30.0and-100.0at7z"))
                .forEach(f -> {
                    String md5 = utils.calculateMd5(f.toPath());
                    int size = (int)f.length();
                    Matcher matcher = pattern.matcher(f.getParentFile().getName());
                    if (matcher.find()) {
                        dataAccessService.insert(
                                new Tile(f.getParent(), f.getName(), size, Integer.parseInt(matcher.group(1)))
                                        .setMd5(md5)
                        );
                    }
                    else {
                        throw new RuntimeException("Invalid file " + f.getAbsolutePath());
                    }
                });
    }

    @Test public void test4() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        DataAccessService dataAccessService = context.getBean(DataAccessService.class);
        AtomicBoolean ableToUpdateAny = new AtomicBoolean(true);

        while (ableToUpdateAny.get()) {
            ableToUpdateAny.set(false);

            dataAccessService.findKernelTiles().forEach(center -> {
                Optional<Tile> up = center.upTile()
                        .flatMap(t -> dataAccessService.getTileByFolderAndName(t.getFolder(), t.getName()));
                Optional<Tile> down = center.downTile()
                        .flatMap(t -> dataAccessService.getTileByFolderAndName(t.getFolder(), t.getName()));
                Optional<Tile> left = center.leftTile()
                        .flatMap(t -> dataAccessService.getTileByFolderAndName(t.getFolder(), t.getName()));
                Optional<Tile> right = center.rightTile()
                        .flatMap(t -> dataAccessService.getTileByFolderAndName(t.getFolder(), t.getName()));

                if (up.isPresent() && (center.getUpId() == null || up.get().getDownId() == null)) {
                    dataAccessService.updateCenterUp(center, up.get());
                    ableToUpdateAny.set(true);
                }
                if (down.isPresent() && (center.getDownId() == null || down.get().getUpId() == null)) {
                    dataAccessService.updateCenterDown(center, down.get());
                    ableToUpdateAny.set(true);
                }
                if (left.isPresent() && (center.getLeftId() == null || left.get().getRightId() == null)) {
                    dataAccessService.updateCenterLeft(center, left.get());
                    ableToUpdateAny.set(true);
                }
                if (right.isPresent() && (center.getRightId() == null || right.get().getLeftId() == null)) {
                    dataAccessService.updateCenterRight(center, right.get());
                    ableToUpdateAny.set(true);
                }
            });
        }
    }

    @Test public void test6() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        context.getBean(DataAccessService.class).complete();
    }

    @Test public void test7() {
        Pattern pattern = Pattern.compile(".+at(\\d+?)z");
        Matcher matcher = pattern.matcher("36.8and-109.1at19z");
        if (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    @Test public void updateDuplicates() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        DataAccessService dataAccessService = context.getBean(DataAccessService.class);
        dataAccessService.findDuplicates().forEach(p -> {
            // need to reload tiles from db because they can be updated in previous loops of forEach
            Optional<Tile> _t1 = dataAccessService.getTileById(p.tile1.getId()),
                           _t2 = dataAccessService.getTileById(p.tile2.getId());
            if (!_t1.isPresent() || !_t2.isPresent()) {
                throw new RuntimeException("Which one of " + p.tile1.getId() + " and " + p.tile2.getId() +
                        " doesn't exist?");
            }
            Tile t1 = _t1.get(), t2 = _t2.get();
            t1.setAsId(t2.getId());
            t2.setAsId(t1.getId());
            dataAccessService.update(new Pair(t1, t2));
        });
    }

    @Test public void test8() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        DataAccessService dataAccessService = context.getBean(DataAccessService.class);
        LinkedList<String> list = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        int leftMostId = 4685;
        while (true) {
            int id = leftMostId;
            list.clear();
            while (true) {
                Optional<Tile> _t = dataAccessService.getTileById(id);
                if (_t.isPresent()) {
                    Tile t = _t.get();
                    list.add(t.getId().toString());
                    if (t.getRightId() != null) {
                        id = t.getRightId();
                    } else if (t.getAsId1() != null) {
                        Optional<Tile> _asId1Tile = dataAccessService.getTileById(t.getAsId1());
                        if (_asId1Tile.isPresent()) {
                            Tile asId1Tile = _asId1Tile.get();
                            if (asId1Tile.getRightId() != null) {
                                String s = list.removeLast() + "/" + t.getAsId1();
                                list.add(s);
                                id = asId1Tile.getRightId();
                            } else {
                                throw new RuntimeException(String.format("This %s has asId1 (%s) but it doesn't have rightId", t, asId1Tile));
                            }
                        } else {
                            throw new RuntimeException(String.format("This %s has non-existing asId1", t));
                        }
                    } else {
                        sb.append(list.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(" "))).append('\n');
                        // throw new RuntimeException(String.format("This %s doesn't have a right tile nor asId1", t));
                        Optional<Tile> _leftMostTile = dataAccessService.getTileById(leftMostId);
                        if (_leftMostTile.isPresent()) {
                            Tile leftMostTile = _leftMostTile.get();
                            if (leftMostTile.getDownId() != null) {
                                leftMostId = leftMostTile.getDownId();
                                break;
                            } else {
                                System.out.println(sb);
                                throw new RuntimeException(String.format("Left most tile %d has no down tile", leftMostId));
                            }
                        } else {
                            throw new RuntimeException("Serious problem: no tile for " + leftMostId);
                        }
                    }
                } else {
                    throw new RuntimeException("No " + id);
                }
            }
        }
    }
}
