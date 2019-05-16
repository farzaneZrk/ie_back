package Service;

import Model.Skill;
import Model.User;
import Model.UserRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;

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
        String endorserID = (String) request.getAttribute("loggedInUserId");
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
        String userID = (String) request.getAttribute("loggedInUserId");
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
        String userID = (String) request.getAttribute("loggedInUserId");
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
            currUser.put("name",(user.getFirstName() + ' ' + user.getLastName()));
            currUser.put("id", user.getId());
            currUser.put("job_title", user.getJobTitle());
            users.add(currUser);
        }

        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("users", users);

        JSONObject json = new JSONObject(responseMap);
        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }

    public static void addUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> resMap = new LinkedHashMap<>();
        boolean isValid = validateParameters(request, resMap);
        if(!isValid){
            JSONObject json = new JSONObject(resMap);
            prepareResponse(response, json, HttpServletResponse.SC_OK);
            return;
        }
        Random rand = new Random();
        String id = String.valueOf(rand.nextInt(10000000));
//        String id = "1";
        String password = DigestUtils.sha256Hex(request.getParameter("password"));
        User newUser = new User(id, request.getParameter("firstname"), request.getParameter("lastname"),
                request.getParameter("username"), password, request.getParameter("jobTitle"),
                request.getParameter("imageURL"), request.getParameter("bio"));
        int res = UserRepo.addUser(newUser);
        if(res != -1){
            // success
            resMap.put("msg", "ok");
            resMap.put("errorCode", "200");
            String jwt = createJWT(res);
            resMap.put("jwt", jwt);
        }
        else{
            resMap.put("msg", "this username has been chosen");
            resMap.put("errorCode", "422");
        }
        JSONObject json = new JSONObject(resMap);
        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }

    private static boolean validateParameters(HttpServletRequest request, Map<String, Object> resMap) {
        String password = request.getParameter("password");
        String pattern1 = "^[آابپتثجچحخدذرزژسشصضطظعغفقکگلمنوهیa-zA-z]+$";
        String pattern2 = "(https:[//]|http:[//])(.+)";
        if(password.matches(".*\\d.*") && password.length() >= 8) {
            String firstname = request.getParameter("firstname");
            System.out.println(firstname);
            System.out.println(firstname.matches(pattern1));
            if (firstname.length() >= 3 && firstname.matches(pattern1)) {
                String lastname = request.getParameter("lastname");
                if (lastname.length() >= 3 && lastname.matches(pattern1)) {
                    String jobTitle = request.getParameter("jobTitle");
                    if (jobTitle.length() >= 2 && jobTitle.matches(pattern1)) {
                        String imageURL = request.getParameter("imageURL");
                        if (imageURL.length() >= 12 && imageURL.matches(pattern2)) {
                            return true;
                        } else
                            resMap.put("msg", "imageURL is too short or has not the required pattern.");
                    } else
                        resMap.put("msg", "jobTitle is too short or contains invalid characters.");
                } else
                    resMap.put("msg", "lastname is too short or contains invalid characters.");
            } else
                resMap.put("msg", "firstname is too short or contains invalid characters.");
        }else
            resMap.put("msg", "password is too short or doesn't have number.");
        resMap.put("errorCode", "422");
        return false;
    }

    public static void checkUsername(HttpServletRequest request, HttpServletResponse response) throws
            SQLException, ServletException, IOException {
        System.out.println("in check username with " + request.getParameter("username"));
        boolean res = UserRepo.checkUsername(request.getParameter("username"));
        Map<String, Object> resMap = new LinkedHashMap<>();
        if(res)
            resMap.put("isValid", true);
        else
            resMap.put("isValid", false);
        JSONObject json = new JSONObject(resMap);
        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }

    public static void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = DigestUtils.sha256Hex(request.getParameter("password"));
        int res = UserRepo.checkUserPass(username, password);
        Map<String, Object> resMap = new LinkedHashMap<>();
        if (res == -2){
            resMap.put("msg", "this username does not exist");
            resMap.put("errorCode", "404");
        }
        else if(res == -1){
            resMap.put("msg", "username or password is not correct");
            resMap.put("errorCode", "403");
        }
        else{
            resMap.put("msg", "ok");
            String jwt = createJWT(res);
            resMap.put("jwt", jwt);
            // todo: user and pass are ok, handle jwt here
        }
        JSONObject json = new JSONObject(resMap);
        prepareResponse(response, json, HttpServletResponse.SC_OK);
    }

    private static String createJWT(int userId) throws UnsupportedEncodingException {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date fiveMinLater = new Date(nowMillis + 5*60*1000);
        String jws = Jwts.builder()
                .setIssuer("joboonja.ut.ac.ir")
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(fiveMinLater.toInstant()))
                .claim("userId", String.valueOf(userId))
                .signWith(
                        SignatureAlgorithm.HS256,
                        Base64.getUrlDecoder().decode(DigestUtils.sha256Hex("joboonja"))
                )
                .compact();

        return jws;

    }
}
