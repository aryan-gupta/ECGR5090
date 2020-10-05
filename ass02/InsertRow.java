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
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connect.prepareStatement("INSERT INTO Attributes " + 
                        "(type,num_gears,wheel_base,height,color,material) " + 
                        "VALUES (?,?,?,?,?,?)"
            );

            statement.setString(1, query.type);
            statement.setInt   (2, query.gears);
            statement.setInt   (3, query.wheelBase);
            statement.setInt   (4, query.height);
            statement.setString(5, query.color);
            statement.setString(6, query.material);

            statement.executeUpdate();

            (new SearchTable(super.connect)).run(query);

        }  catch (SQLException e) {
            System.out.println("Error with run: " + e.getMessage());
            e.printStackTrace();
        }

        return this;
    }
}