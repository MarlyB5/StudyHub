package org.modules;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class UCDModuleService {

    public UCDModuleData findModule(
            String moduleCode
    ) {

        // Make sure the code is clean
        String cleanCode =
                moduleCode
                        .trim()
                        .toUpperCase();


        // Build the UCD module URL
        String url =
                "https://hub.ucd.ie/usis/"
                        + "!W_HU_MENU.P_PUBLISH"
                        + "?MODULE="
                        + cleanCode
                        + "&p_tag=MODULE";


        try {

            Document document =
                    Jsoup.connect(url)
                            .get();


            System.out.println(
                    "Successfully connected to UCD"
            );

            Element moduleHeading =
                    document.select("h4").first();

            if (moduleHeading == null) {

                System.out.println(
                        "Could not find module heading"
                );

                return null;
            }

            String headingText =
                    moduleHeading.text();

            String moduleName =
                    headingText
                            .replace(
                                    "(" + cleanCode + ")",
                                    ""
                            )
                            .trim();

            System.out.println(
                    "Module name: " + moduleName
            );



        } catch (IOException exception) {

            System.out.println(
                    "Could not connect to UCD"
            );

            exception.printStackTrace();
        }


        return null;
    }
}