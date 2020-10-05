import java.sql.SQLException;

public final class AddBikes {
    private static void printHowTo() {
        java.io.PrintStream out = java.lang.System.out;
        out.println("java -cp .:/usr/share/java/mariadb-jdbc/mariadb-java-client-2.7.0.jar AddBikes -type mountain_bike -gear 5 -wheelbase 36 -height 2 -color blue -material steel");
        throw new Error("Show me StackTrace");
    }

    public static void main(String[] args) {
        TableAttributes attributes = BikesUtil.parseArgs(args);
        if (attributes == null) {
            printHowTo();
        }

        String hostip = BikesUtil.getHost();
        String passwd = BikesUtil.getPasswd();

        System.out.println(hostip + "  " + passwd);

        InsertRow ir;
        try {
            ir = new InsertRow(hostip, passwd);
        } catch (SQLException e) {
            System.out.println("Error with connection: " + e.getMessage());
            return;
        }

        ir.run(attributes);
    }
}