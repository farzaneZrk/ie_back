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
        for (Project project: mc.getProjects())
            response += project.getTitle() + "\n" ;

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
