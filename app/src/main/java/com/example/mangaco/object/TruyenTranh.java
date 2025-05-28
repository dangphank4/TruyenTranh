package com.example.mangaco.object;

import org.json.JSONException;
import org.json.JSONObject;

// Lớp đại diện cho một đối tượng truyện tranh
public class TruyenTranh {
    // Các thuộc tính của truyện tranh
    private String ten_truyen; // Tên truyện
    private String ten_chap;   // Tên chương
    private String link_picture; // Link ảnh bìa truyện
    private String link_truyen;  // Link dẫn đến nội dung truyện

    /*
     * Định dạng JSON mẫu mà lớp này có thể nhận:
     * {
     *   "tenTruyen":"Tên truyện",
     *   "tenChap":"Tên chương",
     *   "linkAnh":"Đường dẫn ảnh",
     *   "linkTruyen":"Đường dẫn truyện"
     * }
     */

    // Constructor mặc định (không tham số)
    public TruyenTranh() {
    }

    // Constructor nhận đối tượng JSON, parse dữ liệu để khởi tạo các thuộc tính
    public TruyenTranh(JSONObject o) throws JSONException {
        this.ten_truyen = o.getString("tenTruyen"); // Lấy tên truyện từ JSON
        this.ten_chap = o.getString("tenChap");    // Lấy tên chương từ JSON
        this.link_picture = o.getString("linkAnh"); // Lấy đường dẫn ảnh từ JSON
        this.link_truyen = o.getString("linkTruyen"); // Lấy đường dẫn truyện từ JSON
    }

    // Constructor khởi tạo với các tham số cụ thể
    public TruyenTranh(String ten_truyen, String ten_chap, String link_picture) {
        this.ten_truyen = ten_truyen;   // Gán tên truyện
        this.ten_chap = ten_chap;       // Gán tên chương
        this.link_picture = link_picture; // Gán đường dẫn ảnh
    }

    // Getter cho các thuộc tính
    public String getTen_truyen() {
        return ten_truyen; // Trả về tên truyện
    }

    public String getTen_chap() {
        return ten_chap; // Trả về tên chương
    }

    public String getLink_picture() {
        return link_picture; // Trả về đường dẫn ảnh
    }

    public String getLink_truyen() {
        return link_truyen; // Trả về đường dẫn truyện
    }

    // Setter cho các thuộc tính
    public void setTen_truyen(String ten_truyen) {
        this.ten_truyen = ten_truyen; // Cập nhật tên truyện
    }

    public void setTen_chap(String ten_chap) {
        this.ten_chap = ten_chap; // Cập nhật tên chương
    }

    public void setLink_picture(String link_picture) {
        this.link_picture = link_picture; // Cập nhật đường dẫn ảnh
    }

    public void setLink_truyen(String link_truyen) {
        this.link_truyen = link_truyen; // Cập nhật đường dẫn truyện
    }
}
