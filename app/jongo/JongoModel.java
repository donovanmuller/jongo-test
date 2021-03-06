package jongo;

import com.mongodb.CommandResult;
import com.mongodb.WriteResult;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.Update;
import org.jongo.marshall.jackson.id.Id;
import play.Logger;
import play.exceptions.UnexpectedException;

import static org.jongo.Oid.withOid;

public abstract class JongoModel<T> {

    @Id
    public String id;

    public static <M extends JongoModel> M findById(String id, Class<M> clazz) {

        M model = null;
        try {
            String collection = clazz.getAnnotation(JongoCollection.class).value();

            model = Jongo.getCollection(collection).findOne(withOid(id)).as(clazz);
        } catch (Exception e) {
            Logger.error(e, "Could not find model[%s] by id: %s", clazz.getName(), id);
        }

        return model;
    }

    public T save() {

        WriteResult result = collection(this.getClass()).save(this);
//        CommandResult lastError = result.getLastError();
//        System.out.println(lastError.getErrorMessage());
//        if(!lastError.ok()) {
//            throw new UnexpectedException("We got ourselves an error: " + lastError.getErrorMessage());
//        }

        return (T) this;
    }


    public T saveOrUpdate() {

        return (T) this;
    }

    protected static <M extends JongoModel> MongoCollection collection(Class<M> clazz) {

        return Jongo.getCollection(clazz.getAnnotation(JongoCollection.class).value());
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
