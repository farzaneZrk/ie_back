package Mapper.Project;

import Database.C3poDataSource;
import Model.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectSkillMapper {
    private String findSkills(){
        return "SELECT name, point " +
                "FROM ProjectSkills " +
                "WHERE projectId = ?";
    }

    private String addProjectSkills(){
        return "INSERT IGNORE INTO ProjectSkills (name, point, projectId) " +
                "VALUES (?, ?, ?)";
    }

    private String removeProjectSkill(){
        return "DELETE FROM ProjectSkills " +
                "WHERE projectId = ? and name = ?";
    }

    public List<Skill> getProjectSkills(String pid){
        String findSkills = this.findSkills();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(findSkills);
            pstmt.setString(1, pid);
            ResultSet rs = pstmt.executeQuery();
            return this.loadAllSkills(rs, pid);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insertProjectSkills(String projectId, List<Skill> skills){
        if (skills == null || skills.size() == 0)
            return true;
        for (Skill skill: skills) {
            if (!this.insertProjectSkill(projectId, skill))
                return false;
        }
        return true;
    }

    public boolean insertProjectSkill(String id, Skill skill){
        String addProjectSkills = this.addProjectSkills();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(addProjectSkills);
            pstmt.setString(1, skill.getName());
            pstmt.setString(3, id);
            pstmt.setInt(2, skill.getPoint());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProjectSkill(String id, String skill){
        String removeProjectSkill = this.removeProjectSkill();
        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(removeProjectSkill);
            pstmt.setString(2, skill);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Skill> loadAllSkills(ResultSet rs, String projectId) throws SQLException {
        List<Skill> result = new ArrayList<>();
        while (rs.next())
            result.add((this.loadSkill(rs, projectId)));
        return result;
    }

    private Skill loadSkill(ResultSet rs, String projectId) throws SQLException {
        String name = rs.getString(1);
        int point = rs.getInt(2);
        List<String> endorserList = new ArrayList<>();

        return new Skill(name, point, endorserList);
    }
}
