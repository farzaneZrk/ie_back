package Mapper.User;

import Database.C3poDataSource;
import Model.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSkillMapper {
    private String findSkills(){
        return "SELECT name, point " +
                "FROM UserSkills " +
                "WHERE userId = ?";
    }

    private String findSkillEndorsers(){
        return "SELECT endorserId " +
                "FROM Endorses " +
                "WHERE endorsedId = ? and skillname = ?";
    }

    private String addUserSkills(){
        return "INSERT INTO UserSkills (name, point, userId) " +
                "VALUES (?, ?, ?)";
    }

    private String endorseUserSkill(){
        return "INSERT INTO Endorses (skillname, endorserId, endorsedId) " +
                "VALUES (?, ?, ?)";
    }

    private String increaseSkillPoint(){
        return "UPDATE UserSkills " +
                "SET point = point + 1 " +
                "WHERE userId = ? and name = ?";
    }

    private String removeSkillEndorses(){
        return "DELETE FROM Endorses " +
                "WHERE endorserId = ? and skillname = ?";
    }

    private String removeUserSkill(){
        return "DELETE FROM UserSkills " +
                "WHERE userId = ? and name = ?";
    }

    public List<Skill> getUserSkills(String id){
        String findSkills = this.findSkills();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(findSkills);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            return this.loadAllSkills(rs, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getSkillEndorseList(String id, String name){
        String findSkillEndorsers = this.findSkillEndorsers();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(findSkillEndorsers);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
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

    public boolean insertUserSkills(String userId, List<Skill> skills){
        for (Skill skill: skills) {
            if (!this.insertUserSkill(userId, skill))
                return false;
        }
        return true;
    }

    public boolean insertUserSkill(String id, Skill skill){
        String addUserSkills = this.addUserSkills();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(addUserSkills);
            pstmt.setString(1, skill.getName());
            pstmt.setInt(2, skill.getPoint());
            pstmt.setString(3, id);
            pstmt.executeUpdate();
            if (skill.getEndorsers().size() > 0){
                for (String endorserId : skill.getEndorsers())
                    this.insertEndorse(endorserId, id, skill);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertEndorse(String ider, String ided, Skill skill){
        String endorseUserSkill = this.endorseUserSkill();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(endorseUserSkill);
            pstmt.setString(1, skill.getName());
            pstmt.setString(2, ider);
            pstmt.setString(3, ided);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean increaseUserSkillPoint(String id, Skill skill){
        String increaseSkillPoint = this.increaseSkillPoint();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(increaseSkillPoint);
            pstmt.setString(2, skill.getName());
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEndorse(String id, String skill){
        String removeSkillEndorses = this.removeSkillEndorses();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(removeSkillEndorses);
            pstmt.setString(1, id);
            pstmt.setString(2, skill);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserSkill(String id, String skill){
        String removeUserSkill = this.removeUserSkill();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(removeUserSkill);
            pstmt.setString(2, skill);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Skill> loadAllSkills(ResultSet rs, String userId) throws SQLException {
        List<Skill> result = new ArrayList<>();
        while (rs.next())
            result.add((this.loadSkill(rs, userId)));
        return result;
    }

    private Skill loadSkill(ResultSet rs, String userId) throws SQLException {
        String name = rs.getString(1);
        int point = rs.getInt(2);
        List<String> endorserList = this.getSkillEndorseList(userId, name);

        return new Skill(name, point, endorserList);
    }

}

