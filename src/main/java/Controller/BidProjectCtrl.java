package Controller;

import Model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/bidProject")
public class BidProjectCtrl extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String bidamount = request.getParameter("bidAmount");

        String projectId = request.getParameter("projectID");
        User user = UserController.findUser(request.getParameter("userID"));
        user.addBiddedProject(projectId);

        Project project = ProjectController.findProject(projectId);

        if(project.getBudget() >= Integer.valueOf(bidamount)) {
            project.addBid(new Bid(Integer.valueOf(bidamount), project, user));
            request.setAttribute("msg", "Your bid accepted.");
        }else {
            request.setAttribute("msg", "Your bid rejected! Bid amount is more than the project budget.");
        }

        request.setAttribute("thisUser", user);
        request.setAttribute("project", project);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/project-single.jsp");
        dispatcher.forward(request , response);
    }
}
