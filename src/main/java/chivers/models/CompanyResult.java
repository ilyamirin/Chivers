package chivers.models;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
public class CompanyResult {

    private ObjectId id;
    private ObjectId companyId;
    private ObjectId sourceId;
    private String url;
    private Date foundAt;
}
