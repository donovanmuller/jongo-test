package jongo;

import models.TestModel;
import org.jongo.Find;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Finder<T> {

    private Class<TestModel> clazz;
    private Find find;

    public Finder(Find find, Class<TestModel> clazz) {

        this.find = find;
        this.clazz = clazz;
    }

    public List<T> all() {

        return toList((Iterator<T>) find.as(clazz).iterator());
    }

    public List<T> max(int max) {

        return toList((Iterator<T>) find.limit(max).as(clazz).iterator());
    }


    public List<T> from(int start, int limit) {

        return toList((Iterator<T>) find.skip(start).limit(limit).as(clazz).iterator());
    }

    public T first() {

        T model = null;
        List<T> models = toList((Iterator<T>) find.limit(1).as(clazz).iterator());
        if(!models.isEmpty()) {
            model = models.get(0);
        }

        return model;
    }

    private List<T> toList(Iterator<T> iterator) {

        List<T> models = new ArrayList<>();
        if (iterator != null && iterator.hasNext()) {
            while (iterator.hasNext()) {
                models.add(iterator.next());
            }
        }

        return models;
    }

}
