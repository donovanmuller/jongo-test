package models;

import jongo.Finder;
import jongo.JongoCollection;
import jongo.JongoModel;

@JongoCollection("test")
public class TestModel extends JongoModel<TestModel> {

    public TestModel(String name) {

        this.name = name;
    }

    public String name;
    public String poo;

    public static Finder<TestModel> findByName(String name) {

       return new Finder<>(collection(TestModel.class).find("{name: #}", name), TestModel.class);
    }

    public static Finder<TestModel> findByPoo(String poo) {

        return new Finder<>(collection(TestModel.class).find("{poo: #}", poo), TestModel.class);
    }
}
