package controllers;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import jongo.Jongo;
import models.TestCacheableModel;
import models.TestChild;
import models.TestModel;
import models.TestParent;
import org.bson.types.ObjectId;
import play.mvc.Controller;

import java.io.*;

public abstract class Application extends Controller {

    public static void index(String name) {

        TestModel model = new TestModel(name);
        model.poo = "YAY!";
        model = model.save();

        System.out.println("Saved Id: " + model.id);


        renderText(model.name);
    }

    public static void get(String id) {

        renderText(TestModel.findById(id, TestModel.class));
    }

    public static void name(String name) {

        renderText(TestModel.findByPoo(name).first());
    }

    public static void saveCache(String name) {

        renderText(new TestCacheableModel(name).save());
    }

    public static void getCache(String id) {

        renderText(TestCacheableModel.findById(id, TestCacheableModel.class));
    }

    public static void saveChild(String name) {

        renderText(new TestChild(name, "stuff" + session.getId()).save());
    }


    public static void saveParent(String name) {

        renderText(new TestParent(name).save());
    }

    public static void getChild(String id) {

        renderText(((TestParent) TestChild.findById(id, TestChild.class)).name);
    }

    public static void getChildParent(String id) {

        TestParent parent = TestParent.findById(id, TestParent.class);
        System.out.println("Instanceof: " + parent);

        renderText(((TestChild) TestParent.findById(id, TestParent.class)).childStuff);
    }

    public static void upload() {

        render();
    }

    public static void saveFile(File file) {

        try {
            GridFSInputFile inputFile = Jongo.gridfs().createFile(file);
            inputFile.save();
            renderText("Saved!" + inputFile);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void getFile(String id) {

        try {
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            OutputStream stream = new BufferedOutputStream(arrayOutputStream);

            GridFSDBFile fsdbFile = Jongo.gridfs().findOne(new ObjectId(id));
            fsdbFile.writeTo(stream);

            stream.flush();
            stream.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
            renderBinary(inputStream, fsdbFile.getFilename(), "text/csv", true);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}