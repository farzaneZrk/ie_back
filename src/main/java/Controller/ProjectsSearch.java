package Controller;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static Service.UserService.prepareResponse;

@WebServlet("/searchProjects")
public class ProjectsSearch extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchKey = request.getParameter("searchKey");
        Map<String, Object> resMap = new LinkedHashMap<>();
        resMap.put("msg", "request is gotten.");
        resMap.put("searchKey", searchKey);
        JSONObject json = new JSONObject(resMap);
        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }
}
