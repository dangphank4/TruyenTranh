package com.example.mangaco.api;

import android.os.AsyncTask;
import com.example.mangaco.ChapterActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;

public class UrlLayChuong {
    ChapterActivity activity;

    // Constructor to pass activity context
    public UrlLayChuong(ChapterActivity activity) {
        this.activity = activity;
    }

    public class FetchChaptersTask extends AsyncTask<String, Void, Elements> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Optionally show loading progress here
        }

        @Override
        protected Elements doInBackground(String... strings) {
            String url = strings[0];
            try {
                // Kết nối tới trang và lấy dữ liệu HTML
                Document doc = Jsoup.connect(url).get();

                // Chọn tất cả các phần tử <a> có class "txt_oneline"
                Elements chapElements = doc.select("div.name-chap a");

                return chapElements;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
//
        @Override
        protected void onPostExecute(Elements elements) {
            super.onPostExecute(elements);
            if (elements != null) {
                activity.onChaptersFetched(elements);  // Calling method in ChapterActivity
            } else {
                activity.onError();  // Handle error in ChapterActivity
            }
        }
    }

    public void fetchData(String url) {
        new FetchChaptersTask().execute(url);
    }
}
