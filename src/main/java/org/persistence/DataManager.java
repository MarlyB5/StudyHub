package org.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DataManager {

    private static final String FILE_NAME =
            "app-data.json";

    private final Gson gson;


    public DataManager() {

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }


    public void saveData(AppData data) {

        try (
                FileWriter writer =
                        new FileWriter(FILE_NAME)
        ) {

            gson.toJson(
                    data,
                    writer
            );

            System.out.println(
                    "Data saved"
            );

        } catch (IOException exception) {

            System.out.println(
                    "Could not save data"
            );

            exception.printStackTrace();
        }
    }


    public AppData loadData() {

        File file =
                new File(FILE_NAME);


        if (!file.exists()) {

            System.out.println(
                    "No saved data found. Creating new data."
            );

            return new AppData();
        }


        try (
                FileReader reader =
                        new FileReader(file)
        ) {

            AppData data =
                    gson.fromJson(
                            reader,
                            AppData.class
                    );


            if (data == null) {
                return new AppData();
            }


            System.out.println(
                    "Data loaded"
            );


            return data;


        } catch (Exception exception) {

            System.out.println(
                    "Could not load data; using defaults"
            );

            exception.printStackTrace();

            return new AppData();
        }
    }
}