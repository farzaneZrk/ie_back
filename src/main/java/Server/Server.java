package Server;

import Model.*;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Server {
    public ModelController mc;

    public Server(){
        mc = new ModelController();
        try {
            setUpProjectlist();
            setUpSkillList();
        } catch (IOException e) {
            System.out.println("can't connect to server 142.93.134.194:8000 to get data.");
        }
    }

    private void setUpSkillList() throws IOException {
        String skillsJson = getDataFromServer("http://142.93.134.194:8000/joboonja/skill/");
        mc.addToSkillList(skillsJson);
    }

    private void setUpProjectlist() throws IOException {
        String projectsJson = getDataFromServer("http://142.93.134.194:8000/joboonja/project/");
        mc.addToProjectList(projectsJson);
    }

    private String getDataFromServer(String urlPath) throws IOException {
        StringBuilder response = new StringBuilder();
        String inputLine;
        URL project = new URL(urlPath);
        URLConnection pc = project.openConnection();
        BufferedReader in = new BufferedReader( new InputStreamReader(pc.getInputStream()));

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();
        return response.toString();
    }

    public void startServer() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    public ModelController getMC(){
        return mc;
    }

    class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println(httpExchange.getRequestURI().getPath());
            StringTokenizer tokenizer = new StringTokenizer(httpExchange.getRequestURI().getPath(), "/");
            String page = "";
            String capPlusS = "";
            Class<ViewBuilder> pageClass;
            try {
                page = tokenizer.nextToken();
                capPlusS = page.substring(0, 1).toUpperCase() + page.substring(1) + 's';
                System.out.println("Server." + capPlusS);
            }
            catch (Exception e){
                System.out.println(e.getMessage().toString());
            }
            try {
                pageClass = (Class<ViewBuilder>) Class.forName("Server." + capPlusS);
                ViewBuilder newInstance = pageClass.getDeclaredConstructor().newInstance();
                newInstance.handle(httpExchange, mc);
            } catch (ClassNotFoundException |
                    InstantiationException |
                    IllegalAccessException |
                    IllegalArgumentException |
                    InvocationTargetException |
                    NoSuchMethodException |
                    SecurityException e) {
                e.printStackTrace();
                String response =
                        "<html>"
                                + "<body>Page \""+ page + "\" not found.</body>"
                                + "</html>";
                httpExchange.sendResponseHeaders(404, response.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        //
        ModelController mc = server.getMC();
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill("http", 10));
        User u1 = new User("10", "omid", "amini", "job", "pic", "bio");
        User u2 = new User("20", "farzane", "zirak!", "job2", "pic2", "bio2");
        Project p1 = new Project("10", "job", "descp", "pic", new ArrayList<Skill>(skills), 10, 200);
        Project p2 = new Project("20", "job2", "descp2", "pic2", skills, 20, 300);

        mc.addUser(u1);
        mc.addUser(u2);

        mc.addProject(p1);
        skills.add(new Skill("css", 20));
        mc.addProject(p2);

        mc.addBid(new Bid(100, p1, u1));
        mc.addBid(new Bid(200, p1, u2));

        try {
            server.startServer();
        }
        catch (Exception e){
            System.out.println(e.getMessage() + " hey !");
        }
    }
}
