package Mapper.Skill;

import Mapper.DataMapperImp;
import Model.Skill;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillMapperImp extends DataMapperImp<Skill, String> implements SkillDataMapper {

    protected String findStatement() { return "SELECT " + COLUMNS +
            " FROM Skillnames" +
            " WHERE id = ?"; }

    public static final String COLUMNS = " name ";

    public Skill find(String id) {
        return (Skill) abstractFind(id);
    }

    public Skill find(long id) {
        return find(new Long(id));
    }

    protected Skill doLoad(Long id, ResultSet rs) throws SQLException {
        String name = rs.getString(2);
        int point = rs.getInt(3);
        return new Skill(name, point);
    }
    protected String insertStatement() {
        return "INSERT INTO Skillnames (name)" +
                "VALUES (?);";
    }
    protected String doInsert(Skill abstractSubject, PreparedStatement stmt) throws SQLException {
        Skill subject = (Skill) abstractSubject;
        stmt.setString(1, subject.getName());
        return subject.getName();
    }

    public void setUpSkillList() throws IOException {
        String skills = getDataFromServer("http://142.93.134.194:8000/joboonja/skill/");
        JSONArray jsonarray = new JSONArray(skills);
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String name = jsonobject.getString("name");
            insert(new Skill(name, 0));
        }
    }
}
