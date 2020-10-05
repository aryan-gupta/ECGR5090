import java.sql.SQLException;


public final class RemoveBikes {
        private static void printHowTo() {
        java.io.PrintStream out = java.lang.System.out;
        out.println("java -cp .:/usr/share/java/mariadb-jdbc/mariadb-java-client-2.7.0.jar RemoveBikes -type mountain_bike -gear 5 -wheelbase 36 -height 2 -color blue -material steel");
        throw new Error("Show me StackTrace");
    }

    public static void main(String[] args) {
        TableAttributes attributes = BikesUtil.parseArgs(args);
        if (attributes == null) {
            printHowTo();
        }

        String hostip = BikesUtil.getHost();
        String passwd = BikesUtil.getPasswd();

        // System.out.println(hostip + "  " + passwd);

        DeleteRow dr;
        try {
            dr = new DeleteRow(hostip, passwd);
        } catch (SQLException e) {
            System.out.println("Error with connection: " + e.getMessage());
            return;
        }

        dr.run(attributes);
    }
}