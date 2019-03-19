package Service;

import Model.Skill;
import Model.SkillRepo;
import Model.User;
import Model.UserRepo;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserService {
    public static void showUser(HttpServletRequest request, HttpServletResponse response, String id)
            throws ServletException, IOException {
        User thisUser = UserRepo.findUser("1");
        request.setAttribute("thisUser", thisUser);

        if (id.equals(thisUser.getId())){
            request.setAttribute("user", thisUser);
            request.setAttribute("skillList", SkillRepo.getSkillList());
            RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/user-single-logged-in.jsp");
            dispatcher.forward(request , response);
//            request.getRequestDispatcher("user-single-logged-in.jsp").forward(request, response);
        }else {
            User requestedUser = UserRepo.findUser(id);
            if (requestedUser == null) {
//                request.getRequestDispatcher("user-not-found.jsp").forward(request, response);
                RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/user-not-found.jsp");
                dispatcher.forward(request , response);
            } else {
                request.setAttribute("requestedUser", requestedUser);
//                request.getRequestDispatcher("user-single-guest.jsp").forward(request, response);
                RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/user-single-guest.jsp");
                dispatcher.forward(request , response);
            }
        }
    }

    public static void endorseUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String endorserID = request.getParameter("endorserID");
        String endorsedID = request.getParameter("endorsedID");
        String skillName = request.getParameter("skillName");
        UserRepo.endorseUserSkill(endorserID, endorsedID, skillName);
        request.getRequestDispatcher("user/" + endorsedID).forward(request, response);
    }

    public static void addSkill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userID = request.getParameter("userID");
        User user = UserRepo.findUser(userID);
        user.addSkill(new Skill(request.getParameter("choosedSkill"), 0));
        request.getRequestDispatcher("/user/" + userID).forward(request, response);
    }

    public static void removeSkill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userID = request.getParameter("userID");
        String skillName = request.getParameter("skillName");
        UserRepo.deleteUserSkill(userID, skillName);
        request.getRequestDispatcher("/user/" + userID).forward(request, response);
    }
}
