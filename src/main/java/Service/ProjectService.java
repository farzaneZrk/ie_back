package Service;

import Model.*;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static Service.UserService.prepareResponse;

public class ProjectService {
    public static void showProject (HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {

        User thisUser = UserRepo.findUser((String) request.getAttribute("loggedInUserId"));
        request.setAttribute("thisUser", thisUser);
        JSONObject json = new JSONObject("{}");

        Project project = ProjectRepo.findProject(id);
        if (project == null)
            prepareResponse(response, json, HttpServletResponse.SC_NOT_FOUND); //page not found

//        else if (!project.checkUserForProject(thisUser.getId()))
//            prepareResponse(response, json, HttpServletResponse.SC_FORBIDDEN);//page is forbidden

        else {
            Map<String, Object> thisProject = new LinkedHashMap<>();
            thisProject.put("id", project.getId());
            thisProject.put("title", project.getTitle());
            thisProject.put("budget", project.getBudget());
            thisProject.put("description", project.getDescription());
            thisProject.put("imageURL", project.getImageUrl());
            thisProject.put("skills", project.getSkills());
            thisProject.put("deadline", project.getDeadline());
            thisProject.put("isExpired", project.isExpired());
            thisProject.put("winner", project.getWinner());
            System.out.println(thisProject);
//            json = new JSONObject(project);
            json = new JSONObject(thisProject);
            json.put("hasBid", thisUser.hasBidded(id));

            prepareResponse(response, json, HttpServletResponse.SC_OK);
        }
    }

    public static void validateBid (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bidAmount = request.getParameter("bidAmount");
        String projectId = request.getParameter("projectId");
        User user = UserRepo.findUser((String) request.getAttribute("loggedInUserId"));

        Project project = ProjectRepo.findProject(projectId);

        Map<String, Object> resMap = new LinkedHashMap<>();

        if(project.getBudget() >= Integer.valueOf(bidAmount)) {
            UserRepo.addBiddedProject(user, projectId);
            ProjectRepo.addBid(new Bid(Integer.valueOf(bidAmount), project, user));
            resMap.put("msg", "Your bid accepted.");
            JSONObject json = new JSONObject(resMap);
            prepareResponse(response, json, HttpServletResponse.SC_OK);
        }else {
            resMap.put("msg", "Your bid rejected! Bid amount is more than the project budget.");
            JSONObject json = new JSONObject(resMap);
            prepareResponse(response, json, 422);
        }
    }

    public static void showAllProjects (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("in showAllProject");
        User thisUser = UserRepo.findUser((String) request.getAttribute("loggedInUserId"));

        int offset = Integer.valueOf(request.getParameter("pageNumber"));
        int limit = Integer.valueOf(request.getParameter("projectPerPage"));

        List<Map<String, Object>> projects = new ArrayList<>();
        for (Project project: ProjectRepo.getProjectList(limit, offset)) {
//            if (project.qualifyUser(thisUser)) {
                Map<String, Object> thisProject = new LinkedHashMap<>();
                thisProject.put("id", project.getId());
                thisProject.put("title", project.getTitle());
                thisProject.put("imageURL", project.getImageUrl());
                thisProject.put("budget", project.getBudget());
                thisProject.put("deadline", project.getDeadline());
                thisProject.put("skills", project.getSkills());
                thisProject.put("description", project.getDescription());
                projects.add(thisProject);
//            }
        }
        int projectNumber = ProjectRepo.getNumberOfProjects();
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("projects", projects);
        responseMap.put("projectNumber", projectNumber);
        responseMap.put("elementsBeginIndex", offset*limit + limit);

        JSONObject json = new JSONObject(responseMap);

        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }

    public static void findProjectBySearchKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User thisUser = UserRepo.findUser((String) request.getAttribute("loggedInUserId"));

        String searchKey = request.getParameter("searchKey");
        List<Map<String, Object>> projects = new ArrayList<>();

        int offset = Integer.valueOf(request.getParameter("pageNumber"));
        int limit = Integer.valueOf(request.getParameter("projectPerPage"));
        for (Project project: ProjectRepo.searchProjects(searchKey, limit, offset)) {
//            if (project.qualifyUser(thisUser)) {
                Map<String, Object> thisProject = new LinkedHashMap<>();
                thisProject.put("id", project.getId());
                thisProject.put("title", project.getTitle());
                thisProject.put("deadline", project.getDeadline());
                thisProject.put("budget", project.getBudget());
                thisProject.put("imageURL", project.getImageUrl());
                thisProject.put("description", project.getDescription());
                thisProject.put("skills", project.getSkills());
                projects.add(thisProject);
//            }
        }

        int projectNumber = ProjectRepo.getNumberOfSearchedProjects(searchKey);

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("projects", projects);
        responseMap.put("elementsBeginIndex", offset*limit + limit);
        responseMap.put("projectNumber", projectNumber);

        JSONObject json = new JSONObject(responseMap);
        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }
}
