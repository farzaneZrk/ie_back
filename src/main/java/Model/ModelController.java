package Model;

import java.util.ArrayList;
import java.util.List;

public class ModelController {
    private List<User> users;
    private List<Project> projects;
    private int nextUserId;
    private int nextProjectId;
    private List<String> skills;

    public ModelController() {
        this.nextProjectId = 1;
        this.nextUserId = 1;
        this.users = new ArrayList<User>();
        this.projects = new ArrayList<Project>();
        this.skills = new ArrayList<String>();
    }

    public User findUser(String id) {
        for (User user: this.users)
            if (user.getId().equals(id))
                return user;
        return null;
    }

    public Project findProject(String id) {
        for (Project project: this.projects)
            if (project.getId().equals(id))
                return project;
        return null;
    }

    public int addUser(User user){
        if (this.findUser(user.getId()) != null)
            return -1;

        user.setId(String.valueOf(this.nextUserId++));
        this.users.add(user);
        return 0;
    }

    public int addProject(Project project) {
        if (this.findProject(project.getId()) != null)
            return -1;

        project.setId(String.valueOf(this.nextProjectId++));
        this.projects.add(project);
        return 0;
    }

    public int addBid(Bid bid) {
        Project project = this.findProject(bid.getProject().getId());
        User user = this.findUser(bid.getUser().getId());

        if (project == null)
            return -1;
        if (user == null)
            return -2;

        project.addBid(bid);
        return 0;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
