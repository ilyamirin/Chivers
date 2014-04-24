package chivers.models;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
public class Message {

    private ObjectId id;
    private Date publishedAt;
    private Date parsedAt;
    private String text;
    private ObjectId author;
}
