package chivers.dao;

import chivers.models.Company;
import org.bson.types.ObjectId;

import java.util.List;

public interface CompanyDao {

    Company get(ObjectId id);

    List<Company> all();

    ObjectId create(Company company);
}
