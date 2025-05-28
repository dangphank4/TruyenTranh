package com.example.mangaco.adapter;

// Import các thư viện cần thiết
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.mangaco.R;
import com.example.mangaco.object.TruyenTranh;

import java.util.ArrayList;
import java.util.List;

// Adapter cho GridView để hiển thị danh sách truyện tranh
public class TruyenTranhAdapter extends ArrayAdapter<TruyenTranh> {

    private Context ct; // Context của Activity sử dụng Adapter

    public ArrayList<TruyenTranh> getArr() {
        return arr;
    }

    private ArrayList<TruyenTranh> arr; // Danh sách truyện hiện tại
    private ArrayList<TruyenTranh> originalArr; // Lưu bản gốc của danh sách truyện

    // Constructor khởi tạo Adapter
    public TruyenTranhAdapter(@NonNull Context context, int resource, @NonNull List<TruyenTranh> objects) {
        super(context, resource, objects);
        this.ct = context;
        this.arr = new ArrayList<>(objects); // Sao chép danh sách đầu vào
        this.originalArr = new ArrayList<>(objects); // Lưu danh sách ban đầu để khôi phục khi cần
    }

    // Phương thức sắp xếp danh sách truyện theo chuỗi tìm kiếm
    public void sortTruyen(String s) {
        s = s.toUpperCase(); // Chuyển chuỗi tìm kiếm thành chữ hoa để so sánh không phân biệt chữ thường/chữ hoa
        int k = 0; // Chỉ số để hoán đổi vị trí
        for (int i = 0; i < arr.size(); i++) {
            TruyenTranh t = arr.get(i);
            String ten = t.getTen_truyen().toUpperCase(); // Lấy tên truyện và chuyển thành chữ hoa
            if (ten.indexOf(s) >= 0) { // Kiểm tra nếu tên truyện chứa chuỗi tìm kiếm
                arr.set(i, arr.get(k)); // Hoán đổi vị trí
                arr.set(k, t);
                k++;
            }
        }
        notifyDataSetChanged(); // Cập nhật lại danh sách hiển thị
    }

    // Phương thức khôi phục danh sách về trạng thái ban đầu
    public void resetTruyen() {
        arr.clear(); // Xóa danh sách hiện tại
        arr.addAll(originalArr); // Khôi phục danh sách ban đầu từ `originalArr`
        notifyDataSetChanged(); // Cập nhật lại danh sách hiển thị
    }

    // Phương thức tạo View cho từng mục trong danh sách
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Nếu convertView chưa được tái sử dụng, tạo mới bằng cách inflate layout
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_manga, null); // Layout cho từng mục
        }

        // Nếu danh sách truyện không rỗng, lấy dữ liệu và gán vào View
        if (arr.size() > 0) {
            TruyenTranh truyenTranh = this.arr.get(position); // Lấy truyện tại vị trí `position`

            // Liên kết các View trong layout với mã nguồn
            TextView tenTenTruyen = convertView.findViewById(R.id.txvTenTruyen);
            TextView tenTenChap = convertView.findViewById(R.id.txvTenChap);
            ImageView imgAnhTruyen = convertView.findViewById(R.id.imgAnhTruyen);

            // Gán dữ liệu truyện vào các View
            tenTenTruyen.setText(truyenTranh.getTen_truyen());
            tenTenChap.setText(truyenTranh.getTen_chap());

            // Sử dụng thư viện Glide để tải hình ảnh từ URL
            Glide.with(this.ct).load(truyenTranh.getLink_picture()).into(imgAnhTruyen);
        }
        return convertView; // Trả về View đã được gán dữ liệu
    }
}
