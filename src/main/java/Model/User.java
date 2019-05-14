package Model;

import java.util.ArrayList;
import java.util.List;
//import org.json.*;

public class User {
    private String id;
    private String firstName, lastName;
    private String username;
    private String passWord;
    private String jobTitle;
    private String imageURL;
    private List<Skill> skills;
    private String bio;
    private List<String> biddedProject;

    public User(String id, String firstName, String lastName, String username, String passWord, String jobTitle, String imageURL, String bio) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passWord = passWord;
        this.jobTitle = jobTitle;
        this.imageURL = imageURL;
        this.bio = bio;
    }

    public User(String id, String firstName, String lastName, String jobTitle, String imageURL, List<Skill> skills, String bio, List<String> biddedProject) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.imageURL = imageURL;
        this.skills = skills;
        this.bio = bio;
        this.biddedProject = biddedProject;
    }

    public User(String id, String firstName, String lastName, String jobTitle, String picURL, List<Skill> skills, String bio) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.imageURL = picURL;
        this.skills = new ArrayList<Skill>(skills);
        this.bio = bio;
        this.biddedProject = new ArrayList<>();
    }

    public User(String id, String firstName, String lastName, String jobTitle, String picURL, String bio) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.imageURL = picURL;
        this.bio = bio;
        this.skills = new ArrayList<Skill>();
        this.biddedProject = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getBio() {
        return bio;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public List<String> getBiddedProject() {
        return biddedProject;
    }

    public void setBiddedProject(List<String> biddedProject) {
        this.biddedProject = biddedProject;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean addSkill(Skill skill) {
        for (Skill s : skills) {
            if (s.getName().equals(skill.getName()))
                return false;
        }
        this.skills.add(skill);
        return true;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int removeSkill(String skillName) {
        for (int i=0; i < skills.size(); i++) {
            if (skills.get(i).getName().equals(skillName)) {
                skills.remove(i);
                return 1;
            }
        }
        return 0;
    }

    public void addBiddedProject(String projectId){
        biddedProject.add(projectId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", skills=" + skills +
                ", bio='" + bio + '\'' +
                ", biddedProject=" + biddedProject +
                '}';
    }

//    @Override
//    public String toString() {
//        return "User{" +
//                "id='" + id + '\'' +
//                ", firstName='" + firstName + '\'' +
//                ", skills=" + skills +
//                '}';
//    }

    public boolean hasBidded(String projectID){
        for (String id: biddedProject) {
            if(id.equals(projectID))
                return true;
        }
        return false;
    }
}
