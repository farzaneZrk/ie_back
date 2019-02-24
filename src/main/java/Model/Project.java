package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Project {
    private String id;
    private String title, descp, picURL;
    private List<Skill> skills;
    private List<Bid>  bids;
    private int budget;
    private long deadline;
    private User winner;

    public Project(String id, String title, String descp, String picURL, List<Skill> skills, int budget, long deadline) {
        this.id = id;
        this.title = title;
        this.descp = descp;
        this.picURL = picURL;
        this.skills = skills;
        this.bids = new ArrayList<Bid>();
        this.budget = budget;
        this.deadline = deadline;
        this.winner = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {
        this.bids.add(bid);
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\n' +
                ", title='" + title + '\n' +
                ", descp='" + descp + '\n' +
                ", picURL='" + picURL + '\n' +
                ", skills=" + skills + '\n' +
                ", budget=" + budget + '\n' +
                ", deadline=" + deadline +
                '}';
    }

    public boolean checkUserForProject(User user) {
        List<Skill> userSkillsList = user.getSkills();
        if (userSkillsList.size() == 0)
            return false;
        for( Skill skill: this.skills) {
            List<Skill> result = userSkillsList.stream()
                    .filter(element -> Objects.equals(element.getName(), skill.getName()))
                    .collect(Collectors.toList());
            if (result.size() == 0 || result.get(0).getPoint() < skill.getPoint()) {
                return false;
            }
        }
        return true;
    }
}
