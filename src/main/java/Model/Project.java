package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Project {
    private String id;
    private String title, description, imageUrl;
    private List<Skill> skills;
    private List<Bid> bids;
    private int budget;
    private long deadline;
    private long creationTime;
    private User winner;

    public Project(String id, String title, String description, String imageUrl, List<Skill> skills, List<Bid> bids, int budget, long deadline, long creationTime, User winner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.skills = skills;
        this.bids = bids;
        this.budget = budget;
        this.deadline = deadline;
        this.creationTime = creationTime;
        this.winner = winner;
    }

    public Project(String id, String title, String description, String imageUrl, List<Skill> skills, int budget, long deadline, long creationTime, User winner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.skills = skills;
        this.budget = budget;
        this.deadline = deadline;
        this.creationTime = creationTime;
        this.winner = winner;
    }
    public Project(String id, String title, String description, String imageUrl, int budget, long deadline, long creationTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.budget = budget;
        this.deadline = deadline;
        this.creationTime = creationTime;
    }


    public Project(String id, String title, String descp, String picURL, List<Skill> skills, int budget, long deadline, long creationTime) {
        this.id = id;
        this.title = title;
        this.description = descp;
        this.imageUrl = picURL;
        this.skills = skills;
        this.bids = new ArrayList<Bid>();
        this.budget = budget;
        this.deadline = deadline;
        this.creationTime = creationTime;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getCreationTime(){
        return creationTime;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getWinner() {
        if (winner == null){
            return "no one!";
        }
        return (winner.getFirstName() + " " + winner.getLastName());
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id='" + id + '\n' +
                ", title='" + title + '\n' +
                ", description='" + description + '\n' +
                ", imageUrl='" + imageUrl + '\n' +
                ", skills=" + skills + '\n' +
                ", budget=" + budget + '\n' +
                ", deadline=" + deadline + '\n' +
                ", bids=" + bids +
                '}';
    }


    public boolean checkUserForProject(String userId) {
        User user = UserRepo.findUser(userId);
        return qualifyUser(user);
    }

    public boolean qualifyUser(User user){
        List<Skill> userSkillsList = user.getSkills();
        if (userSkillsList.size() == 0 && skills.size() != 0)
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

    public boolean isExpired() {
        Date deadline = new Date(this.deadline * 1000);
        Date current = new Date();
        return current.after(deadline);
    }
}
