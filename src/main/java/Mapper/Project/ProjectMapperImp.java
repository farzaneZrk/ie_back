package Mapper.Project;

import Database.C3poDataSource;
import Mapper.DataMapperImp;
import Model.Project;
import Model.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProjectMapperImp extends DataMapperImp<Project, String> implements ProjectDataMapper {
    protected String findStatement() {
        return "SELECT" + COLUMNS +
                " FROM Projects" +
                " WHERE projectId = ?";
    }

    protected String selectAllStatement() {
        return "SELECT" + COLUMNS + "FROM Projects";
    }

    public static final String COLUMNS = "projectId, title, description, imageURL, budget, winner, creationDate, deadline ";

    protected Project doLoad(String id, ResultSet rs) throws SQLException {
        String title = rs.getString(2);
        String description = rs.getString(3);
        String imageURL = rs.getString(4);
        List<Skill> skills = null;      //todo: you have to get project skills here with another query on ProjectSkills.
        // private List<Bid> bids;  // todo: you can also get all bids on this projects.
        int budget = rs.getInt(5);
        // todo: winner is a string here but in model.project winner is a user, we have to fix it in db or model.
        long creationDate = rs.getLong(7);
        long deadline = rs.getLong(8);
        return new Project(id, title, description, imageURL, skills, budget, deadline, creationDate);
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

        System.out.println("oops! in do insert with name " + subject.getTitle());
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
