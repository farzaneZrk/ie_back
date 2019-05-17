package Listener;

import Model.ProjectRepo;

public class Auction implements Runnable {
    @Override
    public void run() {
        System.out.println("tend to do auction.");
        ProjectRepo.doAuctionForExpiredProjects();
    }
}


