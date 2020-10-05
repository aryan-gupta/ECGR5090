import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public final class InsertRow extends SQLOperation {
    public InsertRow(String host, String passwd) throws SQLException {
        super(host, passwd);
    }

    public InsertRow run(TableAttributes query) {
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            StringBuilder querystr = new StringBuilder();
            querystr.append("INSERT INTO Attributes (type,num_gears,wheel_base,height,color,material) VALUES (");
            
            if (query.type != null)
                querystr.append("'" + query.type + "',");
            if (query.gears != -1)
                querystr.append("" + query.gears + ",");
            if (query.wheelBase != -1)
                querystr.append("" + query.wheelBase + ",");
            if (query.height != -1)
                querystr.append("" + query.height + ",");
            if (query.color != null)
                querystr.append("'" + query.color + "',");
            if (query.material != null)
                querystr.append("'" + query.material + "')");

            statement = super.connect.createStatement();
            // Result set get the result of the SQL query
            System.out.println(querystr.toString());
            statement.executeUpdate(querystr.toString());

            (new SearchTable(super.connect)).run(query);

        }  catch (SQLException e) {
            System.out.println("Error with run: " + e.getMessage());
            e.printStackTrace();
        }

        return this;
    }
}