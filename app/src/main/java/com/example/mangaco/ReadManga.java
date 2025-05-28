package com.example.mangaco;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangaco.adapter.ImageAdapter;
import com.example.mangaco.object.AnhTruyenChapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ReadManga extends AppCompatActivity {
    Button nextChap, prevChap;
    ArrayList<AnhTruyenChapter> dsChuong = new ArrayList<>();
    String chapterName, chapterLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_manga);

        TextView txvChapterName = findViewById(R.id.ten_chap);
        nextChap = findViewById(R.id.next_chap);
        prevChap = findViewById(R.id.prev_chap);

        // Lấy dữ liệu từ Intent
        chapterName = getIntent().getStringExtra("CHAPTER_NAME");
        chapterLink = getIntent().getStringExtra("CHAPTER_LINK");

        // Hiển thị tên chương
        txvChapterName.setText(chapterName);

        // Tải danh sách hình ảnh từ link chương
        loadChapterImages(chapterLink);

        // Thiết lập sự kiện cho nút
        setClick();
    }

    protected void setClick() {
        prevChap.setOnClickListener(v -> {
            String name = modifyChapName(chapterName, -1);
            String link = modifyChapter(chapterLink, -1);
            if (!name.equals("Chương 0")) {
                chapterName = name;
                chapterLink = link;
                reset(name, link);
            } else {
                Toast.makeText(this, "Đây là chương đầu tiên", Toast.LENGTH_SHORT).show();
            }
        });

        nextChap.setOnClickListener(v -> {
            String name = modifyChapName(chapterName, 1);
            String link = modifyChapter(chapterLink, 1);
            String maxChap = new String(getIntent().getStringExtra("MAX_CHAP_NAME"));
            if (!name.equals(modifyChapName(maxChap, 1))) {
                chapterName = name;
                chapterLink = link;
                reset(name, link);
            } else {
                Toast.makeText(this, "Đây là chương cuối cùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadChapterImages(String chapterLink) {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(chapterLink).get();

                // Tìm tất cả các thẻ <img> có class "lazy"
                Elements imgElements = doc.select("img.lozad");

                // Tạo danh sách URL của hình ảnh
                ArrayList<String> imageUrls = new ArrayList<>();

                for (Element img : imgElements) {
                    String imgUrl = img.attr("data-src");
                    imageUrls.add(imgUrl);
                }

                // Cập nhật giao diện trên UI thread
                runOnUiThread(() -> {
                    if (!imageUrls.isEmpty()) {
                        Toast.makeText(ReadManga.this, "Loading data....", Toast.LENGTH_SHORT).show();
                        displayImages(imageUrls); // Hiển thị danh sách ảnh
                    } else {
                        Toast.makeText(ReadManga.this, "Không tìm thấy hình ảnh nào", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ReadManga.this, "Lỗi khi tải hình ảnh", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void displayImages(ArrayList<String> imageUrls) {
        // Hiển thị ảnh bằng cách sử dụng RecyclerView
        RecyclerView recyclerView = findViewById(R.id.imgAnh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo và gán ImageAdapter cho RecyclerView
        ImageAdapter adapter = new ImageAdapter(this, imageUrls);
        recyclerView.setAdapter(adapter);
    }

    private void reset(String chapterName, String chapterLink) {
        // Cập nhật tên chương mới
        TextView txvChapterName = findViewById(R.id.ten_chap);
        txvChapterName.setText(chapterName);

        // Xóa dữ liệu hiện tại trong RecyclerView (nếu cần)
        RecyclerView recyclerView = findViewById(R.id.imgAnh);
        ImageAdapter adapter = new ImageAdapter(this, new ArrayList<>()); // Truyền danh sách rỗng
        recyclerView.setAdapter(adapter);

        // Tải hình ảnh của chương mới
        loadChapterImages(chapterLink);
    }

    private String modifyChapter(String url, int step) {
        int index = url.lastIndexOf("chuong-");
        if (index == -1) return url;

        try {
            String chapterStr = url.substring(index + 7);
            int chapterNumber = Integer.parseInt(chapterStr) + step;
            if (chapterNumber < 1) return url; // Không cho phép chương nhỏ hơn 1
            return url.substring(0, index + 7) + chapterNumber;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return url;
        }
    }

    private String modifyChapName(String chapName, int step) {
        int index = chapName.lastIndexOf("Chương ");
        if (index == -1) return chapName;

        try {
            String chapterStr = chapName.substring(index + 7);
            int chapterNumber = Integer.parseInt(chapterStr) + step;
            if (chapterNumber < 1) return chapName; // Không cho phép chương nhỏ hơn 1
            return chapName.substring(0, index + 7) + chapterNumber;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return chapName;
        }
    }
}
