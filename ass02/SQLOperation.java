import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.io.PrintStream;


public class SQLOperation {
    protected Connection connect;

    protected SQLOperation(Connection c) {
        connect = c;
    }

    public SQLOperation(String host, String passwd) throws SQLException {

        // This will load the MySQL driver, each DB has its own driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(Exception e) {
            System.out.println("Error with loading driver: " + e.getMessage());
            e.printStackTrace();
        }

        // Setup the connection with the DB
        // https://mariadb.com/kb/en/about-mariadb-connector-j/
        // https://wiki.archlinux.org/index.php/JDBC_and_MySQL
        /// @note Need to install driver with `yay -S mariadb-jdbc`
        connect = DriverManager.getConnection("jdbc:mariadb://" + host + ":3306/Bicycles?user=root&password=" + passwd);
    }

    protected static void writeResultSet(PrintStream out, ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        TableAttributes attributes = new TableAttributes();
        while (resultSet.next()) {
            attributes.type      = resultSet.getString("type");
            attributes.color     = resultSet.getString("color");
            attributes.material  = resultSet.getString("material");
            attributes.gears     = resultSet.getInt("num_gears");
            attributes.wheelBase = resultSet.getInt("wheel_base");
            attributes.height    = resultSet.getInt("height");
            out.println("" + 
                attributes.type      + " | " +
                attributes.gears     + " | " +
                attributes.wheelBase + " | " +
                attributes.height    + " | " +
                attributes.color     + " | " +
                attributes.material
            );
        }
    }
}