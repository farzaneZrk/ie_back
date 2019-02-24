package Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModelController {
    private List<User> users;
    private List<Project> projects;
    private int nextUserId;
    private int nextProjectId;
    private List<String> skills; // list of available skills

    public ModelController() {
        this.nextProjectId = 1;
        this.nextUserId = 1;
        this.users = new ArrayList<>();
        this.projects = new ArrayList<>();
        this.skills = new ArrayList<>();
    }

    private List<Skill> createSingleSkill(String command) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(command);
        List<Skill> skillList = new ArrayList<>();
        Skill skill = new Skill(rootNode.path("name").asText(), rootNode.path("points").asInt());
        skillList.add(skill);
        return skillList;
    }

    private List<Skill> createSkillList(JsonNode skillListNode){
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

    public void addToProjectList(String projects) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(projects);
        Iterator<JsonNode> elements = rootNode.elements();
        List<Skill> skillList = new ArrayList<>();
        while(elements.hasNext()) {
            JsonNode projectNode = elements.next();
            if (!projectNode.toString().contains("[")) {
                skillList = createSingleSkill(projectNode.path("skills").toString());
            } else {
                skillList = this.createSkillList(projectNode.path("skills"));
            }
            this.projects.add(new Project(projectNode.path("id").asText(), projectNode.path("title").asText(),
                    projectNode.path("description").asText(), projectNode.path("imageUrl").asText(), skillList,
                    projectNode.path("budget").asInt(), projectNode.path("deadline").asLong()));
        }
//        for(Project p : this.projects){
//            System.out.println("\n\n\n" + p);
//        }
    }

    public void addToSkillList(String skills) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(skills);
        Iterator<JsonNode> elements = rootNode.elements();
        while(elements.hasNext()){
            JsonNode skillNodes = elements.next();
            this.skills.add(skillNodes.path("name").asText());
        }
//        System.out.println(this.skills);

    }

    public User findUser(String id) {
        for (User user: this.users)
            if (user.getId().equals(id))
                return user;
        return null;
    }

    public Project findProject(String id) {
        for (Project project: this.projects)
            if (project.getId().equals(id))
                return project;
        return null;
    }

    public int addUser(User user){
        if (this.findUser(user.getId()) != null)
            return -1;

//        user.setId(String.valueOf(this.nextUserId++));
        this.users.add(user);
        return 0;
    }

    public int addProject(Project project) {
        if (this.findProject(project.getId()) != null)
            return -1;

//        project.setId(String.valueOf(this.nextProjectId++));
        this.projects.add(project);
        return 0;
    }

    public int addBid(Bid bid) {
        Project project = this.findProject(bid.getProject().getId());
        User user = this.findUser(bid.getUser().getId());

        if (project == null)
            return -1;
        if (user == null)
            return -2;

        project.addBid(bid);
        return 0;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
