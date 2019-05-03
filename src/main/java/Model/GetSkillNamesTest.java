package Model;

import Database.C3poDataSource;
import org.json.JSONArray;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static Model.SkillRepo.getDataFromServer;

public class GetSkillNamesTest {

    public void setUpSkillList() throws IOException {
        String skillsJson = getDataFromServer("http://142.93.134.194:8000/joboonja/skill/");
        this.insertToSkillNames(skillsJson);
    }

    public void insertToSkillNames(String skills) {
        JSONArray jsonarray = new JSONArray(skills);
        String sql = "SELECT * FROM Skillnames";

        try (Connection conn = C3poDataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
             "INSERT INTO Skillnames (name)" +
                  "VALUES (?);"
            );

//            for (int i = 0; i < jsonarray.length(); i++) {
//                JSONObject jsonobject = jsonarray.getJSONObject(i);
//                String name = jsonobject.getString("name");
//                pstmt.setString(1, name);
//                pstmt.executeUpdate();
//            }

            pstmt.setString(1, "second");
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GetSkillNamesTest app = new GetSkillNamesTest();
        app.setUpSkillList();
    }
}
