package chivers.dao;

import chivers.models.Source;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
public class MongoSourceDao implements SourceDao {

    private com.mongodb.DB db;
    private Random random = new Random();

    public void init() {
        try {
            MongoClient mongoClient = new MongoClient();
            db = mongoClient.getDB("chivers");
        } catch (Exception ex) {
        }
    }
    @Override
    public List<Source> lastVisited(int limit) {
        BasicDBObject sortObject = new BasicDBObject("lastVisitedAt", 1);
        DBCursor cursor = db.getCollection("sources").find().sort(sortObject).limit(limit);
        List<Source> result = new ArrayList<Source>(cursor.size());
        while (cursor.hasNext()) {
            DBObject sourceObject = cursor.next();
            Source source = new Source();
            source.setId((ObjectId) sourceObject.get("_id"));
            source.setName((String) sourceObject.get("name"));
            source.setType(Source.Type.valueOf((String) sourceObject.get("type")));
            source.setLastVisitedAt((Date) sourceObject.get("lastVisitedAt"));
            result.add(source);
        }
        return result;
    }

    @Override
    public Source getRandom() {
        int randomPosition = random.nextInt((int) db.getCollection("sources").count());
        DBObject sourceObject = db.getCollection("sources").find().limit(-1).skip(randomPosition).next();
        Source source = new Source();
        source.setId((ObjectId) sourceObject.get("_id"));
        source.setName((String) sourceObject.get("name"));
        source.setType(Source.Type.valueOf((String) sourceObject.get("type")));
        source.setLastVisitedAt((Date) sourceObject.get("lastVisitedAt"));
        return source;
    }

    @Override
    public boolean contains(Source source) {
        BasicDBObject findObject = new BasicDBObject()
                .append("name", source.getName())
                .append("type", source.getType().name());
        return db.getCollection("sources").count(findObject) > 0;
    }

    @Override
    public ObjectId getId(Source source) {
        BasicDBObject findObject = new BasicDBObject()
                .append("name", source.getName())
                .append("type", source.getType().name());
        return (ObjectId) db.getCollection("sources").findOne(findObject).get("_id");
    }

    @Override
    public ObjectId create(Source source) {
        BasicDBObject sourceObject = new BasicDBObject()
                .append("name", source.getName())
                .append("type", source.getType().name())
                .append("lastVisitedAt", source.getLastVisitedAt());
        db.getCollection("sources").insert(sourceObject);
        source.setId((ObjectId) sourceObject.get("_id"));
        log.info("Source was added: " + source);
        return source.getId();
    }

    @Override
    public void updateLastVisitedToCurrent(Source source) {
        source.setLastVisitedAt(new Date());
        BasicDBObject findObject = new BasicDBObject("_id", source.getId());
        BasicDBObject updateObject = new BasicDBObject("$set", new BasicDBObject("lastVisitedAt", source.getLastVisitedAt()));
        db.getCollection("sources").update(findObject, updateObject);
    }

    @Override
    public void addContacts(Source source, List<ObjectId> contactsIds) {
        BasicDBObject findObject = new BasicDBObject("_id", source.getId());
        BasicDBObject contactsObject = new BasicDBObject("$each", contactsIds);
        BasicDBObject addToSetObject = new BasicDBObject("contacts", contactsObject);
        BasicDBObject updateObject = new BasicDBObject("$addToSet", addToSetObject);
        db.getCollection("sources").update(findObject, updateObject);
    }

    @Override
    public void markAsIncorrect(Source source) {
        BasicDBObject findObject = new BasicDBObject("_id", source.getId());
        BasicDBObject updateObject = new BasicDBObject("$set", new BasicDBObject("incorrect", true));
        db.getCollection("sources").update(findObject, updateObject);
    }

}
