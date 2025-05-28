package com.example.mangaco.object;

public class ChuongTruyen {
    private String tenTruyenC;
    private String anhTruyenC;
    private String linkTruyenC;

    public ChuongTruyen(String tenTruyenC, String anhTruyenC, String linkTruyenC) {
        this.tenTruyenC = tenTruyenC;
        this.anhTruyenC = anhTruyenC;
        this.linkTruyenC = linkTruyenC;
    }

    public String getLinkTruyenC() {
        return linkTruyenC;
    }

    public void setLinkTruyenC(String linkTruyenC) {
        this.linkTruyenC = linkTruyenC;
    }

    public String getAnhTruyenC() {
        return anhTruyenC;
    }

    public void setAnhTruyenC(String anhTruyenC) {
        this.anhTruyenC = anhTruyenC;
    }

    public String getTenTruyenC() {
        return tenTruyenC;
    }

    public void setTenTruyenC(String tenTruyenC) {
        this.tenTruyenC = tenTruyenC;
    }




}
