package Server;

import Model.ModelController;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface ViewBuilder {
    public void handle(HttpExchange httpExchange, ModelController mc) throws IOException;
}
