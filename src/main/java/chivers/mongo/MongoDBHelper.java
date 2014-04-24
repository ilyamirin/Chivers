package chivers.mongo;

import chivers.models.Source;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
public class MongoDBHelper {

    private static DB db;

    static {
        try {
            MongoClient mongoClient = new MongoClient();
            db = mongoClient.getDB("chivers");
        } catch (Exception ex) {
            log.error("Oops!", ex);
        }
    }

    public static DBCollection collection(String name) {
        return db.getCollection(name);
    }

    public static <T> T deserialize(DBObject object, Class<T> claz) {
        try {
            T result = claz.newInstance();

            for (Method method : claz.getMethods()) {
                if (method.getName().startsWith("set")) {
                    if (method.getName().equals("setId")) {
                        method.invoke(result, object.get("_id"));

                    } else if (method.getParameterTypes()[0].isEnum()) {
                        String value = (String) object.get(method.getName().substring(3).toLowerCase());
                        method.invoke(result, Source.Type.valueOf(value));

                    } else {
                        Object value = object.get(method.getName().substring(3).toLowerCase());
                        method.invoke(result, method.getParameterTypes()[0].cast(value));
                    }
                }//if
            }

            return result;

        } catch (Exception e) {
            log.error("Oops!", e);
            return null;
        }
    }
}
