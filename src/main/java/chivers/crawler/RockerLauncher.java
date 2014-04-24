package chivers.crawler;

import chivers.dao.MongoSourceDao;
import chivers.dao.SourceDao;

public class RockerLauncher {

    public static void main(String... args) {
        SourceDao sourceDao = new MongoSourceDao();
        Rocker rocker = new Rocker();
        //rocker.setSourceDao(sourceDao);
        new Thread(rocker).start();
    }
}
