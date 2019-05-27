package Model;

import Database.C3poDataSource;

import java.io.UnsupportedEncodingException;
import java.sql.*;

public class DataBaseConnectionTest {

    private Connection connect() {
        String url = "jdbc:sqlite:ie_db.db";
        Connection conn = null;
        try {
            conn = C3poDataSource.getConnection();
//            PreparedStatement pst = conn.prepareStatement( sql );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    private void selectAll() {
        String sql = "SELECT * FROM Users";

        try {
            Connection conn = C3poDataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);
            //-------
//            rs.next();

            while (rs.next()) {
                System.out.println(rs.getString("firstname") + "\n");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertTest(){
        String sql = "insert into Skillnames (name)" +" values (?)";

        try {
            Connection conn = C3poDataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "test skill");
            pst.executeUpdate();

//            while (rs.next()) {
//                System.out.println(rs.getString("firstname") + "\n");
//            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
//
//    public long getLastProjectInDataBaseByCreationTime() {
//        try {
//            String sql = "SELECT max(creationDate)FROM Projects;";
//            Connection conn = DataSource.getConnection();
//            PreparedStatement pst = conn.prepareStatement(sql);
//            return pst.executeUpdate(sql);
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return -1;
//    }
//
//    public void insertProjects(List<Project> projectList) {
//        try {
//            Connection conn = DataSource.getConnection();
//            PreparedStatement pst = conn.prepareStatement(sql);
//            String sql;
//            for (Project project : projectList) {
//                sql = "INSERT INTO Projects (projectId,title,description,imageURL,budget,winner,creationDate)" +
//                        "VALUES (" +
//                        project.getId() + "," +
//                        project.getTitle() + "," +
//                        project.getDescription() + "," +
//                        project.getImageUrl() + "," +
//                        project.getBudget() + "," +
//                        project.getWinner() + "," +
//                        project.getCreationTime() +
//                        ");";
//                pst.executeUpdate(sql);
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//
//    public void insertUsers(User user) {
//        try {
//            Connection conn = DataSource.getConnection();
//            PreparedStatement pst = conn.prepareStatement(sql);
//            String sql;
//            sql = "INSERT INTO Users (userId,firstname,lastname,jobTitle,imageURL,bio)" +
//                    "VALUES (" +
//                    user.getId() + "," +
//                    user.getFirstName() + "," +
//                    user.getLastName() + "," +
//                    user.getJobTitle() + "," +
//                    user.getImageURL() + "," +
//                    user.getBio() +
//                    ");";
//            pst.executeUpdate(sql);
//            ;
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedEncodingException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        DataBaseConnectionTest app = new DataBaseConnectionTest();
        app.insertTest();

//        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
//
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://localhost/ie_test", "root", "mhmdzrk83");
//
//            String sql = "insert into Skillnames(name) values(?)";
//            PreparedStatement pst = conn.prepareStatement(sql);
//            pst.setString(1, "test skill");
//            pst.executeUpdate(sql);
//
//        } catch (SQLException ex) {
//            // handle any errors
//            System.out.println("SQLException: " + ex.getMessage());
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());
//        }
//
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//        Date fiveMinLater = new Date(nowMillis + 15*60*1000);
//        String jws = Jwts.builder()
//                .setIssuer("joboonja.ut.ac.ir")
//                .setIssuedAt(Date.from(now.toInstant()))
//                .setExpiration(Date.from(fiveMinLater.toInstant()))
//                .claim("userId", String.valueOf(1))
//                .signWith(
//                        SignatureAlgorithm.HS256,
//                        Base64.getUrlDecoder().decode(DigestUtils.sha256Hex("joboonja"))
//                )
//                .compact();
//        System.out.println(jws);

    }
}

//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.YtqsaMvGe5hFoIhS2DeW_T5r5Ngo5rJjqaXfHQVYUB8
//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKb2UifQ.VlkJc2sPo-5zqGRBJTZMzz4PzHF8sFFndpLBgVX4BJ8
//eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJKb2UifQ.nWJrcPx7OodROHlNqpqO6V_VSeryQ1qmdYPiuV7J_jk=
//eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJKb2UifQ==.wTJskgVDPY0WUdJR6PZ-BrDwYDOUHJhn_hq4dqGY_nI=