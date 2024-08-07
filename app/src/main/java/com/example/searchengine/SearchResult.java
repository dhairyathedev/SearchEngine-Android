package com.example.searchengine;

public class SearchResult {
    private String title;
    private String body;
    private String url;

    public SearchResult(String title, String body, String url) {
        this.title = title;
        this.body = body;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getUrl() {
        return url;
    }
}