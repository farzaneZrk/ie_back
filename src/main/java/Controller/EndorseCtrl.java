package Controller;

import Service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users/endorse")
public class EndorseCtrl extends HttpServlet {
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService.endorseUser(request, response);
    }

}
