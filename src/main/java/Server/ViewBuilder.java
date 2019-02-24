package Server;

import Model.ModelController;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public interface ViewBuilder {
    public void handle(HttpExchange httpExchange, ModelController mc) throws IOException;
    default public void give404(HttpExchange httpExchange) throws IOException{
        String response =
                "<html>"
                        + "<body>Page " + httpExchange.getRequestURI().toString() + " not found.</body>"
                        + "</html>";
        httpExchange.sendResponseHeaders(404, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
