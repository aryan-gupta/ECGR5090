import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public final class DeleteRow extends SQLOperation {
    public DeleteRow(String host, String passwd) throws SQLException {
        super(host, passwd);
    }

    public DeleteRow run(TableAttributes query) {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // "SELECT * FROM Attributes WHERE type='?',num_gears=?, \
            // wheel_base=?,height=?,color='?',material='?' ORDER BY ?"
            StringBuilder querystr = new StringBuilder();
            querystr.append("DELETE FROM Attributes WHERE ");
            
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

            // System.out.println(querystr.toString());

            // Result set get the result of the SQL query
            statement = super.connect.createStatement();
            statement.executeUpdate(querystr.toString());
            
            (new SearchTable(super.connect)).run(query);

        }  catch (SQLException e) {
            System.out.println("Error with run: " + e.getMessage());
            e.printStackTrace();
        }

        return this;
    }
}