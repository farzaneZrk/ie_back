package Server;

import Model.SkillController;
import Model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static Model.UserController.findUser;

public class Users implements ViewBuilder{

    @Override
    public void handle(HttpExchange httpExchange, SkillController mc) throws IOException {
        String response;
        String dynamicData = "";
        String[] splittedPath = httpExchange.getRequestURI().getPath().split("/");
        String userId = null;

        if (splittedPath.length == 3)
            userId = splittedPath[2];

        if (userId != null){
            User user = findUser(userId);
            if (user == null) {
                this.give404(httpExchange);
                return;
            }

            dynamicData += "\t\t<li>id: ";
            dynamicData += user.getId();
            dynamicData += "</li>\n\t\t<li>first name: ";
            dynamicData += user.getFirstName();
            dynamicData += "</li>\n\t\t<li>last name: ";
            dynamicData += user.getLastName();
            dynamicData += "</li>\n\t\t<li>job title: ";
            dynamicData += user.getJobTitle();
            dynamicData += "</li>\n\t\t<li>bio: ";
            dynamicData += user.getBio();
            dynamicData += "</li>\n";
            response = mergeStaticAndDynamicResponse(dynamicData, "templates/user.html");
//            System.out.println(response);
        }
        else {
            this.give404(httpExchange);
            return;
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
