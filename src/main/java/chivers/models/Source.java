package chivers.models;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Data
public class Source {

    public enum Type {
        LJ, TWITTER, FACEBOOK, VK;
    }

    private ObjectId id;
    private Type type;
    private String name;
    private Date lastVisitedAt;
    private List<ObjectId> contacts;
}
