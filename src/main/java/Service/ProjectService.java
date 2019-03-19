package Service;

import Model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProjectService {
    public static void showProject (HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        User thisUser = UserRepo.findUser("1");
        request.setAttribute("thisUser", thisUser);

        Project project = ProjectRepo.findProject(id);
        if (project == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("<html><body><h1>404 page not found</h1></body></html>");
            return;
        }
        if (!project.checkUserForProject(thisUser.getId())){
//            RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/project-single.jsp");
//            dispatcher.forward(request , response);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("<html><body><h1>403 source forbidden</h1></body></html>");
            return;
        }
        request.setAttribute("project", project);
        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/project-single.jsp");
        dispatcher.forward(request , response);
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
}
