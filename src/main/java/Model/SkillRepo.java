package Model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SkillRepo {
    private static List<String> skillList; // list of available skillList

    static {
        skillList = new ArrayList<>();
        skillList.add("matlab");
        skillList.add("C++");
        skillList.add("C");
        skillList.add("Java");
        skillList.add("python");
        skillList.add("HTML");
        skillList.add("Javascript");

    }

    public static List<String> getSkillList() {
        return skillList;
    }

    public static void addToSkillList(String skills) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(skills);
        Iterator<JsonNode> elements = rootNode.elements();
        while(elements.hasNext()){
            JsonNode skillNodes = elements.next();
            skillList.add(skillNodes.path("name").asText());
        }
//        System.out.println(this.skillList);

    }

}
