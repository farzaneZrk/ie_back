package Model;

import Database.DataSource;

import java.sql.*;

public class DataBaseConnectionTest {


    public void selectAll() {
        String sql = "SELECT * FROM Skillnames";

        try (Connection conn = DataSource.getConnection();
             PreparedStatement pst = conn.prepareStatement( sql );
             ResultSet rs = pst.executeQuery()) {

            while ( rs.next() ) {
                System.out.println(rs.getString("name") + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DataBaseConnectionTest app = new DataBaseConnectionTest();
        app.selectAll();
    }
}
