package Database;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class C3poDataSource {

    private static ComboPooledDataSource cpds = new ComboPooledDataSource();

    static {
        try {
//            cpds.setDriverClass("org.sqlite.JDBC");
//            cpds.setJdbcUrl("jdbc:sqlite:/home/sina/Desktop/DataBaseBack/IE_backend/ie_db.db");
//            cpds.setJdbcUrl("jdbc:sqlite:/Users/farzane/Documents/University/IE/IE_joint/ie_db.db");

            cpds.setDriverClass("com.mysql.cj.jdbc.Driver");
            cpds.setJdbcUrl("jdbc:mysql://localhost:3306/ie_test?user=root&password=mhmdzrk83&useSSL=false");
            cpds.setMaxPoolSize(10);
        } catch (PropertyVetoException e) {
            // handle the exception
        }
    }

    public static Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }

    private C3poDataSource(){}
}
