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
        System.out.println("this is the uri: "+request.getRequestURI());
        StringTokenizer tokenizer = new StringTokenizer(request.getRequestURI(), "/");
        System.out.println("1"+tokenizer.nextToken());
        System.out.println("2"+tokenizer.nextToken());

        String id = tokenizer.nextToken();
        System.out.println("this is Id " + id);

        Project project = ProjectController.findProject(id);
        request.setAttribute("project", project);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/project-single.jsp");
        dispatcher.forward(request , response);
    }
}
