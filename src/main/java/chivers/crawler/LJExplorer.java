package chivers.crawler;

import chivers.dao.SourceDao;
import chivers.models.Source;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LJExplorer implements Runnable {

    @Setter
    private SourceDao sourceDao;

    @Override
    public void run() {
        while (1 == 1) {
            Source source = sourceDao.getRandom();
            String profileUrl = String.format("http://users.livejournal.com/%s/profile", source.getName());
            try {
                Document doc = Jsoup.connect(profileUrl).get();
                String[] splitNames = doc.select(".b-tabs-content").text().split(",\\s?");
                List<ObjectId> newSourcesIds = new ArrayList<ObjectId>();
                for (String name : splitNames) {
                    Source newSource = new Source();
                    newSource.setName(name.replaceAll("lj_maintenance", "").replaceAll("…", "").trim()); //WTF? …
                    newSource.setLastVisitedAt(null);
                    newSource.setType(Source.Type.LJ);
                    if (!sourceDao.contains(newSource)) {
                        newSourcesIds.add(sourceDao.create(newSource));
                    } else {
                        newSourcesIds.add(sourceDao.getId(newSource));
                    }
                }
                sourceDao.addContacts(source, newSourcesIds);
            } catch (IOException e) {
                log.error("Oops!", e);
                sourceDao.markAsIncorrect(source);
            }
            sourceDao.updateLastVisitedToCurrent(source);
        }
    }//run
}
