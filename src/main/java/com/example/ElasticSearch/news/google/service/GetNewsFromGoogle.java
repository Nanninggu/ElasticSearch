package com.example.ElasticSearch.news.google.service;

import com.google.gson.Gson;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GetNewsFromGoogle {
    public static void main(String[] args) throws IOException {
        String tbs = "qdr:w";
        Map<String, String> searchTerms = new HashMap<>();
        searchTerms.put("term1", "삼성전자");
        searchTerms.put("term2", "LG전자");
        searchTerms.put("term3", "SK하이닉스");

        String url = "https://www.google.com/search?";
        List<Map<String, String>> newsData = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        for (Map.Entry<String, String> entry : searchTerms.entrySet()) {
            for (int page = 0; page < 10; page++) {
                int start = page * 10;
                HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
                urlBuilder.addQueryParameter("q", entry.getValue());
                urlBuilder.addQueryParameter("hl", "ko");
                urlBuilder.addQueryParameter("tbm", "nws");
                urlBuilder.addQueryParameter("tbs", tbs);
                urlBuilder.addQueryParameter("start", String.valueOf(start));

                Request request = new Request.Builder()
                        .url(urlBuilder.build().toString())
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (HTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36")
                        .build();

                Response response = client.newCall(request).execute();
                Document doc = Jsoup.parse(response.body().string());
                Elements list = doc.select("div.GI74Re.nDgy9d");

                for (Element element : list) {
                    Map<String, String> newsItem = new HashMap<>();
                    newsItem.put("title", element.text());
                    newsItem.put("search_term", entry.getKey());

                    Element link = element.selectFirst("a");
                    if (link != null) {
                        newsItem.put("link", link.attr("href"));
                    } else {
                        newsItem.put("link", "No link found");
                    }
                    newsData.add(newsItem);
                }
            }
        }

        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".json";
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(new Gson().toJson(newsData));
        }
    }
}
