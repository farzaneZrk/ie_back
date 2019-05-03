package Mapper.Project;

import Database.C3poDataSource;
import Mapper.DataMapperImp;
import Model.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ProjectMapperImp extends DataMapperImp<Project, String> implements ProjectDataMapper {
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

    protected Project insertProject() {
        ResultSet rs = null;
        PreparedStatement insertStatement = null;
        try (Connection conn = C3poDataSource.getConnection()) {
            insertStatement = conn.prepareStatement(insertStatement());
            rs = insertStatement.executeQuery();
            return (Project) loadAll(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String insertStatement() {
        return "INSERT INTO Projects (projectId,title,description,imageURL,budget,winner,creationDate)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
    }


    protected String doInsert(Project abstractSubject, PreparedStatement stmt) throws SQLException {
//        System.out.println("oops! in do insert");
        Project subject = abstractSubject;
        stmt.setString(1, subject.getId());
        stmt.setString(1, subject.getTitle());
        stmt.setString(1, subject.getDescription());
        stmt.setString(1, subject.getImageUrl());
        stmt.setInt(1, subject.getBudget());
        stmt.setString(1, subject.getWinner());
        stmt.setLong(1, subject.getCreationTime());

//        System.out.println("oops! in do insert with name " + subject.getName());
        return subject.getId();
    }




}
