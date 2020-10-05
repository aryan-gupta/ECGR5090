import java.sql.SQLException;

/// A simple runner that parses through the command line
/// arguments and searches the database using the arguments
public final class ShowBikes {
    /// Print the how to run this command string
    private static void printHowTo() {
        java.io.PrintStream out = java.lang.System.out;
        out.println("java -cp .:/usr/share/java/mariadb-jdbc/mariadb-java-client-2.7.0.jar ShowBikes -type mountain_bike -gear 5 -wheelbase");
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

        SearchTable st;
        try {
            st = new SearchTable(hostip, passwd);
        } catch (SQLException e) {
            System.out.println("Error with connection: " + e.getMessage());
            return;
        }

        st.run(attributes);
    }
}