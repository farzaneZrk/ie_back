package Model;

import Mapper.Skill.SkillDataMapper;
import Mapper.Skill.SkillMapperImp;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SkillRepo {
    private static List<String> skillList; // list of available skillList

    static {
        skillList = new ArrayList<>();
    }

    public static void addSkillToSkillList(String newSkill){
        skillList.add(newSkill);
    }

    public static List<String> getSkillList() {
        return skillList;
    }

    public static String getDataFromServer(String urlPath) throws IOException {
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

    public static void setUpSkillList() throws IOException {
        String skillsJson = getDataFromServer("http://142.93.134.194:8000/joboonja/skill/");
        SkillRepo.addToSkillList(skillsJson);
    }

    public static void addToSkillList(String skills) throws IOException {
        System.out.println("oops! at first of addToSkillList");
        JSONArray jsonarray = new JSONArray(skills);
        SkillDataMapper skillDataMapper = new SkillMapperImp();
//        for (int i = 0; i < jsonarray.length(); i++) {
//            System.out.println("in for in skill repo");
//            JSONObject jsonobject = jsonarray.getJSONObject(i);
//            String name = jsonobject.getString("name");
//            System.out.println("oops! in for with name " + name);
//            SkillRepo.addSkillToSkillList(name);
//        }
//        System.out.println("oops! end of add to skill list");
        System.out.println(((SkillMapperImp) skillDataMapper).getAll());
    }

}
