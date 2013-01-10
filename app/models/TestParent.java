package models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jongo.JongoCollection;
import jongo.JongoModel;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "_class")
@JongoCollection("siblings")
public class TestParent extends JongoModel {

    public String name = "parent-";

    public TestParent(String name) {

        this.name += name;
    }
}
