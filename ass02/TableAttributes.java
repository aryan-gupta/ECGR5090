
/// A simple POD class for holding the table attributes
/// so it can easily be passed around as function parameters
/// Also holds an enum for knowing what key to sort by during
/// searching. See database schema for more info
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