package chivers.crawler;

import chivers.dao.SourceDao;
import chivers.models.Source;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.util.Random;

@Slf4j
public class Rocker implements Runnable {

    private Random random = new Random();

    @Setter
    private SourceDao sourceDao;

    @Override
    public void run() {
        while (1==1) {
            for (Source source : sourceDao.lastVisited(10)) {
                try {
                    Thread.sleep(random.nextInt(5) * 1000);

                    Document doc = Jsoup.connect("http://" + source.getName() + ".livejournal.com/").get();

                    for (Element element : doc.select(".entry")) {
                        element.text();
                    }

                } catch (Exception e) {
                    log.error("Oops!", e);
                }

            }
        }//while
    }//run
}
