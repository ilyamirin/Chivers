package chivers.dao;

import chivers.models.CompanyResult;
import org.bson.types.ObjectId;

import java.util.List;

public interface CompanyResultDao {

    ObjectId create(CompanyResult companyResult);

    List<CompanyResult> getResultsForCompany(ObjectId companyId);
}
