package Controller;

import Model.Skill;
import Model.User;
import Model.UserController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

@WebServlet("/addSkill")
public class AddSkillCtrl extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request.getParameter("choosedSkill"));
        String userID = request.getParameter("userID");
        User user = UserController.findUser(userID);
        user.addSkill(new Skill(request.getParameter("choosedSkill"), 0));
        request.getRequestDispatcher("/user/" + userID).forward(request, response);

    }
}
