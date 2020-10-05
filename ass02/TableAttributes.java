

public final class TableAttributes {
    public enum ATTRIBUTES {
        TYPE,
        GEARS,
        WHEEL_BASE,
        HEIGHT,
        COLOR,
        MATERIAL,

        MASK
    }

    public String type = null;
    public String color = null;
    public String material = null;
    public int gears = -1;
    public int wheelBase = -1;
    public int height = -1;

    public ATTRIBUTES sort = ATTRIBUTES.MASK;
}