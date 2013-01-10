package models;

import jongo.CacheingJongoModel;
import jongo.JongoCollection;

@JongoCollection("test-cache")
public class TestCacheableModel extends CacheingJongoModel {

    public String name;

    public TestCacheableModel(String name) {

        this.name = name;
    }
}
