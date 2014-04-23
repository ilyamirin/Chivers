package chivers.dao;

import chivers.models.Source;
import org.bson.types.ObjectId;

import java.util.List;

public interface SourceDao {

    List<Source> lastVisited(int limit);

    Source getRandom();

    boolean contains(Source source);

    ObjectId getId(Source source);

    ObjectId create(Source source);

    void updateLastVisitedToCurrent(Source source);

    void addContacts(Source source, List<ObjectId> contactsIds);

    void markAsIncorrect(Source source);
}
