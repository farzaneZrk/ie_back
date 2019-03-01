package Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Model.UserController.findUser;

public class ProjectController {
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

//        project.setId(String.valueOf(this.nextProjectId++));
        projectList.add(project);
        return 0;
    }

    public static Project findProject(String id) {
        for (Project project: projectList)
            if (project.getId().equals(id))
                return project;
        return null;
    }

    public static List<Skill> createSingleSkill(String command) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(command);
        List<Skill> skillList = new ArrayList<>();
        Skill skill = new Skill(rootNode.path("name").asText(), rootNode.path("points").asInt());
        skillList.add(skill);
        return skillList;
    }

    public static List<Skill> createSkillList(JsonNode skillListNode){
        List<Skill> skillList = new ArrayList<>();
        Iterator<JsonNode> elements = skillListNode.elements();
        while(elements.hasNext()){
            JsonNode skillNodes = elements.next();
            JsonNode nameNode = skillNodes.path("name");
            JsonNode pointsNode = skillNodes.path("points");
            Skill skill = new Skill(nameNode.asText(),pointsNode.asInt());
            skillList.add(skill);
        }
        return skillList;
    }

    public static void addToProjectList(String projects) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(projects);
        Iterator<JsonNode> elements = rootNode.elements();
        List<Skill> skillList;
        while(elements.hasNext()) {
            JsonNode projectNode = elements.next();
            if (!projectNode.toString().contains("[")) {
                skillList = createSingleSkill(projectNode.path("skills").toString());
            } else {
                skillList = createSkillList(projectNode.path("skills"));
            }
            projectList.add(new Project(projectNode.path("id").asText(), projectNode.path("title").asText(),
                    projectNode.path("description").asText(), projectNode.path("imageUrl").asText(), skillList,
                    projectNode.path("budget").asInt(), projectNode.path("deadline").asLong()));
        }
//        for(Project p : this.projectList){
//            System.out.println("\n\n\n" + p);
//        }
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
