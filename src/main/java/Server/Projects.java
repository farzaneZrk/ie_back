package Server;

import Model.ModelController;
import Model.Project;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileInputStream;
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
            String dynamicData = "";
            System.out.println("i am here");
            for (Project project : mc.getProjects()) {
                dynamicData += "\t\t<tr>\n\t\t\t<td>";
                dynamicData += project.getId();
                dynamicData += "</td>\n\t\t\t<td>";
                dynamicData += project.getTitle();
                dynamicData += "</td>\n\t\t\t<td>";
                dynamicData += project.getBudget();
                dynamicData += "</td>\n\t\t</tr>\n";
//                response += project.getTitle() + "\n\n";
            }
            response = mergeStaticAndDynamicResponse(dynamicData, "templates/projects.html");
            System.out.println("i am out of there\n" + response);
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

    private String mergeStaticAndDynamicResponse(String dynamicData, String tHTMLPath) {
        StringBuilder template = new StringBuilder();
        try {
            File file = new File(tHTMLPath);
            FileInputStream fis = new FileInputStream(file);
            char current;
            boolean fCurlyBrace = false;
            while (fis.available() > 0) {
                current = (char) fis.read();
                if (current == '{') fCurlyBrace = true;
                else if(current == '*' && fCurlyBrace){
                    template.append(dynamicData);
                    fCurlyBrace = false;
                } else if(fCurlyBrace) {
                    fCurlyBrace = false;
                    template.append('{').append(current);
                } else
                    template.append(current);
            }
        } catch (IOException e) {
            return "notFoundTemplate()";
        }
        return template.toString();
    }
}
