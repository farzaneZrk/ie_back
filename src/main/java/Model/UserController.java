package Model;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    private static int nextUserId;
    private static List<User> userList;

    static {
        nextUserId = 1;
        userList = new ArrayList<>();
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

    public int addUser(User user){
        if (findUser(user.getId()) != null)
            return -1;

//        user.setId(String.valueOf(this.nextUserId++));
        userList.add(user);
        return 0;
    }

}
