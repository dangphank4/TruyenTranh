package com.example.mangaco.createApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ComicUrl {
    public String fetchDataTolar(int page_on) {
        StringBuilder jsonBuilder = new StringBuilder();
        try {
            // Start JSON array
            jsonBuilder.append("[");
            boolean firstItem = true;

            // Process each page
            for (int page = 1; page <= 10; page++) {
                String url = page == 1 ? "https://mi2manga.biz" : "https://mi2manga.biz/?page=" + page;
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .get();

                Elements comics = doc.select("ul#list_new li");
                for (Element comic : comics) {
                    if (!firstItem) {
                        jsonBuilder.append(",");
                    } else {
                        firstItem = false;
                    }

                    // Extract book name, chapter name, links, and image URL
                    String bookName = comic.select(".book_name h3 a").text();
                    String bookLink = comic.select(".book_name h3 a").attr("href");
                    String chapterName = comic.select(".last_chapter a").text();
                    String imageUrl = comic.select(".book_avatar img").attr("data-src");
                    if (imageUrl.isEmpty()) {
                        imageUrl = comic.select(".book_avatar img").attr("src");
                    }

                    // Build JSON object
                    jsonBuilder.append("{");
                    jsonBuilder.append("\"tenTruyen\": \"").append(bookName).append("\",");
                    jsonBuilder.append("\"tenChap\": \"").append(chapterName).append("\",");
                    jsonBuilder.append("\"linkAnh\": \"").append(imageUrl).append("\",");
                    jsonBuilder.append("\"linkTruyen\": \"").append(bookLink).append("\"");
                    jsonBuilder.append("}");
                }
            }

            // Close JSON array
            jsonBuilder.append("]");

        } catch (IOException e) {
            e.printStackTrace();
            return "[]"; // Return an empty JSON array in case of an error
        }

        return jsonBuilder.toString();
    }

    public String fetchData(int page) {
        // Calculate page numbers
        int page1 = page * 2 - 1;
        int page2 = page * 3 - 1;
        int page3 = page * 2;
        StringBuilder jsonBuilder = new StringBuilder();

        try {
            // Start JSON array
            jsonBuilder.append("[");

            // Fetch and process data for both pages
            fetchPageData(page1, jsonBuilder, true); // Fetch data for page1
//            fetchPageData(page2, jsonBuilder, true);// Fetch data for page2
            fetchPageData(page3, jsonBuilder, false); // Fetch data for page3

            // Close JSON array
            jsonBuilder.append("]");

        } catch (IOException e) {
            e.printStackTrace();
            return "[]"; // Return an empty JSON array in case of an error
        }

        return jsonBuilder.toString();
    }

    // Helper method to fetch and process data for a single page
    private void fetchPageData(int page, StringBuilder jsonBuilder, boolean firstPage) throws IOException {
        // Determine the URL
        String url = page == 1 ? "https://mi2manga.biz" : "https://mi2manga.biz/?page=" + page;

        // Fetch the webpage content
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .get();

        // Select the comic elements
        Elements comics = doc.select("ul#list_new li");

        // Process each comic
        for (Element comic : comics) {
            if (!firstPage || jsonBuilder.charAt(jsonBuilder.length() - 1) != '[') {
                jsonBuilder.append(",");
            }

            // Extract data
            String bookName = comic.select(".book_name h3 a").text();
            String bookLink = comic.select(".book_name h3 a").attr("href");
            String chapterName = comic.select(".last_chapter a").text();
            String imageUrl = comic.select(".book_avatar img").attr("data-src");
            if (imageUrl.isEmpty()) {
                imageUrl = comic.select(".book_avatar img").attr("src");
            }

            // Build JSON object
            jsonBuilder.append("{")
                    .append("\"tenTruyen\": \"").append(bookName).append("\",")
                    .append("\"tenChap\": \"").append(chapterName).append("\",")
                    .append("\"linkAnh\": \"").append(imageUrl).append("\",")
                    .append("\"linkTruyen\": \"").append(bookLink).append("\"")
                    .append("}");
        }
    }

}
