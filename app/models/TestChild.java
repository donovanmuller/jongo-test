package models;

import jongo.JongoCollection;

@JongoCollection("siblings")
public class TestChild extends TestParent {

    public String childStuff;

    public TestChild(String name, String stuff) {

        super(name + "-child");
        this.childStuff = stuff;
    }
}
