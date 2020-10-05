import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/// A class used to insert and add rows to the database
public final class InsertRow extends SQLOperation {
    /// A contructor to create a new connection to the database
    /// Directly calls respective super class constructor
    /// @param host The host or IP of the machine to connect to
    /// @param passwd The password of the database
    public InsertRow(String host, String passwd) throws SQLException {
        super(host, passwd);
    }

    /// Runs the insert/search query using ths supplied attribute parameter
    /// and then output it to stdout
    /// @note this function supports chaining, because the original
    ///       impl had a seperate print meathod. Currently unused
    /// @note SQL injection protection is impl using java's built-in
    ///       PreparedStatement class
    /// @param query The attributes to search for and sort key
    /// @return this for chaining
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