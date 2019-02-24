package Server;

import Model.ModelController;
import Model.Project;
import Model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Projects implements ViewBuilder{
    @Override
    public void handle(HttpExchange httpExchange, ModelController mc) throws IOException {
        String response = "";
        String dynamicData = "";
        String projectId = null;

        User user = mc.findUser("1");

        String[] parts = httpExchange.getRequestURI().getPath().split("/");
        //System.out.println("parts: "+  parts[0]+ "," + parts[1]);
        if (parts.length == 2)
            projectId = null;
        else if (parts.length == 3)
            projectId = parts[2];

        if (projectId == null) {
            for (Project project : mc.getProjects()) {
                if(project.checkUserForProject(user) && !project.isExpired()) {
                    dynamicData += "\t\t<tr>\n\t\t\t<td>";
                    dynamicData += project.getId();
                    dynamicData += "</td>\n\t\t\t<td>";
                    dynamicData += project.getTitle();
                    dynamicData += "</td>\n\t\t\t<td>";
                    dynamicData += project.getBudget();
                    dynamicData += "</td>\n\t\t</tr>\n";
//                break;
//                response += project.getTitle() + "\n\n";
                }
            }
            if (dynamicData == "")
                dynamicData += "\t\t<tr>\n\t\t\t<td>...</td>\n\t\t\t<td>...</td>\n\t\t\t<td>...</td>\n\t\t</tr>\n";
            response = mergeStaticAndDynamicResponse(dynamicData, "templates/projects.html");
//            System.out.println(response);
        }
        else {
            Project thisProject = mc.findProject(projectId);
            //TODO: check for project req and also handle login
            if (thisProject != null) {
                if(thisProject.checkUserForProject(user)) {
                    dynamicData += "\t\t<li>id: ";
                    dynamicData += thisProject.getId();
                    dynamicData += "</li>\n\t\t<li>title: ";
                    dynamicData += thisProject.getTitle();
                    dynamicData += "</li>\n\t\t<li>description: ";
                    dynamicData += thisProject.getDescp();
                    dynamicData += "</li>\n\t\t<li>imageUrl: <img src=\"";
                    dynamicData += thisProject.getPicURL();
                    dynamicData += "\" style=\"width: 150px; height: 150px;\"></li>\n\t\t<li>budget: ";
                    dynamicData += thisProject.getBudget();
                    dynamicData += "</li>\n";
                }else
                    dynamicData = "<h4>This Project is not available for you!</h4>";
                if (thisProject.isExpired())
                    dynamicData = "<h4>This Project has been expired!</h4>";
                response = mergeStaticAndDynamicResponse(dynamicData, "templates/project-single.html");
//                System.out.println(response);
            }
            else {
                this.give404(httpExchange);
                return;
            }
        }
        byte[] bs = response.getBytes("UTF-8");
        httpExchange.sendResponseHeaders(200, bs.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(bs);
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
