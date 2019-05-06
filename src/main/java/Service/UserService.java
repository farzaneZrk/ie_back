package Service;

import Model.Skill;
import Model.User;
import Model.UserRepo;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    public static void showUser(HttpServletRequest request, HttpServletResponse response, String id, String thisUserId)
            throws ServletException, IOException {
        User thisUser = UserRepo.findUser(thisUserId);
        JSONObject json = new JSONObject("{}");

        if (id.equals(thisUserId)){
            json = new JSONObject(thisUser);
            json.put("logged_in_user", true);
            json.remove("biddedProject");
            System.out.println(json);

            prepareResponse(response, json, HttpServletResponse.SC_OK);
        }else {
            User requestedUser = UserRepo.findUser(id);

            if (requestedUser == null) {
                prepareResponse(response, json, HttpServletResponse.SC_NOT_FOUND); //page not found
            } else {
                json = new JSONObject(requestedUser);
                json.put("logged_in_user", false);
                json.remove("biddedProject");
                System.out.println(json);

                prepareResponse(response, json, HttpServletResponse.SC_OK);
            }
        }
    }

    public static void endorseUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String endorserID = request.getParameter("endorserId");
        String endorsedID = request.getParameter("endorsedId");
        String skillName = request.getParameter("skillName");
        System.out.println(endorsedID);
        System.out.println(skillName);
        System.out.println(endorserID);
        Map<String, Object> resMap = new LinkedHashMap<>();
        if (UserRepo.endorseUserSkill(endorserID, endorsedID, skillName) == 1)
            resMap.put("msg", skillName +  " skill endorsed successfully.");
        else {
            resMap.put("msg", "you have endorsed " + skillName + " skill of this user before!");
            resMap.put("errorCode", "200");
        }
        JSONObject json = new JSONObject(resMap);
        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }

    public static void addSkill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userID = request.getParameter("id");
        String skillName = request.getParameter("skillName");
        System.out.println(skillName);
        System.out.println(userID);
        User user = UserRepo.findUser(userID);
        Map<String, Object> resMap = new LinkedHashMap<>();

        if (skillName == null){
            resMap.put("msg", "error occurred! You have to choose a skill first.");
            resMap.put("errorCode", "422");
            response.setHeader("errorCode", "422");
            response.setHeader("description", "Unprocessable Entity");
            JSONObject json = new JSONObject(resMap);
            prepareResponse(response, json, HttpServletResponse.SC_OK);
            return;
        }

        if(UserRepo.addSkill(user, (new Skill(skillName, 0)))){
//        if(user.addSkill(new Skill(skillName, 0))){
            resMap.put("msg", skillName +  " added to your skills successfully.");
            JSONObject json = new JSONObject(resMap);
            prepareResponse(response, json, HttpServletResponse.SC_OK);
            return;
        }
        resMap.put("msg", request.getParameter("skillName") +  " is already in your skills.");
        resMap.put("errorCode", "200");
        response.setHeader("errorCode", "200");
        response.setHeader("description", "Repeated Entity");
        JSONObject json = new JSONObject(resMap);
        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }

    public static void removeSkill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userID = request.getParameter("id");
        String skillName = request.getParameter("skillName");
        Map<String, Object> resMap = new LinkedHashMap<>();
        if (UserRepo.deleteUserSkill(userID, skillName) == 1)
            resMap.put("msg", skillName +  " removed from your skills successfully.");
        else {
            resMap.put("msg", "An error occurred! please specify an existing skill.");
            resMap.put("errorCode", "422");
            response.setHeader("errorCode", "422");
            response.setHeader("description", "Unprocessable Entity");
        }
        JSONObject json = new JSONObject(resMap);
        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }

    public static void showUsersList(HttpServletRequest request, HttpServletResponse response, String thisUserId)
            throws ServletException, IOException {
        User thisUser = UserRepo.findUser(thisUserId);
        List<Map<String, Object>> users = new ArrayList<>();

        for (User user: UserRepo.getUserList()) {
            if (user.getId().equals(thisUserId)) {
                continue;
            }
            Map<String, Object> currUser = new LinkedHashMap<>();
            currUser.put("id", user.getId());
            currUser.put("name",(user.getFirstName() + ' ' + user.getLastName()));
            currUser.put("job_title", user.getJobTitle());
            users.add(currUser);
        }

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("users", users);

        JSONObject json = new JSONObject(responseMap);
        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }

    public static void prepareResponse(HttpServletResponse response, JSONObject json, int status)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        out.print(json.toString());
        System.out.println(json);
        out.flush();
    }

    public static void findUserBySearchKey(HttpServletRequest request, HttpServletResponse response, String thisUserId)
            throws ServletException, IOException {

        String searchKey = request.getParameter("searchKey");
        List<Map<String, Object>> users = new ArrayList<>();
        System.out.println("MANAM\n");
        for (User user: UserRepo.searchUser(searchKey)) {
            if (user.getId().equals(thisUserId)) {
                continue;
            }
            Map<String, Object> currUser = new LinkedHashMap<>();
            currUser.put("id", user.getId());
            currUser.put("name",(user.getFirstName() + ' ' + user.getLastName()));
            currUser.put("job_title", user.getJobTitle());
            users.add(currUser);
        }

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("users", users);

        JSONObject json = new JSONObject(responseMap);
        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }

}
