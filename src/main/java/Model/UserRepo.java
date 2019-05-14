package Model;

import Mapper.User.UserDataMapperImp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepo {
    private static UserDataMapperImp userDataMapper;

    static {
        userDataMapper = new UserDataMapperImp();
    }

    public static void testusermapper(){
        System.out.println("in user repo test");
        List<Skill> skills = new ArrayList<Skill>();
        skills.add(new Skill("HTML", 10));
        User u1 = new User("10", "سینا", "کچویی", "برنامه ‌نویس", "pic", skills,"تولید محتوای الکترونیک یکی از ابزارهای اساسی در زمینه کیفیت آموزش مجازی می باشد.");
        skills.clear();
        skills.add(new Skill("c", 5));
        skills.add(new Skill("Java", 10));
        User u2 = new User("20", "فرزانه", "زیرک", "دانشجو", "pic2", skills, " بسیاری افراد آموزش مجازی را بدلیل نداشتن تعامل و ارتباط محتوا آن را بیشتر مکمل آموزش حضوری میداند لذا لازم به توضیح است که سامانه آی کورسرا با استفاده از تولید محتواهای تعاملی با امکان شرکت در مباحث آموزشی به صورت غیرهمزمان و پاسخ به سوالات استاد فضای آموزش حضوری را در قالب محتوای الکترونیک شبیه سازی میکند");
//        ((UserDataMapperImp) userDataMapper).insert(u1);
//        ((UserDataMapperImp) userDataMapper).insert(u2);
    }

    public static List<User> getUserList() {
        return userDataMapper.getAll();
    }

    public static User findUser(String id) {
        User res = userDataMapper.abstractFind(id);
        System.out.println("user in findUser    " + res);
        return res;
    }

    public static int addUser(User user){
//        while (findUser(user.getId()) != null) {
//            Random rand = new Random();
//            String id = String.valueOf(rand.nextInt(50));
//            user.setId(id);
//        }
//        try {
        String id = userDataMapper.insert(user);
        if (id == null)
            return -1;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return -1;
//        }
        return 0;
    }

    public static int endorseUserSkill(String endorserID, String userID, String skillName){
        User user = findUser(userID);
        System.out.println("user in  endorseUserSkill   " + user);
        for (Skill skill : user.getSkills()) {
            if (skill.getName().equals(skillName)) {
                if (!skill.hasEndorsed(endorserID)) {
                    // it updates the user
                    skill.endorse(endorserID);
                    //it will insert new rows to the database and replace user with updated user
                    userDataMapper.endorseUserSkill(user, endorserID, skill);
                    return 1;
                }
                else return 0;
            }
        }
        return -1;
    }

    public static int deleteUserSkill(String userID, String skillName) {
        User user = findUser(userID);
        if (user != null) {
            int ret = user.removeSkill(skillName);
            userDataMapper.removeUserSkill(user, skillName);
        }
        return 0;
    }

    public static boolean addSkill(User user, Skill skill) {
        boolean res = user.addSkill(skill);
        if(res)
            userDataMapper.addUserSkill(user, skill.getName());
        return res;
    }

    public static void addBiddedProject(User user, String projectId){
        user.addBiddedProject(projectId);
        userDataMapper.updateUser(user);
    }

    public static List<User> searchUser(String searchKey){
        System.out.println("@@@@@@@@@@@@@@@@");
        return userDataMapper.selectMatchedUsers(searchKey);
    }

    public static boolean checkUsername(String username) throws SQLException {
        return userDataMapper.findUserByUsername(username);
    }

    public static int checkUserPass(String username, String password) {
        try {
            if(!UserRepo.checkUsername(username)){
                if(userDataMapper.checkPassword(username, password))
                    return 0;
                else
                    return -1;
            }
            else
                return -2;
        } catch (SQLException e) {
            e.printStackTrace();
            return -3;
        }
    }
}
