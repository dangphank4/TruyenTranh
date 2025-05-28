package com.example.mangaco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.mangaco.api.UrlLayChuong;
import com.example.mangaco.object.AnhTruyenChapter;
import org.jsoup.select.Elements;
import java.util.ArrayList;

public class ChapterActivity extends AppCompatActivity {
    TextView txvTenTruyens;
    ImageView imgAnhTruyens;
    ListView listChap;
    ArrayList<AnhTruyenChapter> dsChuong = new ArrayList<>(); // Lưu danh sách chương với link
    ArrayAdapter<AnhTruyenChapter> adapterc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_activity);
        anhXa();
        setUp();
        init();
        setClick();
    }

    private void init() {
        // Sử dụng adapter với danh sách Chapter
        adapterc = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dsChuong);
        listChap.setAdapter(adapterc);
    }

    private void anhXa() {
        imgAnhTruyens = findViewById(R.id.imgAnhTruyens);
        txvTenTruyens = findViewById(R.id.txvTenTruyens);
        listChap = findViewById(R.id.list_chap);
    }

    private void setUp() {
        String tenTruyen = getIntent().getStringExtra("TEN_TRUYEN");
        String anhTruyen = getIntent().getStringExtra("ANH_TRUYEN");
        String linkTruyen = getIntent().getStringExtra("LINK_TRUYEN");

        // Hiển thị tên và ảnh truyện
        txvTenTruyens.setText(tenTruyen);
        Glide.with(this).load(anhTruyen).into(imgAnhTruyens);

        // Kiểm tra URL và tải danh sách chương
        if (linkTruyen != null && !linkTruyen.isEmpty()) {
            UrlLayChuong urlLayChuong = new UrlLayChuong(this); // Pass the activity context to UrlLayChuong
            urlLayChuong.fetchData(linkTruyen);
        } else {
            Toast.makeText(this, "Không tìm thấy URL truyện", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức xử lý dữ liệu chương sau khi tải xong
    public void onChaptersFetched(final Elements chapters) {
        runOnUiThread(() -> {
            dsChuong.clear(); // Xóa danh sách chương cũ

            for (int i = 0; i < chapters.size(); i++) {
                String chapterName = chapters.get(i).text();
                String chapterLink = chapters.get(i).attr("href");

                // Thêm chương mới vào danh sách
                dsChuong.add(new AnhTruyenChapter(chapterName, chapterLink));
            }

            // Cập nhật lại adapter của ListView
            adapterc.notifyDataSetChanged();
        });
    }




    private void setClick() {
        listChap.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy chương được chọn
            AnhTruyenChapter clickedChapter = dsChuong.get(position);

            if (clickedChapter.getLink() != null) {
                // Tạo Intent để mở ReadManga
                Intent intent = new Intent(ChapterActivity.this, ReadManga.class);
                String maxChap = getIntent().getStringExtra("MAX_CHAP");

                // Truyền dữ liệu tên chương và link chương
                intent.putExtra("CHAPTER_NAME", clickedChapter.getName());
                intent.putExtra("CHAPTER_LINK", clickedChapter.getLink());
                intent.putExtra("MAX_CHAP_NAME", maxChap);

                startActivity(intent);
            } else {
                Toast.makeText(ChapterActivity.this, "Không tìm thấy link cho chương này", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onError() {
        Toast.makeText(this, "Lỗi khi tải danh sách chương", Toast.LENGTH_SHORT).show();
    }
}
