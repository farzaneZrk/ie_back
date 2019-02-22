package Model;

import java.util.ArrayList;
import java.util.List;

public class modelt {
    public static void main(String[] args) {
        ModelController mc = new ModelController();

        mc.addUser(new User(1, "omid", "amini", "job", "pic", "bio"));
        mc.addUser(new User(2, "farzane", "zirak!", "job2", "pic2", "bio2"));

        List<Skill> skills = new ArrayList<Skill>();
        skills.add(new Skill("http", 10));
        int a = mc.addProject(new Project(1, "job", "descp", "pic", skills, 10, 200));
        skills.add(new Skill("css", 20));
        int b = mc.addProject(new Project(2, "job2", "descp2", "pic2", skills, 20, 300));

        System.out.println(String.valueOf(a) + " " +  String.valueOf(b));
        System.out.println("users: ");
        for (User user: mc.getUsers())
            System.out.println(user.getId() + " " + user.getFirstName());

        System.out.println("projects: ");
        for (Project project: mc.getProjects())
            System.out.println(project.getId()+ " " + project.getTitle());

    }
}
