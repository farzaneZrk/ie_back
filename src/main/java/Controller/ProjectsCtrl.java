package Controller;

import Service.ProjectService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/projects")
public class ProjectsCtrl extends HttpServlet {

    // users with get method to get list of all projects or a specific one
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String projectId = request.getParameter("projectId");

        if (projectId != null)                           //one specific project is requested
            ProjectService.showProject(request, response, projectId);
        else                                             //list of projects is requested
            ProjectService.showAllProjects(request, response, "1");
    }

}
