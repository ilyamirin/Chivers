package chivers.crawler;

import chivers.dao.MongoSourceDao;

public class CrawlerLauncher {

    public static void main(String... args) {
        MongoSourceDao sourceDao = new MongoSourceDao();
        sourceDao.init();
        LJExplorer LJExplorer = new LJExplorer();
        LJExplorer.setSourceDao(sourceDao);
        new Thread(LJExplorer).start();
        new Thread(LJExplorer).start();
        new Thread(LJExplorer).start();
    }
}
