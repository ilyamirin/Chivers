package chivers.dao;

import chivers.models.Company;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import static chivers.mongo.MongoDBHelper.*;
import java.util.ArrayList;
import java.util.List;

public class MongoCompanyDao implements CompanyDao {

    @Override
    public Company get(ObjectId id) {
        return null;
    }

    @Override
    public List<Company> all() {
        List<Company> companies = new ArrayList<Company>();
        DBCursor cursor = collection("companies").find();
        while (cursor.hasNext()) {
            DBObject companyObject = cursor.next();
            Company company = deserialize(companyObject, Company.class);
            companies.add(company);
        }
        return companies;
    }

    @Override
    public ObjectId create(Company company) {
        BasicDBObject companyObject = new BasicDBObject()
                .append("name", company.getName())
                .append("threshold", company.getThreshold())
                .append("words", company.getWords());
        collection("companies").insert(companyObject);
        return (ObjectId) companyObject.get("_id");
    }
}
