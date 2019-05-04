package Mapper.User;

import Database.C3poDataSource;
import Mapper.DataMapperImp;
import Model.Skill;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDataMapperImp extends DataMapperImp<User, String> implements UserDataMapper {
    private UserSkillMapper userSkillMapper = new UserSkillMapper();
    protected String findStatement() {
        return "SELECT " + COLUMNS +
                " FROM Users" +
                " WHERE userId = ?";
    }

    protected String selectAllStatement() {
        return "SELECT " + COLUMNS + " FROM Users";
    }

    protected String insertStatement() {
        return "INSERT INTO Users (userId, firstname, lastname, jobTitle, imageURL, bio)" +
                "VALUES (?, ?, ?, ?, ?, ?);";
    }

    private String findUserBids(){
        return "SELECT projectId " +
                "FROM Bids " +
                "WHERE UserId = ?";
    }

    public static final String COLUMNS = "userId, firstname, lastname, jobTitle, imageURL, bio";

    protected User doLoad(String id, ResultSet rs) throws SQLException {
        String firstname = rs.getString(2);
        String lastname = rs.getString(3);
        String jobTitle = rs.getString(4);
        String imageURL = rs.getString(5);
        String bio = rs.getString(6);

        List<Skill> skills = userSkillMapper.getUserSkills(id);

        User res = new User(id, firstname, lastname, jobTitle, imageURL, skills, bio);
        System.out.println("oops! " + res);

        // todo: handle bidded projects;

        return res;
    }

    private List<String> getUserBidList(String id){
        String findUserBids = this.findUserBids();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(findUserBids);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            List<String> result = new ArrayList<>();
            while (rs.next())
                result.add(rs.getString(1));
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String doInsert(User abstractSubject, PreparedStatement stmt) throws SQLException {
        System.out.println("oops! in user do insert");
        stmt.setString(1, abstractSubject.getId());
        stmt.setString(2, abstractSubject.getFirstName());
        stmt.setString(3, abstractSubject.getLastName());
        stmt.setString(4, abstractSubject.getJobTitle());
        stmt.setString(5, abstractSubject.getImageURL());
        stmt.setString(6, abstractSubject.getBio());

        userSkillMapper.insertUserSkills(abstractSubject.getId(), abstractSubject.getSkills());

        return abstractSubject.getId();
    }

    public void endorseUserSkill(User user, String endorserId, Skill skill){
        loadedMap.replace(user.getId(), user);
        userSkillMapper.insertEndorse(endorserId, user.getId(), skill);
        userSkillMapper.increaseUserSkillPoint(user.getId(), skill);
        System.out.println("at the end of endorseUserSkill " + loadedMap.get(user.getId()));
    }

    public void removeUserSkill(User user, String skill){
        loadedMap.replace(user.getId(), user);
        userSkillMapper.deleteUserSkill(user.getId(), skill);
        userSkillMapper.deleteEndorse(user.getId(), skill);
        System.out.println("at the end of removeUserSkill " + loadedMap.get(user.getId()));
    }

    public void addUserSkill(User user, String skill){
        loadedMap.replace(user.getId(), user);
        userSkillMapper.insertUserSkill(user.getId(), new Skill(skill, 0));
        System.out.println("at the end of addUserSkill " + loadedMap.get(user.getId()));
    }

    public void updateUser(User user){
        loadedMap.replace(user.getId(), user);
    }

}
