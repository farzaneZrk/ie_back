package Service;

import Model.*;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProjectService {
    public static void showProject (HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        User thisUser = UserRepo.findUser("1");
        request.setAttribute("thisUser", thisUser);
        JSONObject json = new JSONObject("{}");

        Project project = ProjectRepo.findProject(id);
        if (project == null)
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); //page not found

        else if (!project.checkUserForProject(thisUser.getId()))
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);//page forbidden

        else{
            response.setStatus(HttpServletResponse.SC_OK);
            json = new JSONObject(project);
            json.put("hasBid",thisUser.hasBidded(id));
            System.out.println(json);
        }

        PrintWriter out = response.getWriter();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();
    }

    public static void validateBid (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bidamount = request.getParameter("bidAmount");

        String projectId = request.getParameter("projectID");
        User user = UserRepo.findUser(request.getParameter("userID"));

        Project project = ProjectRepo.findProject(projectId);

        if(project.getBudget() >= Integer.valueOf(bidamount)) {
            user.addBiddedProject(projectId);
            project.addBid(new Bid(Integer.valueOf(bidamount), project, user));
            request.setAttribute("msg", "Your bid accepted.");
        }else {
            request.setAttribute("msg", "Your bid rejected! Bid amount is more than the project budget.");
        }

        request.setAttribute("thisUser", user);
        request.setAttribute("project", project);
        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/project-single.jsp");
        dispatcher.forward(request , response);
    }

    public static void showAllProjects (HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        User thisUser = UserRepo.findUser(id);
        List<Map<String, Object>> projects = new ArrayList<>();

        for (Project project: ProjectRepo.getProjectList()) {
//            if (project.qualifyUser(thisUser) && !project.isExpired()){
            if (project.qualifyUser(thisUser)){
                Map<String, Object> thisProject = new LinkedHashMap<>();
                thisProject.put("id", (Object) project.getId());
                thisProject.put("title", (Object) project.getTitle());
                thisProject.put("budget", (Object) project.getBudget());
                projects.add(thisProject);
            }
        }

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("projects", (Object) projects);

        JSONObject json = new JSONObject(responseMap);

        PrintWriter out = response.getWriter();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        out.print(json.toString());
        System.out.println(json);
        out.flush();

    }
}
