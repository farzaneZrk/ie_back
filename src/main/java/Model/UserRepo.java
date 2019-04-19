package Model;

import java.util.ArrayList;
import java.util.List;

public class UserRepo {
    private static int nextUserId;
    private static List<User> userList;

    static {
        nextUserId = 1;
        userList = new ArrayList<>();
        List<Skill> skills = new ArrayList<Skill>();
        skills.add(new Skill("HTML", 10));
        User u1 = new User("10", "سینا", "کچویی", "برنامه ‌نویس", "pic", skills,"تولید محتوای الکترونیک یکی از ابزارهای اساسی در زمینه کیفیت آموزش مجازی می باشد.");
        skills.clear();
        skills.add(new Skill("c", 5));
        skills.add(new Skill("Java", 10));
        User u2 = new User("20", "فرزانه", "زیرک", "دانشجو", "pic2", skills, " بسیاری افراد آموزش مجازی را بدلیل نداشتن تعامل و ارتباط محتوا آن را بیشتر مکمل آموزش حضوری میداند لذا لازم به توضیح است که سامانه آی کورسرا با استفاده از تولید محتواهای تعاملی با امکان شرکت در مباحث آموزشی به صورت غیرهمزمان و پاسخ به سوالات استاد فضای آموزش حضوری را در قالب محتوای الکترونیک شبیه سازی میکند");
        skills.clear();
        skills.add(new Skill("HTML", 5));
        skills.add(new Skill("Javascript", 4));
        skills.add(new Skill("C++", 2));
        skills.add(new Skill("Java", 3));
//        skills.add(new Skill("Python", 5));
        User u3 = new User("1", "علی", "شریف زاده", "برنامه نویس وب", "...", skills, "روی سنگ قبرم بنویسید: خدا بیامرز میخواست خیلی کارا بکنه ولی پول نداشت");
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
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

    public static int endorseUserSkill(String endorserID, String userID, String skillName){
        User user = findUser(userID);
        for (Skill skill : user.getSkills()) {
            if (skill.getName().equals(skillName)) {
                if (!skill.hasEndorsed(endorserID)) {
                    skill.endorse(endorserID);
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
            return user.removeSkill(skillName);
        }
        return 0;
    }


}
