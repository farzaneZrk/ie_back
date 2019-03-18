package Model;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Model.SkillRepo.getDataFromServer;
import static Model.UserRepo.findUser;

public class ProjectRepo {
    private static int nextProjectId;
    private static List<Project> projectList;

    static {
        nextProjectId = 1;
        projectList = new ArrayList<>();

    }


    public static List<Project> getProjectList() {
        return projectList;
    }

    public static int addProject(Project project) {
        if (findProject(project.getId()) != null)
            return -1;

        projectList.add(project);
        return 0;
    }

    public static Project findProject(String id) {
        for (Project project: projectList)
            if (project.getId().equals(id))
                return project;
        return null;
    }

    public static void setUpProjectlist() throws IOException {
        String projectsJson = getDataFromServer("http://142.93.134.194:8000/joboonja/project/");
        addToProjectList(projectsJson);
    }

    public static List<Skill> createSkillList(JSONArray jsonSkillList){
        List<Skill> skillList = new ArrayList<>();
        for (int i = 0; i < jsonSkillList.length(); i++) {
            JSONObject jsonobject = jsonSkillList.getJSONObject(i);
            Skill skill = new Skill(jsonobject.getString("name"), jsonobject.getInt("point"));
            skillList.add(skill);
        }
        return skillList;
    }

    public static void addToProjectList(String projects) {
        JSONArray jsonarray = new JSONArray(projects);
        List<Skill> skillList;
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            JSONArray jsonSkillList = jsonobject.getJSONArray("skills");
            skillList = createSkillList(jsonSkillList);
            projectList.add(new Project(jsonobject.getString("id"), jsonobject.getString("title"),
                    jsonobject.getString("description"), jsonobject.getString("imageUrl"), skillList,
                    jsonobject.getInt("budget"),  jsonobject.getLong("budget")));
        }
    }

    public static int addBid(Bid bid) {
        Project project = findProject(bid.getProject().getId());
        User user = findUser(bid.getUser().getId());

        if (project == null)
            return -1;
        if (user == null)
            return -2;

        project.addBid(bid);
        return 0;
    }
}
