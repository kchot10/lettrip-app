package com.cookandroid.travelerapplication.helper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileHelper {

    private Context context;

    public FileHelper(Context context) {
        this.context = context;
    }

    public void writeToFile(String fileName, String fileContents) {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readFromFile(String fileName) {
        FileInputStream inputStream;

        try {
            inputStream = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String fileContents = sb.toString();
            inputStream.close();
            return fileContents;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
