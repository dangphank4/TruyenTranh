package com.example.mangaco.object;

public class AnhTruyenChapter {
    private String name;
    private String link;

    public AnhTruyenChapter(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return name; // Hiển thị tên chương trong ListView
    }

}
