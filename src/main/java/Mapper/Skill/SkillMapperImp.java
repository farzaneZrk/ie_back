package Mapper.Skill;

import Mapper.DataMapperImp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillMapperImp extends DataMapperImp<String, String> implements SkillDataMapper {

    protected String findStatement() {
        return "SELECT *" +
            " FROM Skillnames" +
            " WHERE name = ?";
    }

    protected String selectAllStatement() {
        return "SELECT * FROM Skillnames";
    }


    protected String doLoad(String id, ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        return name;
    }
    protected String insertStatement() {
        return "INSERT IGNORE INTO Skillnames (name)" +
                "VALUES (?);";
    }
    protected String doInsert(String abstractSubject, PreparedStatement stmt) throws SQLException {
        System.out.println("oops! in skill do insert");
        stmt.setString(1, abstractSubject);
        System.out.println("oops! in do insert with name " + abstractSubject);
        return abstractSubject;
    }
}
