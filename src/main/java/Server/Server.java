package Server;

import Model.SkillRepo;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.StringTokenizer;

import static Model.ProjectRepo.addToProjectList;

public class Server {
    public SkillRepo mc;

    public Server(){
        mc = new SkillRepo();
        try {
            setUpProjectlist();
            setUpSkillList();
            System.out.println("server is ready...");
        } catch (IOException e) {
            System.out.println("can't connect to server 142.93.134.194:8000 to get data.");
        }
    }

    private void setUpSkillList() throws IOException {
        String skillsJson = getDataFromServer("http://142.93.134.194:8000/joboonja/skill/");
        mc.addToSkillList(skillsJson);
        System.out.println(mc.getSkillList());
    }

    private void setUpProjectlist() throws IOException {
        String projectsJson = getDataFromServer("http://142.93.134.194:8000/joboonja/project/");
        addToProjectList(projectsJson);
    }

    private static String getDataFromServer(String urlPath) throws IOException {
//        StringBuilder response = new StringBuilder();
//        String inputLine;
//        URL project = new URL(urlPath);
//        URLConnection pc = project.openConnection();
//        BufferedReader in = new BufferedReader( new InputStreamReader(pc.getInputStream()));
//
//        while ((inputLine = in.readLine()) != null)
//            response.append(inputLine);
//
//        in.close();
//        return response.toString();
        return "";
    }

    public void startServer() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    public SkillRepo getMC(){
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

    public static void main(String[] args) throws URISyntaxException, IOException {
//        Server server = new Server();
//        //
//        SkillRepo mc = server.getMC();
//        List<Skill> skills = new ArrayList<>();
//        skills.add(new Skill("HTML", 5));
//        skills.add(new Skill("Javascript", 4));
//        skills.add(new Skill("C++", 2));
//        skills.add(new Skill("Java", 3));
//        User u1 = new User("1", "علی", "شریف زاده", "برنامه نویس وب", "...", skills, "روی سنگ قبرم بنویسید: خدا بیامرز میخواست خیلی کارا بکنه ولی پول نداشت");
////        Project p1 = new Project("10", "job", "descp", "pic", new ArrayList<Skill>(skills), 10, 200);
////        Project p2 = new Project("20", "job2", "descp2", "pic2", skills, 20, 300);
//
////        mc.addUser(u1);
//
////        mc.addProject(p1);
////        skills.add(new Skill("css", 20));
////        mc.addProject(p2);
////
////        mc.addBid(new Bid(100, p1, u1));
////        mc.addBid(new Bid(200, p1, u2));
//
//        try {
//            server.startServer();
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage() + " hey !");
//        }

        String skillsJson = getDataFromServer("http://142.93.134.194:8000/joboonja/skill/");
        JSONArray jsonarray = new JSONArray(skillsJson);
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String name = jsonobject.getString("name");
            System.out.println(name);
        }





////        URI uri = new URI("http://142.93.134.194:8000/joboonja/skill/");
////        JSONTokener tokener = new JSONTokener("{\nskills:" + uri.toURL().openStream() + "\n}");
////        Object json = new JSONTokener(skillsJson).nextValue();
////        System.out.println(json.toString());
////        System.out.println(tokener);
//        JSONObject root = new JSONObject("{\nskills:" + skillsJson  + "\n}");
////        JSONArray names = root.names();
////        System.out.println(names);
////        System.out.println(names.getString(1));
//        System.out.println(root);
//
//        Map<String, Object> map = new HashMap<>();
//
//        String[] fields = JSONObject.getNames("{\nskills:" + skillsJson  + "\n}");
//        System.out.println(fields);
////
//        for (String field : fields) {
//            System.out.println(field);
////            Object entry = object.get(field);
////
////            if (entry instanceof JSONObject) {
////                map.put(field, toMap((JSONObject) entry));
////            } else if (entry instanceof JSONArray) {
////                map.put(field, toCollection((JSONArray) entry));
////            } else {
////                map.put(field, entry);
////            }
//        }
    }
}
