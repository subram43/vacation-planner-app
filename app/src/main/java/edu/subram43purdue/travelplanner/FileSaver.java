package edu.subram43purdue.travelplanner;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*
 * Created by subram43 on 12/30/17.
 */

/**
 * This class is used to store our data to external storage. In order for our program to continue without
 * wiping out the memory every time the app is restarted, we need to use java's Object Input and Output
 * capabilities to store the vacations to memory
 */
public class FileSaver {
    private static final String TAG = "edu.subram43purdue.travelplanner.FileSaver";
    /**
     * This variable is the base path of our directory where we can use external storage
     */
    private static String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    /**
     * This variable represents the path where we can find our list of vacations in our directory. This
     * is the base path plus the path to the vacations
     */
    private static String VACATION_PATH = BASE_PATH+"/vacations.dat";

    /**
     * This method reads in an Object from the file path in order to store it to external storage.
     * This is done when the app is closed so that it can read all the memory first before the app is closed
     * @param path The file path which we want to store on external storage
     * @return Object
     */
    private static Object readObject(String path) {
        Object obj = null;
        try{
            /*
            In order to read an object from the path, we must create a file input stream and an
            object input stream to read the object from it. We must also place this in a try-catch
            block since the code could throw an IO Exception or a class not found exception
             */
            FileInputStream fileInputStream = new FileInputStream(new File(path));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            obj = objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * This method writes out the object to the file path from external storage when the app is resumed
     * or restarted, ensuring that none of the memory of our list of vacations is lost.
     * @param obj the object to be written out
     * @param path Path that the object parameter will be written out to
     */
    public static void writeObject(Object obj, String path){
        try {
            /*
            In order to write an object to a designated path, we must create a file output stream and
            an object output stream to write the object to a path. Once again, we must place this in a try-
            catch block as it may throw an IOException
             */
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(obj);


            fileOutputStream.close();
            objectOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method writes our list of vacations to external storage using the vacation path, which
     * is defined as one of this class' fields
     * @param vacationList this is our list of vacations to be written to external storage
     */
    public static void writeVacationList(ArrayList<Vacation> vacationList){
        writeObject(vacationList,VACATION_PATH);
    }


    /**
     * This method reads our vacation list from the vacation path and stores it to external storage
     * @return ArrayList
     */
    public static ArrayList<Vacation> readVacationList() {
        ArrayList<Vacation> vacation = (ArrayList<Vacation>)readObject(VACATION_PATH);
        if(vacation==null)
            vacation = new ArrayList<Vacation>();
        return vacation;
    }
}
