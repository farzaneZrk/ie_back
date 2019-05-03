package Mapper.Project;

import Database.C3poDataSource;
import Mapper.DataMapperImp;
import Model.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectMapperImp extends DataMapperImp<Project, String> implements ProjectDataMapper {
    protected String findStatement() {
        return "SELECT *" +
                " FROM Projects" +
                " WHERE projectId = ?";
    }

    protected String selectAllStatement() {
        return "SELECT * FROM Projects";
    }

    protected String selectNonUpdatedStatement(){
        return "SELECT max(creationDate) FROM Projects";
    }

    protected Project findCreationDate() {
        ResultSet rs = null;
        PreparedStatement selectNonUpdatedStatement = null;
        try (Connection conn = C3poDataSource.getConnection()) {
            selectNonUpdatedStatement = conn.prepareStatement(selectNonUpdatedStatement());
            rs = selectNonUpdatedStatement.executeQuery();
            return (Project) loadAll(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    protected String insertStatement() {
        return "INSERT INTO Projects (projectId,title,description,imageURL,budget,winner,creationDate, deadline)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    }


    protected String doInsert(Project abstractSubject, PreparedStatement stmt) throws SQLException {
//        System.out.println("oops! in do insert");
        Project subject = abstractSubject;
        stmt.setString(1, subject.getId());
        stmt.setString(2, subject.getTitle());
        stmt.setString(3, subject.getDescription());
        stmt.setString(4, subject.getImageUrl());
        stmt.setInt(5, subject.getBudget());
        stmt.setString(6, subject.getWinner());
        stmt.setLong(7, subject.getCreationTime());
        stmt.setLong(8, subject.getDeadline());

        System.out.println("oops! in do insert with name " + subject.getName());
        return subject.getId();
    }


//    public String insertProjects() {
//        PreparedStatement insertStatement = null;
//        try (Connection conn = C3poDataSource.getConnection()) {
//            insertStatement = conn.prepareStatement(insertStatement());
//            insertStatement.executeQuery();
//            String id = doInsert(subject, insertStatement);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


}
