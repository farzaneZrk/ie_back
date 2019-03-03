package Controller;

import Model.Project;
import Model.ProjectController;
import Model.User;
import Model.UserController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.StringTokenizer;

@WebServlet("/project/*")
public class SingleProjectCtrl extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] splittedPath = request.getRequestURI().split("/");
        String id = splittedPath[3];

        User thisUser = UserController.findUser("1");
        request.setAttribute("thisUser", thisUser);

        Project project = ProjectController.findProject(id);
        request.setAttribute("project", project);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/project-single.jsp");
        dispatcher.forward(request , response);
    }
}
