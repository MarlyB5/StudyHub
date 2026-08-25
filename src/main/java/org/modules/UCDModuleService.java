package org.modules;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class UCDModuleService {

    public UCDModuleData findModule(
            String moduleCode
    ) {

        double autonomousHours = 0;
        double contactHours = 0;
        double totalHours = 0;

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
                    headingText.replace("(" + cleanCode + ")", "")
                            .trim();

            System.out.println(
                    "Module name: " + moduleName
            );

            for (Element row : document.select("tr")) {

                String rowText = row.text();

                if (rowText.startsWith("Autonomous Student Learning")) {

                    String hoursText =
                            rowText.replace(
                                    "Autonomous Student Learning",
                                    ""
                            ).trim();

                    autonomousHours =
                            Double.parseDouble(hoursText);
                }

                else if (rowText.startsWith("Lectures")) {

                    String hoursText =
                            rowText.replace(
                                    "Lectures",
                                    ""
                            ).trim();

                    contactHours +=
                            Double.parseDouble(hoursText);
                }

                else if (rowText.startsWith("Tutorial")) {

                    String hoursText =
                            rowText.replace(
                                    "Tutorial",
                                    ""
                            ).trim();

                    contactHours +=
                            Double.parseDouble(hoursText);
                }

                else if (rowText.startsWith("Practical")) {

                    String hoursText =
                            rowText.replace(
                                    "Practical",
                                    ""
                            ).trim();

                    contactHours +=
                            Double.parseDouble(hoursText);
                }

                else if (rowText.startsWith("Online Learning")) {

                    String hoursText =
                            rowText.replace(
                                    "Online Learning",
                                    ""
                            ).trim();

                    contactHours +=
                            Double.parseDouble(hoursText);
                }

                else if (rowText.startsWith("Total")) {

                    String hoursText =
                            rowText.replace(
                                    "Total",
                                    ""
                            ).trim();

                    totalHours =
                            Double.parseDouble(hoursText);
                }
            }
            System.out.println(
                    "Autonomous hours: " + autonomousHours
            );

            System.out.println(
                    "Contact hours: " + contactHours
            );

            System.out.println(
                    "Total hours: " + totalHours
            );

            return new UCDModuleData(
                    cleanCode,
                    moduleName,
                    autonomousHours,
                    contactHours,
                    totalHours
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