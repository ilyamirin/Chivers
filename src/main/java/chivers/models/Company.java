package chivers.models;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

@Data
public class Company {

    private ObjectId id;
    private String name;
    private Map<String, Integer> words = new HashMap<String, Integer>();
}
