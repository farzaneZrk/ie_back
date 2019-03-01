package Controller;

import Model.User;
import Model.UserController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(1);
        List<User> sl = UserController.getUserList();
        System.out.println(2);
        request.setAttribute("users", sl);
        System.out.println(3);
        request.getRequestDispatcher("userlist.jsp").forward(request, response);
        System.out.println(4);
    }
}
