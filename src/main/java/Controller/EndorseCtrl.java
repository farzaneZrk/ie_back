package Controller;

import Model.Skill;
import Model.User;
import Model.UserController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/endorse")
public class EndorseCtrl extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String endorserID = request.getParameter("endorserID");
        String endorsedID = request.getParameter("endorsedID");
        String skillName = request.getParameter("skillName");
        UserController.endorseUserSkill(endorserID, endorsedID, skillName);
        request.getRequestDispatcher("user/" + endorsedID).forward(request, response);
    }
}