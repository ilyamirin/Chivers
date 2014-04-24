package chivers.crawler;

import chivers.dao.*;
import chivers.models.Company;
import chivers.models.CompanyResult;

import java.util.HashMap;
import java.util.Map;

public class RockerLauncher {

    public static void main(String... args) {
        CompanyDao companyDao = new MongoCompanyDao();

        CompanyResultDao companyResultDao = new MongoCompanyResultDao();
        SourceDao sourceDao = new MongoSourceDao();

        Rocker rocker = new Rocker();
        rocker.setSourceDao(sourceDao);
        rocker.setCompanyResultDao(companyResultDao);
        rocker.setCompanyDao(companyDao);

        new Thread(rocker).start();
    }
}
