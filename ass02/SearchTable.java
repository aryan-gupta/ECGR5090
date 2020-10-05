import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public final class SearchTable {
    private Connection connect;

    public SearchTable(String host, String passwd) throws SQLException {

        // This will load the MySQL driver, each DB has its own driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(Exception e) {}

        // Setup the connection with the DB
        // https://mariadb.com/kb/en/about-mariadb-connector-j/
        // https://wiki.archlinux.org/index.php/JDBC_and_MySQL
        /// @note Need to install driver with `yay -S mariadb-jdbc`
        connect = DriverManager.getConnection("jdbc:mariadb://" + host + ":3306/Bicycles?user=root&password=" + passwd);
    }

    public SearchTable run(TableAttributes query) {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // "SELECT * FROM Attributes WHERE type='?',num_gears=?, \
            // wheel_base=?,height=?,color='?',material='?' ORDER BY ?"
            StringBuilder querystr = new StringBuilder();
            querystr.append("SELECT * FROM Attributes WHERE ");
            
            if (query.type != null)
                querystr.append("type='" + query.type + "' AND ");
            if (query.gears != -1)
                querystr.append("num_gears=" + query.gears + " AND ");
            if (query.wheelBase != -1)
                querystr.append("wheel_base=" + query.wheelBase + " AND ");
            if (query.height != -1)
                querystr.append("height=" + query.height + " AND ");
            if (query.color != null)
                querystr.append("color='" + query.color + "' AND ");
            if (query.material != null)
                querystr.append("material='" + query.material + "' AND ");

            // Remove the last AND
            if (querystr.substring(querystr.length() - 4).equals("AND "))
                querystr.delete(querystr.length() - 5, querystr.length());

            switch (query.sort) {
                case TYPE:
                    querystr.append(" ORDER BY type");
                    break;
                case GEARS:
                    querystr.append(" ORDER BY num_gears");
                    break;
                case WHEEL_BASE:
                    querystr.append(" ORDER BY wheel_base");
                    break;
                case HEIGHT:
                    querystr.append(" ORDER BY height");
                    break;
                case COLOR:
                    querystr.append(" ORDER BY color");
                    break;
                case MATERIAL:
                    querystr.append(" ORDER BY material");
                    break;
            }

            statement = connect.createStatement();
            // Result set get the result of the SQL query
            System.out.println(querystr.toString());
            resultSet = statement.executeQuery(querystr.toString());
            
            // ResultSet is initially before the first data set
            TableAttributes attributes = new TableAttributes();
            while (resultSet.next()) {
                attributes.type      = resultSet.getString("type");
                attributes.color     = resultSet.getString("color");
                attributes.material  = resultSet.getString("material");
                attributes.gears     = resultSet.getInt("num_gears");
                attributes.wheelBase = resultSet.getInt("wheel_base");
                attributes.height    = resultSet.getInt("height");
                System.out.println("" + 
                    attributes.type      + " | " +
                    attributes.gears     + " | " +
                    attributes.wheelBase + " | " +
                    attributes.height    + " | " +
                    attributes.color     + " | " +
                    attributes.material
                );
            }

        }  catch (SQLException e) {
            System.out.println("Error with run: " + e.getMessage());
            e.printStackTrace();
        }

        return this;
    }
}