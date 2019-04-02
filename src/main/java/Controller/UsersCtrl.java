package Controller;

import Service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users")
public class UsersCtrl extends HttpServlet {

    // users with get method to get list of all users or a specific one
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("userId");

        if (id != null){        //one specific user profile is requested
            System.out.println(request.getParameter("userId"));
            UserService.showUser(request, response, id, "1");
        }
        else                                               //list of users is requested
            UserService.showUsersList(request, response, "1");
    }

    // users with put method to add skill to the current user
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService.addSkill(request, response);
    }

    // users with delete method to delete skill from the current user
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService.removeSkill(request, response);
    }

}
