package com.mark.seabattle.io;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class FileIO {

    public void save(Context context, StoreObject storeObj, String filename) throws IOException {
        FileOutputStream fileStream = context.openFileOutput(filename,Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fileStream);
        os.writeObject(storeObj);
        os.close();
    }

    public StoreObject load (Context context,String name) throws IOException, ClassNotFoundException {
        StoreObject loadObj;
        //InputStream fileStream = new FileInputStream(file);
        FileInputStream fileStream = context.openFileInput(name);
        ObjectInputStream os = new ObjectInputStream(fileStream);
        loadObj = (StoreObject) os.readObject();
        os.close();
        return loadObj;
    }
}
