package controllers;

import com.mongodb.CommandResult;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import jongo.Jongo;
import models.*;
import org.bson.types.ObjectId;
import org.jongo.Update;
import play.exceptions.UnexpectedException;
import play.mvc.Controller;

import java.io.*;
import java.util.ArrayList;

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

    public static void populateStuff() {

        TestClobber clobber = new TestClobber();
        clobber.stuff.add("1");
        clobber.stuff.add("2");
        clobber.stuff.add("3");

        clobber.save();

        renderText("Created stuff: " + clobber.id);
    }

    public static void addStuff(String id) {

//        TestClobber clobber = TestClobber.findById(id, TestClobber.class);
        TestClobber clobber = new TestClobber();
        clobber = clobber.save();
        try {
            for(int x = 0 ; x < 1000000 ; x++) {
                clobber.stuff.add("This is a stupid string and means nothing: " + x);

                System.out.println("Saved: " + x);
                if(x % 10000 == 0) {
                    WriteResult upsert = Jongo.getCollection("clobber").update(new ObjectId(clobber.id)).with("{$pushAll: {stuff: #}}", clobber.stuff);
                    CommandResult lastError = upsert.getLastError();
                    System.out.println(lastError.getErrorMessage());
                    if(!lastError.ok()) {
                        throw new UnexpectedException("We got ourselves an error: " + lastError.getErrorMessage());
                    }

                    clobber.stuff.clear();
                }
            }
        } catch(UnexpectedException ue) {
            renderText("Error: " + ue.getMessage());
        }

        renderJSON("All done! :)");
    }

    public static void getStuff(String id) {

        TestClobber byId = TestClobber.findById(id, TestClobber.class);

        renderText(byId.stuff.size());
    }
}