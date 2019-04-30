package Model;

import Database.DataSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GetSkillNamesTest {

    public String getDataFromServer(String urlPath) throws IOException {
        StringBuilder response = new StringBuilder();
        String inputLine;
        URL project = new URL(urlPath);
        URLConnection pc = project.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(pc.getInputStream()));

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();
        return response.toString();
    }

    public void setUpSkillList() throws IOException {
        String skillsJson = getDataFromServer("http://142.93.134.194:8000/joboonja/skill/");
        this.insertToSkillNames(skillsJson);
    }

    public void insertToSkillNames(String skills) {
        JSONArray jsonarray = new JSONArray(skills);
        String sql = "SELECT * FROM Skillnames";

        try (Connection conn = DataSource.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
             "INSERT INTO Skillnames (name)" +
                  "VALUES (?);"
            );

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String name = jsonobject.getString("name");
                pstmt.setString(1, name);
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GetSkillNamesTest app = new GetSkillNamesTest();
        app.setUpSkillList();
    }
}
