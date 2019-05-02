package Model;

import Database.DataSource;
import Model.Project;
import Model.User;

import java.sql.*;
import java.util.List;

public class DataBaseConnectionTest {

    private Connection connect() {
        String url = "jdbc:sqlite:ie_db.db";
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
//            PreparedStatement pst = conn.prepareStatement( sql );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    private void selectAll() {
        String sql = "SELECT * FROM Users";

        try {
            Connection conn = DataSource.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString("firstname") + "\n");
            }
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
    public static void main(String[] args) {
        DataBaseConnectionTest app = new DataBaseConnectionTest();
        app.selectAll();

    }
}
