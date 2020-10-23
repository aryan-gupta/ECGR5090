import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.io.PrintStream;

/// A parent class for all 3 operations. This class holds the
/// connection object and the print to console function. This
/// class can be used to convert between the 3 operations
/// on-the-fly
public class SQLOperation {
    /// A connection to the database
    protected Connection connect;

    /// A protected constructor used for converting between the 3 operations
    /// @note Not fully tested so may need more work
    /// @param c The connection from the other class to copy
    protected SQLOperation(Connection c) {
        connect = c;
    }

    /// A contructor to create a new connection to the database
    /// @note this function is only tested with mariadb and more
    ///       work needs to be done to test with MySQL Workbench
    /// @note this function assumes the user to connect with is
    ///       root, since this is not a production database
    ///       indivisual user accounts will not be created
    /// @param host The host or IP of the machine to connect to
    /// @param passwd The password of the database
    public SQLOperation(String host, String passwd) throws SQLException {

        // This will load the MySQL driver, each DB has its own driver
        // try {
        //     Class.forName("com.mysql.jdbc.Driver");
        // } catch(Exception e) {
        //     System.out.println("Error with loading driver: " + e.getMessage());
        //     e.printStackTrace();
        // }

        // Setup the connection with the DB
        // https://mariadb.com/kb/en/about-mariadb-connector-j/
        // https://wiki.archlinux.org/index.php/JDBC_and_MySQL
        /// @note Need to install driver with `yay -S mariadb-jdbc`
        connect = DriverManager.getConnection("jdbc:mariadb://" + host + ":3306/Bicycles?user=root&password=" + passwd);
    }

    /// A static function that writes the result set returned from a query to the output
    /// passed in as the parameter
    /// @todo check what the parent class of PrintStream is so we can output directly to
    ///       files and other types of streams
    /// @param out The output stream to output to
    /// @param resultSet The reply from the SQL server to output
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