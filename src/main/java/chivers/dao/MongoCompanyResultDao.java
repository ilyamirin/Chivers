package chivers.dao;

import chivers.models.CompanyResult;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import static chivers.mongo.MongoDBHelper.*;
import java.util.List;

public class MongoCompanyResultDao implements CompanyResultDao {

    @Override
    public ObjectId create(CompanyResult companyResult) {
        BasicDBObject companyResultObject = new BasicDBObject();
        companyResultObject.append("foundAt", companyResult.getFoundAt());
        companyResultObject.append("companyId", companyResult.getCompanyId());
        companyResultObject.append("sourceId", companyResult.getSourceId());
        companyResultObject.append("url", companyResult.getUrl());

        collection("companiesResults").insert(companyResultObject);

        return (ObjectId) companyResultObject.get("_id");
    }

    @Override
    public List<CompanyResult> getResultsForCompany(ObjectId companyId) {
        return null;
    }
}
