package Controller;

import Model.User;
import Model.UserController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.StringTokenizer;

@WebServlet("/user/*")
public class SingleUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User thisUser = UserController.findUser("10");
        request.setAttribute("thisUser", thisUser);

        System.out.println("this is the uri: "+request.getRequestURI());
        StringTokenizer tokenizer = new StringTokenizer(request.getRequestURI(), "/");
        System.out.println("1"+tokenizer.nextToken());
        System.out.println("2"+tokenizer.nextToken());

        String id = tokenizer.nextToken();
        System.out.println("this is Id" + id);

        if (id.equals(thisUser.getId())){
            System.out.println("in here");
            request.setAttribute("user", thisUser);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/user-single-logged-in.jsp");
            dispatcher.forward(request , response);
//            request.getRequestDispatcher("user-single-logged-in.jsp").forward(request, response);
        }else {
            System.out.println("this is the id:   " + id);
            User requestedUser = UserController.findUser(id);
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
