package Controller;

import Model.SkillRepo;
import Model.User;
import Model.UserRepo;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/*")
public class SingleUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User thisUser = UserRepo.findUser("1");
        request.setAttribute("thisUser", thisUser);

        String[] splittedPath = request.getRequestURI().split("/");
        String id = splittedPath[3];

        if (id.equals(thisUser.getId())){
            request.setAttribute("user", thisUser);
            request.setAttribute("skillList", SkillRepo.getSkillList());
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/user-single-logged-in.jsp");
            dispatcher.forward(request , response);
//            request.getRequestDispatcher("user-single-logged-in.jsp").forward(request, response);
        }else {
            User requestedUser = UserRepo.findUser(id);
            if (requestedUser == null) {
//                request.getRequestDispatcher("user-not-found.jsp").forward(request, response);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/user-not-found.jsp");
                dispatcher.forward(request , response);
            } else {
                request.setAttribute("requestedUser", requestedUser);
//                request.getRequestDispatcher("user-single-guest.jsp").forward(request, response);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/user-single-guest.jsp");
                dispatcher.forward(request , response);
            }
        }
    }
}
