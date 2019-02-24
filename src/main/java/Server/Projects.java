package Server;

import Model.ModelController;
import Model.Project;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Projects implements ViewBuilder{
    @Override
    public void handle(HttpExchange httpExchange, ModelController mc) throws IOException {
        String response = "";
        String projectId = null;

        String[] parts = httpExchange.getRequestURI().getPath().split("/");
        //System.out.println("parts: "+  parts[0]+ "," + parts[1]);
        if (parts.length == 2)
            projectId = null;
        else if (parts.length == 3)
            projectId = parts[2];

        if (projectId == null) {
            for (Project project : mc.getProjects())
                response += project.getTitle() + "\n";
        }
        else {
            Project thisProject = mc.findProject(projectId);
            //TODO: check for project req and also handle login
            if (thisProject != null) {
                response += "title: " + thisProject.getTitle() + "\n" +
                        "skills: " + thisProject.getSkills().toString() + "\n" +
                        "bids: " + thisProject.getBids().toString() + "\n" +
                        "budget: " + thisProject.getBudget() + "\n" +
                        "description: " + thisProject.getDescp() + "\n" +
                        "pic url: " + thisProject.getPicURL() + "\n" +
                        "deadline: " + thisProject.getDeadline() + "\n";
            }
            else {
                this.give404(httpExchange);
                return;
            }
        }

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
