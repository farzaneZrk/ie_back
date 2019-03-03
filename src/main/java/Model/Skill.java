package Model;

import java.util.ArrayList;
import java.util.List;

public class Skill {
    private String name;
    private int point;
    private List<String> endorserList;

    public Skill(String name, int point) {
        this.name = name;
        this.point = point;
        this.endorserList = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }

    public List<String> getEndorsers() {
        return endorserList;
    }

    public void setEndorsers(List<String> endorsers) {
        this.endorserList = endorsers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void addEndorser(String endorser){
        endorserList.add(endorser);
    }

    public void incrementPoint(){
        this.point += 1;
    }

    public void endorse(String endorser){
        addEndorser(endorser);
        incrementPoint();
    }

    public boolean hasEndorsed(String id){
        return endorserList.contains(id);
    }

    @Override
    public String toString() {
        return this.name + ":" + String.valueOf(this.point);
    }
}
