package Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Model.UserRepo.findUser;

public class ProjectRepo {
    private static int nextProjectId;
    private static List<Project> projectList;

    static {
        nextProjectId = 1;
        projectList = new ArrayList<>();
        List<Skill> skills = new ArrayList<Skill>();
        skills.add(new Skill("c", 10));
        Project p1 = new Project("10", "job", "descp", "pic", skills, 10, 200);
        List<Skill> skills2 = new ArrayList<Skill>();
        skills2.add(new Skill("HTML", 3));
        skills2.add(new Skill("Javascript", 3));
        Project p2 = new Project("20", "job2", "descp2", "pic2", skills2, 20, 300);
        Project p3 = new Project("30","وبسایت فروشگاهی مشابه دیجی کالا", "یک فروشگاه اینرنتی با قابلیت مدیریت حرفه ای  سبد خرید حرفه ای مقایسه محصولات ارسال پیامک و ایمیل گزارش گیری جامع قالب...", "https://cdn1.vectorstock.com/i/1000x1000/71/55/software-development-vector-5647155.jpg", skills2, 50000000, 419168000);
        projectList.add(p1);
        projectList.add(p2);
        projectList.add(p3);

    }

    private static void setUpProjectlist() throws IOException {
        String projectsJson = getDataFromFile("projects.txt");
        addToProjectList(projectsJson);
    }

    public static String getDataFromFile(String filePath) throws IOException {
        System.out.println("in get data from file");
        File file = new File(filePath);
        System.out.println("1");
        FileInputStream fis = new FileInputStream(file);
        System.out.println("2");
        byte[] data = new byte[(int) file.length()];
        System.out.println("3");
        fis.read(data);
        System.out.println("4");
        fis.close();
        System.out.println("5");
        String str = new String(data, "UTF-8");
        System.out.println("6");
        return  str;
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
