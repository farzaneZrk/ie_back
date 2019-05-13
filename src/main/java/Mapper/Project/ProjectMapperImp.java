package Mapper.Project;

import Database.C3poDataSource;
import Mapper.DataMapperImp;
import Mapper.User.UserDataMapperImp;
import Model.Bid;
import Model.Project;
import Model.Skill;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectMapperImp extends DataMapperImp<Project, String> implements ProjectDataMapper {
    private ProjectSkillMapper projectSkillMapper = new ProjectSkillMapper();
    protected String findStatement() {
        return "SELECT " + COLUMNS +
                " FROM Projects" +
                " WHERE projectId = ?";
    }

    protected String insertStatement() {
        return "INSERT OR IGNORE INTO Projects " +
                "(projectId,title,description,imageURL,budget,winner,creationDate, deadline)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    }

    protected String selectAllStatement() {
        return "SELECT " + COLUMNS +
                " FROM Projects";
    }

    protected String findProjectsByOffset(){
        return "SELECT " + COLUMNS +
                " FROM Projects "+
                " LIMIT ? OFFSET ?;";
    }

    protected String findProjectsNumber(){
        return "SELECT COUNT(*) " +
                " FROM Projects ";
    }

    protected String findSearchedProjectsNumber(){
        return "SELECT COUNT(*) " +
                " FROM Projects " +
                " WHERE title LIKE ? OR description LIKE ?";
    }

    protected String addProjectBid(){
        return "INSERT INTO Bids (amount, userId, projectId) " +
                "VALUES (?, ?, ?)";
    }

    protected String findBidStatement() {
        return "SELECT amount, userId, projectId" +
                " FROM Bids" +
                " WHERE projectId = ?";
    }

    protected String searchProjectsStatement() {
        return "SELECT " + COLUMNS +
                " FROM Projects" +
                " WHERE title LIKE ? OR description LIKE ? " +
                " LIMIT ? OFFSET ?;";

    }

    public static final String COLUMNS = "projectId, title, description, imageURL, budget, winner, creationDate, deadline ";

    protected Project doLoad(String id, ResultSet rs) throws SQLException {
        String title = rs.getString(2);
        String description = rs.getString(3);
        String imageURL = rs.getString(4);
        List<Skill> skills = projectSkillMapper.getProjectSkills(id);
        List<Bid> bids = getProjectBids(id);
        int budget = rs.getInt(5);
        // todo: winner is a string here but in model.project winner is a user, we have to fix it in db or model.
        long creationDate = rs.getLong(7);
        long deadline = rs.getLong(8);
        return new Project(id, title, description, imageURL, skills, bids, budget, deadline, creationDate, null);
    }

    private List<Bid> getProjectBids(String id){
        String findBidStatement = this.findBidStatement();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(findBidStatement);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            List<Bid> result = new ArrayList<>();
            while (rs.next())
                result.add((this.loadBid(rs)));
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bid loadBid(ResultSet rs) throws SQLException {
        int amount = rs.getInt(1);
        String userId = rs.getString(2);
        String projectId = rs.getString(3);

        Project project = abstractFind(projectId);

        UserDataMapperImp userDataMapper = new UserDataMapperImp();
        User user = userDataMapper.abstractFind(userId);

        return new Bid(amount, project, user);
    }

    protected String doInsert(Project abstractSubject, PreparedStatement stmt) throws SQLException {
        Project subject = abstractSubject;
        stmt.setString(1, subject.getId());
        stmt.setString(2, subject.getTitle());
        stmt.setString(3, subject.getDescription());
        stmt.setString(4, subject.getImageUrl());
        stmt.setInt(5, subject.getBudget());
        stmt.setString(6, subject.getWinner());
        stmt.setLong(7, subject.getCreationTime());
        stmt.setLong(8, subject.getDeadline());

        projectSkillMapper.insertProjectSkills(abstractSubject.getId(), abstractSubject.getSkills());

        return subject.getId();
    }

    public void inserProjectBid(Project project, Bid bid){
        loadedMap.replace(project.getId(), project);
        insertBid(bid);
        System.out.println("at the end of bidProject " + loadedMap.get(project.getId()));
    }

    private boolean insertBid(Bid bid){
        String addProjectBid = this.addProjectBid();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(addProjectBid);
            pstmt.setInt(1, bid.getAmount());
            pstmt.setString(2, bid.getUser().getId());
            pstmt.setString(3, bid.getProject().getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Project> selectMatchedProjects(String searchKey, int projectPerPage, int pageNumber){
        String searchProjectsStatement = this.searchProjectsStatement();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(searchProjectsStatement);
            pstmt.setString(1, '%' + searchKey + '%');
            pstmt.setString(2, '%' + searchKey + '%');
            pstmt.setInt(3, projectPerPage);
            pstmt.setInt(4, (pageNumber-1)*projectPerPage);
            ResultSet rs = pstmt.executeQuery();

            return loadAll(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int findNumberOfProjects(){
        String findProjectsNumber = this.findProjectsNumber();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(findProjectsNumber);
            ResultSet rs = pstmt.executeQuery();

            int result = 0;
            while (rs.next()){
                result = rs.getInt(1);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int findNumberOfSearchedProjects(String searchKey){
        String findSearchedProjectsNumber = this.findSearchedProjectsNumber();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(findSearchedProjectsNumber);
            ResultSet rs = pstmt.executeQuery();
            pstmt.setString(1, '%' + searchKey + '%');
            pstmt.setString(2, '%' + searchKey + '%');

            int result = 0;
            while (rs.next()){
                result = rs.getInt(1);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Project> selectProjectsByOffset(int projectPerPage, int pageNumber){
        String findProjectsByOffset = this.findProjectsByOffset();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(findProjectsByOffset);
            pstmt.setInt(1, projectPerPage);
            pstmt.setInt(2, (pageNumber-1)*projectPerPage);
            ResultSet rs = pstmt.executeQuery();

            return loadAll(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
