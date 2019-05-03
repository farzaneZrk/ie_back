package Listener;

import Model.ProjectRepo;
import Model.SkillRepo;

import java.io.IOException;

public class FiveMinJobTask implements Runnable {
    @Override
    public void run() {
        // Do your hourly job here.
        System.out.println("Projects have been updated after 5 minutes !\n");
        String skillsJson = null;
        try {
            ProjectRepo.setUpProjectlist();
            System.out.println("get initial data successfully");
        } catch (IOException e) {
            System.out.println("!!!!can't connect to server 142.93.134.194:8000 to get data!!!!");
        }
    }
}
