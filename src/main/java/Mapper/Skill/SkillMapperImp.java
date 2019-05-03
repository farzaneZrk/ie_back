package Mapper.Skill;

import Mapper.DataMapperImp;
import Model.Skill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillMapperImp extends DataMapperImp<Skill, String> implements SkillDataMapper {

    protected String findStatement() {
        return "SELECT *" +
            " FROM Skillnames" +
            " WHERE name = ?";
    }

    protected String selectAllStatement() {
        return "SELECT * FROM Skillnames";
    }

    public static final String COLUMNS = " name ";

    public Skill find(String id) {
        return (Skill) abstractFind(id);
    }

    public Skill find(long id) {
        return find(new Long(id));
    }

    protected Skill doLoad(String id, ResultSet rs) throws SQLException {
        String name = rs.getString(1);
        return new Skill(name, 0);
    }
    protected String insertStatement() {
        return "INSERT INTO Skillnames (name)" +
                "VALUES (?);";
    }
    protected String doInsert(Skill abstractSubject, PreparedStatement stmt) throws SQLException {
        System.out.println("oops! in do insert");
        Skill subject = (Skill) abstractSubject;
        stmt.setString(1, subject.getName());
        System.out.println("oops! in do insert with name " + subject.getName());
        return subject.getName();
    }
}
