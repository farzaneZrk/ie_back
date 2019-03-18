package Controller;

import Model.UserRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteSkill")
public class DeleteSkillCtrl extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userID = request.getParameter("userID");
        String skillName = request.getParameter("skillName");
        UserRepo.deleteUserSkill(userID, skillName);

//        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/user/" + userID);
//        dispatcher.forward(request , response);
        request.getRequestDispatcher("/user/" + userID).forward(request, response);
    }
}
