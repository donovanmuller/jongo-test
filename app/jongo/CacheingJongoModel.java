package jongo;

import play.Logger;
import play.cache.Cache;

import java.io.Serializable;

import static org.jongo.Oid.withOid;

public class CacheingJongoModel<T extends CacheingJongoModel> extends JongoModel<T> implements Serializable {

    @Override
    public T save() {

        Cache.delete(this.id);
        T model = super.save();
        Cache.set(model.id, model);

        return model;
    }

    public static <M extends JongoModel> M findById(String id, Class<M> clazz) {

        M model = Cache.get(id, clazz);
        if(model == null) {
            Logger.info("Model[%s | Id: %s] not cached, fetching from mongo", clazz, id);
            try {
                String collection = clazz.getAnnotation(JongoCollection.class).value();

                model = Jongo.getCollection(collection).findOne(withOid(id)).as(clazz);
                Cache.set(id, model);
                Logger.info("Model[%s | Id: %s] fetched and cached", clazz, id);
            } catch (Exception e) {
                Logger.error(e, "Could not find model[%s] by id: %s", clazz.getName(), id);
            }
        }

        return model;
    }
}
