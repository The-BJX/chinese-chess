package data;

public class InitialPositions {
    public static final Position redGeneral = new Position(9, 4);
    public static final Position blackGeneral = new Position(0, 4);
    public static final Position[] redSoldiers = {
        new Position(6, 0),
        new Position(6, 2),
        new Position(6, 4),
        new Position(6, 6),
        new Position(6, 8)
    };
    public static final Position[] blackSoldiers = {
        new Position(3, 0),
        new Position(3, 2),
        new Position(3, 4),
        new Position(3, 6),
        new Position(3, 8)
    };
    public static final Position[] redCannon = {
        new Position(7, 1),
        new Position(7, 7)
    };
    public static final Position[] blackCannon = {
        new Position(2, 1),
        new Position(2, 7)
    };
}
