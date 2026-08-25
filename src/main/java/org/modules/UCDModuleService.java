package org.modules;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class UCDModuleService {

    public UCDModuleData findModule(
            String moduleCode
    ) {

        double autonomousHours = -1;
        double totalHours = -1;

        // Make sure the code is clean
        String cleanCode =
                moduleCode
                        .trim()
                        .toUpperCase();

        if (cleanCode.isBlank()) {
            System.out.println("Invalid module code: empty");
            return null;
        }

        // Build the UCD module URL
        String url =
                "https://hub.ucd.ie/usis/"
                        + "!W_HU_MENU.P_PUBLISH"
                        + "?MODULE="
                        + cleanCode
                        + "&p_tag=MODULE";

        try {
            Document document = Jsoup.connect(url).get();
            System.out.println("Successfully connected to UCD");

            Element moduleHeading = document.select("h4").first();
            if (moduleHeading == null) {
                System.out.println("Could not find module heading");
                return null;
            }

            String headingText = moduleHeading.text();
            // Validate the heading contains the requested code like "(COMP31020)"
            if (!headingText.toUpperCase().contains("(" + cleanCode + ")")) {
                System.out.println("Module not found or page did not match requested code: " + cleanCode);
                return null;
            }

            String moduleName = headingText
                    .replace("(" + cleanCode + ")", "")
                    .trim();

            System.out.println("Module name: " + moduleName);

            // Parse Student Effort table rows generically
            for (Element row : document.select("tr")) {
                String rowText = row.text();
                String lower = rowText.toLowerCase();

                if (lower.startsWith("autonomous student learning")) {
                    Double parsed = parseLastNumber(rowText);
                    if (parsed != null) {
                        autonomousHours = parsed;
                    } else {
                        System.out.println("Could not parse Autonomous Student Learning hours from row: " + rowText);
                    }
                } else if (lower.startsWith("total")) {
                    Double parsed = parseLastNumber(rowText);
                    if (parsed != null) {
                        totalHours = parsed;
                    } else {
                        System.out.println("Could not parse Total hours from row: " + rowText);
                    }
                }
                // All other categories are ignored intentionally; contact will be derived as total - autonomous
            }

            // Validation and derivation
            if (moduleName.isBlank()) {
                System.out.println("Parsed module name is blank");
                return null;
            }
            if (autonomousHours < 0 || totalHours <= 0) {
                System.out.println("Invalid parsed hours. Autonomous: " + autonomousHours + ", Total: " + totalHours);
                return null;
            }
            if (totalHours < autonomousHours) {
                System.out.println("Total hours < autonomous hours; parser failure");
                return null;
            }

            double contactHours = totalHours - autonomousHours;

            System.out.println("Autonomous hours: " + autonomousHours);
            System.out.println("Contact hours: " + contactHours);
            System.out.println("Total hours: " + totalHours);

            return new UCDModuleData(
                    cleanCode,
                    moduleName,
                    autonomousHours,
                    contactHours,
                    totalHours
            );

        } catch (IOException exception) {
            System.out.println("Could not connect to UCD");
            exception.printStackTrace();
        } catch (Exception e) {
            System.out.println("Parser error while processing UCD module page");
            e.printStackTrace();
        }

        return null;
    }

    private Double parseLastNumber(String text) {
        String[] parts = text.trim().split("\\s+");
        if (parts.length == 0) return null;
        String last = parts[parts.length - 1];
        try {
            return Double.parseDouble(last);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}