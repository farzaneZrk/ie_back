package Model;


import Mapper.Project.ProjectMapperImp;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static Model.SkillRepo.getDataFromServer;
import static Model.UserRepo.findUser;

public class ProjectRepo {
    private static ProjectMapperImp projectDataMapper;

    static {
        projectDataMapper = new ProjectMapperImp();
    }

    public static List<Project> getProjectList(int limit, int offset) {
        return projectDataMapper.selectProjectsByOffset(limit, offset);
    }

    public static int addProject(Project project) {
        if (findProject(project.getId()) != null)
            return -1;
        projectDataMapper.insert(project);
        return 0;
    }

    public static Project findProject(String id) {
        System.out.println("project id is: " + id);
        Project res = projectDataMapper.abstractFind(id);

        return res;
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

            projectDataMapper.insert(
                new Project(
                    jsonobject.getString("id"), jsonobject.getString("title"),
                    jsonobject.getString("description"), jsonobject.getString("imageUrl"),
                    skillList, jsonobject.getInt("budget"),  jsonobject.getLong("deadline"),
                    jsonobject.getLong("creationDate")
                )
            );

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
        projectDataMapper.inserProjectBid(project, bid);
        return 0;
    }

    public static List<Project> searchProjects(String searchKey, int limit, int offset) {
        return projectDataMapper.selectMatchedProjects(searchKey, limit, offset);

    }

    public static int getNumberOfProjects(){
        return projectDataMapper.findNumberOfProjects();
    }

    public static int getNumberOfSearchedProjects(String searchKey){
        return projectDataMapper.findNumberOfSearchedProjects(searchKey);
    }


    private static long calculateBidderScore(User bidder, Long bidAmount, Project project) {
        long score = project.getBudget() - bidAmount;
        List<Skill> projectSkillsList = project.getSkills();
        List<Skill> userSkillsList = bidder.getSkills();
        for( Skill skill: projectSkillsList) {
            List<Skill> result = userSkillsList.stream()
                    .filter(element -> Objects.equals(element.getName(), skill.getName()))
                    .collect(Collectors.toList());
            long delta = skill.getPoint() - result.get(0).getPoint();
            score += 10000 * Math.pow(delta, 2);
        }
        return score;
    }

    private static String findBidWinner(Project project) {
        List<Bid> projectBids = project.getBids();
        System.out.println("project bids for " + project.getTitle() + " is " + projectBids);
        long maxScore = 0;
        User winner = null;
        for (Bid bid: projectBids) {
            long score = calculateBidderScore(bid.getUser(), (long) bid.getAmount(), project);
            System.out.println("score for " + bid.getUser() + "is " + score + "in " + project.getTitle());
            if(score > maxScore) {
                maxScore = score;
                winner = bid.getUser();
            }
        }
        if(winner != null){
            System.out.println("winner for  " + project.getTitle() + " is " + winner.getFirstName() + ' ' + winner.getLastName());
            project.setWinner(winner);
            projectDataMapper.updateProject(project);
            return winner.getFirstName() + ' ' + winner.getLastName();
        }
        project.setWinner(new User("0", "no one!", "", "", "", ""));
        projectDataMapper.updateProject(project);
        return "no one!";
    }

    public static void doAuctionForExpiredProjects(){
        List<Project> projects = projectDataMapper.getAll();
        for (Project project: projects) {
            if(project.isExpired()){
                findBidWinner(project);
            }
        }
    }
}
