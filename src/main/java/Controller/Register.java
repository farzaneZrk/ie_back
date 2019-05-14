package Controller;

import Service.UserService;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/registerUser")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Map<String, String[]> pm =  request.getParameterMap();
//        System.out.println("ehem");
//        for (String key: pm.keySet()){
//            String value = pm.get(key).toString();
//            System.out.println(key + " " + value);
//        }
//        System.out.println("in register");
//        System.out.println(request.getParameter("bio"));
        UserService.addUser(request, response);
        System.out.println(DigestUtils.sha256Hex(request.getParameter("bio")));
    }
}

