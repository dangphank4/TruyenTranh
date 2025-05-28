package com.example.mangaco;

// Import các thư viện cần thiết
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mangaco.adapter.TruyenTranhAdapter;
import com.example.mangaco.api.ApiLayTruyen;
import com.example.mangaco.inteface.LayTruyenVe;
import com.example.mangaco.object.TruyenTranh;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// MainPage: Activity chính hiển thị danh sách truyện
public class MainPage extends AppCompatActivity implements LayTruyenVe {

    // Các thành phần giao diện
    GridView gdvDSTruyen; // GridView hiển thị danh sách truyện
    TruyenTranhAdapter adapter; // Adapter kết nối dữ liệu với GridView
    ArrayList<TruyenTranh> truyenTranhArrayList; // Danh sách truyện
    EditText editTimKiem; // EditText để tìm kiếm truyện
    TextView frist_page,prev_page, page_1, page_2, page_3, page_4, page_5, next_page, last_page;

    private int current_page = 1;
    private int tolar_page = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo và thiết lập ban đầu
        init();
        anhxa();
        setUp();
        setClick();

        // Bắt đầu lấy dữ liệu truyện từ API
        new ApiLayTruyen(this, current_page).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    // Phương thức khởi tạo danh sách truyện và adapter
    private void init() {
        truyenTranhArrayList = new ArrayList<>();
        // Tạo adapter với danh sách truyện ban đầu
        adapter = new TruyenTranhAdapter(this, 0, truyenTranhArrayList);
    }

    // Ánh xạ các thành phần giao diện
    private void anhxa() {
        editTimKiem = findViewById(R.id.editTimKiem);
        gdvDSTruyen = findViewById(R.id.gdvDSTruyen);
        frist_page = findViewById(R.id.first_page);
        prev_page = findViewById(R.id.prev_page);
        page_1 = findViewById(R.id.page_1);
        page_2 = findViewById(R.id.page_2);
        page_3 = findViewById(R.id.page_3);
        page_4 = findViewById(R.id.page_4);
        page_5 = findViewById(R.id.page_5);
        next_page = findViewById(R.id.next_page);
        last_page = findViewById(R.id.last_page);

    }

    // Thiết lập adapter cho GridView
    private void setUp() {
        gdvDSTruyen.setAdapter(adapter);
    }

    // Thiết lập các sự kiện click và tìm kiếm
    private void setClick() {
        // Xử lý tìm kiếm khi người dùng nhập vào EditText
        editTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần xử lý
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String truyen = editTimKiem.getText().toString();
                if (truyen.isEmpty()) {
                    // Nếu ô tìm kiếm trống, reset danh sách về trạng thái ban đầu
                    adapter.resetTruyen();
                    truyenTranhArrayList = adapter.getArr();//cap nhat lại vi tri
                } else {
                    // Nếu có chuỗi tìm kiếm, lọc danh sách truyện
                    adapter.sortTruyen(truyen);
                    truyenTranhArrayList = adapter.getArr();//cap nhat lại vi tri
                }
            }
        });

        // Xử lý sự kiện click vào một mục trong GridView
        gdvDSTruyen.setOnItemClickListener((parent, view, position, id) -> {

            //Lấy thông tin truyện được chọn
            TruyenTranh selectedTruyen = truyenTranhArrayList.get(position);
            // Chuyển sang màn hình ChapterActivity khi click vào truyện
            Intent intent = new Intent(MainPage.this, ChapterActivity.class);
            intent.putExtra("TEN_TRUYEN", selectedTruyen.getTen_truyen());
            intent.putExtra("ANH_TRUYEN", selectedTruyen.getLink_picture());
            intent.putExtra("LINK_TRUYEN", selectedTruyen.getLink_truyen());
            intent.putExtra("MAX_CHAP", selectedTruyen.getTen_chap());
            startActivity(intent);
        });
        // Xử lý các nút chuyển trang
        frist_page.setOnClickListener(v -> changePage(1));
        prev_page.setOnClickListener(v -> changePage(current_page - 1));
        next_page.setOnClickListener(v -> changePage(current_page + 1));
        last_page.setOnClickListener(v -> changePage(tolar_page));

        // Xử lý click vào từng trang cụ thể
        page_1.setOnClickListener(v -> changePage(getPageNumber(page_1)));
        page_2.setOnClickListener(v -> changePage(getPageNumber(page_2)));
        page_3.setOnClickListener(v -> changePage(getPageNumber(page_3)));
        page_4.setOnClickListener(v -> changePage(getPageNumber(page_4)));
        page_5.setOnClickListener(v -> changePage(getPageNumber(page_5)));
    }
    private void changePage(int page) {
        if (page < 1 || page > tolar_page) {
            Toast.makeText(this, "Trang không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        current_page = page;
        updatePageNumbers();
        new ApiLayTruyen(this, current_page).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updatePageNumbers() {
        int startPage = Math.max(1, current_page - 2);
        int endPage = Math.min(tolar_page, startPage + 4);
        startPage = Math.max(1, endPage - 4);

        page_1.setText(startPage <= tolar_page ? String.valueOf(startPage) : "");
        page_2.setText(startPage + 1 <= tolar_page ? String.valueOf(startPage + 1) : "");
        page_3.setText(startPage + 2 <= tolar_page ? String.valueOf(startPage + 2) : "");
        page_4.setText(startPage + 3 <= tolar_page ? String.valueOf(startPage + 3) : "");
        page_5.setText(startPage + 4 <= tolar_page ? String.valueOf(startPage + 4) : "");

        page_1.setBackgroundResource(startPage == current_page ? R.drawable.page_selected : R.drawable.page_unselected);
        page_2.setBackgroundResource(startPage + 1 == current_page ? R.drawable.page_selected : R.drawable.page_unselected);
        page_3.setBackgroundResource(startPage + 2 == current_page ? R.drawable.page_selected : R.drawable.page_unselected);
        page_4.setBackgroundResource(startPage + 3 == current_page ? R.drawable.page_selected : R.drawable.page_unselected);
        page_5.setBackgroundResource(startPage + 4 == current_page ? R.drawable.page_selected : R.drawable.page_unselected);
    }

    private int getPageNumber(TextView page) {
        try {
            return Integer.parseInt(page.getText().toString());
        } catch (NumberFormatException e) {
            return current_page;
        }
    }
    // Phương thức thực thi khi bắt đầu lấy dữ liệu từ API
    @Override
    public void Start() {
        Toast.makeText(this, "Đang lấy dữ liệu...", Toast.LENGTH_SHORT).show();
    }

    // Phương thức xử lý khi lấy dữ liệu thành công
    @Override
    public void End(String data) {
        try {
            // Xóa danh sách cũ
            truyenTranhArrayList.clear();

            // Chuyển dữ liệu JSON thành danh sách truyện
            JSONArray arr = new JSONArray(data);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                truyenTranhArrayList.add(new TruyenTranh(o));
            }

            // Cập nhật adapter với danh sách mới
            adapter = new TruyenTranhAdapter(this, 0, truyenTranhArrayList);
            gdvDSTruyen.setAdapter(adapter);
        } catch (JSONException e) {
            // Xử lý lỗi khi đọc dữ liệu JSON
            e.printStackTrace();
            Toast.makeText(this, "Lỗi đọc dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức xử lý khi gặp lỗi kết nối API
    @Override
    public void Error() {
        Toast.makeText(this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
    }
}
