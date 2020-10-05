import java.sql.SQLException;

/// A simple class that runs through all three operations and
/// tests them together
public final class TestMyBicycles {
    public static void main(String[] args) throws SQLException {
        String hostip = BikesUtil.getHost();
        String passwd = BikesUtil.getPasswd();

        TableAttributes ta = new TableAttributes();
        ta.type = "race_bike";
        ta.color = "black";
        ta.material = "carbon";
        ta.gears = 6;
        ta.sort = TableAttributes.ATTRIBUTES.HEIGHT;
        System.out.println("Search for bicycles");
        (new SearchTable(hostip, passwd)).run(ta);


        ta.wheelBase = 42;
        ta.height = 2;
        System.out.println("");
        System.out.println("Insert a bicycle");
        (new InsertRow(hostip, passwd)).run(ta);

        System.out.println("");
        System.out.println("Delete all bicycle");
        (new DeleteRow(hostip, passwd)).run(ta);

        System.out.println("");
        System.out.println("Re-add one bicycle");
        (new InsertRow(hostip, passwd)).run(ta);

        ta.wheelBase = -1;
        ta.height = -1;
        ta.sort = TableAttributes.ATTRIBUTES.WHEEL_BASE;
        System.out.println("");
        System.out.println("Search Bicycles again");
        (new SearchTable(hostip, passwd)).run(ta);
    }
}