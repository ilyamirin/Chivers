package chivers.crawler;

import chivers.dao.CompanyDao;
import chivers.dao.CompanyResultDao;
import chivers.dao.SourceDao;
import chivers.models.Company;
import chivers.models.CompanyResult;
import chivers.models.Source;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
public class Rocker implements Runnable {

    private Random random = new Random();

    @Setter
    private SourceDao sourceDao;

    @Setter
    private CompanyDao companyDao;

    @Setter
    private CompanyResultDao companyResultDao;

    @Override
    public void run() {
        while (1 == 1) {
            List<Company> companies = companyDao.all();

            for (Source source : sourceDao.lastVisited(10)) {
                try {
                    String url = "http://" + source.getName() + ".livejournal.com/";
                    log.info("Connect to " + url);

                    Document doc = Jsoup.connect(url).get();
                    String text = doc.text();

                    for (Company company : companies) {
                        long result = 0;

                        for (Map.Entry<String, Integer> entry : company.getWords().entrySet()) {
                            String word = entry.getKey();
                            Integer weight = entry.getValue();

                            if (text.contains(word)) {
                                result += weight;
                            }
                        }

                        if (result > company.getThreshold()) {
                            CompanyResult companyResult = new CompanyResult();
                            companyResult.setCompanyId(company.getId());
                            companyResult.setFoundAt(new Date());
                            companyResult.setSourceId(source.getId());
                            companyResult.setUrl(url);

                            log.info("Company result was found: " + companyResult);

                            companyResultDao.create(companyResult);
                        }
                    }

                    Thread.sleep(random.nextInt(2) * 1000);

                } catch (Exception e) {
                    log.error("Oops!", e);
                }

                sourceDao.updateLastVisitedToCurrent(source);

            }//for source
        }//while
    }//run
}
