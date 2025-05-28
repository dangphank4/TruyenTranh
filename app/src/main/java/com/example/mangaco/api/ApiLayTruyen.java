package com.example.mangaco.api;

// Import các thư viện cần thiết
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.mangaco.createApi.ComicUrl;
import com.example.mangaco.inteface.LayTruyenVe;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

// Lớp thực hiện việc gọi API để lấy danh sách truyện
public class ApiLayTruyen extends AsyncTask<Void, Void, Void> {
    // Biến lưu dữ liệu trả về từ API
    String data;



    // URL của API cung cấp danh sách truyện
    //https://www.myjsons.com/v/f87e3782
    //https://www.myjsons.com/v/1aa33783
    //String linkTruyen = "https://www.myjsons.com/v/1aa33783";//flle json tu tao

    // Giao diện callback để thông báo trạng thái lấy dữ liệu
    LayTruyenVe layTruyenVe;
    int page;
    //so trang
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    // Constructor nhận một instance của `LayTruyenVe` để callback
    public ApiLayTruyen(LayTruyenVe layTruyenVe,int page) {
        this.layTruyenVe = layTruyenVe;
        this.page = page;
        // Gọi phương thức `Start` để thông báo bắt đầu tải dữ liệu
        this.layTruyenVe.Start();
    }

//    // Phương thức chạy ở background để thực hiện gọi API
//    @Override
//    protected Void doInBackground(Void... voids) {
//        // Tạo một client OkHttp để thực hiện gọi HTTP
//        OkHttpClient client = new OkHttpClient();
//
//        // Tạo yêu cầu HTTP đến URL của API
//        Request request = new Request.Builder()
//                .url(linkTruyen)
//                .build();
//
//        // Khởi tạo giá trị mặc định cho biến dữ liệu
//        data = null;
//
//        try {
//            // Gửi yêu cầu và nhận phản hồi từ API
//            Response response = client.newCall(request).execute();
//
//            // Kiểm tra xem phản hồi có hợp lệ không
//            if (response.isSuccessful() && response.body() != null) {
//                // Lưu dữ liệu trả về thành chuỗi JSON
//                data = response.body().string();
//            } else {
//                // Ghi log nếu phản hồi không thành công
//                Log.e("ApiLayTruyen", "Response failed: " + response.code());
//            }
//        } catch (IOException e) {
//            // Ghi log nếu xảy ra lỗi trong quá trình gọi API
//            Log.e("ApiLayTruyen", "Error in API call", e);
//            data = null;
//        }
//
//        // Trả về null vì không cần kết quả ở bước này
//        return null;
//    }

    // Phương thức chạy ở background để thực hiện gọi API
    @Override
    protected Void doInBackground(Void... voids) {

        // Khởi tạo giá trị mặc định cho biến dữ liệu
        data = null;

        try {
            ComicUrl comicUrl = new ComicUrl();
            data = comicUrl.fetchData(page);
        } catch (Exception e) {
            // Ghi log nếu xảy ra lỗi trong quá trình gọi API
            Log.e("ApiLayTruyen", "Error in API call", e);
            data = null;
        }

        // Trả về null vì không cần kết quả ở bước này
        return null;
    }

    // Phương thức chạy trên luồng chính sau khi hoàn thành `doInBackground`
    @Override
    protected void onPostExecute(Void unused) {
        // Nếu không nhận được dữ liệu, thông báo lỗi thông qua callback
        if (data == null) {
            this.layTruyenVe.Error();
        } else {
            // Nếu nhận được dữ liệu, gửi dữ liệu về thông qua callback
            this.layTruyenVe.End(data);
        }
    }
}
