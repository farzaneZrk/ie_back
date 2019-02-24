package Server;

import Model.ModelController;
import Model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Users implements ViewBuilder{

    @Override
    public void handle(HttpExchange httpExchange, ModelController mc) throws IOException {
        String response = "";
        String[] splittedPath = httpExchange.getRequestURI().getPath().split("/");
        String userId = null;

        if (splittedPath.length == 3)
            userId = splittedPath[2];

        if (userId != null){
            User user = mc.findUser(userId);
            if (user == null) {
                this.give404(httpExchange);
                return;
            }

            //TODO: farzane change this
            response = "name: " + user.getFirstName() + " " + user.getLastName() + "\n" +
                    "job Title: " + user.getJobTitle() + "\n" +
                    "pic url: " + user.getPicURL() + "\n" +
                    "skills: " + user.getSkills().toString() + "\n" +
                    "bio: " + user.getBio() + "\n";
        }
        else {
            this.give404(httpExchange);
            return;
        }


        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
