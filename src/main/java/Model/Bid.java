package Model;

public class Bid {
    private int amount;
    private Project project;
    private User user;

    public Bid(int amount, Project project, User user) {
        this.amount = amount;
        this.project = project;
        this.user = user;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return this.project.getTitle() + ":" + this.user.getFirstName() + ":" + String.valueOf(this.amount);
    }
}
