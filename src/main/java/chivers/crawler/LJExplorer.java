package chivers.crawler;

import chivers.dao.SourceDao;
import chivers.models.Source;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
public class LJExplorer implements Runnable {

    @Setter
    private SourceDao sourceDao;

    @Override
    public void run() {
        Random random = new Random();

        while (1 == 1) {
            Source source = sourceDao.getRandom();
            String profileUrl = String.format("http://users.livejournal.com/%s/profile", source.getName());

            try {
                Thread.sleep(random.nextInt(5) * 1000);

                Document doc = Jsoup.connect(profileUrl).get();

                String[] splitNames = doc.select(".b-tabs-content").text().split(",\\s?");
                log.info(doc.select(".b-tabs-content").text());

                List<ObjectId> newSourcesIds = new ArrayList<ObjectId>();
                for (String name : splitNames) {
                    Source newSource = new Source();
                    newSource.setName(name.replaceAll("lj_maintenance", "").replaceAll("…", "").trim()); //WTF? …
                    newSource.setLastVisitedAt(null);
                    newSource.setType(Source.Type.LJ);
                    newSource.setFoundAt(new Date());

                    if (!sourceDao.contains(newSource)) {
                        newSourcesIds.add(sourceDao.create(newSource));
                    } else {
                        newSourcesIds.add(sourceDao.getId(newSource));
                    }
                }
                sourceDao.addContacts(source, newSourcesIds);

            } catch (Exception e) {
                log.error("Oops!", e);
                sourceDao.markAsIncorrect(source);

            }

        }//while
    }//run
}
