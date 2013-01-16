package models;

import jongo.JongoCollection;
import jongo.JongoModel;

import java.util.ArrayList;
import java.util.List;

@JongoCollection("clobber")
public class TestClobber extends JongoModel<TestClobber> {

    public List<String> stuff = new ArrayList<>();
}
