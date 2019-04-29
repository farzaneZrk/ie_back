package Model;

import java.util.ArrayList;
import java.util.List;

public class modelt {
    public static void main(String[] args) {
        SkillRepo mc = new SkillRepo();
        List<Skill> skills = new ArrayList<Skill>();
        skills.add(new Skill("http", 10));
        User u1 = new User("10", "omid", "amini", "job", "pic", "bio");
        User u2 = new User("20", "farzane", "zirak!", "job2", "pic2", "bio2");
//        Project p1 = new Project("10", "job", "descp", "pic", new ArrayList<Skill>(skills), 10, 200);
//        Project p2 = new Project("20", "job2", "descp2", "pic2", skills, 20, 300);

//        mc.addUser(u1);
//        mc.addUser(u2);
//
//        mc.addProject(p1);
//        skills.add(new Skill("css", 20));
//        mc.addProject(p2);
//
//        mc.addBid(new Bid(100, p1, u1));
//        mc.addBid(new Bid(200, p1, u2));
//
//        System.out.println("users: ");
//        for (User user: mc.getUserList())
//            System.out.println(user.getId() + " " + user.getFirstName());
//
//        System.out.println("projects: ");
//        for (Project project: mc.getProjectList())
//            System.out.println(project.getId()+ " " + project.getTitle() + " " + project.getSkills().toString() +
//                    " " + project.getBids().toString());

    }
}
