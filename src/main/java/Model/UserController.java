package Model;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    private static int nextUserId;
    private static List<User> userList;

    static {
        nextUserId = 1;
        userList = new ArrayList<>();
        List<Skill> skills = new ArrayList<Skill>();
        skills.add(new Skill("http", 10));
        skills.add(new Skill("c", 5));
        User u1 = new User("10", "omid", "amini", "job", "pic", skills,"bio");
        User u2 = new User("20", "farzane", "zirak!", "job2", "pic2", skills, "bio2");
        userList.add(u1);
        userList.add(u2);
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static User findUser(String id) {
        for (User user: userList)
            if (user.getId().equals(id))
                return user;
        return null;
    }

    public static int addUser(User user){
        if (findUser(user.getId()) != null)
            return -1;

//        user.setId(String.valueOf(this.nextUserId++));
        userList.add(user);
        return 0;
    }

    public static void endorseUserSkill(String endorserID, String userID, String skillName){
        User user = findUser(userID);
        for (Skill skill : user.getSkills()) {
            if (skill.getName().equals(skillName)) {
                skill.endorse(endorserID);
                return;
            }
        }
    }


}
