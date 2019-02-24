package Model;

import java.util.ArrayList;
import java.util.List;
//import org.json.*;

public class User {
    private String id;
    private String firstName, lastName;
    private String jobTitle;
    private String picURL;
    private List<Skill> skills;
    private String bio;

    public User(String id, String firstName, String lastName, String jobTitle, String picURL, List<Skill> skills, String bio) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.picURL = picURL;
        this.skills = new ArrayList<Skill>(skills);
        this.bio = bio;
    }

    public User(String id, String firstName, String lastName, String jobTitle, String picURL, String bio) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.picURL = picURL;
        this.bio = bio;
        this.skills = new ArrayList<Skill>();
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

    public String getPicURL() {
        return picURL;
    }

    public String getBio() {
        return bio;
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

    public List<Skill> getSkills() {
        return skills;
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
