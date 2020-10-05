// import java.lang.Integer;
// import java.lang.System;
import java.nio.file.Path;
import java.nio.file.Files;
import java.sql.SQLException;

public final class ShowBikes {
    private static void printHowTo() {
        java.io.PrintStream out = java.lang.System.out;
        out.println("ShowBikes –type mountain_bike –gear 5 –wheelbase");
        throw new Error("Show me StackTrace");
    }

    public static void main(String[] args) {
        TableAttributes attributes = BikesCLI.parseArgs(args);
        if (attributes == null) {
            printHowTo();
        }

        String hostip, passwd;
        try {
            hostip = Files.readString(Path.of("./tmp-db-ip")).trim();
            passwd = Files.readString(Path.of("./db-root-passwd")).trim();
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println(hostip + "  " + passwd);

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